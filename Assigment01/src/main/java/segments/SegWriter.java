package segments;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class SegWriter {
    
    private Map<String, String> newFile;
    private String path;
    private int nFile;
    private String fileName;
    private int nLine;

    public SegWriter(String path) {
        this.path = path;
        this.newFile = new LinkedHashMap<>();
        this.nFile = 0;
        this.setFileName();
        this.nLine = 0;
    }

    /**
     * increment the number of the line that will be written
     * 
     * @param key
     * @param value
     */
    public void addDoc(String key, String value) {

        this.newFile.put(key, value);
        this.nLine++;
    }

    public void saveToDisk() {

        try {
            Writer writer = Files.newBufferedWriter(
                    Paths.get(this.path, this.fileName) );
            
            newFile.forEach((key, value) -> {
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

        this.nFile++;
        this.setFileName();                     // set file name for the next one to be written
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