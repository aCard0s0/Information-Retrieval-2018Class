package main;

import core.Assignment3;
import core.Dictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Stream;

import core.Indexer;
import core.SegCollection;
import operations.io.CorpusReader;
import core.DocCollection;
import core.Ranker;
import magement.Memory;
import magement.Timer;
import models.Doc;
import models.Posting;
import models.Query;
import models.Relevance;
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
                //System.out.println("*\n*\nisHighUsage\n*\n");

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
        segColl.saveDicionaryToDisk(this.cr.getNumOfDocs());        // KEEP_IN_MEMORY   o numero de docs é guardado no nome.
        dic.setnDoc(this.cr.getNumOfDocs());
        System.gc();
        System.out.println("\tFinished merging parcial indexers.");
        System.out.println("\tDicionary completed in memory and disk.");
        System.out.println("\tTotal time:"+ this.time.getCurrentTime());
    }

    /**
     *      3º Phase Calculate the weight with 1+log(nFreq)
     */
    public void readUserInputTermsAss2() {
        
        Scanner sc = new Scanner(System.in);
        List<String> qTerms;
        System.out.println("*Search in data set:");
        System.out.println("\tWrite \"!exit\" to exit the program.");

        
        boolean stop = true;
        System.out.print("Query: ");
        String[] userTerms = sc.nextLine().split(" ");
        
        Timer t = new Timer();
        while (stop) {

            if (userTerms.length == 1 && userTerms.equals("!exit")) {
                sc.close();
                System.exit(0);
            }

            qTerms = this.tokens.applyFilter( userTerms );      // same filter that use on courpus reader
            
            if(qTerms.size() == 1) {

                String term = qTerms.get(0);
                if (!this.dic.hasTerm(term)) {
                    System.out.println(term +", not found in dictionary.");
                    //continue;
                }
                System.out.print("Term: "+term+"\n");

                // if does not contain the segment file in memory, load it.
                if (!this.dic.hasSegmentInMem(term)) {
                    if (mem.isHighUsage()) {
                        //this.dic.freeSegLessUsed();  // 1 or 2 ...
                        System.gc();
                    }
                    this.dic.loadSegmentToMem(term);
                }
                System.out.println(this.dic.showStatus());

                // Calculate Rank & Save, if revelant. (Top 10)
                this.ranking.rankTerm(this.dic.postingList(term), this.dic.getNDoc());
                this.ranking.printRevelantDocIds();
                stop = false;

            } else {

                String term;
                Map<String, Set<String>> qPostingStrList = new HashMap<>();

                // load posting list
                for(int i=0; i < qTerms.size(); i++) {

                    term = qTerms.get(i);
                    if (!this.dic.hasSegmentInMem(term)) {
                        if (mem.isHighUsage()) {
                            //this.dic.freeSegLessUsed();  // 1 or 2 ...
                            System.gc();
                        }
                        this.dic.loadSegmentToMem(term);
                    }
                    qPostingStrList.put(term, this.dic.postingList(term) );
                }

                // convert  
                Map<String, Set<Posting>> qPosting = getQueryMap(qTerms, qPostingStrList);
                
                // verify doc id
                
                Set<Posting> posList = qPosting.get(qTerms.get(0));

                for(Posting p : posList) {
                    
                    boolean flag = false;
                    for(int i=1; i < qTerms.size(); i++) {
                        Set<Posting> posList2 = qPosting.get(qTerms.get(i));
                        for(Posting p2 : posList2) {
                            if (p.getDocId() == p2.getDocId()) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) 
                            break;
                        else if (i != qTerms.size()-1 )
                            flag = false;
                    }
                    if (flag) {                 // encontramos doc com o mesmo id, vamos olhar para as posições dos termos
                        // analise position
                        Set<Integer> pos = p.getPositionList();
                        for(Integer p1pos : pos) {
                            flag = false;

                            for(int i=1; i < qTerms.size(); i++) {
                            
                                Set<Posting> posList2 = qPosting.get(qTerms.get(i));
                                for(Posting p2 : posList2) {
                                    if (p.getDocId() == p2.getDocId()) {
                                        for(Integer qpos : p2.getPositionList()) {
                                            if (p1pos == qpos-i) {
                                                flag = true;
                                            }
                                        }
                                        if (!flag) 
                                            break;
                                        else if (i != qTerms.size()-1 )
                                            flag = false;
                                    }
                                }
                            }
                            if(flag){
                                
                                System.out.println("DocId da query: "+ p.getDocId());

                                stop = false;
                            }
                        }
                    }
                }
                stop = false;
            }
            
        }
        t.printTotalDuration();
    }


    private Map<String, Set<Posting>> getQueryMap(List<String> qList, Map<String, Set<String>> qPostingStr) {

        Map<String, Set<Posting>> qPosting = new HashMap<>();
        Set<Posting> posList = new HashSet<>();

        for(int i=0; i < qList.size(); i++) {
            
            String t = qList.get(i);
            Set<String> posListStr = qPostingStr.get( t );  

            for(String p : posListStr) {
                posList.add( new Posting(p) );
            }

            qPosting.put(t, posList);
        }

        return qPosting;
    }

    double tp = 0;
    double fp = 0;
    double map = 0.0;    // mean average 
    double mpr10 = 0.0;
    int itr = 0;

    public void readUserInputTermsAss3() {

        Assignment3 ass = new Assignment3();

        Map<Integer, Map<Integer, Integer>> lRelevance = ass.readQueryRelevance();
        List<Query> lQueries = ass.readQueriesFile(this.tokens);

        Map<String, Double> qtnormalized;
        Map<Integer, Double> resultDoc;
        List<Double> lap = new ArrayList<>();
        List<Integer> lrel10 = new ArrayList<>();

        // Timer
        Timer qt = new Timer();
        Timer[] ql = new Timer[lQueries.size()];

        int nq = 0;
        for(Query query : lQueries) {

            ql[nq] = new Timer();

            resultDoc = new HashMap<>();
            qtnormalized = ass.normalize(query);

            for(Entry<String, Double> pair : qtnormalized.entrySet() ) {
                
                Set<Posting> postingList =  this.indexer.getPostingListFromTerm( pair.getKey() );
                if (postingList == null) {
                    continue;
                }

                Double score;
                int k;

                for(Posting p : postingList) {

                    k = p.getDocId();
                    score = pair.getValue() * p.getWeight() * Math.log( this.cr.getNumOfDocs() / postingList.size() );
                    
                    if (!resultDoc.containsKey(k)) {
                        resultDoc.put(k, score);
                    
                    } else {
                        resultDoc.replace(k, resultDoc.get(k) + score);
                    }
                    
                }
            }

            this.tp = 0;
            this.fp = 0;
            this.itr = 0;
            this.mpr10 = 0.0;
            this.map = 0.0;

            // Order map by value
            resultDoc.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .forEach( (e) -> {
                        // calculate tp & fp
                        if ( lRelevance.get( query.getId() ).containsKey( e.getKey() ))  {
                            this.tp++;
                            // sum average precision
                            this.map += this.tp / (this.tp + this.fp);
                        } else {
                            this.fp++;
                        }

                        if (this.itr < 10) {
                            this.mpr10 += this.tp / (this.tp + this.fp);

                            // NDCG
                            Integer tmp = lRelevance.get( query.getId() ).get(e.getKey());
                            if (tmp != null) {
                                lrel10.add(5 - tmp);
                            } else {
                                lrel10.add(5);
                            }
                            this.itr++;
                        }
                    } );
            
            // calculate fn
            int fn = 0;
            for(Map.Entry<Integer, Integer> entry : lRelevance.get( query.getId() ).entrySet() ) {

                if( !resultDoc.containsKey( entry.getKey() ) ) {
                    fn++;
                }
            }
            
            double precision = this.tp / (this.tp + this.fp);
            double recall = this.tp / (this.tp + fn);
            double fprecision = (2 * recall * precision) / (recall + precision);

            // mean average precision
            if (this.map != 0)
                this.map = this.map / this.tp;
            lap.add(this.map);
            
            // NDCG
            double dcg;
            double idcg;
            double ndcg;
            if(lrel10.size() == 0) {
                dcg = 0;
                idcg = 0;
                ndcg = 0;

            } else {
                dcg = lrel10.get(0);
                for(int i=1; i < lrel10.size(); i++) {
                    dcg += lrel10.get(i) / (Math.log(i+1) / Math.log(2.0) );
                }

                idcg = lrel10.get(0);
                Collections.sort(lrel10, Collections.reverseOrder());
                for(int i=1; i < lrel10.size(); i++) {
                    idcg += lrel10.get(i) / (Math.log(i+1) / Math.log(2.0) );
                }
                ndcg = dcg / idcg;
            }
            
            /* Print resutls */
            System.out.println("Query #"+ (nq+1));
            System.out.println("\tPrecision: "+ precision);
            System.out.println("\tRecall: "+ recall);
            System.out.println("\tF-measure: "+ fprecision);
            System.out.println("\tMean Precision at Rank 10: "+ mpr10 / 10);
            System.out.println("\tNormalized Discounted Cumulative Gain (NDGC): "+ ndcg);
            System.out.print("\t");
            ql[nq].printTotalDuration();

            nq++;
        }

        double med = 0.0;
        for(double d : lap) {
            med += d;
        }
        med = med /  lap.size();
        System.out.println("\nMean Average Precision: "+ med);
        qt.printTotalDuration();

        Long[] l = new Long[ql.length];
        for(int i=0; i < l.length; i++) {
            l[i] = ql[i].getTime();
        }
        Arrays.sort(l);
        System.out.println("Median: "+ l[ l.length / 2 ]);
    }
}