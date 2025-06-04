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

    @Override
    public int compareTo(ScoreRecord other) {
        return Integer.compare(other.score, this.score);
    }

    public static Comparator<ScoreRecord> byScore() {
        return Comparator.comparingInt(ScoreRecord::getScore).reversed();
    }

    public static Comparator<ScoreRecord> byKill() {
        return Comparator.comparingInt(ScoreRecord::getKill).reversed();
    }

    public static Comparator<ScoreRecord> byTime() {
        return Comparator.comparingInt(ScoreRecord::getTime).reversed();
    }

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
