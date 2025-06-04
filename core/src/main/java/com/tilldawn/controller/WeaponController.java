package com.tilldawn.controller;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tilldawn.Main;
import com.tilldawn.model.*;


import java.util.ArrayList;
import java.util.Iterator;

public class WeaponController {
    private Weapon weapon;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private WorldController worldController;

    public WeaponController(Weapon weapon, WorldController worldController){
        this.weapon = weapon;
        this.worldController = worldController;
    }

    public void update(){
        weapon.getGunSprite().draw(Main.getBatch());
        updateBullets();
        handleBulletCollisions();
    }

    public void handleWeaponRotation(int x, int y) {
        Sprite weaponSprite = weapon.getGunSprite();

        float weaponCenterX = (float) Gdx.graphics.getWidth() / 2;
        float weaponCenterY = (float) Gdx.graphics.getHeight() / 2;

        float angle = (float) Math.atan2(y - weaponCenterY, x - weaponCenterX);

        weaponSprite.setRotation((float) (3.14 - angle * MathUtils.radiansToDegrees));
    }

    public void handleWeaponShoot(int x, int y){
        if (weapon.getCurrentAmmo() < 1 || weapon.isReloading()) {
            return;
        }

        bullets.add(new Bullet(x, y));
        weapon.shootBullet();
        if (App.isIsSFXOn()) {
            weapon.getGunshot().play();
        }
    }

    public void updateBullets() { //TODO: add shooting here
        for(Bullet b : bullets) {
            b.getSprite().draw(Main.getBatch());
            Vector2 direction = new Vector2(
                Gdx.graphics.getWidth()/2f - b.getX(),
                Gdx.graphics.getHeight()/2f - b.getY()
            ).nor();

            b.getSprite().setX(b.getSprite().getX() - direction.x * 5);
            b.getSprite().setY(b.getSprite().getY() + direction.y * 5);
            b.updateRectangle();
        }
    }


    private void handleBulletCollisions() {
        // Use iterators to safely remove items during iteration
        Iterator<Bullet> bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            boolean bulletHit = false;

            for (Enemy enemy : EnemyController.getEnemies()) {
                enemy.updateRectangle();

                // CONVERT ENEMY POSITION TO SCREEN SPACE
                float enemyScreenX = worldController.getCamera().getScreenX(enemy.getPosX());
                float enemyScreenY = worldController.getCamera().getScreenY(enemy.getPosY());

                // CREATE TEMPORARY SCREEN-SPACE RECTANGLE FOR ENEMY
                Rectangle enemyScreenRect = new Rectangle(
                    enemyScreenX,
                    enemyScreenY,
                    enemy.getRectangle().width,
                    enemy.getRectangle().height
                );

                if (bullet.getRectangle().overlaps(enemyScreenRect)) {
                    if (App.isIsSFXOn()) {
                        GameAssetsManager.getGameAssetsManager().getMonsterDamage().play();
                    }
                    enemy.takeDamage(bullet.getDamage());
                    bulletIterator.remove();
                    bulletHit = true;
                    break;
                }
            }

            // Remove bullets that go off-screen
            if (!bulletHit && isBulletOffScreen(bullet)) {
                bulletIterator.remove();
            }
        }
    }

    private boolean isBulletOffScreen(Bullet bullet) {
        return bullet.getSprite().getX() < 0 ||
            bullet.getSprite().getX() > Gdx.graphics.getWidth() ||
            bullet.getSprite().getY() < 0 ||
            bullet.getSprite().getY() > Gdx.graphics.getHeight();
    }
}
