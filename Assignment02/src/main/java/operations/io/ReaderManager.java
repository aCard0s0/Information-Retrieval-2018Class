/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import models.DataType;
import models.Dic;

/**
 *
 * @author aCard0s0
 */
public class ReaderManager {
    
    private final String path;
    private FileInputStream inputStream;
    private BufferedReader reader;
    private String line;
    private final DataType type;
    
    public ReaderManager(DataType type, String path) {
        this.type = type;
        this.path = path;
        this.inputStream = null;
        this.reader = null;
        this.line = null;
    }

    public void init() {
        
        try {
            this.inputStream = new FileInputStream(this.path);
            this.reader = new BufferedReader(new InputStreamReader(this.inputStream, "UTF-8"));
            readNextLine();
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public boolean hasNextLine() {
        return this.line != null && !this.line.isEmpty();
    }
    
    public void readNextLine () {

        try {
            this.line = this.reader.readLine();

        } catch (IOException e) {
            //e.printStackTrace();
            this.line = null;

        } catch (NullPointerException ee){
            //ee.printStackTrace();
            this.line = null;

        } catch (ArrayIndexOutOfBoundsException eee){
            //eee.printStackTrace();
            this.line = null;
        }
    }
    
    public void closeFile() {
        try {
            this.reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLine() {
        return line;
    }
    
    public String getTerm() {
        
        switch (this.type) {
            case DIC: {
                return this.line.split("=")[0];
            }
            default:
                return "";
        }
    }
    
    public Dic toDic() {
        String[] tmp = this.line.split("=")[1].split("-");
        return new Dic(tmp[0], Integer.parseInt(tmp[1]));
    }
}
