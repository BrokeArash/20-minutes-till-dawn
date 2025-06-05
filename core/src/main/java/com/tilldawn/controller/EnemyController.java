package com.tilldawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.tilldawn.Main;
import com.tilldawn.model.*;
import com.tilldawn.model.enums.EnemyEnum;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnemyController {
    private static final int NUM_TREES = 25;
    private static final float MIN_TREE_SEPARATION = 200f;
    private static final int TREE_SPAWN_ATTEMPTS = 100;
    private static List<Enemy> enemies;

    private final PlayerController playerController;
    private final WorldController worldController;
    private final WeaponController weaponController;

    private float elapsedTime;
    private float spawnTimerTentacle;
    private float spawnTimerEyeBat;
    private boolean elderSpawned;
    private boolean treesInitialized;
    private float lastTreeDamageTime = -999f;
    private static final float TREE_DAMAGE_COOLDOWN = 3f;


    private final Array<EnemyEnum> tentaclePool;
    private final Array<EnemyEnum> eyeBatPool;
    private final Array<XPDrop> xpDrops;

    public EnemyController(PlayerController playerController,
                           WorldController worldController,
                           WeaponController weaponController) {
        this.playerController = playerController;
        this.worldController = worldController;
        this.weaponController = weaponController;

        enemies = new ArrayList<>();
        xpDrops = new Array<>();

        elapsedTime = 0f;
        spawnTimerTentacle = 0f;
        spawnTimerEyeBat = 0f;
        elderSpawned = false;
        treesInitialized = false;

        tentaclePool = new Array<>();
        tentaclePool.add(EnemyEnum.TENTACLE_MONSTER);

        eyeBatPool = new Array<>();
        eyeBatPool.add(EnemyEnum.EYE_BAT);
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        elapsedTime += deltaTime;

        if (!treesInitialized) {
            spawnStaticTrees();
            treesInitialized = true;
        }

        Player player = playerController.getPlayer();
        float playerX = player.getPosX();
        float playerY = player.getPosY();
        float playerW = player.getPlayerSprite().getWidth();
        float playerH = player.getPlayerSprite().getHeight();
        Rectangle playerRect = new Rectangle(
            playerX - playerW / 2f,
            playerY - playerH / 2f,
            playerW,
            playerH
        );

        spawnTimerTentacle += deltaTime;
        if (spawnTimerTentacle >= 3f) {
            int i = (int)(elapsedTime / 3f);
            int countToSpawn = (i / 30) + 1;
            for (int n = 0; n < countToSpawn; n++) {
                spawnMovingEnemy(EnemyEnum.TENTACLE_MONSTER);
            }
            spawnTimerTentacle -= 3f;
        }

        if (elapsedTime >= 10f / 4f) {
            spawnTimerEyeBat += deltaTime;
            if (spawnTimerEyeBat >= 10f) {
                int t = (int) elapsedTime;
                int count = MathUtils.floor((4 * (elapsedTime / 10f) - t + 30) / 30f);
                count = Math.max(count, 1);
                for (int n = 0; n < count; n++) {
                    spawnMovingEnemy(EnemyEnum.EYE_BAT);
                }
                spawnTimerEyeBat -= 10f;
            }
        }

        if (!elderSpawned && elapsedTime >= (App.getGame().getMode().getTime()*60f) / 2f) {
            spawnMovingEnemy(EnemyEnum.ELDER);
            elderSpawned = true;
        }

        Iterator<Enemy> iter = enemies.iterator();
        while (iter.hasNext()) {
            Enemy enemy = iter.next();

            if (enemy.getType() == EnemyEnum.TREE) {
                float treeX = enemy.getPosX();
                float treeY = enemy.getPosY();
                TextureRegion treeFrame =
                    enemy.getType().getAnimation().getKeyFrame(elapsedTime);
                float treeW = treeFrame.getRegionWidth();
                float treeH = treeFrame.getRegionHeight();
                Rectangle treeRect = new Rectangle(
                    treeX - treeW / 2f,
                    treeY - treeH / 2f,
                    treeW,
                    treeH
                );

                if (treeRect.overlaps(playerRect)) {
                    if (elapsedTime - lastTreeDamageTime >= TREE_DAMAGE_COOLDOWN) {
                        lastTreeDamageTime = elapsedTime;
                        player.takeDamage(enemy.getType().getAttackDamage());
                        if (App.isSFXOn()) {
                            GameAssetsManager.getGameAssetsManager().getMonsterAttack().play();
                        }
                    }
                }

                Camera camera = worldController.getCamera();
                float screenX = camera.getScreenX(treeX);
                float screenY = camera.getScreenY(treeY);
                Main.getBatch().draw(treeFrame, screenX, screenY, treeW, treeH);

                continue;
            }
            enemy.update(deltaTime, player);

            if (enemy.canAttack(player)) {
                enemy.startAttacking();
            } else {
                enemy.stopAttacking();
                moveEnemy(enemy, deltaTime);
            }

            if (!enemy.isAlive()) {
                if (App.isSFXOn()) {
                    GameAssetsManager.getGameAssetsManager().getMonsterKill().play();
                }
                App.getGame().addKill();
                spawnXpDrop(enemy.getPosX(), enemy.getPosY(), 3);
                iter.remove();
                continue;
            }

            Camera camera = worldController.getCamera();
            float screenX = camera.getScreenX(enemy.getPosX());
            float screenY = camera.getScreenY(enemy.getPosY());
            TextureRegion frame =
                enemy.getType().getAnimation().getKeyFrame(elapsedTime);
            Main.getBatch().draw(frame, screenX, screenY, frame.getRegionWidth(), frame.getRegionHeight());
        }

        handleBulletCollisions();

        handleXpDrops(player);
    }

    private void spawnStaticTrees() {
        List<Enemy> placedTrees = new ArrayList<>();

        for (int i = 0; i < NUM_TREES; i++) {
            Enemy placed = null;

            for (int attempt = 0; attempt < TREE_SPAWN_ATTEMPTS; attempt++) {
                float worldX = MathUtils.random(-1000f, 1000f);
                float worldY = MathUtils.random(-1000f, 1000f);

                TextureRegion sampleFrame =
                    EnemyEnum.TREE.getAnimation().getKeyFrame(elapsedTime);
                float w = sampleFrame.getRegionWidth();
                float h = sampleFrame.getRegionHeight();
                if (!worldController.isPositionValid(worldX, worldY, w, h)) {
                    continue;
                }

                boolean tooClose = false;
                for (Enemy existing : placedTrees) {
                    float dx = existing.getPosX() - worldX;
                    float dy = existing.getPosY() - worldY;
                    if (dx * dx + dy * dy < MIN_TREE_SEPARATION * MIN_TREE_SEPARATION) {
                        tooClose = true;
                        break;
                    }
                }
                if (tooClose) continue;

                placed = new Enemy(worldX, worldY, EnemyEnum.TREE);
                placed.updateRectangle();
                placedTrees.add(placed);
                enemies.add(placed);
                break;
            }

            if (placed == null) {
                Gdx.app.log("EnemyController", "Could not place tree #" + i + " after " +
                    TREE_SPAWN_ATTEMPTS + " attempts.");
            }
        }
    }

    private void spawnMovingEnemy(EnemyEnum type) {
        Player player = playerController.getPlayer();
        int maxAttempts = 10;
        float spawnRadius = 300f;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            float angle = MathUtils.random(0f, MathUtils.PI2);
            float spawnX = player.getPosX() + MathUtils.cos(angle) * spawnRadius;
            float spawnY = player.getPosY() + MathUtils.sin(angle) * spawnRadius;

            TextureRegion sampleFrame =
                type.getAnimation().getKeyFrame(elapsedTime);
            float w = sampleFrame.getRegionWidth();
            float h = sampleFrame.getRegionHeight();

            if (worldController.isPositionValid(spawnX, spawnY, w, h)) {
                Enemy e = new Enemy(spawnX, spawnY, type);
                e.updateRectangle();
                enemies.add(e);
                return;
            } else {
                float clampedX = worldController.clampX(spawnX, w);
                float clampedY = worldController.clampY(spawnY, h);
                float dx = clampedX - player.getPosX();
                float dy = clampedY - player.getPosY();
                float distToPlayer = (float)Math.hypot(dx, dy);
                if (distToPlayer > 100f) {
                    Enemy e = new Enemy(clampedX, clampedY, type);
                    e.updateRectangle();
                    enemies.add(e);
                    return;
                }
            }
        }
    }

    private void handleBulletCollisions() {
        ArrayList<Bullet> bullets = weaponController.getBullets();
        Iterator<Bullet> bulletIter = bullets.iterator();

        while (bulletIter.hasNext()) {
            Bullet bullet = bulletIter.next();
            boolean removed = false;

            for (Enemy enemy : enemies) {
                if (enemy.getType() == EnemyEnum.TREE) continue;

                Camera camera = worldController.getCamera();
                float enemyScreenX = camera.getScreenX(enemy.getPosX());
                float enemyScreenY = camera.getScreenY(enemy.getPosY());
                TextureRegion frame =
                    enemy.getType().getAnimation().getKeyFrame(elapsedTime);
                Rectangle enemyRect = new Rectangle(
                    enemyScreenX,
                    enemyScreenY,
                    frame.getRegionWidth(),
                    frame.getRegionHeight()
                );

                if (bullet.getRectangle().overlaps(enemyRect)) {
                    if (App.isSFXOn()) {
                        GameAssetsManager.getGameAssetsManager().getMonsterDamage().play();
                    }
                    enemy.takeDamage(bullet.getDamage());
                    bulletIter.remove();
                    removed = true;
                    break;
                }
            }

            if (!removed && isBulletOffScreen(bullet)) {
                bulletIter.remove();
            }
        }
    }

    private boolean isBulletOffScreen(Bullet bullet) {
        return bullet.getSprite().getX() < 0 ||
            bullet.getSprite().getX() > Gdx.graphics.getWidth() ||
            bullet.getSprite().getY() < 0 ||
            bullet.getSprite().getY() > Gdx.graphics.getHeight();
    }

    private void handleXpDrops(Player player) {
        for (int i = xpDrops.size - 1; i >= 0; i--) {
            XPDrop drop = xpDrops.get(i);

            float pw = player.getPlayerSprite().getWidth();
            float ph = player.getPlayerSprite().getHeight();
            Rectangle playerRect = new Rectangle(
                player.getPosX() - pw / 2f,
                player.getPosY() - ph / 2f,
                pw, ph
            );

            if (drop.collidesWith(playerRect)) {
                player.addXP(drop.getXpValue());
                if (App.isSFXOn()) {
                    GameAssetsManager.getGameAssetsManager().getDropGet().play();
                }
                xpDrops.removeIndex(i);
            } else {
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
    }

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

    private void moveEnemy(Enemy enemy, float deltaTime) {
        Player player = playerController.getPlayer();
        float dirX = player.getPosX() - enemy.getPosX();
        float dirY = player.getPosY() - enemy.getPosY();
        float distance = (float)Math.hypot(dirX, dirY);

        if (distance > 0) {
            float moveX = (dirX / distance) * enemy.getSpeed() * deltaTime;
            float moveY = (dirY / distance) * enemy.getSpeed() * deltaTime;
            float newX = enemy.getPosX() + moveX;
            float newY = enemy.getPosY() + moveY;

            TextureRegion frame = enemy.getType().getAnimation().getKeyFrame(elapsedTime);
            float w = frame.getRegionWidth();
            float h = frame.getRegionHeight();

            if (worldController.isPositionValid(newX, newY, w, h)) {
                enemy.setPosX(newX);
                enemy.setPosY(newY);
            } else {
                enemy.setPosX(worldController.clampX(newX, w));
                enemy.setPosY(worldController.clampY(newY, h));
            }
            enemy.updateRectangle();
        }
    }

    public void dispose() {
    }
}
