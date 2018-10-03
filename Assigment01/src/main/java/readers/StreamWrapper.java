package readers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import models.*;

public class StreamWrapper {

    // https://www.baeldung.com/java-read-lines-large-file

    private FileInputStream inputStream;
    private Scanner sc;
    private Collection<Corpus> list;

    public StreamWrapper() {
        this.inputStream = null;
        this.sc = null;
        this.list = new ArrayList();
    }


    public Collection<Corpus> read(String path) {

        try {
            inputStream = new FileInputStream(path);
            sc = new Scanner(inputStream, "UTF-8");

            int i = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                //System.out.println(line);

                String[] lines = line.split("\\t");
                list.add(new Corpus(i, lines[5], lines[12], lines[13]));
                i++;
            }

            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

		} finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }

            return this.list;
        }
    }

}