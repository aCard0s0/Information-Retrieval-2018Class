package operations.segments;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import magement.Constantes;
import models.Dic;

/**
 *  Não mecher, não existe muito mais para melhorar aqui.
 */
public class SegWriter {
    
    private Map<String, Set<String>> lines;
    private String fileName;
    private int nFile;
    private int nLine;
    private Writer writer;

    public SegWriter() {
        setFolder();
        setFileName();                     // set file name for the next one to be written
        this.lines = new LinkedHashMap<>();   // keep insertion order
        this.nFile = 0;
        this.nLine = 0;
    }
    
    private void setFolder() {
        // write folders if not exist
        File f = new File(Constantes.ORDER_INXDEXER_FOLDER);
        if(!f.exists()) {
            f.mkdirs();
        }
    }

    /**
     * increment the number of the line that will be written
     * 
     * @param key
     * @param value
     */
    public void addLine(String key, Set<String> value) {

        this.lines.put(key, value);
        this.nLine++;
    }

    public void saveToDisk() {

        try {
            this.writer = Files.newBufferedWriter(
                    Paths.get(Constantes.ORDER_INXDEXER_FOLDER, this.fileName) );
            
            this.lines.forEach((key, value) -> {
                try {
                    writer.write(key + "=" + value + System.lineSeparator());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            this.writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.nFile++;
        this.setFileName();                     // set file name for the next one to be written
        this.lines = new LinkedHashMap<>();   // freeReferences
        this.nLine = 0;
    }

    public void setFileName() {
        this.fileName = "order_indexer_" + 
                this.nFile + 
                ".txt";
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getnLine() {
        return this.nLine;
    }
    
    public Dic toDic() {
        return new Dic(getFileName(), getnLine());
    }
    
}