/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import magement.Constantes;
import models.DataType;
import models.Dic;
import operations.io.ReaderManager;
import operations.segments.SegReader;

/**
 *      This Structure doesn't need to maintain insertion order.
 * @author aCard0s0
 */
public class Dictionary {
    
    private Map<String, Dic> dic;               // Dicionario completo
    private Map<String, SegReader> segrList;    // Lista de Segmentos
    private int nDocs;
    private File f;
    private ReaderManager reader;
    private String srcPath;
    
    public Dictionary(String path) {
        setFolder();
        this.srcPath = path;
        this.dic = new HashMap<>();
        loadFromDisk(path);
        this.segrList = new HashMap<>();
    }

    public Dictionary() {
        setFolder();
        this.dic = new HashMap<>();
        this.srcPath = Constantes.DICIONARY_FOLDER;
        this.reader = null;
        this.segrList = new HashMap<>();
    }

    private void setFolder() {
        // write folders if not exist
        f = new File(Constantes.DICIONARY_FOLDER);
        if(!f.exists()) {
            f.mkdirs();
        }
    }
    
    /**
     *  Dictionary File Structure:
     *      File name: nDoc_id.dic;
    *       File content: Term=SegmentFileName-lineNumber;
    * 
     * @param path from the file or folder with dictionary structure
     */
    private void loadFromDisk(String path) {
        
        f = new File(path);
        
        if (f.isFile()) {
            load(path +"/"+ f);
            setnDoc(f.getName());
            
        } else {
            if (f.isDirectory()) {
                for(String fname : f.list()) {
                    if (fname.endsWith(Constantes.DICIONARY_EXTENSION)) {
                        load(path +"/"+ fname);
                        setnDoc(fname);
                    }
                }
            }
        }
    }
    
    private void setnDoc(String name) {
        this.nDocs = Integer.parseInt(name.split("_")[0]);
    }

    
    
    private void load(String path) {
        
        this.reader = new ReaderManager(DataType.DIC, path);
        this.reader.init();
        do {
            this.dic.put(reader.getTerm(), reader.toDic());     // bad programming
            this.reader.readNextLine();
        } while(this.reader.hasNextLine());
    }
    
    /**
     *  Used to set the dictionary in memory in mining is done.
     * @param term
     * @param dic 
     */
    public void addTerm(String term, Dic dic) {
        this.dic.put(term, dic);
    }
    
//    public void loadOrderIndexerFor(String[] queryTerms) {
//
//        String value;
//        String[] dicInfo = value.split("-");
//        
//        for(String term : queryTerms) {
//            
//            value = this.dic.get(term);
//            dicInfo = value.split("-");
//            
//            if (!this.segtList.containsKey(dicInfo[0])){
//                SegReader segr = new SegReader(Constantes.ORDER_INXDEXER_FOLDER + dicInfo[0]);
//                this.segtList.put(dicInfo[0], segr);
//            }
//        }
//        
//        this.segtList.init();   // already read the 1º line
//        
//        System.out.println(this.segtList.getTerm());
//        System.out.println(this.segtList.getPostingList());
//
//        calculateDF(this.segtList.getPostingList());
//    }

    public boolean hasSegmentInMem(String term) {
        
        Dic location = this.dic.get(term);
        if(segrList.size()==0)
        {
            return false;
        }
        return segrList.containsKey(location.getFileName());
    }

    public void freeSegLessUsed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void loadSegmentToMem(String term) {
        
        Dic location = this.dic.get(term);

        if (!this.segrList.containsKey( location.getFileName() )){

            SegReader segr = new SegReader(Constantes.ORDER_INXDEXER_FOLDER + location.getFileName());
            this.segrList.put(location.getFileName(), segr);
        }

    }

    public Set<String> postingList(String term) {

        Dic location = this.dic.get(term);

        SegReader segr = segrList.get(location.getFileName());
        segr.jumpToLine(location.getLine());    // test this

        return segr.getPostingList();
    }
    
    public String getSrcPath() {
        return this.srcPath;
    }

    public String showStatus() {
        return "Dic: "+ this.dic.size() +" & SegReaderList: "+ this.segrList.size();
    }

    public boolean hasTerm(String term) {
        return this.dic.containsKey(term);
    }

	public int getNDoc() {
		return this.nDocs;
    }
    
    public void setnDoc(int ndoc) {
        this.nDocs = ndoc;
    }

	public void print() {

        // Order printing
        Object[] keys = this.dic.keySet().toArray();
        Arrays.sort(keys);
        for (Object key : keys) {
            System.out.println(key + "=" + this.dic.get(key));
        }
	}
}
