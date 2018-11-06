package segments;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import magement.Constantes;

/**
 *  Não mecher, não existe muito mais para melhorar aqui.
 */
public class SegWriter {
    
    private Map<String, Set<String>> newFile;
    private String fileName;
    private int nFile;
    private int nLine;
    private Writer writer;

    public SegWriter() {
        this.newFile = new LinkedHashMap<>();   // order hashmap
        this.nFile = 0;
        this.nLine = 0;

        // write folders if not exist
        File f = new File(Constantes.COMPLETE_INXDEXER_FOLDER);
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
    public void addDoc(String key, Set<String> value) {

        this.newFile.put(key, value);
        this.nLine++;
    }

    public void saveToDisk() {

        this.setFileName();                     // set file name for the next one to be written
        try {
            this.writer = Files.newBufferedWriter(
                    Paths.get(Constantes.COMPLETE_INXDEXER_FOLDER, this.fileName) );
            
            this.newFile.forEach((key, value) -> {
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
        this.newFile = new LinkedHashMap<>();   // freeReferences
        this.nLine = 0;
    }

    /**
     *  This function also reset the number of lines.
     * @param nFile the number of the new file
     */
    public void setFileName() {
        this.fileName = "order_indexer_" + this.nFile + ".txt";
    }

	public String getFileName() {
		return this.fileName;
	}

	public int getnLine() {
		return this.nLine;
	}

}