package segments;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SegCollection {

    private final String PATH_READER = "testing/tmp/partial_index_";
    private final String PATH_WRITER = "testing/final/";

    private SegReader[] segReader;          // reader que cada file
    private boolean[] segrState;            // array qure reflete o estado de cada reader
    private String[] segrTerms;             // termo actual de cada reader

    private String term;                    // the smaller Term between all tmp files to be written
    private String value;                   // value that pretence to the smaller Term
    private List<Integer> readerIdList;     // The Reader that will be autorize to do next line method

    private SegWriter segw;                 // responsable to write collection on disk

    private Map<String, String> dic;
    private int nDic;

    public SegCollection(int num) {

        this.segReader = new SegReader[num];
        this.segrState = new boolean[num];
        this.segrTerms = new String[num];

        for (int i =0; i < num; i++) {
            this.segReader[i] = new SegReader(PATH_READER+ i +".txt");
            this.segReader[i].init();
            this.segrTerms[i] = this.segReader[i].getTerm();
            this.segrState[i] = true;
        }

        this.term = "";
        this.value = "";
        this.readerIdList = new ArrayList<>();

        this.segw = new SegWriter(PATH_WRITER);
        this.dic = new LinkedHashMap<>();
        this.nDic = 0;
    }

    /**
     * This function compare them and sets the frist to be written by alfabetic order
     * If terms are equal merge the value associate to each one.
     * @return
     */
    public void calcuteNextTermToWrite() {

        int comparation = 0;
        this.term = "{";    // assume last position

        for(int i=0; i < this.segrTerms.length; i++) {
            if(this.segrState[i] == true)
            {
                comparation = this.term.compareTo(this.segrTerms[i]);
                if ( comparation > 0) {
                    this.readerIdList = new ArrayList<>();
                    this.readerIdList.add(i);
                    this.term = this.segrTerms[i];
                    this.value = this.segReader[i].getPostingList();

                } else if (comparation == 0) {
                    this.readerIdList.add(i);
                    this.value = this.value.replace("]", ", ");
                    this.value += this.segReader[i].getPostingList().replace("[", "");
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
        this.segw.addDoc(this.term, this.value);
        this.dic.put(this.term,  this.segw.getFileName() +"-"+ this.segw.getnLine() );
	}

    public void saveOrderIndexerToDisk() {
        this.segw.saveToDisk();
	}

    public void setNextLineToSelectedReader() {

        for(Integer id : this.readerIdList) {

            this.segReader[id].readNextLine();
            this.segrTerms[id] = this.segReader[id].getTerm();

            if ( this.segrTerms[id] == null ) {
                this.segrState[id] = false;
            }
        }
        this.readerIdList = new ArrayList<>();      // Free References
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

	public void saveDicionaryToDisk() {

        try {
            Writer writer = Files.newBufferedWriter(
                    Paths.get(PATH_WRITER, "Dicionary"+ this.nDic +".txt") );
            
            this.dic.forEach((key, value) -> {
                try {
                    writer.write(key + "=" + value + System.lineSeparator());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.nDic++;
        this.dic = new LinkedHashMap<>();
	}
}