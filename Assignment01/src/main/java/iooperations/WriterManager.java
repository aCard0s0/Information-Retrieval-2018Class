package iooperations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import magement.Constantes;
import models.Posting;

public class WriterManager {

    

    private FileOutputStream fos;
    private FileChannel channel;
    private File folder;
    private Writer writer;

    public void write() {
        
    }

    /**
     * ************* EXPLORE OPTIONS *************************
     */
    
    public void close() {
        try {
            fos.close();
            channel.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void init2(String filename) {

        try {
            fos = new FileOutputStream(filename);
            channel =  fos.getChannel();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void write2(String line) {

        try {
            byte[] strBytes = line.getBytes();                          // Note: tirar instanciação de variaveis
            ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
            buffer.put(strBytes);
            buffer.flip();          // prepare the channel-write
            channel.write(buffer);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void close2() {
        try {
            fos.close();
            channel.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public void test2() {

        try {
            File fout = new File("myOutFile.txt");
            FileOutputStream fos = new FileOutputStream(fout);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            osw.write("Soe content ...");
            osw.close();

          } catch (FileNotFoundException e) {
            // File not found
            e.printStackTrace();
          } catch (IOException e) {
            // Error when writing to the file
            e.printStackTrace();
          }
    }

    public void test3() {

        try {
            File fout = new File("myOutFile.txt");
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write("Write somthing to the file ...");
            bw.newLine();
            bw.close();

          } catch (FileNotFoundException e){
            // File was not found
            e.printStackTrace();
          } catch (IOException e) {
            // Problem when writing to the file
            e.printStackTrace();
          }
    }

    
}