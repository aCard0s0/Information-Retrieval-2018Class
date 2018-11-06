package models;

public class Posting {

    private int docId;
    private int nFreq;

    public Posting(int docId, int nFreq) {
        this.setDocId(docId);
        this.setnFreq(nFreq);
    }

    @Override
    public String toString() {
        return getDocId() +":"+ getnFreq();
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
    public int getnFreq() {
        return nFreq;
    }

    /**
     * @param nFreq the nFreq to set
     */
    public void setnFreq(int nFreq) {
        this.nFreq = nFreq;
    }
}