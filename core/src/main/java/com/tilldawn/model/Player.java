package com.tilldawn.model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tilldawn.model.enums.Hero;

public class Player {


    private Texture playerTexture;
    private Sprite playerSprite;
    private Weapon weapon;
    private Hero hero;
    private float posX = 0;
    private float posY = 0;
    private float playerHealth = 100;
    private CollisionRect rect ;
    private float time = 0;
    private float speed = 5;

    public Player(Hero hero, Weapon weapon) {
        this.weapon = weapon;
        this.hero = hero;
        playerTexture = new Texture(hero.getCharacter_idle0());
        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(playerTexture.getWidth() * 3, playerTexture.getHeight() * 3);
    }

    public void setInitialPosition() {
        float centerX = (float) Gdx.graphics.getWidth() / 2 - playerSprite.getWidth() / 2;
        float centerY = (float) Gdx.graphics.getHeight() / 2 - playerSprite.getHeight() / 2;
        playerSprite.setPosition(centerX, centerY);
        rect = new CollisionRect(centerX, centerY, playerTexture.getWidth() * 3, playerTexture.getHeight() * 3);
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

    public void setPlayerHealth(float playerHealth) {
        this.playerHealth = playerHealth;
    }

    public CollisionRect getRect() {
        return rect;
    }

    public void setRect(CollisionRect rect) {
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


}
