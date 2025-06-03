package com.tilldawn.model;

import com.tilldawn.model.enums.Mode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {

    private String username;

    private String password;

    private String sec;

    private final List<ScoreRecord> scoreRecords = new ArrayList<>();

    public User() {
    }

    public User(String username, String password, String sec) {
        this.username = username;
        this.password = password;
        this.sec = sec;
    }

    public void addScoreRecord(ScoreRecord rec) {
        scoreRecords.add(rec);
        // (Optional) keep the user‐side list sorted as well:
        Collections.sort(scoreRecords);
    }

    public List<ScoreRecord> getScoreRecords() {
        return scoreRecords;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public int getMaxScore() {
        if (App.getScoreManager().getUserTopScores(this, 1).isEmpty()) return 0;
        return App.getScoreManager().getUserTopScores(this, 1).get(0).getScore();
    }

}
