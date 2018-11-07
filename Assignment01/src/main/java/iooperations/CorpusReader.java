package iooperations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import magement.Constantes;
import models.*;

/**
 *  ref: https://www.baeldung.com/java-read-lines-large-file
 */
public class CorpusReader {

    private String src;
    private FileInputStream inputStream;
    private int docId;
    private boolean firstLine;

    private BufferedReader reader;
    private Doc result;
    private String line;
    private String[] tokens;
    private File f; 

    public CorpusReader(String path) {
        this.src = path;
        this.inputStream = null;
        this.docId = 0;
        this.firstLine = true;
        this.reader = null;
        this.result = null;   // must start null
        this.line = null;
        this.tokens = null;
        isValidFile(this.src);
    }

    public CorpusReader() {
        this.src = Constantes.SOURCE_FILE;
        this.inputStream = null;
        this.docId = 0;
        this.firstLine = true;
        this.reader = null;
        this.result = null;   // must start null
        this.line = null;
        this.tokens = null;
        isValidFile(this.src);
    }

    private void isValidFile(String path) {

        /* f = new File(path);
        if ( !(f.isFile() || f.getName().substring(f.getName().length()-4).equals(".tsv")) ) {
            System.err.println( path + "\nFile not found, please provide the correct path.");
            System.exit(1);
        } */
    }

    public void initFile() {

        try {
            this.inputStream = new FileInputStream(this.src);
            this.reader = new BufferedReader(new InputStreamReader(this.inputStream, "UTF-8"));

            // Skip first line
        if(this.firstLine) {
            try {
                this.reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.firstLine = false;
        }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
		} catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the Doc object or null when reach the document final.
     */
    public Doc read () {

        try {
            this.line = this.reader.readLine();

            this.tokens = this.line.split("([\t])+");

            this.result = new Doc(this.docId, this.tokens[5], this.tokens[12], this.tokens[13]);
            this.docId++;

        } catch (IOException e) {
            //e.printStackTrace();
            return null;
            
        } catch (NullPointerException ee){
            //ee.printStackTrace();
            return null;
        } 
        return this.result;
    }

    public void closeFile() {
        try {
            this.reader.close();
            this.inputStream = null;
            this.reader = null;
            this.result = null;   // must start null
            this.line = null;
            this.tokens = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumOfDocs() {
        return this.docId;
    }

	public String getSrcPath() {
		return this.src;
	}
}