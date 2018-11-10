package core;

import java.util.ArrayList;
import java.util.List;

import models.Doc;

public class DocCollection {

	private List<Doc> docList;

	public DocCollection() {

		this.docList = new ArrayList<>();
	}

	public void addDoc(Doc doc) {
		this.docList.add(doc);
	}

	public void saveIntoDisk() {
		System.out.println("\t *Saving document collection");
	}

	public void freeColReferences() {
		System.out.println("\t *Free memory");
	}
}