package main;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import models.Corpus;
import readers.StreamWrapper;
import tokenizer.TokenizerWrapper;

public class App {

    public static final String SRC_PATH = "testing";

    private File src;
    private File folder;
    private int fileCounter; 
    private int subdirCounter;

    private StreamWrapper reader;
    private TokenizerWrapper tokens;

    public App() {
        this.src = new File(SRC_PATH);
        this.fileCounter = 0; 
        this.subdirCounter = 0;

        this.reader = new StreamWrapper();
        
    }

    /**
     *  Start the app doing frist a directory evaluation.
     */
    public void start() {

        workEvaluation();
        
        Collection<Corpus> list = readDocs();

        tokenizer(list);
    }

    private void tokenizer(Collection<Corpus> list) {

        for (Iterator<Corpus> i = list.iterator(); i.hasNext();) {
            Corpus item = i.next();
            this.tokens = new TokenizerWrapper(item.getDocInfo());
            this.tokens.normalizer();
        }
    }

    /**
     *  
     */
    private void workEvaluation() {

        for (String file : src.list()) {
            folder = new File(SRC_PATH +"/"+ file);

            if (folder.isFile())
                fileCounter++;

            else if (folder.isDirectory())
                subdirCounter++;
        }
        System.out.println("Directories: "+ subdirCounter);
        System.out.println("Files: "+ fileCounter);
    }

    private Collection<Corpus> readDocs() {

        Collection<Corpus> list = this.reader.read(SRC_PATH +"/sample_us.tsv");

        /* System.out.print(list.size());
        for (Corpus c : list) {
            System.out.println(c);
        } */

        return list;
    }

}