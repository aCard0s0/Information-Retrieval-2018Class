package segments;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

public class SegReader {

    private String path;
    private FileInputStream inputStream;
    private BufferedReader reader;
    private String line;
    private String term;
    
    private Set<String> postingList;

    public SegReader(String path) {
        this.path = path;
        this.inputStream = null;
        this.reader = null;
        this.line = null;
        this.term = null;
        this.postingList = null;
    }

    /**
     *  This functions instanciate the reader and read the fristline
     *  Frist term and PostingList already define.
     */
    public void init() {
        
        try {
            this.inputStream = new FileInputStream(this.path);
            this.reader = new BufferedReader(new InputStreamReader(this.inputStream, "UTF-8"));
            readNextLine();
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readNextLine () {

        String tmp[];
        postingList = new HashSet<>();

        try {
            this.line = this.reader.readLine();
            tmp = this.line.split("=");
            this.term = tmp[0];

            for(String term : tmp[1].replaceAll("\\[|\\]", "").split(", ")) {
                postingList.add(term);
            }

        } catch (IOException e) {
            //e.printStackTrace();
            this.term = null;
            
        } catch (NullPointerException ee){
            //ee.printStackTrace();
            this.term = null;
            
        } catch (ArrayIndexOutOfBoundsException eee){
            //eee.printStackTrace();
            this.term = null;
        }
    }

    public void closeFile() {
        try {
            this.reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasNextLine() {
        return this.term != null;
    }

    public String getTerm() {
        return this.term;
    }

	public Set<String> getPostingList() {
        return this.postingList;
	}

	public String getFileName() {
        String[] tmp = this.path.split("/");
		return tmp[tmp.length-1];
	}
}