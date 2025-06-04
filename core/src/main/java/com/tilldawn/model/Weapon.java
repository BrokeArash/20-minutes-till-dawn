package com.tilldawn.model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tilldawn.model.enums.WeaponEnum;

public class Weapon {
    private final Texture gunTexture;
    private Sprite gunSprite;
    private final int maxAmmo;
    private WeaponEnum weapon;
    private int currentAmmo;
    private float reloadTime;
    private float reloadTimer;
    private Sound reloadSound;
    private Sound gunshot;
    private boolean isReloading = false;

    public Weapon(WeaponEnum weapon) {
        this.weapon = weapon;
        gunTexture = new Texture(weapon.getGun());
        maxAmmo = weapon.getAmmoMax();
        gunSprite = new Sprite(gunTexture);
        gunSprite.setX((float) Gdx.graphics.getWidth() / 2);
        gunSprite.setY((float) Gdx.graphics.getHeight() / 2);
        gunSprite.setSize(50,50);
        currentAmmo = maxAmmo;
        this.reloadTime = weapon.getTimeReload();
        this.reloadSound = GameAssetsManager.getGameAssetsManager().getReloadSound();
        this.gunshot = GameAssetsManager.getGameAssetsManager().getGunshot();
        this.isReloading = false;
        this.reloadTimer = 0;
    }

    public void update(float delta) {
        if (isReloading) {
            reloadTimer += delta;

            if (reloadTimer >= reloadTime) {
                finishReload();
            }
        }
    }

    public void startReload() {
        if (!isReloading && getCurrentAmmo() < getMaxAmmo()) {
            isReloading = true;
            reloadTimer = 0;
            if (App.isIsSFXOn()) {
                reloadSound.play();
            }
        }
    }

    private void finishReload() {
        setCurrentAmmoMax();
        isReloading = false;
    }

    public boolean isReloading() {
        return isReloading;
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

    public Sound getGunshot() {
        return gunshot;
    }
}
