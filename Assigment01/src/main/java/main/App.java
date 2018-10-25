package main;

import indexer.Indexer;
import iooperations.CorpusReader;
import magement.DocCollection;
import magement.Memory;
import magement.Timer;
import models.Doc;
import segments.SegCollection;
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

    private SegCollection segColl;

    public App(Memory mem, Timer time) {
        this.mem = mem;
        this.time = time;

        this.cr = new CorpusReader();
        this.docColl = new DocCollection();
        // token instanciation depends on option
        this.indexer = new Indexer();
    }

    /**
     *      1º Phase
     */
    public void readSrcFile(boolean tokenizer) {

        System.out.println("Start reading the source file, creating Doc and normalize the Tokens.");
        
        if (tokenizer) {
            this.tokens = new SimpleTokenizer();
        } else {
            this.tokens = new ImprovedTokenizer();
        }
        
        this.cr.initFile();
        while( (this.doc = this.cr.read()) != null ){
            
            //this.docColl.addDoc(doc);                      // por enquanto não é necessario

            this.tokens.applyFilter(this.doc);
            this.indexer.addTerms( this.tokens.getDocId(), this.tokens.getTermsList() );  // Core operation
            
            // Mem Test - use threads?
            if (mem.isHighUsage()) {
                //System.out.println("Memory usage is high!");
                //docColl.saveIntoDisk();
                //docColl.freeColReferences();
                indexer.saveParcialIndexerIntoDisk();
                System.gc();
            }
        }
        cr.closeFile();
        // this.docColl.save ?
        indexer.saveParcialIndexerIntoDisk();
        //docColl.mergeCollections();
        System.gc();
        System.out.println("*Finished reading the file.");
        System.out.println("\t"+ this.cr.getNumOfDocs() +" Docs processed in "+ this.time.getCurrentTime());
        //this.indexer.print();
    }

    /**
     *      2º Phase
     */
    public void createDicionary() {

        System.out.println("Merging Parcial Indexers");
        this.segColl = new SegCollection(this.indexer.getNumSegments());     // this.indexer.getNumSegments()
        
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
        System.out.println("*Finished Merging Parcial Indexers");
        System.out.println("\tTime:"+ this.time.getCurrentTime());

        //segColl.loadDicionaryToMemory();
    }

}