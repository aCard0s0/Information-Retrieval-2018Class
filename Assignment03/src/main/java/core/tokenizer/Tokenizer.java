package core.tokenizer;


import java.util.List;

import models.Doc;
import models.Query;

public interface Tokenizer {

    /**
     * 
     * @return
     */
    void applyFilter(Doc doc);

    /**
     * 
     * @param userTerms
     */
    List<String> applyFilter(String[] userTerms);

    /**
     * 
     * @return
     */
	int getDocId();

    /**
     * 
     * @return
     */
	List<String> getTermsList();

	Query getQueryTerms();
}