package com.tilldawn.model.enums;

import com.badlogic.gdx.graphics.Texture;

public enum EnemyEnum {
    TREE("tree", -1, 10, new Texture("Images_grouped_1/Sprite/T/T_TreeMonster_2.png"), 30, 0),
    TENTACLE_MONSTER("tentacle monster", 25, 3, new Texture("Images_grouped_1/Sprite/T/T_TentacleEnemy_1.png"), 20, 10),
    EYE_BAT("eye bat", 50, 10, new Texture("Images_grouped_1/Sprite/T/T_EyeBat_0.png"), 30, 15),
    ELDER("elder", 400, 20, new Texture("Images_grouped_1/Sprite/ElderBrain/ElderBrain.png"), 40, 25),
    ;

    private String name;
    private int hp;
    private int spawnRate;
    private Texture texture;
    private float attackDamage;
    private float attackRange;

    EnemyEnum(String name, int hp, int spawnRate, Texture texture, float attackDamage, float attackRange) {
        this.name = name;
        this.hp = hp;
        this.spawnRate = spawnRate;
        this.texture = texture;
        this.attackDamage = attackDamage;
        this.attackRange = attackRange;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getSpawnRate() {
        return spawnRate;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public float getAttackRange() {
        return attackRange;
    }
}
