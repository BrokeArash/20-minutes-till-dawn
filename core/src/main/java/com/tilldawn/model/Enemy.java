package com.tilldawn.model;

import com.badlogic.gdx.math.Rectangle;
import com.tilldawn.model.enums.EnemyEnum;

public class Enemy {
    private float posX;
    private float posY;
    private float speed;
    private float health;
    private boolean alive;
    private Rectangle rectangle;
    private EnemyEnum type;
    private float attackCooldown;
    private float attackTimer;
    private boolean isAttacking;

    public Enemy(float x, float y, EnemyEnum type) {
        this.type = type;
        this.posX = x;
        this.posY = y;
        this.speed = 50f;
        this.health = type.getHp();
        this.alive = true;
        rectangle = new Rectangle(x, y, EnemyEnum.ELDER.getTexture().getWidth(),
            EnemyEnum.ELDER.getTexture().getHeight());
        this.attackCooldown = 2.0f;
        this.attackTimer = 0;
        this.isAttacking = false;
    }

    public void update(float deltaTime, Player player) {
        if (isAttacking) {
            attackTimer += deltaTime;
            if (attackTimer >= attackCooldown) {
                attack(player);
                attackTimer = 0;
            }
        }

        updateRectangle();
    }

    public boolean canAttack(Player player) {
        float distance = (float) Math.sqrt(
            Math.pow(player.getPosX() - posX, 2) +
                Math.pow(player.getPosY() - posY, 2)
        );
        return distance <= type.getAttackRange();
    }

    public void startAttacking() {
        this.isAttacking = true;
    }

    public void stopAttacking() {
        this.isAttacking = false;
    }

    private void attack(Player player) {
        player.takeDamage(type.getAttackDamage());
        if (App.isIsSFXOn()) {
            GameAssetsManager.getGameAssetsManager().getMonsterAttack().play();
        }
    }

    public EnemyEnum getType() {
        return type;
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

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void updateRectangle() {
        rectangle.setPosition(posX, posY);
    }
}
