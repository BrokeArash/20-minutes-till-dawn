package com.tilldawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.tilldawn.Main;
import com.tilldawn.model.*;
import com.tilldawn.model.enums.EnemyEnum;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages spawning, updating, rendering, and removal of all Enemies + XP drops.
 */
public class EnemyController {
    private static List<Enemy> enemies;
    private PlayerController playerController;
    private WorldController worldController;
    private float spawnTimer;
    private float spawnInterval = 2.0f;
    private int maxEnemies = 10;
    private float spawnRadius = 300f;

    // Holds all active XPDrop instances currently on the ground
    private Array<XPDrop> xpDrops;

    // Enemy spawning pool
    private Array<EnemyEnum> spawnable = new Array<>();

    public EnemyController(PlayerController playerController, WorldController worldController) {
        this.playerController = playerController;
        this.worldController = worldController;
        enemies = new ArrayList<>();
        xpDrops = new Array<>();

        spawnTimer = 0f;
        for (EnemyEnum type : EnemyEnum.values()) {
            if (type.getSpawnRate() > 0) {
                spawnable.add(type);
            }
        }
    }

    /** Called each frame. Updates enemies, spawns XP drops when they die, updates & renders XP drops. */
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Player player = playerController.getPlayer();

        // 1) Possibly spawn new enemies
        spawnTimer += deltaTime;
        if (spawnTimer >= spawnInterval && enemies.size() < maxEnemies) {
            spawnEnemy();
            spawnTimer = 0f;
        }

        // 2) Update each enemy; if it just died, spawn an XP drop at its position.
        Iterator<Enemy> enemyIter = enemies.iterator();
        while (enemyIter.hasNext()) {
            Enemy enemy = enemyIter.next();

            if (!enemy.isAlive()) {
                // Play monster‐killed SFX
                if (App.isIsSFXOn()) {
                    GameAssetsManager.getGameAssetsManager().getMonsterKill().play();
                }
                // Award a kill count to the Game
                App.getGame().addKill();

                // Spawn an XP drop at the enemy’s last known position
                spawnXpDrop(enemy.getPosX(), enemy.getPosY(), 3);

                // Remove this enemy from the list
                enemyIter.remove();
                continue;
            }

            // If still alive, update its AI / movement / attacks
            enemy.update(deltaTime, player);
            if (enemy.canAttack(player)) {
                enemy.startAttacking();
            } else {
                enemy.stopAttacking();
                moveEnemy(enemy, deltaTime);
            }

            // Draw the enemy on screen
            Camera camera = worldController.getCamera();
            float screenX = camera.getScreenX(enemy.getPosX());
            float screenY = camera.getScreenY(enemy.getPosY());
            Texture tex = enemy.getType().getTexture();
            Main.getBatch().draw(tex, screenX, screenY);
        }

        // 3) Update & check all active XP drops for player pickup
        // 3) Update & check all active XP drops for player pickup

// Build the player's bounding box in WORLD coordinates:
        float pw = player.getPlayerSprite().getWidth();
        float ph = player.getPlayerSprite().getHeight();
// If your Player’s (x,y) is its center, subtract half‐size; otherwise adjust as needed.
        Rectangle playerWorldBounds = new Rectangle(
            player.getPosX() - pw/2f,
            player.getPosY() - ph/2f,
            pw,
            ph
        );

        for (int i = xpDrops.size - 1; i >= 0; i--) {
            XPDrop drop = xpDrops.get(i);

            // Now both `drop.bounds` and `playerWorldBounds` are in WORLD space:
            if (drop.collidesWith(playerWorldBounds)) {
                // Player picks it up:
                player.addXP(drop.getXpValue());
                if (App.isIsSFXOn()) {
                    GameAssetsManager.getGameAssetsManager().getDropGet().play();
                }
                xpDrops.removeIndex(i);
                continue;
            }

            // Otherwise, render the drop at screen coords as before:
            Camera camera = worldController.getCamera();
            float screenX = camera.getScreenX(drop.getWorldX());
            float screenY = camera.getScreenY(drop.getWorldY());

            Main.getBatch().draw(
                drop.getTexture(),
                screenX,
                screenY,
                XPDrop.getWidth(),
                XPDrop.getHeight()
            );
        }

    }

    /** Helper: spawn one enemy, at a random point around the player. */
    private void spawnEnemy() {
        Player player = playerController.getPlayer();
        int maxAttempts = 10;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            float angle = (float) (Math.random() * Math.PI * 2f);
            float spawnX = player.getPosX() + (float) Math.cos(angle) * spawnRadius;
            float spawnY = player.getPosY() + (float) Math.sin(angle) * spawnRadius;
            EnemyEnum type = getRandomEnemyType();
            float w = type.getTexture().getWidth() * 1.5f;
            float h = type.getTexture().getHeight() * 1.5f;

            if (worldController.isPositionValid(spawnX, spawnY, w, h)) {
                Enemy newEnemy = new Enemy(spawnX, spawnY, type);
                newEnemy.updateRectangle();
                enemies.add(newEnemy);
                return;
            } else {
                float clampedX = worldController.clampX(spawnX, w);
                float clampedY = worldController.clampY(spawnY, h);
                float dx = clampedX - player.getPosX();
                float dy = clampedY - player.getPosY();
                float distToPlayer = (float) Math.hypot(dx, dy);
                if (distToPlayer > 100f) {
                    Enemy newEnemy = new Enemy(clampedX, clampedY, type);
                    newEnemy.updateRectangle();
                    enemies.add(newEnemy);
                    return;
                }
            }
        }

