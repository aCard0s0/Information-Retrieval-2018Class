package main;

import magement.ArgsSelector;
import magement.Timer;

/**
 * Frist Assigment for Retrivel Information Class.
 * 
 * @author André Cardoso, 65069 & Ivo Angélico, 41351
 *
 */
public class Main 
{
    public static void main( String[] args )
    {
        
        ArgsSelector opts = new ArgsSelector(args);
        opts.setOptions();
        
        App app = new App(
                        new Timer(),            // init timer
                        opts.getMemory(),       // according with options selected, get Memory obj
                        opts.getCorpusReader(), // according with options selected, get Source File to read
                        opts.getTokenizer(),    // according with options selected, get Tokenizer obj
                        opts.getDicionary()
                    );

        opts.showStartMessage();
        
        if (opts.skipDataMinig()) {  // load dictionary instead
            
        /* Start for reading the source file, creating the Doc and normalizing the Token.
        For memory magement are created paricial_indexer files in tmp folder. */
            app.readSrcFile();

        /* After the source file being treated, this function merge all files in tmp folder
        in alfabetic order and create a dicionary that is mantain the memory and is written 
        is disk for the next use. */
            app.mergeIndexerAndCreateDicionary();
        }
        
        if (!opts.isXMLreader()) {
            app.readUserInputTermsAss2();       /* Assignment 2 */

        } else {
            app.readUserInputTermsAss3();       /* Assignment 3 */
        }
        
        System.exit(0);
    }
}