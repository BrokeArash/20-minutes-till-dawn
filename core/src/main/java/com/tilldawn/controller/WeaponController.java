package com.tilldawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tilldawn.Main;
import com.tilldawn.model.*;
import com.tilldawn.model.Player;

import java.util.ArrayList;
import java.util.Iterator;

public class WeaponController {
    private Weapon weapon;
    private Player player;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private WorldController worldController;

    public WeaponController(Weapon weapon, WorldController worldController, Player player) {
        this.weapon = weapon;
        this.worldController = worldController;
        this.player = player;
    }

    public void update() {
        weapon.getGunSprite().draw(Main.getBatch());

        updateBullets();
        handleBulletCollisions();
    }

    public void handleWeaponRotation(int x, int y) {
        Sprite weaponSprite = weapon.getGunSprite();

        float weaponCenterX = Gdx.graphics.getWidth() / 2f;
        float weaponCenterY = Gdx.graphics.getHeight() / 2f;

        float angle = (float) Math.atan2(y - weaponCenterY, x - weaponCenterX);
        weaponSprite.setRotation(180f - angle * MathUtils.radiansToDegrees);
    }

    public void handleWeaponShoot(int x, int y) {
        if (weapon.getCurrentAmmo() < 1 || weapon.isReloading()) {
            return;
        }

        float baseDamage = weapon.getWeapon().getDamage();

        float actualDamage = baseDamage * player.getDamageMultiplier();

        Bullet primary = new Bullet(x, y);
        primary.setDamage((int)actualDamage);
        bullets.add(primary);

        if (player.shouldFireExtraProjectile()) {
            Bullet extra = new Bullet(x, y);
            extra.setDamage((int)actualDamage);

            float offsetAngle = 10f * MathUtils.degRad;
            Vector2 dir = new Vector2(
                Gdx.graphics.getWidth() / 2f - x,
                Gdx.graphics.getHeight() / 2f - y
            ).nor();

            float cos = MathUtils.cos(offsetAngle);
            float sin = MathUtils.sin(offsetAngle);
            Vector2 rotated = new Vector2(
                dir.x * cos - dir.y * sin,
                dir.x * sin + dir.y * cos
            ).nor();
            extra.getSprite().setRotation(rotated.angleDeg() + 90f);

            bullets.add(extra);

            player.clearExtraProjectileFlag();
        }

        weapon.shootBullet();
        if (App.isIsSFXOn()) {
            weapon.getGunshot().play();
        }
    }

    private void updateBullets() {
        for (Bullet b : bullets) {
            b.getSprite().draw(Main.getBatch());
            Vector2 direction = new Vector2(
                Gdx.graphics.getWidth() / 2f - b.getX(),
                Gdx.graphics.getHeight() / 2f - b.getY()
            ).nor();

            b.getSprite().translate(
                -direction.x * 5f,
                direction.y * 5f
            );
            b.updateRectangle();
        }
    }

    private void handleBulletCollisions() {
        Iterator<Bullet> bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            boolean bulletHit = false;

            for (Enemy enemy : EnemyController.getEnemies()) {
                enemy.updateRectangle();

                float enemyScreenX = worldController.getCamera().getScreenX(enemy.getPosX());
                float enemyScreenY = worldController.getCamera().getScreenY(enemy.getPosY());

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

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public WorldController getWorldController() {
        return worldController;
    }

    public void setWorldController(WorldController worldController) {
        this.worldController = worldController;
    }

}
