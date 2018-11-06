package tokenizer;

import java.util.HashSet;
import java.util.Set;

import models.Doc;
import org.tartarus.snowball.SnowballStemmer;

/**
 * This tokenizer implements a Steemer and Stopwords.
 */
public class ImprovedTokenizer implements Tokenizer{

    private final int LIMIT_SIZE = 3;

    private Doc doc;
    private Set<String> termsList;
    private Set<String> stopwords;
    private SnowballStemmer stemmer;    
    
    public ImprovedTokenizer(Set<String> stopwords) {

        this.stopwords = stopwords;
        
        try {
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

    private String tokens;

    @Override
    public void applyFilter(Doc doc) {

        this.doc = doc;
        this.termsList = new HashSet<String>();
        
        String[] tokensArr = doc.toTokens().toLowerCase().split(" ");
        String[] aux = null;
        
        // token = aux[0].replaceAll("[^a-zA-Z']", "")+"@"+aux[1];

        for(String token : tokensArr){

            token = token.replaceAll("[^a-zA-Z0-9'@-]", "");
            if (isStopWord(token)) {
                continue;
            
            } else if (isEmail(token)) {
                this.termsList.add(token);

            } else if (isContration()) {
                token = setContration();
                this.termsList.add(token);

            } else if (isDash()) {
                token = setDash();
                this.termsList.add(token);
            }
            
            token = token.replaceAll("[^a-zA-Z]", "");


            /* if (token.contains("@")) {    //if it's email go to list
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
               
                if(!this.stopwords.contains(token)){
                    this.stemmer.setCurrent(token);
                    if (this.stemmer.stem()){
                        token= this.stemmer.getCurrent();
                    }

                    if(token.length() > LIMIT_SIZE){
                        this.termsList.add(token);
                    }
                }
            } */
        }
    }

    private String setDash() {
        return null;
    }

    private boolean isDash() {
        return false;
    }

    private String setContration() {
        return null;
    }

    private boolean isContration() {
        return false;
    }

    private boolean isEmail(String token) {
        return false;
    }

    private boolean isStopWord(String token) {
        return false;
    }

    @Override
    public int getDocId() {
        return doc.getId();
    }

    @Override
    public Set<String> getTermsList() {
        return termsList;
    }
}