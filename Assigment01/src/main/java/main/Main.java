package main;

import magement.Memory;
import magement.Timer;

/**
 *  Frist Assigment for Retrivel Information Class.
 * 
 *  @author André Cardoso, 65069 & Ivo Angélico, 41351
 *
 */
public class Main 
{
    public static void main( String[] args )
    {
        // TODO: improve code to run with args
        boolean runAsSimpleTokenizer = false;
        if (args.length != 1){
            System.err.println("Use: -st as argument to run program apllying Simple Tokenizer");
            System.err.println("Use: -it as argument to run program apllying Improved Tokenizer and Stemmer");
            System.exit(1);
        }

        if (args[0].equals("-st")){
            runAsSimpleTokenizer = true;

        } else if (args[0].equals("-it")){
            runAsSimpleTokenizer = false;

        } else {
            System.err.println("Use: -st as argument to run program apllying Simple Tokenizer");
            System.err.println("Use: -it as argument to run program apllying Improved Tokenizer and Stemmer");
            System.exit(1);
        }

        Memory mem = new Memory(3000, 0.8);         /* Responsavel pela gestão de memoria */
        Timer time = new Timer();                   /* Responsavel por tempo de execução */
        
        App app = new App(mem, time);
        
        /* Start for reading the source file, creating the Doc and normalizing the Token.
        For memory magement are created paricial_indexer files in tmp folder. */
        app.readSrcFile(runAsSimpleTokenizer);

        /* After the source file being treated, this function merge all files in tmp folder
        in alfabetic order and create a dicionary that is mantain the memory and is written 
        is disk for the next use. */
        app.createDicionary();    
        
        System.exit(0);
    }
}