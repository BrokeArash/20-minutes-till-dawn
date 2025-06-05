package com.tilldawn.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.tilldawn.Main;
import com.tilldawn.model.Camera;
import com.tilldawn.model.GameAssetsManager;


public class WorldController {
    private PlayerController playerController;
    private Texture backgroundTexture;
    private Camera camera;
    private float backgroundWidth;
    private float backgroundHeight;
    private boolean cameraInitialized = false;


    public WorldController(PlayerController playerController) {
        this.backgroundTexture = GameAssetsManager.getGameAssetsManager().getBackgroundTexture();
        this.playerController = playerController;
        this.camera = new Camera();
        this.backgroundWidth = backgroundTexture.getWidth();
        this.backgroundHeight = backgroundTexture.getHeight();

    }

    public void update() {
        if (!cameraInitialized) {
            camera.initialize(playerController.getPlayer().getPosX(), playerController.getPlayer().getPosY());
            cameraInitialized = true;
        }

        camera.setTarget(playerController.getPlayer().getPosX(), playerController.getPlayer().getPosY());
        camera.update();

        float backgroundLeftX = -backgroundWidth / 2f;
        float backgroundBottomY = -backgroundHeight / 2f;

        float screenX = camera.getScreenX(backgroundLeftX);
        float screenY = camera.getScreenY(backgroundBottomY);

        Main.getBatch().draw(backgroundTexture, screenX, screenY);
    }

    public Camera getCamera() {
        return camera;
    }

    public boolean isPositionValid(float x, float y, float entityWidth, float entityHeight) {
        float minX = -backgroundWidth / 2f;
        float maxX = backgroundWidth / 2f - entityWidth;
        float minY = -backgroundHeight / 2f;
        float maxY = backgroundHeight / 2f - entityHeight;


        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    public float clampX(float x, float entityWidth) {
        float minX = -backgroundWidth / 2f;
        float maxX = backgroundWidth / 2f - entityWidth;

        return Math.max(minX, Math.min(maxX, x));
    }

    public float clampY(float y, float entityHeight) {
        float minY = -backgroundHeight / 2f;
        float maxY = backgroundHeight / 2f - entityHeight;

        return Math.max(minY, Math.min(maxY, y));
    }
}
