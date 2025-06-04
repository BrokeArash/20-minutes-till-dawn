package com.tilldawn.model;

import com.tilldawn.model.enums.Mode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreManager {
    private List<ScoreRecord> allScores = new ArrayList<>();

    public void addScore(User user, int score, int kill, int time, Mode gameMode) {
        ScoreRecord record = new ScoreRecord(user, score, kill, time, gameMode);
        user.addScoreRecord(record);
        allScores.add(record);
        Collections.sort(allScores); // Keep sorted
    }

    // Get top N scores globally
    public List<ScoreRecord> getTopNScores(int n) {
        int endIndex = Math.min(n, allScores.size());
        return new ArrayList<>(allScores.subList(0, endIndex));
    }

    // Get top N scores for a specific user
    public List<ScoreRecord> getUserTopScores(User user, int n) {
        return user.getScoreRecords().stream()
            .sorted()
            .limit(n)
            .collect(Collectors.toList());
    }

    // Get all scores sorted
    public List<ScoreRecord> getAllScoresSorted() {
        return new ArrayList<>(allScores);
    }
}
