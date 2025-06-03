package com.tilldawn.model;


import com.tilldawn.model.enums.Mode;

public class Game {

    private Mode mode;
    private Player player;
    private float timeRemaining;
    private boolean timerActive;
    private String status;
    private int kill;
    private int time;

    public void startGame() {
        timeRemaining = mode.getTime() * 60f;
        timerActive = true;
    }

    public void updateTime(float delta) {
        if (timerActive && timeRemaining > 0) {
            timeRemaining -= delta;
            if (timeRemaining <= 0) {
                timeRemaining = 0;
                timerActive = false;
                System.out.println("TIME'S UP!");
            }
        }
    }
    public String getFormattedTime() {
        if (timeRemaining <= 0) return "00:00";

        int minutes = (int) (timeRemaining / 60);
        int seconds = (int) (timeRemaining % 60);
        int milliseconds = (int) ((timeRemaining - (minutes * 60) - seconds) * 100);

        // For tense moments: show milliseconds when under 10 seconds
        if (timeRemaining < 10) {
            return String.format("%01d:%02d:%02d", minutes, seconds, milliseconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public float getTimeRemaining() {
        return timeRemaining;
    }

    public boolean isTimerActive() {
        return timerActive;
    }

    public int getTime() {
        return (mode.getTime() * 60) - (int) timeRemaining;
    }

    public int getKill() {
        return kill;
    }

    public void addKill() {
        this.kill ++;
    }

    public String getStatus() {
        return status;
    }

    public int getScore() {
        return getKill() * getTime();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
