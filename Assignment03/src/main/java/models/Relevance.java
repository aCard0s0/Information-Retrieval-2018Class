package models;

import java.util.HashMap;
import java.util.Map;

public class Relevance {

    private int qId;
    private Map<Integer, Integer> relevancePair;

    public Relevance(int qId) {
        this.qId = qId;
        relevancePair = new HashMap<>();
    }

    public void addPair(int docId, int relevance) {
        this.relevancePair.put(docId, relevance);
    }
}