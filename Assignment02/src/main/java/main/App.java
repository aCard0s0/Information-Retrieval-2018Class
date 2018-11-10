package main;

import core.Dictionary;
import java.util.Scanner;

import core.Indexer;
import core.SegCollection;
import operations.io.CorpusReader;
import core.DocCollection;
import core.Ranker;
import magement.Memory;
import magement.Timer;
import models.Doc;
import core.tokenizer.Tokenizer;

public class App {

    private final Memory mem;                 /** Responsavel pola gestão de memoria */
    private final Timer time;

    private final CorpusReader cr;            /** Responsavel pela leitura de ficheiro */
    private final DocCollection docColl;      /** Responsavel por guardar os Docs lidos do ficheiro */
    private final Tokenizer tokens;           /** Responsavel por normalizar os termos */
    private final Indexer indexer;            /** Responsavel pelo index invertido */
    private Dictionary dic;
    
    private Doc doc;                    // doc currently reading

    private SegCollection segColl;      /* Controll a collection of segments */
    private Ranker ranking;

    public App(Timer time, Memory mem, CorpusReader reader, Tokenizer tokens, Dictionary dic) {
        this.time = time;
        this.mem = mem;
        this.cr = reader;
        this.tokens = tokens;
        this.dic = dic;
        this.docColl = new DocCollection();
        this.indexer = new Indexer();
        this.ranking = new Ranker();
    }
    
    /**
     *      1º Phase - Read each line (Doc)
     */
    public void readSrcFile() {

        System.out.println("*Start reading the source file, creating Doc and normalize the Tokens.");
        
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

        System.out.println("\tFile read completed");
        System.out.println("\t"+ this.cr.getNumOfDocs() +" Docs processed in "+ this.time.getCurrentTime());
        //this.indexer.debugPrint();
    }

    /**
     *      2º Phase Create Dicionary & Merge Indexer
     */
    public void mergeIndexerAndCreateDicionary() {

        System.out.println("*Merging parcial indexers and creating dicionary");
        this.segColl = new SegCollection(this.indexer.getNumSegments());     // this.indexer.getNumSegments()
        
        //this.mem.setNewUsage(0.75);
        
        while( segColl.readerHasDocToRead() ) {

            segColl.calculateNextTermToWrite();     // already merge values if key are the same across all tmp files.
            segColl.setSegWriter();                 // set the previous line to be written
            //segw.addLine(segColl.getTerm(), segColl.getValue());
            
            // also set dicionary stucture TODO:improve code
            dic.addTerm(segColl.getTerm(), segColl.toDic());
            
            if (mem.isHighUsage()) {
                segColl.saveOrderIndexerToDisk();
                //dic.saveDicionaryToDisk(this.cr.getNumOfDocs());    // KEEP_IN_MEMORY
                segColl.saveDicionaryToDisk(this.cr.getNumOfDocs());    // KEEP_IN_MEMORY
                System.gc();
            }
            segColl.setNextLineToSelectedReader();
        }
        segColl.saveOrderIndexerToDisk();
        //dic.saveDicionaryToDisk(this.cr.getNumOfDocs());    // KEEP_IN_MEMORY
        segColl.saveDicionaryToDisk(this.cr.getNumOfDocs());        // KEEP_IN_MEMORY
        System.gc();
        System.out.println("\tFinished merging parcial indexers.");
        System.out.println("\tDicionary completed in memory and disk.");
        System.out.println("\tTotal time:"+ this.time.getCurrentTime());
    }

    /**
     *      3º Phase Calculate the weight with 1+log(nFreq)
     */
    public void readUserInputTerms() {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("*Search in data set:");
        System.out.println("\tWrite \"!exit\" to exit the program.");
        
        while (true) {
            System.out.print("Query: ");
            String[] userTerms = sc.nextLine().split(" ");

            if (userTerms.length == 1 && userTerms.equals("!exit")) {
                System.exit(0);
            }
            // validar mais termos ?

            for(String term : userTerms) {

                if (!this.dic.hasTerm(term)) {
                    System.out.println(term +", not found in dictionary.");
                    continue;
                }
                // if does not contain the segment file in memory, load it.
                if (!this.dic.hasSegmentInMem(term)) {
                    if (mem.isHighUsage()) {
                        //this.dic.freeSegLessUsed();  // 1 or 2 ...
                        System.gc();
                    }
                }
                this.dic.loadSegmentToMem(term);
                // Calculate Rank & Save, if revelant. (Top 10)
                //this.ranking.rankTerm(this.dic.postingList(term), this.cr.getNumOfDocs());
            }
            //this.ranking.printRevelantDocIds();
            System.out.println(this.dic.showStatus());
        }
        
    }

}