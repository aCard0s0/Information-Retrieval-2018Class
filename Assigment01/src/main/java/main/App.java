package main;

import java.io.File;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import models.Docs;
import readers.StreamWrapper;
import tokenizer.TokenizerWrapper;

public class App {

    public static final String SRC_PATH = "testing";

    private File src;
    private File folder;
    private int fileCounter; 
    private int subdirCounter;

    private StreamWrapper reader;
    private TokenizerWrapper tokens;

    public App() {
        this.src = new File(SRC_PATH);
        this.fileCounter = 0; 
        this.subdirCounter = 0;

        this.reader = new StreamWrapper();
        
    }

    /**
     *  Start the app doing frist a directory evaluation.
     */
    public void start() {

        workEvaluation();
        
        Collection<Docs> list = readCollection();

        HashMap<Integer, List<String>> tokens = tokenizer(list);

        indexer2(tokens);
    }

    /**
     *  
     */
    private void workEvaluation() {

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

    private Collection<Docs> readCollection() {

        Collection<Docs> list = this.reader.read(SRC_PATH +"/sample_us.tsv");

        /* for (Docs c : list) {
            System.out.println(c);
        } */

        return list;
    }

    /**
     *      TODO:  StreamTokenizer 
     * @param list
     * @return
     */
    private HashMap<Integer, List<String>> tokenizer(Collection<Docs> list) {

        HashMap<Integer, List<String>> hash = new HashMap<Integer, List<String>>();

        int id =0;
        for (Iterator<Docs> i = list.iterator(); i.hasNext();) {
            Docs item = i.next();
            this.tokens = new TokenizerWrapper(item.getDocInfo());
            this.tokens.applyFilter();
            hash.put(id, this.tokens.getTokenList());
            id++;
        }

        return hash;
    }

    /* public void indexer(HashMap<Integer, List<String>> hash) {

        HashSet<Posting> hashPosting = new HashSet<Posting>();      // dont care about order!

        for (Integer key: hash.keySet()) {

            List<String> value = hash.get(key);

            for(Iterator<String> i = value.iterator(); i.hasNext();) {
                hashPosting.add( new Posting(key, i.next() )) ;
            }
            System.out.println(key +" "+ value);
        }

        for(Iterator<Posting> i= hashPosting.iterator(); i.hasNext();){
            System.out.println(i.next().toString());
        }

        HashMap<String, List<DocIdAndNumFreq>> mapped = new HashMap<String, List<DocIdAndNumFreq>>();

        for(Iterator<Posting> i= hashPosting.iterator(); i.hasNext();){
            
            Posting post = i.next();
            String newkey = post.getValue();        // token

            List<DocIdAndNumFreq> values = new ArrayList<DocIdAndNumFreq>();

            if (mapped.containsKey(newkey)) {
                values = mapped.get(newkey);
                values.add(new DocIdAndNumFreq( post.getId(), 1 ));

            } else {
                //values.add( new DocIdAndNumFreq(post.getId(), 1)
                //mapped.put(newkey,  values));
            }

           
        }

    } */

    public void indexer2(HashMap<Integer, List<String>> hash) {
    
        List< Entry<Integer, List<String>> > list = new ArrayList< Map.Entry<Integer, List<String> >>( hash.entrySet() );
        HashMap<String, List<Posting> > result = new HashMap<>( );

        for( Iterator<Entry<Integer, List<String>>> i = list.iterator(); i.hasNext();) {

            Entry<Integer, List<String>> pair = i.next();
            List<String> sortTokens = pair.getValue();
            Collections.sort(sortTokens);                                               // sorting the token list for each document id

            //System.out.println( pair.getKey() +" "+ sortTokens );

            String token = null;
            List<Posting> postingList = null;
            for( Iterator<String> t = sortTokens.iterator(); t.hasNext();) {

                token = t.next();
                if (result.containsKey(token)) {
                    postingList = result.get(token);
                    boolean flag = true;                                                   // flag verify if docId already exist in Posting List
                    for (Iterator<Posting> p = postingList.iterator(); p.hasNext();) {
                        if (p.next().getDocId() == pair.getKey()) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        int nfreq = Collections.frequency(sortTokens, token);
                        postingList.add(new Posting(pair.getKey(), nfreq));
                        result.put(token, postingList);
                    }

                } else {
                    postingList = new ArrayList<>();
                    int nfreq = Collections.frequency(sortTokens, token); 
                    postingList.add(new Posting(pair.getKey(), nfreq) );       // create the number of frequency into a document
                    result.put(token, postingList);
                }
            }
        }

        /* 
        // Efficient printing
        List< Entry<String, List<Posting>> > list2 = new ArrayList< Map.Entry<String, List<Posting> >>( result.entrySet() );
        for( Iterator< Entry<String, List<Posting> >> d = list2.iterator(); d.hasNext();) { 
            System.out.println(d.next());
        } */

        // Order printing
        /* Object[] keys = result.keySet().toArray();
        Arrays.sort(keys);
        for(Object key : keys) {
            System.out.println(key +"="+ result.get(key));
        } */
    }

    public <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {

        List<Entry<K, V>> list = new ArrayList<Map.Entry<K,V>>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<K,V>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

}