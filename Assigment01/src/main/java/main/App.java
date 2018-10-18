package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import indexer.Indexer;
import iooperations.CorpusReader;
import magement.DocCollection;
import magement.Memory;
import magement.Timer;
import models.Doc;
import models.Posting;
import tokenizer.ImprovedTokenizer;
import tokenizer.SimpleTokenizer;
import tokenizer.Tokenizer;

public class App {

    public static final String SRC_PATH = "testing";

    private Memory mem;                 /** Responsavel pola gestão de memoria */
    private Timer time;
    private CorpusReader cr;            /** Responsavel pela leitura de ficheiro */
    private DocCollection docColl;      /** Responsavel por guardar os Docs lidos do ficheiro */
    private Tokenizer tokens;           /** Responsavel por normalizar os termos */
    private Indexer indexer;            /** Responsavel pelo index invertido */
    private Doc doc;                    // doc currently reading

    public App() {
        this.mem = new Memory(1510, 0.9);     // 500 Mbs    TODO: 400mb testing file
        this.time = new Timer();
        this.cr = new CorpusReader();
        this.docColl = new DocCollection();
        // token instanciation depends on option
        this.indexer = new Indexer();
    }

    /**
     *  Start the app doing frist a directory evaluation.
     */
    public void start() {

        // workEvaluation();

        this.cr.initFile();
        while( (this.doc = this.cr.read()) != null ){
            // System.out.println(doc);
            
            //this.docColl.addDoc(doc);                      // é necessário? Se sim temos que guarda-lo em segmentos, e o doc result?

            //if (arg do tipo de tokenizer)
                this.tokens = new SimpleTokenizer(this.doc);
            //else
                //tokens = new ImprovedTokenizer(tokens);
            
            // stop words
            // stemmer implementation

            this.tokens.applyFilter();
            this.indexer.addTerms( this.tokens.getDocId(), this.tokens.getTermsList() );  // Core operation
            
            // Mem Test - use threads?
            if (mem.isHighUsage()) {
                //System.out.println("Memory usage is high!");
                mem.printMemory();
                //docColl.saveIntoDisk();
                //docColl.freeColReferences();
                indexer.saveIntoDisk();
                indexer.freeMapReferences();
                System.gc();
                mem.printMemory();
                time.currentTime();
            }
        }
        cr.closeFile();
        System.out.println("Finished reading the file...");
        // docColl ?
        indexer.saveIntoDisk();
        indexer.freeMapReferences();  // probably not needed
        System.gc();
        //docColl.mergeCollections();
        indexer.mergeMaps();

        //this.indexer.print();
        //time.printTotalDuration();
    }

    /**
     *  
     */
    private void workEvaluation() {

        File src = new File(SRC_PATH);
        int fileCounter = 0; 
        int subdirCounter = 0;
        File folder;

        for (String file : src.list()) {
            folder = new File(SRC_PATH +"/"+ file);

            if (folder.isFile())
                fileCounter++;

            else if (folder.isDirectory())
                subdirCounter++;
        }
        System.out.println("Directories: "+ subdirCounter);
        System.out.println("Files: "+ fileCounter);
    }

}