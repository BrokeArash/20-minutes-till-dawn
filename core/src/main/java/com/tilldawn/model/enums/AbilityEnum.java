package com.tilldawn.model.enums;

import java.util.Random;

public enum AbilityEnum {
    VITALITY("Vitality", "adds 1 hp"),
    DAMAGER("Damager", "adds 25% to your gun damage for 10 secs"),
    PROCREASE("Procrease", "adds a projectile"),
    AMOCREASE("Amocrease", "adds 5 to your gun ammo"),
    SPEEDY("Speedy", "doubles your speed for 10 secs"),
    ;
    String name;
    String detail;

    AbilityEnum(String name, String detail) {
        this.name = name;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    static AbilityEnum[] abilities = AbilityEnum.values();

    public static AbilityEnum getRandomAbility() {
        return AbilityEnum.abilities[new Random().nextInt(abilities.length)];
    }
}
