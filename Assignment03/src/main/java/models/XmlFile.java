package models;

public class XmlFile extends Doc {
    
    private String title;
    private String text;

    public XmlFile(int id, String title, String text) {
        
        super.setId(id);
        this.title = title;
        this.text = text;
    }

    @Override
    public String toTokens() {
        return getText() +" "+ getTitle();
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return super.getId() +super.getSeperator()+ getTitle() +super.getSeperator()+ getText();
    }
}