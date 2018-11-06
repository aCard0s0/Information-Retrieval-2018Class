package main;

import indexer.Indexer;
import iooperations.CorpusReader;
import magement.DocCollection;
import magement.Memory;
import magement.Timer;
import models.Doc;
import segments.SegCollection;
import tokenizer.Tokenizer;

public class App {

    private Memory mem;                 /** Responsavel pola gestão de memoria */
    private Timer time;

    private CorpusReader cr;            /** Responsavel pela leitura de ficheiro */
    private DocCollection docColl;      /** Responsavel por guardar os Docs lidos do ficheiro */
    private Tokenizer tokens;           /** Responsavel por normalizar os termos */
    private Indexer indexer;            /** Responsavel pelo index invertido */
    private Doc doc;                    // doc currently reading

    private SegCollection segColl;

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
    public void createDicionary() {

        System.out.println("Merging parcial indexers and creating dicionary");
        this.segColl = new SegCollection(80);     // this.indexer.getNumSegments()
        
        //this.mem.setNewUsage(0.75);

        while( segColl.readerHasDocToRead() ) {

            segColl.calcuteNextTermToWrite();   // already merge values if key are the same across all tmp files.
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
        System.out.println("*Finished merging parcial indexers");
        System.out.println("*Dicionry complete");
        System.out.println("\tTotal time:"+ this.time.getCurrentTime());

        //segColl.loadDicionaryToMemory();
    }

}