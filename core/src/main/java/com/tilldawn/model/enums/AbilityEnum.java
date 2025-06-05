package com.tilldawn.model.enums;

import java.util.Random;

public enum AbilityEnum {
    VITALITY("Vitality"),
    DAMAGER("Damager"),
    PROCREASE("Procrease"),
    AMOCREASE("Amocrease"),
    SPEEDY("Speedy"),
    ;
    String name;

    AbilityEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    static AbilityEnum[] abilities = AbilityEnum.values();

    public static AbilityEnum getRandomAbility() {
        return AbilityEnum.abilities[new Random().nextInt(abilities.length)];
    }
}
