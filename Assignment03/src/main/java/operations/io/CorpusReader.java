package operations.io;

import models.Doc;

/**
 *  ref: https://www.baeldung.com/java-read-lines-large-file
 */
public interface CorpusReader {

	void initFile();

	Doc read();

	void closeFile();

	int getNumOfDocs();

	String getSrcPath();

}