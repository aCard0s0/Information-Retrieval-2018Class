package tokenizer;

import java.util.Set;

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
	Set<String> getTermsList();
}