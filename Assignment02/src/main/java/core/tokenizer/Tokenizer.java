package core.tokenizer;


import java.util.List;

import models.Doc;

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
}