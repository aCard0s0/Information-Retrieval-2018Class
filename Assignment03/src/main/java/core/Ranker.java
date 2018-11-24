package core;

import java.util.ArrayList;
import java.util.Set;
import models.Posting;

public class Ranker {

    private Posting max;
    
    public Ranker() {
        this.max = new Posting();
    }

    public void rankTerm(Set<String> postingList, int nDocs) {

        double idf = Math.log(nDocs/postingList.size());

        Posting post;
        for(String posting : postingList) {
            post = new Posting(posting);
            post.calculateIDF(idf);      // score
            if (max.getTf_idf() < post.getTf_idf()) {
                this.max = post;
            }
            System.out.print("score = "+ post.printScore() +"\n");
        }

        //calcular a soma do score para cada documento
    }
    
    public void printRevelantDocIds() {
        System.out.println("DocId: "+ this.max.getDocId() +" weight: "+ this.max.getWeight() +" idf: "+ this.max.getTf_idf() +" position: "+ this.max.getPositions());
        
    }
}