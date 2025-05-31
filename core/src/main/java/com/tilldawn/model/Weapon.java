package com.tilldawn.model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tilldawn.model.enums.WeaponEnum;

public class Weapon {
    private final Texture gunTexture;
    private Sprite gunSprite;
    private final int maxAmmo;
    private WeaponEnum weapon;
    private int currentAmmo;

    public Weapon(WeaponEnum weapon) {
        this.weapon = weapon;
        gunTexture = new Texture(weapon.getGun());
        maxAmmo = weapon.getAmmoMax();
        gunSprite = new Sprite(gunTexture);
        gunSprite.setX((float) Gdx.graphics.getWidth() / 2);
        gunSprite.setY((float) Gdx.graphics.getHeight() / 2);
        gunSprite.setSize(50,50);
        currentAmmo = maxAmmo;
    }

    public Sprite getGunSprite() {
        return gunSprite;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public WeaponEnum getWeapon() {
        return weapon;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public void setCurrentAmmoMax() {
        this.currentAmmo = getMaxAmmo();
    }

    public void shootBullet() {
        this.currentAmmo --;
    }
}
