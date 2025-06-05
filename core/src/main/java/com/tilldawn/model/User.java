package com.tilldawn.model;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String username;
    private String password;
    private String sec;
    private List<ScoreRecord> scoreRecords;
    private Texture avatar;

    public User(String username, String password, String sec) {
        this.username = username;
        this.password = password;
        this.sec = sec;
        this.scoreRecords = new ArrayList<>();
        this.avatar = GameAssetsManager.getGameAssetsManager().getRandomAvatar();
    }

    public User(int id, String username, String password, String sec) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.sec = sec;
        this.scoreRecords = new ArrayList<>();
        this.avatar = GameAssetsManager.getGameAssetsManager().getRandomAvatar();
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
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
        if (this.avatar != null && this.avatar != newAvatar) {
            this.avatar.dispose();
        }
        this.avatar = newAvatar;
    }

    public Texture getAvatar() {
        return avatar;
    }
}
