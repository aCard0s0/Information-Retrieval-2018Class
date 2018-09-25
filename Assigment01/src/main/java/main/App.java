package main;

import java.io.File;

public class App {

    public static final String SRC_PATH = "testing";

    private File src;
    private File folder;
    private int fileCounter; 
    private int subdirCounter;

    public App() {
        this.src = new File(SRC_PATH);
        this.fileCounter = 0; 
        this.subdirCounter = 0;
    }

    /**
     *  Start the app doing frist a directory evaluation.
     */
    public void start() {

        workEvaluation();
        
        readDocs();
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

    private void readDocs() {

    }

}