package main;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import indexer.Indexer;
import indexer.SegCollection;
import iooperations.CorpusReader;
import magement.Constantes;
import magement.DocCollection;
import magement.Memory;
import magement.Timer;
import models.Doc;
import models.Posting;
import segments.SegReader;
import segments.SegWriter;
import tokenizer.Tokenizer;
import rating.SegController;
import rating.WeightCompute;

public class App {

    private Memory mem;                 /** Responsavel pola gestão de memoria */
    private Timer time;

    private CorpusReader cr;            /** Responsavel pela leitura de ficheiro */
    private DocCollection docColl;      /** Responsavel por guardar os Docs lidos do ficheiro */
    private Tokenizer tokens;           /** Responsavel por normalizar os termos */
    private Indexer indexer;            /** Responsavel pelo index invertido */
    private Doc doc;                    // doc currently reading

    private SegCollection segColl;      /* Controll a collection of segments */

    public App(Timer time, Memory mem, CorpusReader reader, Tokenizer tokens) {
        this.time = time;
        this.mem = mem;
        this.cr = reader;
        this.tokens = tokens;
        this.docColl = new DocCollection();
        this.indexer = new Indexer();
    }

	/**
     *      1º Phase - Read each line (Doc)
     */
    public void readSrcFile() {

        System.out.println("Start reading the source file, creating Doc and normalize the Tokens.");
        
        this.cr.initFile();
        while( (this.doc = this.cr.read()) != null ){
            
            //this.docColl.addDoc(doc);                      // por enquanto não é necessario

            this.tokens.applyFilter(this.doc);
            this.indexer.addTerms( this.tokens.getDocId(), this.tokens.getTermsList() );  // Core operation
            
            if (mem.isHighUsage()) {
                //docColl.saveIntoDisk();
                //docColl.freeColReferences();
                indexer.saveParcialIndexerIntoDisk();
                System.gc();
            }
        }
        cr.closeFile();
        indexer.saveParcialIndexerIntoDisk();
        System.gc();

        System.out.println("*File read completed");
        System.out.println("\t"+ this.cr.getNumOfDocs() +" Docs processed in "+ this.time.getCurrentTime());
        //this.indexer.debugPrint();
    }

    /**
     *      2º Phase Create Dicionary & Merge Indexer
     */
    public void mergeIndexerAndCreateDicionary() {

        System.out.println("Merging parcial indexers and creating dicionary");
        this.segColl = new SegCollection(this.indexer.getNumSegments());     // this.indexer.getNumSegments()
        
        //this.mem.setNewUsage(0.75);

        while( segColl.readerHasDocToRead() ) {

            segColl.calculateNextTermToWrite();   // already merge values if key are the same across all tmp files.
            segColl.setSegWriter(); // set the previous line to be written
            
            if (mem.isHighUsage()) {
                segColl.saveOrderIndexerToDisk();
                segColl.saveDicionaryToDisk();
                System.gc();
            }

            segColl.setNextLineToSelectedReader();
        }
        segColl.saveOrderIndexerToDisk();
        segColl.saveDicionaryToDisk();
        System.gc();
        System.out.println("*Finished merging parcial indexers.");
        System.out.println("*Dicionry complete.");
        System.out.println("\tTotal time:"+ this.time.getCurrentTime());

        //segColl.loadDicionaryToMemory();
    }

    /**
     *      3º Phase Calculate the weight with 1+log(nFreq)
     */
	public void calculateWeight() {

        System.out.println("Calculating weight");

        SegController segcrtl = new SegController();    // control one segment
        //WeightCompute compute = new WeightCompute();

        while(segcrtl.existSegToRead()) {

            segcrtl.computeLog();

            if (mem.isHighUsage()) {
                segcrtl.saveOrderIndexerToDisk();
                System.gc();
            }

            //segcrtl.nextSegment();
        }
        segcrtl.saveOrderIndexerToDisk();
        System.gc();
        System.out.println("*Finished rating the terms.");

	}

}