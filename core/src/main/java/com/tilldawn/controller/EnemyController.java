package com.tilldawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.tilldawn.Main;
import com.tilldawn.model.Camera;
import com.tilldawn.model.Enemy;
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

    public EnemyController(PlayerController playerController, WorldController worldController) {
        this.playerController = playerController;
        this.worldController = worldController;
        this.enemies = new ArrayList<>();
        this.enemyTexture = new Texture("Images_grouped_1/Sprite/EyeMonster/EyeMonster_0.png");
        this.spawnTimer = 0f;
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
            float screenY = camera.getScreenY(enemy.getPosY());;

            Main.getBatch().draw(enemyTexture, screenX, screenY);
        }
    }

    private void  spawnEnemy() {
        Player player = playerController.getPlayer();

        float angle = MathUtils.random(0f, MathUtils.PI2);

        float spawnX = player.getPosX() + MathUtils.cos(angle) * spawnRadius;
        float spawnY = player.getPosY() + MathUtils.sin(angle) * spawnRadius;

        Enemy newEnemy = new Enemy(spawnX, spawnY);
        enemies.add(newEnemy);
    }

    private void moveEnemy(Enemy enemy, float deltaTime) {
        Player player = playerController.getPlayer();

        float dirX = player.getPosX() - enemy.getPosX();
        float dirY = player.getPosY() -enemy.getPosY();

        float distance = (float) Math.sqrt(dirX * dirX + dirY * dirY);

        if (distance > 0) {
            dirX = (dirX / distance) * enemy.getSpeed() * deltaTime;
            dirY = (dirY / distance) * enemy.getSpeed() * deltaTime;

            enemy.setPosX(enemy.getPosX() + dirX);
            enemy.setPosY(enemy.getPosY() + dirY);
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

    public void  dispose() {
        if (enemyTexture != null) {
            enemyTexture.dispose();
        }
    }
}
