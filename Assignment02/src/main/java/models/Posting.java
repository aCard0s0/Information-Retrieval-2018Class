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
        this.setWeight( Double.parseDouble( tmp[1] ));
    }

    @Override
    public String toString() {
        return getDocId() +":"+ getWeight();
    }
    public String printScore() {
        return getDocId() +":"+ getIdf();
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
    public double getIdf() {
        return idf;
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

    public void calculateIDF(double df) {

        this.idf = this.weight * df;
    }

}