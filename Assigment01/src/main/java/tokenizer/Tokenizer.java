package tokenizer;

import java.util.List;

public interface Tokenizer {

    /**
     * 
     * @return
     */
    void applyFilter();

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