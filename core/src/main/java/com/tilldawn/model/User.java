package com.tilldawn.model;

public class User {

    private String username;

    private String password;

    private String sec;

    private int point;

    public User() {
    }

    public User(String username, String password, String sec) {
        this.username = username;
        this.password = password;
        this.sec = sec;
        this.point = 0;
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

    public int getPoint() {
        return point;
    }

    public void addPoint(int point) {
        this.point += point;
    }
}
