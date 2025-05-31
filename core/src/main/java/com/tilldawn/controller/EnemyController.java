package com.tilldawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.tilldawn.Main;
import com.tilldawn.model.App;
import com.tilldawn.model.Camera;
import com.tilldawn.model.Enemy;
import com.tilldawn.model.Player;
import com.tilldawn.model.enums.EnemyEnum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnemyController {
    private static List<Enemy> enemies;
    private PlayerController playerController;
    private WorldController worldController;
    private Texture enemyTexture;
    private float spawnTimer;
    private float spawnInterval = 2.0f;
    private int maxEnemies = 10;
    private float spawnRadius = 300f;
    private EnemyEnum type;
    private float enemyWidth;
    private float enemyHeight;
    private Array<EnemyEnum> spawnable = new Array<>();

    public EnemyController(PlayerController playerController, WorldController worldController) {
        this.playerController = playerController;
        this.worldController = worldController;
        this.enemies = new ArrayList<>();
        this.spawnTimer = 0f;

        for (EnemyEnum type : EnemyEnum.values()) {
            if (type.getSpawnRate() > 0) {
                spawnable.add(type);
            }
        }
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Player player = App.getGame().getPlayer();

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

            enemy.update(deltaTime, player);

            if (enemy.canAttack(player)) {
                enemy.startAttacking();
            } else {
                enemy.stopAttacking();
                moveEnemy(enemy, deltaTime);
            }
            Camera camera = worldController.getCamera();
            float screenX = camera.getScreenX(enemy.getPosX());
            float screenY = camera.getScreenY(enemy.getPosY());
            Texture enemyTexture = enemy.getType().getTexture();
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
            type = getRandomEnemyType();
            enemyWidth = type.getTexture().getWidth();
            enemyHeight = type.getTexture().getHeight();

            if (worldController.isPositionValid(spawnX, spawnY, enemyWidth, enemyHeight)) {
                Enemy newEnemy = new Enemy(spawnX, spawnY, type);
                newEnemy.updateRectangle();
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
                    Enemy newEnemy = new Enemy(clampedX, clampedY, type);
                    newEnemy.updateRectangle();
                    enemies.add(newEnemy);
                    return;
                }
            }
        }

        float[] bounds = worldController.getWorldBounds(enemyWidth, enemyHeight);
        float fallbackX = bounds[0] + MathUtils.random() * (bounds[1] - bounds[0]);
        float fallbackY = bounds[2] + MathUtils.random() * (bounds[3] - bounds[2]);
        EnemyEnum type = getRandomEnemyType();

        Enemy newEnemy = new Enemy(fallbackX, fallbackY, type);
        newEnemy.updateRectangle();
        enemies.add(newEnemy);
    }

    private EnemyEnum getRandomEnemyType() {
        if (spawnable.size == 0) return EnemyEnum.TENTACLE_MONSTER;

        // Create weighted list based on spawn rates
        Array<EnemyEnum> weightedList = new Array<>();
        for (EnemyEnum type : spawnable) {
            for (int i = 0; i < type.getSpawnRate(); i++) {
                weightedList.add(type);
            }
        }

        return weightedList.random();
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
            enemy.updateRectangle();
        }
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
