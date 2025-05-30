package com.tilldawn.controller;


import com.badlogic.gdx.graphics.Texture;
import com.tilldawn.Main;
import com.tilldawn.model.Camera;

public class WorldController {
    private PlayerController playerController;
    private Texture backgroundTexture;
    private Camera camera;

    public WorldController(PlayerController playerController) {
        this.backgroundTexture = new Texture("background.png");
        this.playerController = playerController;
        this.camera = new Camera();
    }

    public void update() {
        camera.setTarget(playerController.getPlayer().getPosX(), playerController.getPlayer().getPosY());
        camera.update();
        float x = camera.getScreenX(0);
        float y = camera.getScreenY(0);
        Main.getBatch().draw(backgroundTexture, x, y);
    }

    public Camera getCamera() {
        return camera;
    }

}
