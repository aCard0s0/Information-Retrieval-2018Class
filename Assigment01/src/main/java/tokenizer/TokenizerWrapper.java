package tokenizer;

import java.util.StringTokenizer;

import models.Corpus;

public class TokenizerWrapper {

    private final int LIMIT_SIZE = 3;

    private StringTokenizer st;
    private String terms;

    public TokenizerWrapper(String info) {

        this.terms = info;
    }

    public String normalizer() {

        this.terms = this.terms.toLowerCase();
        this.terms = this.terms.replaceAll("[^a-zA-Z ]", "");

        this.st = new StringTokenizer(this.terms);

        String result = "";

        while (this.st.hasMoreElements()) {

            String tmp = this.st.nextToken();

            if (tmp.length() > LIMIT_SIZE) {
                result += tmp +" ";
            }

            this.st.nextElement();
        }
        
        return result;
    }
}