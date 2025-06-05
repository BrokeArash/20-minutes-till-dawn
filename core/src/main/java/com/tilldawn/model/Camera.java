package com.tilldawn.model;

import com.badlogic.gdx.Gdx;

public class Camera {
    private float x;
    private float y;
    private float targetX;
    private float targetY;

    public Camera() {
        this.x = 0;
        this.y = 0;
        this.targetX = 0;
        this.targetY = 0;
    }

    public void initialize(float playerX, float playerY) {
        this.x = playerX;
        this.y = playerY;
        this.targetX = playerX;
        this.targetY = playerY;
    }

    public void setTarget(float targetX, float targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public void update() {
        float lerp = 5.0f * Gdx.graphics.getDeltaTime();
        this.x += (targetX - x) * lerp;
        this.y += (targetY - y) * lerp;
    }

    public float getScreenX(float worldX) {
        return worldX - x + (Gdx.graphics.getWidth() / 2f);
    }

    public float getScreenY(float worldY) {
        return worldY - y + (Gdx.graphics.getHeight() / 2f);
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
