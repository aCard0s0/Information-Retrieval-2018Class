package core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import core.tokenizer.Tokenizer;
import magement.Constantes;
import models.Doc;
import models.Query;
import models.Relevance;

public class Assignment3 {
    
    private String[] tokens;
    
    FileInputStream inputStream;
    BufferedReader reader;

    public Assignment3() {}

    public List<Query> readQueriesFile(Tokenizer tokenizer) {

        List<Query> lqueries = new ArrayList<>();

        try {
            inputStream = new FileInputStream( Constantes.QUERIES_FOLDER  );
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            Doc doc;
            int i= 1;

            while((line = reader.readLine()) != null) {
                
                doc = new Query(i, line);
                tokenizer.applyFilter(doc);
                lqueries.add( tokenizer.getQueryTerms() );
                i++;
            }
            reader.close();

        } catch (IOException e) {
            //e.printStackTrace();
		} catch (NullPointerException ee){
            //ee.printStackTrace();
        }

        return lqueries;
    }

	public Map<String, Double> normalize(Query query) {

        Map<String, Double> termCounted = new HashMap<>();
        double normalization = 0;
        int nFreq;

        for(String qt : query.getLTerms()) {
            if(!termCounted.containsKey(qt)) {
                nFreq = Collections.frequency(query.getLTerms(), qt);
                termCounted.put(qt, nFreq * 1.0);
                normalization += nFreq * nFreq;
            }
        }
        normalization = Math.sqrt(normalization);

        Map<String, Double> termNormalized = new HashMap<>();
        String k;
        double v;
        for(Entry<String, Double> pair : termCounted.entrySet()) {
            k = pair.getKey();
            v = pair.getValue() / normalization;
            termNormalized.put(k, v );
        }
        return termNormalized;
    }

	public Map<Integer, Map<Integer, Integer>> readQueryRelevance() {

        Map<Integer, Map<Integer, Integer>> relevance = new HashMap<>();
        
        try {
            inputStream = new FileInputStream( Constantes.QUERIES_RELEVANCE_FOLDER );
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            String[] tmp;
            int qid = 0;
            int prevQid = 0;
            Map<Integer, Integer> relTmp = null;

            while((line = reader.readLine()) != null) {
                
                tmp = line.split("\\s* ");
                qid = Integer.parseInt(tmp[0]);
                
                if ( qid != prevQid ) {
                    relTmp = new HashMap<>();
                    relTmp.put(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                    relevance.put(qid, relTmp);
                    prevQid = qid;

                } else {
                    relTmp.put(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                }

            }
            reader.close();

        } catch (IOException e) {
            //e.printStackTrace();
		} catch (NullPointerException ee){
            //ee.printStackTrace();
        }
        return relevance;
	}
    
    
}