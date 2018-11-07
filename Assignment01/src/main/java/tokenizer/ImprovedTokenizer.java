package tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import models.Doc;
import org.tartarus.snowball.SnowballStemmer;

/**
 * This tokenizer implements a Steemer and Stopwords.
 */
public class ImprovedTokenizer implements Tokenizer{

    private final int LIMIT_SIZE = 3;

    private Doc doc;
    private List<String> termsList;
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
        this.termsList = new ArrayList<String>();
        
        String[] tokensArr = doc.toTokens().toLowerCase().split(" ");

        for(String token : tokensArr){

            token = token.replaceAll("[^a-zA-Z0-9'@-]", "");

            if(token.length() <= LIMIT_SIZE){
                continue;

            } else if (isStopWord(token)) {
                continue;
            
            } else if (isEmail(token)) {
                this.termsList.add(token);
                continue;

            } else if (isContration(token)) {
                token = setContration(token);

            } else if (isDash(token)) {
                token = setDash(token);
            }
            // remove number and ' @ -
            token = token.replaceAll("[^a-zA-Z]", "");
            // stemming
            this.stemmer.setCurrent(token);
            if (this.stemmer.stem()){
                token= this.stemmer.getCurrent();
            }
            
            this.termsList.add(token);
        }
    }

    private String setDash(String token) {
        try {
            return token.split("-")[1];     // english word dont exist sufix
        }catch(ArrayIndexOutOfBoundsException e){
            return token;
        }
    }

    private boolean isDash(String token) {

        if(token.contains("-")){
            String[] aux = token.split("-", -2);
            if (aux[1].length() < 3 ) {      //it's a prefix
               return true;
            }
        }
        return false;
    }

    private String setContration(String token) {
        return token.split("'")[0];
    }

    private boolean isContration(String token) {

        if (token.contains("'")) {          //get reed of the contraction
            String[] aux = token.split("'", -2);
            if (aux[1].length() < 3 && aux[1].length() > 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmail(String token) {

        try {
            InternetAddress emailAddr = new InternetAddress(token);
            emailAddr.validate();

        } catch (AddressException ex) {
            return false;
        }
        return true;
    }

    private boolean isStopWord(String token) {
        return this.stopwords.contains(token);
    }

    @Override
    public int getDocId() {
        return doc.getId();
    }

    @Override
    public List<String> getTermsList() {
        return termsList;
    }
}