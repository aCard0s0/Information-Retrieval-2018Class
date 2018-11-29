package models;

import java.util.List;

public class Query extends Doc {

    private String tokens;
    private List<String> terms;

    public Query(int id, String tokens) {
        super.setId(id);
        this.tokens = tokens;
    }

	public Query(int id, List<String> termsList) {
        super.setId(id);
        this.terms = termsList;
	}

    @Override
    public String toTokens() {
        return tokens;
    }

    public List<String> getLTerms() {
        return this.terms;
    }

}