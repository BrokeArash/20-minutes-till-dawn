package com.tilldawn.model.enums;


public enum Mode {

    T20("20 minutes", 20),
    T10("10 minutes", 10),
    T5("5 minutes", 5),
    T2("2 minutes", 2),
    ;

    private String name;
    private int time;

    Mode(String name, int time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
