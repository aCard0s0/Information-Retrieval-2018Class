package models;

public class DocResult {

    private int docId;
    private String customerId;
    private String reviewId;
    private String productId;

    public DocResult(int docId, String customerId, String reviewId, String productId) {
        this.docId = docId;
        this.customerId = customerId;
        this.reviewId = reviewId;
        this.productId = productId;
    }

    /**
     * @return the productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return the reviewId
     */
    public String getReviewId() {
        return reviewId;
    }

    /**
     * @param reviewId the reviewId to set
     */
    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the docId
     */
    public int getDocId() {
        return docId;
    }

    /**
     * @param docId the docId to set
     */
    public void setDocId(int docId) {
        this.docId = docId;
    }
}