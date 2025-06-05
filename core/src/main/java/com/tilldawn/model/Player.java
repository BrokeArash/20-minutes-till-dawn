package com.tilldawn.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.tilldawn.model.enums.Hero;
import com.tilldawn.model.enums.AbilityEnum;

import java.util.ArrayList;

public class Player {

    private Texture playerTexture;
    private Sprite playerSprite;
    private Weapon weapon;
    private Hero hero;
    private float posX = 0;
    private float posY = 0;
    private float playerHealth;
    private Rectangle rect;
    private float time = 0;
    private float speed;
    private int xp = 0;
    private int level = 1;
    private ArrayList<AbilityEnum> abilities = new ArrayList<>();

    private float damageMultiplier = 1f;

    private boolean extraProjectile = false;

    private boolean doubleSpeed = false;

    private float damagerTimer = 0f;
    private float speedyTimer   = 0f;

    public Player(Hero hero, Weapon weapon) {
        this.weapon = weapon;
        this.hero = hero;
        playerTexture = new Texture(hero.getCharacter_idle0());
        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(playerTexture.getWidth() * 2, playerTexture.getHeight() * 2);
        this.playerHealth = hero.getBaseHp();
        this.speed = hero.getSpeed();
    }

    public void setInitialPosition() {
        float centerX = Gdx.graphics.getWidth() / 2f - playerSprite.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f - playerSprite.getHeight() / 2f;
        playerSprite.setPosition(centerX, centerY);
        rect = new Rectangle(centerX, centerY, playerTexture.getWidth() * 3, playerTexture.getHeight() * 3);
    }

    public Hero getHero() {
        return hero;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public float getSpeed() {
        return speed;
    }

    public void applyAbility(AbilityEnum ability) {
        switch (ability) {
            case VITALITY:
                playerHealth += 1f;
                Gdx.app.log("Player", "Vitality applied: +1 HP");
                break;

            case DAMAGER:
                damageMultiplier = 1.25f;
                damagerTimer = 10f;
                Gdx.app.log("Player", "Damager applied: +25% damage for 10s");
                break;

            case PROCREASE:
                extraProjectile = true;
                Gdx.app.log("Player", "Procrease applied: next shot fires 2 bullets");
                break;

            case AMOCREASE:
                int newAmmo = weapon.getCurrentAmmo() + 5;
                if (newAmmo > weapon.getMaxAmmo()) {
                    newAmmo = weapon.getMaxAmmo();
                }
                weapon.setMaxAmmo(newAmmo);
                weapon.setCurrentAmmoMax();
                Gdx.app.log("Player", "Amocrease applied: +5 ammo (capped at "
                    + weapon.getMaxAmmo() + ")");
                break;

            case SPEEDY:
                doubleSpeed = true;
                speedyTimer = 10f;
                speed = hero.getSpeed() * 2f;
                Gdx.app.log("Player", "Speedy applied: speed doubled for 10s");
                break;
        }
    }

    public void updateAbilities(float delta) {
        if (damagerTimer > 0f) {
            damagerTimer -= delta;
            if (damagerTimer <= 0f) {
                damageMultiplier = 1f;
                damagerTimer = 0f;
                Gdx.app.log("Player", "Damager expired → back to normal damage");
            }
        }

        if (speedyTimer > 0f) {
            speedyTimer -= delta;
            if (speedyTimer <= 0f) {
                doubleSpeed = false;
                speedyTimer = 0f;
                speed = hero.getSpeed();
                Gdx.app.log("Player", "Speedy expired → back to normal speed");
            }
        }
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public boolean shouldFireExtraProjectile() {
        return extraProjectile;
    }
    public void clearExtraProjectileFlag() {
        extraProjectile = false;
    }

    private boolean isPlayerIdle = true;
    private boolean isPlayerRunning = false;

    public Texture getPlayerTexture() {
        return playerTexture;
    }

    public void setPlayerTexture(Texture playerTexture) {
        this.playerTexture = playerTexture;
    }

    public Sprite getPlayerSprite() {
        return playerSprite;
    }

    public void setPlayerSprite(Sprite playerSprite) {
        this.playerSprite = playerSprite;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getPlayerHealth() {
        return playerHealth;
    }

    public void takeDamage(float amount) {
        this.playerHealth -= amount;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public boolean isPlayerIdle() {
        return isPlayerIdle;
    }

    public void setPlayerIdle(boolean playerIdle) {
        isPlayerIdle = playerIdle;
    }

    public boolean isPlayerRunning() {
        return isPlayerRunning;
    }

    public void setPlayerRunning(boolean playerRunning) {
        isPlayerRunning = playerRunning;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public void addXP(int amount) {
        xp += amount;
        if (xp >= 20 * level) {
            addLevel();
        }
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<AbilityEnum> getAbilities() {
        return abilities;
    }

    public void addLevel() {
        this.level++;
        this.xp = 0;
        AbilityEnum abilityEnum = AbilityEnum.getRandomAbility();
        this.abilities.add(abilityEnum);
        this.applyAbility(abilityEnum);
    }

    public String getAbilitiesText() {
        StringBuilder s = new StringBuilder();
        for (AbilityEnum abilityEnum : abilities) {
            s.append(abilityEnum.getName());
            s.append("\n\n");
        }
        return s.toString();
    }
}
