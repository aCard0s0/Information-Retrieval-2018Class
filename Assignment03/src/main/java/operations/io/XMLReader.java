package operations.io;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import magement.Constantes;
import models.Doc;
import models.XmlFile;

public class XMLReader implements CorpusReader {

    private String src;
    private File fodler;
    private String[] files;
    private int docId;

    public XMLReader() {
        this.src = Constantes.XML_SOURCE_Folder;
        this.files = new String[0];
        this.docId = 0;
    }

    public XMLReader(String path) {
        this.src = path;
        this.files = new String[0];
        this.docId = 0;
    }

    @Override
    public void initFile() {
        this.fodler = new File(this.src);
        this.files = this.fodler.list();
    }

    @Override
    public Doc read() {

        // https://stackoverflow.com/questions/7704827/java-reading-xml-file

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        Document document;
        XmlFile result;

        if (this.docId == this.files.length) {
            return null;
        }

        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(this.src + "/" + this.files[this.docId]);

            String title = document.getElementsByTagName("TITLE").item(0).getTextContent();
            String text = document.getElementsByTagName("TEXT").item(0).getTextContent();

            result = new XmlFile(this.docId, title, text);
            this.docId++;

        } catch (ParserConfigurationException | SAXException | IOException e) {
            return null;
        }
        return result;
    }

    @Override
    public void closeFile() {
    }

    @Override
    public int getNumOfDocs() {
        return this.docId;
    }

    @Override
    public String getSrcPath() {
		return this.src;
	}
}