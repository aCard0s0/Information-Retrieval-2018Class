package models;

import java.util.HashSet;
import java.util.Set;

public class Posting {

    private int docId;
    private double weight;
    private double tf_idf;
    private Set<Integer> lposition;

    public Posting(int docId, int nFreq, int position) {

        this.setDocId(docId);
        this.setWeight( 1+Math.log(nFreq) );
        this.tf_idf = 0;
        this.lposition = new HashSet<>();
        this.lposition.add(position);
    }

    public Posting(String posting) {

        String[] tmp = posting.split(":");
        this.setDocId( Integer.parseInt( tmp[0] ));
        String[] tmp2 = tmp[1].split("-");
        this.setWeight( Double.parseDouble( tmp2[0] ));
        this.tf_idf = 0;
        this.lposition = new HashSet<>();
        for(int i=1; i < tmp2.length; i++) {
            lposition.add( Integer.parseInt( tmp2[i] ) );
        }
    }

    public Posting() {
        this.docId = 0;
        this.weight = 0;
        this.tf_idf = 0;
        this.lposition = new HashSet<>();
    }

    public void addPosition(Integer pos) {
        this.lposition.add(pos);
    }

    @Override
    public String toString() {
        return getDocId() +":"+ getWeight() + getPositions();
    }
    
    public String getPositions() {
        String result = "";

        for(Integer pos : this.lposition) {
            result += "-"+ pos;
        }
        return result;
    }

    public String printScore() {
        return getDocId() +":"+ getTf_idf();
    }
    /**
     * @param docId the docId to set
     */
    public void setDocId(int docId) {
        this.docId = docId;
    }

    /**
     * @return the docId
     */
    public int getDocId() {
        return docId;
    }

    /**
     * @return the nFreq
     */
    public double getWeight() {
        return weight;
    }    
    public double getTf_idf() {
        return tf_idf;
    }

    /**
     * @param nFreq the nFreq to set
     */
    private void setWeight(double weight) {
        this.weight = weight;
    }

    public void calculateWeight(double normalization) {
        this.weight = this.weight / normalization;
    }

    public void calculateIDF(double idf) {

        this.tf_idf = this.weight * idf;
    }

    public Set<Integer> getPositionList() {
        return this.lposition;
    }
}