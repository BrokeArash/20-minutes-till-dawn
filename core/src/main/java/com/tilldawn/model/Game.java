package com.tilldawn.model;


import com.tilldawn.model.enums.Mode;

public class Game {

    private Mode mode;
    private Player player;

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
}
