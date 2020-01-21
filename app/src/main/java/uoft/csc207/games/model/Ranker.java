package uoft.csc207.games.model;


import java.util.ArrayList;
import java.util.List;

import uoft.csc207.games.controller.Score;

public class Ranker {
    public ArrayList<Score> scores;
    public Ranker(ArrayList<Score> scores){
        this.scores = scores;
    }

    public void createListByScore(int begin, int end){
        if (begin < end) {
            int partitionIndex = partition(scores, begin, end);

            createListByScore(begin, partitionIndex-1);
            createListByScore(partitionIndex+1, end);
        }
    }

    public void createListByCurrency(int begin, int end){
        if (begin < end) {
            int partitionIndex = partition2(scores, begin, end);

            createListByCurrency(begin, partitionIndex-1);
            createListByCurrency(partitionIndex+1, end);
        }
    }

    private void swap(int a, int b, ArrayList s)
    {
        Object temp = s.get(a);
        if (s.size() > Math.max(a,b)) {
            s.set(a, s.get(b));
            s.set(b, temp);
        }
    }

    private int partition(ArrayList<Score> score_all, int begin, int end) {

        Score pivot = score_all.get(end);
        int i = (begin-1);

        for (int j = begin; j < end; j++) {
            if (score_all.get(j).getPoints() <= pivot.getPoints()) {
                i++;
                swap(i, j, score_all);
            }
        }
        swap(i+1, end, score_all);
        return i+1;
    }
    private int partition2(ArrayList<Score> score_all, int begin, int end) {

        Score pivot = score_all.get(end);
        int i = (begin-1);

        for (int j = begin; j < end; j++) {
            if (score_all.get(j).getMoney() <= pivot.getMoney()) {
                i++;
                swap(i, j, score_all);
            }
        }
        swap(i+1, end, score_all);
        return i+1;
    }

    public void addScore(Score p){
       scores.add(p);
    }

    public int getSize(){
        return scores.size();
    }


}
