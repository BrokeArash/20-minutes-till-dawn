package com.tilldawn.model;

import com.tilldawn.model.enums.Mode;

import java.util.Comparator;

public class ScoreRecord implements Comparable<ScoreRecord> {
    private final User user;
    private final int score;
    private final int kill;
    private final int time;
    private long timestamp;
    private final Mode gameMode;

    public ScoreRecord(User user, int score, int kill, int time, Mode gameMode) {
        this.user = user;
        this.score = score;
        this.kill = kill;
        this.time = time;
        this.gameMode = gameMode;
        this.timestamp = System.currentTimeMillis();
    }

    // Default compareTo: sort by score descending.
    @Override
    public int compareTo(ScoreRecord other) {
        return Integer.compare(other.score, this.score);
    }

    // === Static comparators for alternative sorts ===

    /** Comparator that sorts by score descending (higher score first). */
    public static Comparator<ScoreRecord> byScore() {
        return Comparator.comparingInt(ScoreRecord::getScore).reversed();
    }

    /** Comparator that sorts by kill descending (more kills first). */
    public static Comparator<ScoreRecord> byKill() {
        return Comparator.comparingInt(ScoreRecord::getKill).reversed();
    }

    /** Comparator that sorts by time descending (longer survival time first). */
    public static Comparator<ScoreRecord> byTime() {
        return Comparator.comparingInt(ScoreRecord::getTime).reversed();
    }

    // ========== Getters ==========

    public User getUser() {
        return user;
    }

    public int getScore() {
        return score;
    }

    public int getKill() {
        return kill;
    }

    public int getTime() {
        return time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Mode getGameMode() {
        return gameMode;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
