package com.tilldawn.model.enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

public enum Hero {


    DIAMOND("Diamond", 7, 1,
        "Heros/Heros/Diamond/idle/Idle_0 #8328.png",
        "Heros/Heros/Diamond/idle/Idle_1 #8358.png",
        "Heros/Heros/Diamond/idle/Idle_2 #8817.png",
        "Heros/Heros/Diamond/idle/Idle_3 #8455.png",
        "Heros/Heros/Diamond/idle/Idle_4 #8316.png",
        "Heros/Heros/Diamond/idle/Idle_5 #8305.png"),
    DASHER("Dasher", 2, 10,
        "Heros/Heros/Dasher/idle/Idle_0 #8325.png",
   "Heros/Heros/Dasher/idle/Idle_1 #8355.png",
    "Heros/Heros/Dasher/idle/Idle_2 #8814.png",
    "Heros/Heros/Dasher/idle/Idle_3 #8452.png",
    "Heros/Heros/Dasher/idle/Idle_4 #8313.png",
    "Heros/Heros/Dasher/idle/Idle_5 #8302.png"),
    LILITH("Lilith", 5, 3,
        "Heros/Heros/Lilith/idle/Idle_0 #8333.png",
        "Heros/Heros/Lilith/idle/Idle_1 #8363.png",
        "Heros/Heros/Lilith/idle/Idle_2 #8822.png",
        "Heros/Heros/Lilith/idle/Idle_3 #8460.png",
        "Heros/Heros/Lilith/idle/Idle_4 #8321.png",
        "Heros/Heros/Lilith/idle/Idle_5 #8310.png"),
    SCARLET("Scarlet", 3, 5,
        "Heros/Heros/Scarlet/idle/Idle_0 #8327.png",
        "Heros/Heros/Scarlet/idle/Idle_1 #8357.png",
        "Heros/Heros/Scarlet/idle/Idle_2 #8816.png",
        "Heros/Heros/Scarlet/idle/Idle_3 #8454.png",
        "Heros/Heros/Scarlet/idle/Idle_4 #8315.png",
        "Heros/Heros/Scarlet/idle/Idle_5 #8304.png"),
    SHANA("Shana", 4, 4,
        "Heros/Heros/Shana/idle/Idle_0 #8330.png",
        "Heros/Heros/Shana/idle/Idle_1 #8360.png",
        "Heros/Heros/Shana/idle/Idle_2 #8819.png",
        "Heros/Heros/Shana/idle/Idle_3 #8457.png",
        "Heros/Heros/Shana/idle/Idle_4 #8318.png",
        "Heros/Heros/Shana/idle/Idle_5 #8307.png"),
    ;

    private String name;
    private int baseHp;
    private int speed;
    private final String character_idle0;
    private final String character_idle1;
    private final String character_idle2;
    private final String character_idle3;
    private final String character_idle4;
    private final String character_idle5;
    private final Texture character_idle0_tex;
    private final Texture character_idle1_tex;
    private final Texture character_idle2_tex;
    private final Texture character_idle3_tex;
    private final Texture character_idle4_tex;
    private final Texture character_idle5_tex;
    private final Animation<Texture> character_idle_frames;

    Hero(String name, int baseHp, int speed, String character_idle0, String character_idle1, String character_idle2, String character_idle3, String character_idle4, String character_idle5) {
        this.name = name;
        this.baseHp = baseHp;
        this.speed = speed;
        this.character_idle0 = character_idle0;
        this.character_idle1 = character_idle1;
        this.character_idle2 = character_idle2;
        this.character_idle3 = character_idle3;
        this.character_idle4 = character_idle4;
        this.character_idle5 = character_idle5;
        this.character_idle0_tex = new Texture(character_idle0);
        this.character_idle1_tex = new Texture(character_idle1);
        this.character_idle2_tex = new Texture(character_idle2);
        this.character_idle3_tex = new Texture(character_idle3);
        this.character_idle4_tex = new Texture(character_idle4);
        this.character_idle5_tex = new Texture(character_idle5);
        this.character_idle_frames = new Animation<>(0.1f, character_idle0_tex, character_idle1_tex, character_idle2_tex, character_idle3_tex, character_idle4_tex, character_idle5_tex);
    }

    public String getName() {
        return name;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public int getSpeed() {
        return speed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBaseHp(int baseHp) {
        this.baseHp = baseHp;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getCharacter_idle0() {
        return character_idle0;
    }

    public String getCharacter_idle1() {
        return character_idle1;
    }

    public String getCharacter_idle2() {
        return character_idle2;
    }

    public String getCharacter_idle3() {
        return character_idle3;
    }

    public String getCharacter_idle4() {
        return character_idle4;
    }

    public String getCharacter_idle5() {
        return character_idle5;
    }

    public Texture getCharacter_idle0_tex() {
        return character_idle0_tex;
    }

    public Texture getCharacter_idle1_tex() {
        return character_idle1_tex;
    }

    public Texture getCharacter_idle2_tex() {
        return character_idle2_tex;
    }

    public Texture getCharacter_idle3_tex() {
        return character_idle3_tex;
    }

    public Texture getCharacter_idle4_tex() {
        return character_idle4_tex;
    }

    public Texture getCharacter_idle5_tex() {
        return character_idle5_tex;
    }

    public Animation<Texture> getCharacter_idle_frames() {
        return character_idle_frames;
    }
}

