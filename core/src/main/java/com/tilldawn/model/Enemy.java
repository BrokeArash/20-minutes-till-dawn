package com.tilldawn.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    private float posX;
    private float posY;
    private float speed;
    private float health;
    private boolean alive;

    public Enemy(float x, float y) {
        this.posX = x;
        this.posY = y;
        this.speed = 50f;
        this.health = 100f;
        this.alive = true;
    }

    public float getPosX() { return posX; }
    public void setPosX(float posX) { this.posX = posX; }

    public float getPosY() { return posY; }
    public void setPosY(float posY) { this.posY = posY; }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public float getHealth() { return health; }
    public void setHealth(float health) { this.health = health; }

    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }

    public void takeDamage(float damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.alive = false;
        }
    }
}