//        // Fallback: random in world bounds
//        float[] bounds = worldController.getWorldBounds(
//            type.getTexture().getWidth(), type.getTexture().getHeight()
//        );
//        float randX = bounds[0] + (float) Math.random() * (bounds[1] - bounds[0]);
//        float randY = bounds[2] + (float) Math.random() * (bounds[3] - bounds[2]);
//        Enemy fallback = new Enemy(randX, randY, getRandomEnemyType());
//        fallback.updateRectangle();
//        enemies.add(fallback);
    }

    /** Returns a weighted random EnemyEnum type based on spawnRate. */
    private EnemyEnum getRandomEnemyType() {
        if (spawnable.size == 0) {
            return EnemyEnum.TENTACLE_MONSTER;
        }
        Array<EnemyEnum> weighted = new Array<>();
        for (EnemyEnum t : spawnable) {
            for (int i = 0; i < t.getSpawnRate(); i++) {
                weighted.add(t);
            }
        }
        return weighted.random();
    }

    /** Moves one enemy toward the player, respecting world collisions. */
    private void moveEnemy(Enemy enemy, float deltaTime) {
        Player player = playerController.getPlayer();
        float dirX = player.getPosX() - enemy.getPosX();
        float dirY = player.getPosY() - enemy.getPosY();
        float distance = (float) Math.hypot(dirX, dirY);

        if (distance > 0) {
            float moveX = (dirX / distance) * enemy.getSpeed() * deltaTime;
            float moveY = (dirY / distance) * enemy.getSpeed() * deltaTime;
            float newX = enemy.getPosX() + moveX;
            float newY = enemy.getPosY() + moveY;

            if (worldController.isPositionValid(newX, newY,
                enemy.getType().getTexture().getWidth() * 1.5f,
                enemy.getType().getTexture().getHeight() * 1.5f)) {
                enemy.setPosX(newX);
                enemy.setPosY(newY);
            } else {
                enemy.setPosX(worldController.clampX(newX, enemy.getType().getTexture().getWidth()));
                enemy.setPosY(worldController.clampY(newY, enemy.getType().getTexture().getHeight()));
            }
            enemy.updateRectangle();
        }
    }

    /** Create a new XPDrop at (worldX, worldY) with the given xpValue. */
    private void spawnXpDrop(float worldX, float worldY, int xpValue) {
        XPDrop drop = new XPDrop(
            GameAssetsManager.getGameAssetsManager().getDrop(),
            worldX,
            worldY,
            xpValue
        );
        xpDrops.add(drop);
    }

    public static List<Enemy> getEnemies() {
        return enemies;
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }

    public float getSpawnTimer() {
        return spawnTimer;
    }

    public void setSpawnTimer(float spawnTimer) {
        this.spawnTimer = spawnTimer;
    }

    public float getSpawnInterval() {
        return spawnInterval;
    }

    public void setSpawnInterval(float spawnInterval) {
        this.spawnInterval = spawnInterval;
    }

    public int getMaxEnemies() {
        return maxEnemies;
    }

    public void setMaxEnemies(int maxEnemies) {
        this.maxEnemies = maxEnemies;
    }

    public float getSpawnRadius() {
        return spawnRadius;
    }

    public void setSpawnRadius(float spawnRadius) {
        this.spawnRadius = spawnRadius;
    }

    public void dispose() {
        // dispose any enemy‐related textures if needed
    }
}
