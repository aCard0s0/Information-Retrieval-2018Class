package core;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import magement.Constantes;
import models.Dic;
import operations.segments.SegReader;
import operations.segments.SegWriter;

public class SegCollection {

    private final String FINAL_POSITION = "{";

    private final SegReader[] segReader;          // reader que cada file
    private final boolean[] segrState;            // array qure reflete o estado de cada reader
    private final String[] segrTerms;             // termo actual de cada reader

    private String term;                    // the smaller Term between all tmp files to be written
    private Set<String> value;                   // value that pretence to the smaller Term
    private Set<Integer> readerIdList;     // The Reader that will be autorize to do next line method

    private final SegWriter segw;                 // responsable to write collection on disk

    private Map<String, String> dic;
    private int nDic;
    private Writer writer;

    public SegCollection(int num) {

        this.segReader = new SegReader[num];
        this.segrState = new boolean[num];
        this.segrTerms = new String[num];

        for (int i =0; i < num; i++) {
            this.segReader[i] = new SegReader(
                Constantes.UNORDER_INDEXER_FOLDER +"parcial_index_"+ i +".txt");
            this.segReader[i].init();   // alreadey read the next line
            this.segrTerms[i] = this.segReader[i].getTerm(); // set the term to read
            this.segrState[i] = true;   // when reach the limit is set to False
        }

        this.term = "";
        this.value = null;
        this.readerIdList = new HashSet<>();

        this.segw = new SegWriter();
        this.dic = new LinkedHashMap<>();
        this.nDic = 0;
    }

    /**
     * This function compare them and sets the frist to be written by alfabetic order
     * If terms are equal merge the value associate to each one.
     * @return
     */
    public void calculateNextTermToWrite() {

        int comparation = 0;
        this.term = FINAL_POSITION;    // assume last position

        for(int i=0; i < this.segrTerms.length; i++) {
            if(this.segrState[i] == true)
            {
                comparation = this.term.compareTo(this.segrTerms[i]);
                if ( comparation > 0) {
                    this.readerIdList = new HashSet<>();
                    this.readerIdList.add(i);
                    this.term = this.segrTerms[i];
                    this.value = this.segReader[i].getPostingList();

                } else if (comparation == 0) {      // this block is only executed after at least one time the preview block was executed
                    //System.out.println("T: "+ this.term +" i: "+ i);
                    //System.out.println("Value: "+ this.value.size());
                    //https://stackoverflow.com/questions/9062574/is-there-a-better-way-to-combine-two-string-sets-in-java
                    this.readerIdList.add(i);
                    this.value = Stream.concat(
                        this.value.stream(), this.segReader[i].getPostingList().stream()
                    ).collect( Collectors.toSet() );
                }
            }
        }
    }
    
    /**
     *  This function will add the Term and Value to the HashMap structure that will be written in case
     * the mem reach the limit. Also will add them to dicionary that will be allways in memory.
     * The dicionary struture will be:
     *      Term=order_indexer_N.txt-line_number
     */
    public void setSegWriter() {

        this.segw.addLine(this.term, this.value);
        this.dic.put(this.term,  this.segw.getFileName() +"-"+ this.segw.getnLine() );  // TODO: to delete
    }

    public String getTerm() {
        return this.term;
    }
    
    public Set<String> getValue() {
        return this.value;
    }
    
    public Dic toDic() {
        return new Dic(this.segw.getFileName(), this.segw.getnLine());
    }
    
    public void setNextLineToSelectedReader() {

        for(Integer id : this.readerIdList) {

            this.segReader[id].readNextLine();
            this.segrTerms[id] = this.segReader[id].getTerm();

            if ( this.segrTerms[id] == null ) {
                this.segrState[id] = false;
            }
        }
        this.readerIdList = new HashSet<>();      // Free References
	}

    /**
     * @return true if at least one reader still active, 
     * false when all reader are finish reading the file.
     */
    public boolean readerHasDocToRead() {
        for (boolean rs : this.segrState) {
            if (rs) return true;
        }
        return false;
    }

    public void saveOrderIndexerToDisk() {
        this.segw.saveToDisk();
    }
    
    /**
     *  Preve-se uma class dedicada ao Dicionario
     */
    public void saveDicionaryToDisk(int filename) {

        try {
            this.writer = Files.newBufferedWriter(
                    Paths.get(Constantes.DICIONARY_FOLDER, filename +"_"+ this.nDic +".dic") 
            );
            this.dic.forEach((key, value) -> {
                try {
                    this.writer.write(key + "=" + value + System.lineSeparator());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            this.writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.nDic++;
        this.dic = new LinkedHashMap<>();
    }

    public Map<String, String> getDicionary() {
        return this.dic;
    }

    
}