package tokenizer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ivo
 */
public class StopWords {

    private Set<String> stopWords;
    private final String DEFAULT_PATH = "src/main/java/tokenizer/stopwords.txt"; 
    private final int LIMIT_SIZE = 3;

    public StopWords() {

        this.stopWords = new HashSet<String>();
    }

	public void readStoptWords() {
        try {
            BufferedReader swReader = new BufferedReader(new FileReader(DEFAULT_PATH));
            String s;

            while( (s = swReader.readLine()) != null ) {

                // As stopwords devem ir com forme elas são, não?

                /* if(s.contains("'")){ //get reed of the contraction
                    if(s.split("'", 2)[1].length()<3){
                        s = s.split("'", 2)[0];
                    }
                }
                if(s.length() > LIMIT_SIZE){
                    this.stopWords.add(s);
                } */
                this.stopWords.add(s);
            }
            swReader.close();

        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex2) {
            ex2.printStackTrace();
        }
	}

	public Set<String> getStopWords() {
		return this.stopWords;
	}
}
