package tokenizer;

import java.util.ArrayList;
import java.util.List;


public class TokenizerWrapper {

    private final int LIMIT_SIZE = 3;

    /* private StringTokenizer st; */
    private String tokens;
    private List<String> tokenList;

    public TokenizerWrapper(String info) {

        this.tokens = info;
        this.tokenList = new ArrayList<String>();
    }

    public void applyFilter() {

        this.tokens = this.tokens.toLowerCase();
        this.tokens = this.tokens.replaceAll("[^a-zA-Z ]", "");
        String[] tokensArr = this.tokens.split(" ");

        for(String token : tokensArr){
            if(token.length() > LIMIT_SIZE)
                this.tokenList.add(token);
        }
    }

    public List<String> getTokenList() {
        return this.tokenList;
    }
}