package com.tilldawn.model.enums;

import com.badlogic.gdx.Input;

public enum Keys {
    UP(Input.Keys.W),
    DOWN(Input.Keys.S),
    LEFT(Input.Keys.A),
    RIGHT(Input.Keys.D),
    RELOAD(Input.Keys.R),
    ;
    int keys;

    Keys(int keys) {
        this.keys = keys;
    }

    public int getKeys() {
        return keys;
    }

    public void setKeys(int keys) {
        this.keys = keys;
    }
}
