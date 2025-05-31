package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.Main;
import com.tilldawn.controller.GameMenuController;
import com.tilldawn.model.App;
import com.tilldawn.model.Player;

public class GameMenu implements Screen, InputProcessor {

    private Stage stage;
    private GameMenuController controller;
    private TextField bulletNum;
    private Player player = App.getGame().getPlayer();

    public GameMenu(GameMenuController controller, Skin skin) {
        this.controller = controller;
        controller.setView(this);
        int ammo = App.getGame().getPlayer().getWeapon().getCurrentAmmo();
        bulletNum = new TextField(String.valueOf(ammo), skin);
        bulletNum.setPosition(10, Gdx.graphics.getHeight() - 30); // Adjust position
        bulletNum.setSize(100, 30); // Set size
        bulletNum.setDisabled(true); // Make it read-only

        stage = new Stage(new ScreenViewport());
        stage.addActor(bulletNum);
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

        // 2. Handle input
        if (Gdx.input.isKeyPressed(Input.Keys.R) && !player.getWeapon().isReloading()) {
            player.getWeapon().startReload();
        }

        Main.getBatch().begin();
        controller.updateGame();
        Main.getBatch().end();

        bulletNum.setText(String.valueOf(App.getGame().getPlayer().getWeapon().getCurrentAmmo()));
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

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
