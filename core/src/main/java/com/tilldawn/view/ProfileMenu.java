package com.tilldawn.view;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.controller.ProfileMenuController;
import com.tilldawn.model.GameAssetsManager;

public class ProfileMenu implements Screen {

    private Stage stage;
    private ProfileMenuController controller;
    private GameAssetsManager assetsManager;
    TextButton backButton;
    TextButton changeUsernameButton;
    TextButton changePasswordButton;
    TextButton deleteAccountButton;
    Table table;

    public ProfileMenu(ProfileMenuController controller, Skin skin) {
        this.controller = controller;
        this.assetsManager = GameAssetsManager.getGameAssetsManager();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table = new Table(skin);
        table.setFillParent(true);
        stage.addActor(table);

        changeUsernameButton = new TextButton("change username", skin);
        changePasswordButton = new TextButton("change password", skin);
        deleteAccountButton = new TextButton("delete account", skin);
        backButton = new TextButton("Back", skin);
        controller.setView(this);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        table.clear();
        table.add(changeUsernameButton).colspan(2).fillX().uniformX();
        table.add(changePasswordButton).colspan(2).fillX().uniformX();
        table.add(deleteAccountButton).colspan(2).fillX().uniformX();
        table.add(backButton).colspan(2).fillX().uniformX();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    public TextButton getBackButton() {
        return backButton;
    }

    public TextButton getChangeUsernameButton() {
        return changeUsernameButton;
    }

    public TextButton getChangePasswordButton() {
        return changePasswordButton;
    }

    public TextButton getDeleteAccountButton() {
        return deleteAccountButton;
    }
}
