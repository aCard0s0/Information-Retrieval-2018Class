package core.tokenizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Doc;

public class SimpleTokenizer implements Tokenizer{

    private final int LIMIT_SIZE = 3;

    private Doc doc;
    private List<String> termsList;     // Has to be a list!

    public SimpleTokenizer() {

        this.termsList = new ArrayList<String>();
    }

    @Override
    public void applyFilter( Doc doc) {

        this.doc = doc;
        this.termsList = new ArrayList<String>();

        for(String token : doc.toTokens().split(" ") ) {
            token = token.toLowerCase().replaceAll("[^a-z]", "");   // all to lower case, replace non letters
            if(token.length() > LIMIT_SIZE) {
                this.termsList.add(token);
            }
        }
        //Collections.sort(this.termsList);
    }

    @Override
    public List<String> applyFilter(String[] userTerms) {

        List<String> tmp = new ArrayList<>();

        for(String token : userTerms ) {
            token = token.toLowerCase().replaceAll("[^a-z]", "");   // all to lower case, replace non letters
            if(token.length() > LIMIT_SIZE) {
                tmp.add(token);
            }
        }
        return tmp;
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