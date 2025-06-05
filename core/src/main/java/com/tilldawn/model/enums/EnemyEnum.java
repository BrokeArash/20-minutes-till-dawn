package com.tilldawn.model.enums;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public enum EnemyEnum {
    TENTACLE_MONSTER("tentacle monster",    25,  3, new String[]{
        "Images_grouped_1/Sprite/TentacleIdle/TentacleIdle0.png",
        "Images_grouped_1/Sprite/TentacleIdle/TentacleIdle1.png",
        "Images_grouped_1/Sprite/TentacleIdle/TentacleIdle2.png",
        "Images_grouped_1/Sprite/TentacleIdle/TentacleIdle3.png",
    }, 1, 10),

    EYE_BAT("eye bat",                      50, 10, new String[]{
        "Images_grouped_1/Sprite/T/T_EyeBat_0.png",
        "Images_grouped_1/Sprite/T/T_EyeBat_1.png",
        "Images_grouped_1/Sprite/T/T_EyeBat_2.png",
        "Images_grouped_1/Sprite/T/T_EyeBat_3.png",
    }, 1, 10),

    ELDER("elder",                         400, -1, new String[]{
        "Images_grouped_1/Sprite/EyeMonster/EyeMonster_0.png",
        "Images_grouped_1/Sprite/EyeMonster/EyeMonster_1.png",
        "Images_grouped_1/Sprite/EyeMonster/EyeMonster_2.png",
    }, 2, 10),

    TREE("tree",                           -1, -2, new String[]{
        "Images_grouped_1/Sprite/T/T_TreeMonster_0.png",
        "Images_grouped_1/Sprite/T/T_TreeMonster_1.png",
        "Images_grouped_1/Sprite/T/T_TreeMonster_2.png",
    }, 1, 10);

    private String name;
    private int hp;
    private int spawnRate;
    private Texture[] textures;
    private Animation<TextureRegion> animation;
    private float attackDamage;
    private float attackRange;

    EnemyEnum(
        String name,
        int hp,
        int spawnRate,
        String[] texturePaths,
        float attackDamage,
        float attackRange
    ) {
        this.name = name;
        this.hp = hp;
        this.spawnRate = spawnRate;
        this.attackDamage = attackDamage;
        this.attackRange = attackRange;

        textures = new Texture[texturePaths.length];
        Array<TextureRegion> regions = new Array<>();
        for (int i = 0; i < texturePaths.length; i++) {
            textures[i] = new Texture(texturePaths[i]);
            regions.add(new TextureRegion(textures[i]));
        }
        animation = new Animation<>(0.2f, regions, Animation.PlayMode.LOOP);
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public float getAttackDamage()  { return attackDamage; }
    public float getAttackRange()   { return attackRange; }
    public int getHp()              { return hp; }
    public int getSpawnRate()       { return spawnRate; }
    public String getName()         { return name; }
}
