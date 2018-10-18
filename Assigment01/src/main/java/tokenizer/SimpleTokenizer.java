package tokenizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import models.Doc;


public class SimpleTokenizer implements Tokenizer{

    private final int LIMIT_SIZE = 3;

    private Doc doc;
    private List<String> termsList;

    public SimpleTokenizer(Doc doc) {

        this.doc = doc;
        this.termsList = new ArrayList<String>();
    }

    @Override
    public void applyFilter() {

        String tokens = doc.toTokens();
        tokens = tokens.toLowerCase();
        tokens = tokens.replaceAll("[^a-zA-Z ]", "");
        String[] tokensArr = tokens.split(" ");

        for(String token : tokensArr){
            if(token.length() > LIMIT_SIZE)
               this. termsList.add(token);
        }

        Collections.sort(this.termsList);
    }

    @Override
    public int getDocId() {
        return doc.getId();
    }

    @Override
    public List<String> getTermsList() {
        return termsList;
    }
   

    /**
     * This implementation much be final...
     * ref: https://stackoverflow.com/questions/3110547/java-how-to-create-new-entry-key-value
     * 
     * @param <K>
     * @param <V>
     */
    final class MyEntry<K, V> implements Map.Entry<K, V> {

        private final K key;
        private V value;
    
        public MyEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    
        @Override
        public K getKey() {
            return key;
        }
    
        @Override
        public V getValue() {
            return value;
        }
    
        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public String toString() {
            return getKey() +"="+ getValue();
        }
    }

}