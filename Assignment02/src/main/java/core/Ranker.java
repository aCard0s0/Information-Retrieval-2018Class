package core;

import java.util.Set;
import models.Posting;

public class Ranker {

    public Ranker() {

    }

    public void rankTerm(Set<String> postingList, int nDocs) {

        double df = Math.log(nDocs/postingList.size() + 1);

        Posting post;
        for(String posting : postingList) {
            post = new Posting(posting);
            post.calculateIDF(df);      // score
            System.out.print("score="+post.printScore()+"\n");

        }

        //calcular a soma do score para cada documento
    }
    
    public void printRevelantDocIds() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}