/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author aCard0s0
 */
public class Dic {
    
    private String fileName;
    private int line;

    public Dic(String term, String fileName, int line) {
        this.fileName = fileName;
        this.line = line;
    }
    
    public Dic(String fileName, int line) {
        this.fileName = fileName;
        this.line = line;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLine() {
        return line;
    }
    
    @Override
    public String toString() {
        return this.fileName +"-"+ this.line;
    }
}
