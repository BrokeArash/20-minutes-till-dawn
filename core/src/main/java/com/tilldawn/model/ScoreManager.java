package com.tilldawn.model;

import com.tilldawn.model.enums.Mode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreManager {
    private final List<ScoreRecord> allScores = new ArrayList<>();

    /**
     * How the client wants to sort the scoreboard.
     * SCORE = sort by score (default)
     * KILL  = sort by number of kills
     * TIME  = sort by survival time
     */
    public enum SortType {
        SCORE,
        KILL,
        TIME
    }

    /**
     * Add a new score record for a user.
     * @param user     the user who played
     * @param score    the numeric score
     * @param kill     number of kills
     * @param time     survival time (e.g. in seconds)
     * @param gameMode which Mode was played
     */
    public void addScore(User user, int score, int kill, int time, Mode gameMode) {
        ScoreRecord record = new ScoreRecord(user, score, kill, time, gameMode);
        // Make sure the user keeps track of their own ScoreRecord (you'll need to update User.addScoreRecord accordingly)
        user.addScoreRecord(record);
        allScores.add(record);
        // Keep the list sorted by score DESC by default:
        Collections.sort(allScores);
    }

    /**
     * Get top N scores globally, sorted by "score" DESC (default).
     */
    public List<ScoreRecord> getTopNScores(int n) {
        int endIndex = Math.min(n, allScores.size());
        return new ArrayList<>(allScores.subList(0, endIndex));
    }

    /**
     * Get top N scores globally, but sorted by whichever SortType the client chooses.
     * This does not modify the original `allScores` list; it works on a copy.
     */
    public List<ScoreRecord> getTopNScores(int n, SortType sortType) {
        // Make a shallow copy:
        List<ScoreRecord> copy = new ArrayList<>(allScores);

        // Choose comparator:
        Comparator<ScoreRecord> comparator;
        switch (sortType) {
            case KILL:
                comparator = ScoreRecord.byKill();
                break;
            case TIME:
                comparator = ScoreRecord.byTime();
                break;
            case SCORE:
            default:
                comparator = ScoreRecord.byScore();
                break;
        }

        // Sort that copy with the chosen comparator:
        copy.sort(comparator);

        // Return the top N (or fewer if not enough records)
        int endIndex = Math.min(n, copy.size());
        return copy.subList(0, endIndex);
    }

    /**
     * Get top N scores for a specific user, always sorted by score DESC.
     * (If you also want kill/time sorts per-user, you could add overloads similarly.)
     */
    public List<ScoreRecord> getUserTopScores(User user, int n) {
        return user.getScoreRecords().stream()
            .sorted()         // uses compareTo() == by score DESC
            .limit(n)
            .collect(Collectors.toList());
    }

    /**
     * Get all scores sorted by "score" DESC.
     */
    public List<ScoreRecord> getAllScoresSorted() {
        return new ArrayList<>(allScores);
    }
}
