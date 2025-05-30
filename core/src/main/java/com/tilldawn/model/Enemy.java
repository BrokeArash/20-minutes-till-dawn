package com.tilldawn.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    private float posX;
    private float posY;
    private float speed;
    private float health;
    //private Sprite enemySprite;
    //private Vector2 direction;
    private boolean alive;
    //private float time; // For animations

    public Enemy(float x, float y) {
        this.posX = x;
        this.posY = y;
        this.speed = 50f;
        this.health = 100f;
        this.alive = true;
        //this.time = 0f;
        //this.direction = new Vector2();

        //this.enemySprite = new Sprite(texture);
        //this.enemySprite.setPosition(x, y);
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

//    public void update(float deltaTime) {
//        time += deltaTime;
//
//        // Update position based on direction
//        posX += direction.x * speed * deltaTime;
//        posY += direction.y * speed * deltaTime;
//
//        // Update sprite position
//        enemySprite.setPosition(posX, posY);
//    }

//    public void moveTowards(float targetX, float targetY) {
//        // Calculate direction vector towards target
//        float dx = targetX - posX;
//        float dy = targetY - posY;
//
//        // Normalize the direction vector
//        float length = (float) Math.sqrt(dx * dx + dy * dy);
//        if (length > 0) {
//            direction.set(dx / length, dy / length);
//        }
//    }
//
//    // Add collision rectangle for bullet collision detection
//    public boolean isCollidingWith(float bulletX, float bulletY, float bulletWidth, float bulletHeight) {
//        float enemyWidth = enemySprite.getWidth();
//        float enemyHeight = enemySprite.getHeight();
//
//        return posX < bulletX + bulletWidth &&
//            posY < bulletY + bulletHeight &&
//            posX + enemyWidth > bulletX &&
//            posY + enemyHeight > bulletY;
//    }
//
//    public void takeDamage(float damage) {
//        health -= damage;
//        if (health <= 0) {
//            alive = false;
//        }
//    }
//
//    public float getDistanceToTarget(float targetX, float targetY) {
//        float dx = targetX - posX;
//        float dy = targetY - posY;
//        return (float) Math.sqrt(dx * dx + dy * dy);
//    }
//
//    // Getters and Setters
//    public float getPosX() { return posX; }
//    public void setPosX(float posX) {
//        this.posX = posX;
//        enemySprite.setX(posX);
//    }
//
//    public float getPosY() { return posY; }
//    public void setPosY(float posY) {
//        this.posY = posY;
//        enemySprite.setY(posY);
//    }
//
//    public float getSpeed() { return speed; }
//    public void setSpeed(float speed) { this.speed = speed; }
//
//    public float getHealth() { return health; }
//    public void setHealth(float health) { this.health = health; }
//
//    public Sprite getEnemySprite() { return enemySprite; }
//
//    public Vector2 getDirection() { return direction; }
//
//    public boolean isAlive() { return alive; }
//    public void setAlive(boolean alive) { this.alive = alive; }
//
//    public float getTime() { return time; }
//    public void setTime(float time) { this.time = time; }
}
