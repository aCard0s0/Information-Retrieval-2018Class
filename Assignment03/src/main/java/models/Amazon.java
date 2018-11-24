package models;

public class Amazon extends Doc{

    private String productTitle;
    private String reviewHeadline;
    private String reviewBody;

    public Amazon(int id, String productTitle, String reviewHeadline, String reviewBody) {
        
        super.setId(id);
        this.productTitle = productTitle;
        this.reviewHeadline = reviewHeadline;
        this.reviewBody = reviewBody;
    }

    @Override
    public String toTokens() {
        return getProductTitle() +" "+ getReviewHeadline() +" "+ getReviewBody();
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public String getReviewHeadline() {
        return reviewHeadline;
    }

    public String getProductTitle() {
        return productTitle;
    }

    @Override
    public String toString() {
        return super.getId() +super.getSeperator()+ getProductTitle() +super.getSeperator()+ getReviewHeadline() +super.getSeperator()+ getReviewBody();
    }
}