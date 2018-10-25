package tokenizer;

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
     * @return
     */
	int getDocId();

    /**
     * 
     * @return
     */
	List<String> getTermsList();
}