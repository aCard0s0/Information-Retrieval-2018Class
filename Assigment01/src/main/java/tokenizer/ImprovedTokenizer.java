package tokenizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.Doc;
import org.tartarus.snowball.SnowballStemmer;

/**
 * This tokenizer implements a Steemer and Stopwords.
 */
public class ImprovedTokenizer implements Tokenizer{
    private final int LIMIT_SIZE = 3;

    private Doc doc;
    private List<String> termsList;
    private StopWords stopwords;
    private SnowballStemmer stemmer;    
    public ImprovedTokenizer(Doc doc) {
        try {
            this.doc = doc;
            this.termsList = new ArrayList<String>();
            this.stopwords = new StopWords();
            
            Class stemClass = Class.forName("org.tartarus.snowball.ext.englishStemmer");
            this.stemmer = (SnowballStemmer) stemClass.newInstance();
            
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void applyFilter() {

        String tokens = doc.toTokens();
        tokens = tokens.toLowerCase();
        String[] tokensArr = tokens.split(" ");
        String []aux=null;
        
        for(String token : tokensArr){
            if(token.contains("@")){    //if it's email go to list
                aux=token.split("@", 2);
                if(aux[0].length()>1 && aux[1].length()>3 && aux[1].contains(".")){
                    this.termsList.add(token);
                }
            }else{
               
                if(token.contains("'")){ //get reed of the contraction
                    aux=token.split("'", -2);

                    if(aux[1].length()<3){
                        token = aux[0];
                        token = token.replaceAll("[^a-zA-Z']", "");
                    }else{                
                        token = token.replaceAll("[^a-zA-Z]", "");
                    }
                }else{
                    if(token.contains("-")){
                        aux=token.split("-", 2);
                        if(aux[1].length()<3){//it's a prefix
                            token = aux[1];
                        }
                    }
                    token = token.replaceAll("[^a-zA-Z]", "");
                }
                
               
                if(!this.stopwords.check(token)){
                    this.stemmer.setCurrent(token);
                    if (this.stemmer.stem()){
                        token= this.stemmer.getCurrent();
                    }

                    if(token.length() > LIMIT_SIZE){
                        this.termsList.add(token);
                    }
                }
            }
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