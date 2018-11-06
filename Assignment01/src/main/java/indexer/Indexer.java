package indexer;

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
import java.util.Map;
import java.util.TreeMap;

import magement.Constantes;


public class Indexer {

    private Map<String, Set<Posting>> pairs;
    private int num;                            /* Number of segments */
    private Writer writer;

    public Indexer() {
        this.num = 0;
        this.pairs = new HashMap<>();

        // write folders if not exist
        File f = new File(Constantes.PARCIAL_INDEXER_FOLDER);
        if(!f.exists()) {
            f.mkdirs();
        }
    }

    public void addTerms(int docId, Set<String> termsList) {

        Set<String> tmpStr = new HashSet<String>();      // Temporary token holder
        Set<Posting> tmpPosting;
        String term;
        int nFreq;

        for (Iterator<String> t = termsList.iterator(); t.hasNext();) {

            term = t.next();

            if (!tmpStr.contains(term)) {
                tmpStr.add(term);
                nFreq = Collections.frequency(termsList, term);

                if (this.pairs.containsKey(term)) {
                    this.pairs.get(term).add(new Posting(docId, nFreq));

                } else {
                    tmpPosting = new HashSet<Posting>();
                    tmpPosting.add(new Posting(docId, nFreq));
                    this.pairs.put(term, tmpPosting);
                }
            }
        }
        
        tmpStr = new HashSet<String>();  // free Set memory ?
    }

    /**
     *  Convert HashMap to TreeMap to sort the terms.
     *  Write the TreeMap in disk, each line with the format: 
     *      term=[docId:nºfrequency, ...]
     */
    public void saveParcialIndexerIntoDisk()  {
       
        Map<String, Set<Posting>> treeMap = new TreeMap<>(this.pairs);

        try {
            this.writer = Files.newBufferedWriter(
                Paths.get(Constantes.PARCIAL_INDEXER_FOLDER, "parcial_index_" + this.num + ".txt"));
            
            treeMap.forEach((key, value) -> {
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