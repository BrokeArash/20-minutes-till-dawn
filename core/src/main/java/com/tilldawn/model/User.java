package com.tilldawn.model;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id; // Add database ID
    private String username;
    private String password;
    private String sec; // security answer
    private List<ScoreRecord> scoreRecords; // Add this field
    private Texture avatar;

    // Constructor for new users (without ID)
    public User(String username, String password, String sec) {
        this.username = username;
        this.password = password;
        this.sec = sec;
        this.scoreRecords = new ArrayList<>(); // Initialize the list
        this.avatar = GameAssetsManager.getGameAssetsManager().getRandomAvatar();
    }

    // Constructor for existing users (with ID from database)
    public User(int id, String username, String password, String sec) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.sec = sec;
        this.scoreRecords = new ArrayList<>(); // Initialize the list
        this.avatar = GameAssetsManager.getGameAssetsManager().getRandomAvatar();
    }

    // Add method to add a score record
    public void addScoreRecord(ScoreRecord record) {
        if (scoreRecords == null) {
            scoreRecords = new ArrayList<>();
        }
        scoreRecords.add(record);
    }

    // Add method to get score records
    public List<ScoreRecord> getScoreRecords() {
        if (scoreRecords == null) {
            scoreRecords = new ArrayList<>();
        }
        return new ArrayList<>(scoreRecords); // Return a copy to prevent external modification
    }

    // Add method to set score records (useful when loading from database)
    public void setScoreRecords(List<ScoreRecord> scoreRecords) {
        this.scoreRecords = scoreRecords != null ? new ArrayList<>(scoreRecords) : new ArrayList<>();
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSec() {
        return sec;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", sec='" + sec + '\'' +
            ", scoreCount=" + (scoreRecords != null ? scoreRecords.size() : 0) +
            '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id == user.id && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    public void setAvatar(Texture newAvatar) {
        // Dispose old if you want. Or simply replace:
        if (this.avatar != null && this.avatar != newAvatar) {
            this.avatar.dispose();
        }
        this.avatar = newAvatar;
    }

    public Texture getAvatar() {
        return avatar;
    }
}
