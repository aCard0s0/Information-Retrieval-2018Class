package models;

public class Posting {

    private int docId;
    private double weight;
    private double idf;

    public Posting(int docId, int nFreq) {

        this.setDocId(docId);
        this.setWeight( 1+Math.log(nFreq) );
    }

    public Posting(String posting) {

        String[] tmp = posting.split(":");
        this.setDocId( Integer.parseInt( tmp[0] ));
        this.setWeight( Integer.parseInt( tmp[1] ));
    }

    @Override
    public String toString() {
        return getDocId() +":"+ getWeight();
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

    /**
     * @param nFreq the nFreq to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void calculateWeight(double normalization) {
        this.weight = this.weight / normalization;
    }

    public void calculateIDF(double df) {
        this.idf = this.weight * df;
    }

}