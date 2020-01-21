package uoft.csc207.games.controller.scoreboard;


import java.util.ArrayList;

import uoft.csc207.games.model.Score;

class Ranker {
    /**
     * Sorts and ArrayList of Scores
     *
     * Fields:
     * scores: ArrayList<Score> - Stores the scores to be sorted
     */

    private ArrayList<Score> scores;
    Ranker(ArrayList<Score> scores){
        this.scores = scores;
    }

    /**
     * Sort scores by Points
     * @param begin: start index
     * @param end: last index
     */
    void createListByScore(int begin, int end){
        if (begin < end) {
            int partitionIndex = partition(scores, begin, end);

            createListByScore(begin, partitionIndex-1);
            createListByScore(partitionIndex+1, end);
        }
    }

    /**
     * Sort scores by Money
     * @param begin: start index
     * @param end: last index
     */
    void createListByCurrency(int begin, int end){
        if (begin < end) {
            int partitionIndex = partition2(scores, begin, end);

            createListByCurrency(begin, partitionIndex-1);
            createListByCurrency(partitionIndex+1, end);
        }
    }

    /**
     * Swap two items in ArrayList
     * @param a: index of item one
     * @param b: index of item two
     * @param s: ArrayList with items
     */
    private void swap(int a, int b, ArrayList s)
    {
        Object temp = s.get(a);
        if (s.size() > Math.max(a,b)) {
            s.set(a, s.get(b));
            s.set(b, temp);
        }
    }

    /**
     * Helper method for Sortng by Points
     * @param score_all: ArrayList being sorted
     * @param begin: start index
     * @param end: last index
     * @return - update sub ArraList of Scores
     */
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

    /**
     * Helper method for Sortng by Money
     * @param score_all: ArrayList being sorted
     * @param begin: start index
     * @param end: last index
     * @return - update sub ArraList of Scores
     */
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

    /**
     * @return - ArrayList of Scores
     */
    ArrayList<Score> getScores() {
        return scores;
    }
}
