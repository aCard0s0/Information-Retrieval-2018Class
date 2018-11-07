package rating;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import magement.Constantes;
import segments.SegReader;
import segments.SegWriter;

public class SegController {

    private SegReader segReader;
    private SegWriter segWritter;
    private Set<String> newPostingList;
    private String[] files;

    public SegController() {

        files = new File(Constantes.ORDER_INXDEXER_FOLDER).list();
        this.segReader = new SegReader(Constantes.ORDER_INXDEXER_FOLDER+files[0]);
        
        this.segWritter = new SegWriter();
        this.newPostingList = new HashSet<>();
        
    }

	public boolean existSegToRead() {
		return !this.files[this.files.length-1].equals(this.segReader.getFileName());
	}

    /**
     *  Atenção estamos a assumir que lê um ficheiro inteiro sem faltar memoria.
     */
	public void computeLog() {

        /* segReader.init();   // already read 1º line
        while(segReader.hasNextLine())

            newPostingList = new HashSet<>();
            for(String posting : segReader.getPostingList()){

            newPostingList.add(
                new Posting(posting).logOperation()
            );
            segReader.readNextLine();
        }
        segWritter.addDoc(segReader.getTerm(), newPostingList); */
	}

	public void saveOrderIndexerToDisk() {
	}

    
}