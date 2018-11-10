package magement;


import core.Dictionary;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import operations.io.CorpusReader;
import core.tokenizer.ImprovedTokenizer;
import core.tokenizer.SimpleTokenizer;
import core.tokenizer.StopWords;
import core.tokenizer.Tokenizer;

public class ArgsSelector {

    private final String[] args;
    private Memory mem;             /** Responsavel pela gestão de memoria */
    private CorpusReader creader;   /** Responsavel pela leitura do ficheira e criação de Docs*/
    private Tokenizer tokens;       /** Responsavel por normalizar os termos */
    private StopWords stpwds;
    private Dictionary dic;          /** Responsavel pelo gestão do dicionario */
    
    private Options opts;
    private final HelpFormatter help;
    private boolean skipDataMining;

    public ArgsSelector(String[] args) {
        this.args = args;
        this.mem = null;
        this.creader = null;
        this.tokens = null;
        this.stpwds = null;
        this.dic = null;
        this.opts = null;
        this.help = new HelpFormatter();
        this.skipDataMining = true;
    }

    public void setOptions() {

        if ( this.args.length != 0 ){
            addOptions();
            parseOptions();
        } 
    }
    

    /**     Quando -m [value], dá erro.
     * 
     *  Check if options inputted by the user are valid.
     * @return  true if option exist, false otherwise.
     */
    private boolean isInputOptionsValid() {

        for(String arg : args) {
            if (!this.opts.hasOption(arg)){
                printHelp();
                return false;
            }
        }
        return true;
    }

    /**
     *  Options avaible for the user.
     *  TODO: 
     *      *add output file path for indexer
     *      *add output file path for dicionary
     *      *add read dicionary path, note that this option will need two inputs. Existing indexer and dicinary path
     *      *add source file for stopwords
     */
    private void addOptions() {

        this.opts = new Options();

        this.opts.addOption(
            OptionBuilder.withDescription("It use an Improved Tokenizer to generate the terms and Stopword.")
                        .withArgName("Improved Tokenizer")
                        .create("it")
        );
        this.opts.addOption(
            OptionBuilder.withDescription("Sets the max memory to be use.")
                        .hasArg()
                        .create("m")
        );

        this.opts.addOption("cr", true, "Source .tsv file for doc creation");
        this.opts.addOption("d", false, "Load default location the dicionary file to memory if not especify");

        this.opts.addOption(
            OptionBuilder.withLongOpt("version")
                        .withDescription("Print the version of the application")
                        .create('v')
        );
        this.opts.addOption("h", false, "Print usage menu");
    }

    private void parseOptions() {

        try {
            CommandLine cmd = new DefaultParser().parse(this.opts, this.args);

            if (cmd.hasOption("h")) {
                printHelp();
            }
            if (cmd.hasOption("v")) {
                System.out.println("version 0.2");
                System.exit(0);
            }
            if (cmd.hasOption("m")) {
                this.mem = new Memory( Integer.parseInt(cmd.getOptionValue("m", String.valueOf(Constantes.MEMORY))), Constantes.PERCENTAGEM );
            }
            if (cmd.hasOption("cr")) {
                this.creader = new CorpusReader( cmd.getOptionValue("cr") );
            }
            if (cmd.hasOption("d")) {
                setToSkipDataMinig();
                this.dic = new Dictionary( cmd.getOptionValue("d", Constantes.DICIONARY_FOLDER) );
            }
            if (cmd.hasOption("it")) {
                this.stpwds = new StopWords();
                this.stpwds.readStoptWords();
                this.tokens = new ImprovedTokenizer( this.stpwds.getStopWords() );
            }

        } catch (ParseException e) {
            System.err.println("Error parsing the options.");
            //e.printStackTrace();
            printHelp();
            System.exit(1);
        }
    }

    /**
     * @return the tokenizer manager, if null will create with the default Simple Tokenizer
     */
    public Tokenizer getTokenizer() {

        if (this.tokens == null) {
            tokens = new SimpleTokenizer();
        }
        return tokens;
    }

    /**
     * @return the memory manager, if null will create with default values
     */
    public Memory getMemory() {

        if (this.mem == null) {
            mem = new Memory();
        }
        return mem;
    }

    /**
     * @return the corpus reader, if null will create with default values
     */
    public CorpusReader getCorpusReader() {

        if (this.creader == null) {
            this.creader = new CorpusReader();
        }
        return this.creader;
    }
    
    /**
     * Default message when aren't arguments.
     */
    public void showStartMessage() {

        String msg ="*Default usage:\n"+
                    "\tMax Memory: "+ this.mem.getUsageMem() +"\n"+
                    "\tFlush at: "+ this.mem.getFlushPercentage() +"\n";
        
        if (skipDataMinig()) {
            msg += "\tSource file: "+ this.creader.getSrcPath() +"\n";

            if (this.tokens instanceof SimpleTokenizer){
                msg += "\tUsing Simple Tokenizer\n";

            } else if (this.tokens instanceof ImprovedTokenizer) {
                msg += "\tUsing Improved Tokenizer with Stop Words\n";
            }
        } else {
            msg += "\tSkip data minig... \n"
                   +"\tLoading dictionary from: "+ this.dic.getSrcPath() +"\n";
        }
        System.out.print(msg);
    }

    /**
     *  Print help menu usage
     */
    private void printHelp() {
        this.help.printHelp(
                    "java -jar name.jar", 
                    "Assignment from Information retrivel class at Aveiro's University\n", 
                    this.opts, 
                    "\nDeveloper by: André Cardoso 65069 & Ivo Angélico 41351", 
                    true
        );
        System.exit(0);
    }

    public Dictionary getDicionary() {
        if (this.dic == null) {
            this.dic = new Dictionary();
        }
        return this.dic;
    }
    
    private void setToSkipDataMinig() {
        this.skipDataMining = false;
    }
    
    public boolean skipDataMinig() {
        return this.skipDataMining;
    }
}