package com.tilldawn.model.enums;

import com.badlogic.gdx.graphics.Texture;

public enum WeaponEnum {
    REVOLVER("Revolver", 20, 1, 1, 6, "Images_grouped_1/Sprite/RevolverStill/RevolverStill.png"),
    SHOTGUN("Shotgun", 10, 4, 1, 2, "Images_grouped_1/Sprite/T/T_Shotgun_SS_0.png"),
    SMG("SMGs dual", 8, 1, 2, 24, "Images_grouped_1/Sprite/T/T_DualSMGs_Icon.png"),
    ;

    private String name;
    private int damage;
    private int projectile;
    private int timeReload;
    private int ammoMax;

    private String gun;
    private Texture gunTexture;

    WeaponEnum(String name, int damage, int projectile, int timeReload, int ammoMax, String gun) {
        this.name = name;
        this.damage = damage;
        this.projectile = projectile;
        this.timeReload = timeReload;
        this.ammoMax = ammoMax;
        this.gun = gun;
        this.gunTexture  = new Texture(gun);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getProjectile() {
        return projectile;
    }

    public void setProjectile(int projectile) {
        this.projectile = projectile;
    }

    public int getTimeReload() {
        return timeReload;
    }

    public void setTimeReload(int timeReload) {
        this.timeReload = timeReload;
    }

    public int getAmmoMax() {
        return ammoMax;
    }

    public void setAmmoMax(int ammoMax) {
        this.ammoMax = ammoMax;
    }

    public String getGun() {
        return gun;
    }

    public Texture getGunTexture() {
        return gunTexture;
    }
}
