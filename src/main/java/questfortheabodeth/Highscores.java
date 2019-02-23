package main.java.questfortheabodeth;

import java.util.ArrayList;

public class Highscores
{
    private String filename = "res/assets/highscores.txt";
    private FileOperator file;
    private ArrayList<String> scores = new ArrayList<>();
    private int NumOfScores;

    public Highscores()
    {
        file = new FileOperator(filename);
        if (!file.isAvailable()) {
            System.err.println("Highscores unavailable");
            return;
        }

        readHighscores();
        readNumOfScores();
        limitHighScores(5);
    }

    private void readHighscores()
    {
        scores = file.readToList();
    }

    public boolean addScore(String score)
    {
        for(int i = 0; i < scores.size(); i++) {
            if (scores.get(i).compareTo(score) > 0) {
                scores.add(i, score);
                return true;
            }
        }

        if (scores.size() < Settings.MAX_SCORES) {
            scores.add(score);
        }

        return false;
    }

    public void save()
    {
        file.clearFile();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < scores.size(); i++) {
            builder.append(scores.get(i));
            if (i < scores.size() - 1) {
                builder.append("\n");
            }
        }

        file.writeNewLine(builder.toString());
    }

    public ArrayList<String> getScores()
    {
        return scores;
    }

    public static void main(String[] args)
    {
        Highscores h = new Highscores();
        h.readHighscores();
        for (String s: h.scores) {
            System.out.println(s);
        }
        System.out.println("###########");

        h.addScore("04:12:27");
        for (String s: h.scores) {
            System.out.println(s);
        }

        System.out.println("###########");
        h.save();
        h.readHighscores();
        for (String s: h.scores) {
            System.out.println(s);
        }
    }

    public void readNumOfScores() {
        NumOfScores = file.getNumberOfRows();
    }

    public void limitHighScores(int amount) {
        while (file.getNumberOfRows() > amount) {
            file.removeLine(file.getNumberOfRows() - 1);
        }
    }
}

