package tokenizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.Doc;


public class SimpleTokenizer implements Tokenizer{

    private final int LIMIT_SIZE = 3;

    private Doc doc;
    private List<String> termsList;

    public SimpleTokenizer() {

        this.termsList = new ArrayList<String>();
    }

    @Override
    public void applyFilter( Doc doc) {

        // TODO:
        this.doc = doc;
        this.termsList = new ArrayList<String>();

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
    
}