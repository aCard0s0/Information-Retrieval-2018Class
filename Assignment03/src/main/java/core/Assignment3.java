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
import java.util.Map.Entry;

import core.tokenizer.Tokenizer;
import models.Doc;
import models.Query;

public class Assignment3 {
    
    private String QUERIES_FOLDER = "./cranfield-queries/cranfield.queries.txt";

    private String[] tokens;
    

    public Assignment3() {}

    public List<Query> readQueriesFile(Tokenizer tokenizer) {

        List<Query> lqueries = new ArrayList<>();

        try {
            FileInputStream inputStream = new FileInputStream( QUERIES_FOLDER );
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            Doc doc;
            int i= 0;

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
    
    
}