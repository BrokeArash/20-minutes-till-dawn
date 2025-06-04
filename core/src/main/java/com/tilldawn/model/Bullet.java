package com.tilldawn.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {
    private Texture texture = new Texture(GameAssetsManager.getGameAssetsManager().getBullet());
    private Sprite sprite = new Sprite(texture);
    private int damage = 5;
    private int x;
    private int y;
    private Rectangle rectangle;

    public Bullet(int x, int y){
        sprite.setSize(20 , 20);
        this.x = x;
        this.y = y;
        sprite.setX((float) Gdx.graphics.getWidth() / 2);
        sprite.setY((float) Gdx.graphics.getHeight() / 2);
        rectangle = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
    }

    public Texture getTexture() {
        return texture;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getDamage() {
        return damage;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void updateRectangle() {
        rectangle.setPosition(sprite.getX(), sprite.getY());
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
