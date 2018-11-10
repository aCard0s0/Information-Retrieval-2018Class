package core;

import java.util.Set;

import models.Posting;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import magement.Constantes;


public class Indexer {

    private Map<String, Set<Posting>> pairs;
    private int num;                            /* Number of segments */
    private Writer writer;

    public Indexer() {
        this.num = 0;
        this.pairs = new HashMap<>();

        // write folders if not exist
        File f = new File(Constantes.UNORDER_INDEXER_FOLDER);
        if(!f.exists()) {
            f.mkdirs();
        }
    }

    /**
     * 
     * @param docId
     * @param termsList generated from tokenizer. Note that has to be list with repeations.
     */
    public void addTerms(int docId, List<String> termsList) {

        Map<String, Posting> tmppairs = new HashMap<>();
        
        double normalization = 0;
        String term;
        int nFreq;

        for (Iterator<String> t = termsList.iterator(); t.hasNext();) {

            term = t.next();

            if (!tmppairs.containsKey(term)) {
                nFreq = Collections.frequency(termsList, term);
                tmppairs.put(term, new Posting(docId, nFreq));
                normalization += nFreq * nFreq;
            }
        }

        normalization = Math.sqrt(normalization);

        Set<Posting> tmpPosting;
        String k;
        Posting v;
        for(Entry<String, Posting> pair : tmppairs.entrySet() ) {

            k = pair.getKey();
            v = pair.getValue();
            v.calculateWeight(normalization);

            if (this.pairs.containsKey(k)) {
                this.pairs.get(k).add(v);
            
            } else {
                tmpPosting = new HashSet<Posting>();
                tmpPosting.add(v);
                this.pairs.put(k, tmpPosting);
            }
        }
        tmpPosting = new HashSet<Posting>();        // free references 
    }

    /**
     *  Convert HashMap to TreeMap to sort the terms.
     *  Write the TreeMap in disk, each line with the format: 
     *      term=[docId:nºfrequency, ...]
     */
    public void saveParcialIndexerIntoDisk()  {
       
        this.pairs = new TreeMap<>(this.pairs);     // Transformation to order the key

        try {
            this.writer = Files.newBufferedWriter(
                Paths.get(Constantes.UNORDER_INDEXER_FOLDER, "parcial_index_" + this.num + ".txt"));
            
            this.pairs.forEach((key, value) -> {
                try {
                    this.writer.write(key + "=" + value + System.lineSeparator());
    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.writer.close();
        
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        this.num++;
        this.pairs = new HashMap<>();       // free References
    }

    /**
     * @return the number of files in the tmp folder
     */
    public int getNumSegments() {
        return this.num;
    }

    public void debugPrint() {

        // Order printing
        Object[] keys = this.pairs.keySet().toArray();
        Arrays.sort(keys);
        for (Object key : keys) {
            System.out.println(key + "=" + this.pairs.get(key));
        }
    }
}