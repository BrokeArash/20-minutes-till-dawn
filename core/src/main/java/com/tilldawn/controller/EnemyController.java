package com.tilldawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.tilldawn.Main;
import com.tilldawn.model.Camera;
import com.tilldawn.model.Enemy;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.model.Player;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnemyController {
    private List<Enemy> enemies;
    private PlayerController playerController;
    private WorldController worldController;
    private Texture enemyTexture;
    private float spawnTimer;
    private float spawnInterval = 2.0f;
    private int maxEnemies = 10;
    private float spawnRadius = 300f;
    private float enemyWidth = 32f; // Default enemy width
    private float enemyHeight = 32f; // Default enemy height

    public EnemyController(PlayerController playerController, WorldController worldController) {
        this.playerController = playerController;
        this.worldController = worldController;
        this.enemies = new ArrayList<>();
        this.enemyTexture = GameAssetsManager.getGameAssetsManager().getEnemyTexture();
        this.spawnTimer = 0f;

        this.enemyWidth = enemyTexture.getWidth();
        this.enemyHeight = enemyTexture.getHeight();
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        spawnTimer += deltaTime;
        if (spawnTimer >= spawnInterval && enemies.size() < maxEnemies) {
            spawnEnemy();
            spawnTimer = 0f;
        }

        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (!enemy.isAlive()) {
                iterator.remove();
                continue;
            }

            moveEnemy(enemy, deltaTime);
            Camera camera = worldController.getCamera();
            float screenX = camera.getScreenX(enemy.getPosX());
            float screenY = camera.getScreenY(enemy.getPosY());

            Main.getBatch().draw(enemyTexture, screenX, screenY);
        }
    }

    private void spawnEnemy() {
        Player player = playerController.getPlayer();

        int maxAttempts = 10;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            float angle = MathUtils.random(0f, MathUtils.PI2);
            float spawnX = player.getPosX() + MathUtils.cos(angle) * spawnRadius;
            float spawnY = player.getPosY() + MathUtils.sin(angle) * spawnRadius;

            if (worldController.isPositionValid(spawnX, spawnY, enemyWidth, enemyHeight)) {
                Enemy newEnemy = new Enemy(spawnX, spawnY);
                enemies.add(newEnemy);
                return;
            } else {
                float clampedX = worldController.clampX(spawnX, enemyWidth);
                float clampedY = worldController.clampY(spawnY, enemyHeight);

                float distToPlayer = (float) Math.sqrt(
                    Math.pow(clampedX - player.getPosX(), 2) +
                        Math.pow(clampedY - player.getPosY(), 2)
                );

                if (distToPlayer > 100f) {
                    Enemy newEnemy = new Enemy(clampedX, clampedY);
                    enemies.add(newEnemy);
                    return;
                }
            }
        }

        float[] bounds = worldController.getWorldBounds(enemyWidth, enemyHeight);
        float fallbackX = bounds[0] + MathUtils.random() * (bounds[1] - bounds[0]);
        float fallbackY = bounds[2] + MathUtils.random() * (bounds[3] - bounds[2]);

        Enemy newEnemy = new Enemy(fallbackX, fallbackY);
        enemies.add(newEnemy);
    }

    private void moveEnemy(Enemy enemy, float deltaTime) {
        Player player = playerController.getPlayer();

        float dirX = player.getPosX() - enemy.getPosX();
        float dirY = player.getPosY() - enemy.getPosY();
        float distance = (float) Math.sqrt(dirX * dirX + dirY * dirY);

        if (distance > 0) {
            float moveX = (dirX / distance) * enemy.getSpeed() * deltaTime;
            float moveY = (dirY / distance) * enemy.getSpeed() * deltaTime;

            float newX = enemy.getPosX() + moveX;
            float newY = enemy.getPosY() + moveY;

            if (worldController.isPositionValid(newX, newY, enemyWidth, enemyHeight)) {
                enemy.setPosX(newX);
                enemy.setPosY(newY);
            } else {
                enemy.setPosX(worldController.clampX(newX, enemyWidth));
                enemy.setPosY(worldController.clampY(newY, enemyHeight));
            }
        }
    }

    public List<Enemy> getEnemies() {
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

    public Texture getEnemyTexture() {
        return enemyTexture;
    }

    public void setEnemyTexture(Texture enemyTexture) {
        this.enemyTexture = enemyTexture;
        // Update dimensions when texture changes
        this.enemyWidth = enemyTexture.getWidth();
        this.enemyHeight = enemyTexture.getHeight();
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

    public float getEnemyWidth() {
        return enemyWidth;
    }

    public float getEnemyHeight() {
        return enemyHeight;
    }

    public void dispose() {
        if (enemyTexture != null) {
            enemyTexture.dispose();
        }
    }
}
