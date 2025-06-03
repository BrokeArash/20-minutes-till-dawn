package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.Main;
import com.tilldawn.controller.GameMenuController;
import com.tilldawn.model.*;

public class GameMenu implements Screen, InputProcessor {

    private Stage stage;
    private GameMenuController controller;
    private TextField bulletNum;
    private TextField hp;
    private TextField kill;
    private TextField level;
    private Player player = App.getGame().getPlayer();
    private TextField timerField;

    public GameMenu(GameMenuController controller, Skin skin) {
        this.controller = controller;

        controller.setView(this);
        int ammo = App.getGame().getPlayer().getWeapon().getCurrentAmmo();
        float hp = player.getPlayerHealth();
        this.hp = new TextField(String.valueOf(hp), skin);
        this.kill = new TextField(String.valueOf(App.getGame().getKill()), skin);
        bulletNum = new TextField(String.valueOf(ammo), skin);
        bulletNum.setPosition(10, Gdx.graphics.getHeight() - 30); // Adjust position
        bulletNum.setSize(100, 30); // Set size
        bulletNum.setDisabled(true); // Make it read-only
        this.hp.setPosition(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 30);
        this.hp.setSize(120, 30);
        this.hp.setDisabled(true);
        this.kill.setPosition(10, Gdx.graphics.getHeight() - 100);
        this.kill.setSize(100, 30);
        this.kill.setDisabled(true);

        stage = new Stage(new ScreenViewport());
        stage.addActor(bulletNum);
        stage.addActor(this.hp);
        stage.addActor(this.kill);

        timerField = new TextField("", skin);
        timerField.setPosition(Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 30);
        timerField.setSize(150, 30);
        timerField.setDisabled(true);
        stage.addActor(timerField);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);

        App.getGame().getPlayer().setInitialPosition();

        System.out.println(App.getGame().getPlayer().getHero().getName());
        System.out.println(App.getGame().getPlayer().getWeapon().getWeapon().getName());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        player.getWeapon().update(delta); // Handles reload timing
        App.getGame().updateTime(delta);
        updateKill();

        if (App.getGame().getTimeRemaining() < 30) { // Last 30 seconds
            timerField.getStyle().fontColor = Color.RED;
        } else if (App.getGame().getTimeRemaining() < 60) { // Last minute
            timerField.getStyle().fontColor = Color.ORANGE;
        } else {
            timerField.getStyle().fontColor = Color.BLACK;
        }

        timerField.setText(App.getGame().getFormattedTime());
        if (App.getGame().getTimeRemaining() <= 0) {
            handleGameOver();
        }

        // 2. Handle input
        if (Gdx.input.isKeyPressed(Input.Keys.R) && !player.getWeapon().isReloading()) {
            player.getWeapon().startReload();
        }

        Main.getBatch().begin();
        controller.updateGame();
        Main.getBatch().end();

        bulletNum.setText(String.valueOf(App.getGame().getPlayer().getWeapon().getCurrentAmmo()));
        this.hp.setText(String.valueOf(App.getGame().getPlayer().getPlayerHealth()));
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    private void updateKill() {
        kill.setText(String.valueOf(App.getGame().getKill()));
    }

    private void handleGameOver() {
        // Show game over screen
        App.getGame().setStatus("you won");
        App.getScoreManager().addScore(App.getCurrentUser(), App.getGame().getScore(),
            App.getGame().getKill(), App.getGame().getTime(), App.getGame().getMode());
        Main.getMain().changeScreen(new GameEndMenu(App.getGame().getStatus(),App.getGame().getScore(),
            App.getGame().getKill(), App.getGame().getTime(), GameAssetsManager.getGameAssetsManager().getSkin()));
        // You might want to:
        // 1. Stop game updates
        // 2. Show a game over screen
    }

        @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        controller.getWeaponController().handleWeaponShoot(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        controller.getWeaponController().handleWeaponRotation(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public TextField getBulletNum() {
        return bulletNum;
    }
}
