package com.tilldawn.model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tilldawn.model.enums.WeaponEnum;

public class Weapon {
    private final Texture gunTexture;
    private Sprite gunSprite;
    private int ammo;
    private WeaponEnum weapon;

    public Weapon(WeaponEnum weapon) {
        this.weapon = weapon;
        gunTexture = new Texture(weapon.getGun());
        ammo = weapon.getAmmoMax();
        gunSprite = new Sprite(gunTexture);
        gunSprite.setX((float) Gdx.graphics.getWidth() / 2);
        gunSprite.setY((float) Gdx.graphics.getHeight() / 2);
        gunSprite.setSize(50,50);
    }

    public Sprite getGunSprite() {
        return gunSprite;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo){
        this.ammo = ammo;
    }

    public WeaponEnum getWeapon() {
        return weapon;
    }
}
