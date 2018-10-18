package indexer;

import java.util.List;
import java.util.Set;

import models.Posting;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Indexer {

    private HashMap<String, List<Posting>> pairs;
    private int num; /* Number of segments */

    public Indexer() {
        this.pairs = new HashMap<>();
        this.num = 1;
    }

    public void saveIntoDisk()  {

        //System.out.println("\t *Saving indexer");
        Writer writer;

        try {
            
            writer = Files.newBufferedWriter(
                Paths.get("testing/indexer/", "partial_index_"+ this.num +".txt") );

            pairs.forEach((key, value) -> {
                try {
                    writer.write(key + "=" + value + System.lineSeparator());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.num++;
    }

    public void freeMapReferences() {
        //System.out.println("\t *Free memory");
        this.pairs = new HashMap<>();
    }

    public void addTerms(int docId, List<String> termsList) {

        //List<String> tmpStr = new ArrayList<>();
        Set<String> str = new HashSet<>();
        List<Posting> tmpPos;
        String term;
        int nFreq;

        for (Iterator<String> t = termsList.iterator(); t.hasNext();) {

            term = t.next();

            if (!str.contains(term)) {
                str.add(term);
                nFreq = Collections.frequency(termsList, term);

                if (this.pairs.containsKey(term)) {
                    this.pairs.get(term).add(new Posting(docId, nFreq));

                } else {
                    tmpPos = new ArrayList<>();
                    tmpPos.add(new Posting(docId, nFreq));
                    this.pairs.put(term, tmpPos);
                }
            }
        }
        str = new HashSet<>();  // free Set memory ?
    }

    public void mergeMaps() {
    }

    public void print() {

        // Order printing
        Object[] keys = this.pairs.keySet().toArray();
        Arrays.sort(keys);
        for (Object key : keys) {
            System.out.println(key + "=" + this.pairs.get(key));
        }
    }

}