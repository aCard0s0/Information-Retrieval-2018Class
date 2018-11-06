package tokenizer;

import java.util.HashSet;
import java.util.Set;

import models.Doc;

public class SimpleTokenizer implements Tokenizer{

    private final int LIMIT_SIZE = 3;

    private Doc doc;
    private Set<String> termsList;

    public SimpleTokenizer() {

        this.termsList = new HashSet<String>();
    }

    @Override
    public void applyFilter( Doc doc) {

        this.doc = doc;
        this.termsList = new HashSet<String>();

        for(String token : doc.toTokens().split(" ") ) {
            token = token.toLowerCase().replaceAll("[^a-z]", "");   // all to lower case, replace non letters
            if(token.length() > LIMIT_SIZE) {
                this. termsList.add(token);
            }
        }
        //Collections.sort(this.termsList);
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