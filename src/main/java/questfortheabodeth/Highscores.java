package main.java.questfortheabodeth;

import java.util.ArrayList;

/**
 * The Highscores class keeps track of all te highscores in the game.
 * When the game is won by the player the players time is passed here and this
 * class decides whether or no to save the score
 */
public class Highscores
{
    private String filename = "res/assets/highscores.txt";
    private FileOperator file;
    private ArrayList<String> scores = new ArrayList<>();
    private int NumOfScores;

    /**
     * Creates a new highscores object
     */
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

    /**
     * Reads the contents of the highscores file into
     * an {@link ArrayList}
     */
    private void readHighscores()
    {
        scores = file.readToList();
    }

    /**
     * Adds a score to the list of scores. If the score is lower than
     * all previous scores and the list is already full, the score is not
     * saved
     *
     * @param score (String) The score to save
     * @return (boolean) Whether or not the score was actually saved
     */
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

    /**
     * Saves the list of highscores held in the class
     * to the highscores file
     */
    public void save()
    {
        // Empty the file first so we can write all the scores back
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

    /**
     * Returns a list of all the scores
     * @return (ArrayList) List of scores
     */
    public ArrayList<String> getScores()
    {
        return scores;
    }

    /**
     * The current number of scores held in the file
     */
    public void readNumOfScores()
    {
        NumOfScores = file.getNumberOfRows();
    }

    /**
     * Sets the maximum amount of scores allowed to be held at any one time,
     * This simulates a leaderboard rather than just all scores
     * @param amount (int) Number of scores to hold
     */
    public void limitHighScores(int amount) {
        while (file.getNumberOfRows() > amount) {
            file.removeLine(file.getNumberOfRows() - 1);
        }
    }
}

