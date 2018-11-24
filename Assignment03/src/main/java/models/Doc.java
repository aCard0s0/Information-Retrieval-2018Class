package models;

public abstract class Doc {

    private final String SEPARATOR = ";;";

    private int id;

    public String toTokens() {
        return "";
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

    public String getSeperator() {
        return SEPARATOR;
    }

}