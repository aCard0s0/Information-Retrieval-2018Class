package models;

public class Docs {

    private int id;
    private String productTitle;
    private String reviewHeadline;
    private String reviewBody;

    public Docs() {}

    public Docs(int id, String productTitle, String reviewHeadline, String reviewBody) {
        this.id = id;
        this.productTitle = productTitle;
        this.reviewHeadline = reviewHeadline;
        this.reviewBody = reviewBody;
    }

    public String getDocInfo() {
        return getProductTitle() +" "+ getReviewHeadline() +" "+ getReviewBody();
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the reviewBody
     */
    public String getReviewBody() {
        return reviewBody;
    }

    /**
     * @param reviewBody the reviewBody to set
     */
    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    /**
     * @return the reviewHeadline
     */
    public String getReviewHeadline() {
        return reviewHeadline;
    }

    /**
     * @param reviewHeadline the reviewHeadline to set
     */
    public void setReviewHeadline(String reviewHeadline) {
        this.reviewHeadline = reviewHeadline;
    }

    /**
     * @return the productTitle
     */
    public String getProductTitle() {
        return productTitle;
    }

    /**
     * @param productTitle the productTitle to set
     */
    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    @Override
    public String toString() {
        return getId() +";;"+ getProductTitle() +";;"+ getReviewHeadline() +";;"+ getReviewBody();
    }


}