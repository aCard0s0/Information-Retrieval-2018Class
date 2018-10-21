package iooperations;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import models.*;

/**
 *  ref: https://www.baeldung.com/java-read-lines-large-file
 */
public class CorpusReader {

    private final String DEFAULT_PATH = "testing/amazon_reviews_us_Wireless_v1_00.tsv"; //"testing/sample_us.tsv";

    private FileInputStream inputStream;
    private int docId;
    private boolean firstLine;

    private BufferedReader reader;
    private Doc result;
    private String line;
    private String[] tokens;

    public CorpusReader() {
        this.inputStream = null;
        this.docId = 0;
        this.firstLine = true;
        this.reader = null;
        this.result = null;   // must start null
        this.line = null;
        this.tokens = null;
    }

    public void initFile() {
        try {
            this.inputStream = new FileInputStream(DEFAULT_PATH);
            this.reader = new BufferedReader(new InputStreamReader(this.inputStream, "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
		} catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Doc read () {

        
        // Skip first line
        if(this.firstLine) {
            try {
                this.reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.firstLine = false;
        }
        
        try {
            this.line = this.reader.readLine();

            this.tokens = this.line.split("([\t])+");

            this.result = new Doc(this.docId, this.tokens[5], this.tokens[12], this.tokens[13]);
            this.docId++;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
            
        } catch (NullPointerException ee){
            ee.printStackTrace();
            return null;
        } catch (ArrayIndexOutOfBoundsException eee){
            eee.printStackTrace();
            return null;
        }
        return this.result;
    }

    public void closeFile() {
        try {
            this.reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}