package models;

public class Posting {

    private int docId;
    private int nFreq;

    public Posting(int docId, int nFreq) {

        this.setDocId(docId);
        this.setnFreq(nFreq);
    }

    public Posting(String posting) {

        String[] tmp = posting.split(":");
        this.setDocId( Integer.parseInt( tmp[0] ));
        this.setnFreq( Integer.parseInt( tmp[1] ));
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

	public String logOperation() {
		return this.docId +":"+ 1 + Math.log(this.nFreq);
	}
}