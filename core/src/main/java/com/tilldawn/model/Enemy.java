package com.tilldawn.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tilldawn.model.enums.EnemyEnum;


public class Enemy {
    private float posX, posY;
    private float speed;
    private float health;
    private boolean alive;
    private Rectangle rectangle;
    private EnemyEnum type;
    private float attackCooldown;
    private float attackTimer;
    private boolean isAttacking;
    private float stateTime;

    public Enemy(float x, float y, EnemyEnum type) {
        this.type = type;
        this.posX = x;
        this.posY = y;
        this.speed = 50f;
        this.health = type.getHp();
        this.alive = true;
        this.rectangle = new Rectangle(x, y,
            type.getAnimation().getKeyFrame(0).getRegionWidth(),
            type.getAnimation().getKeyFrame(0).getRegionHeight());
        this.attackCooldown = 2.0f;
        this.attackTimer = 0f;
        this.isAttacking = false;
        this.stateTime = 0f;
    }

    public void update(float deltaTime, Player player) {
        this.stateTime += deltaTime;
        if (isAttacking) {
            attackTimer += deltaTime;
            if (attackTimer >= attackCooldown) {
                attack(player);
                attackTimer = 0f;
            }
        }
        updateRectangle();
    }
    public void takeDamage(float damage) {
        if (type == EnemyEnum.TREE) {
            return;
        }
        this.health -= damage;
        if (this.health <= 0) {
            this.alive = false;
        }
    }

    public void checkCollisionWithPlayer(Player player) {
        if (this.rectangle.overlaps(player.getRect())) {
            player.takeDamage(type.getAttackDamage());
            if (type != EnemyEnum.TREE) {
                this.isAttacking = true;
            }
        }
    }

    public boolean canAttack(Player player) {
        float dx = player.getPosX() - posX;
        float dy = player.getPosY() - posY;
        float distance = (float) Math.sqrt(dx*dx + dy*dy);
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
        if (App.isSFXOn()) {
            GameAssetsManager.getGameAssetsManager()
                .getMonsterAttack().play();
        }
    }

    public void updateRectangle() {
        rectangle.setPosition(posX, posY);
    }

    public boolean isAlive() {
        if (type == EnemyEnum.TREE) return true;
        return alive;
    }

    public EnemyEnum getType() {
        return type;
    }

    public float getPosX() { return posX; }
    public void setPosX(float px) { this.posX = px; }
    public float getPosY() { return posY; }
    public void setPosY(float py) { this.posY = py; }

    public float getSpeed() { return speed; }
    public void setSpeed(float s) { this.speed = s; }

    public Rectangle getRectangle() {
        return rectangle;
    }

    /** Returns the current frame of this enemy’s idle animation */
    public TextureRegion getCurrentFrame() {
        return type.getAnimation().getKeyFrame(stateTime);
    }
}
