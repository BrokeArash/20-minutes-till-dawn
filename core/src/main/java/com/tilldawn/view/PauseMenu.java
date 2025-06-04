package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.Main;
import com.tilldawn.model.GameAssetsManager;

public class PauseMenu implements Screen {

    private Stage stage;
    private Table table;
    private TextButton button;
    private GameMenu gameMenu;
    private Skin skin = GameAssetsManager.getGameAssetsManager().getSkin();

    public PauseMenu(GameMenu gameMenu) {
        this.gameMenu = gameMenu;
        this.stage = new Stage(new ScreenViewport());
        this.table = new Table(skin);
        this.button = new TextButton("button", skin);
        table.add(button);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        table.setFillParent(true);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
        Main.getBatch().begin();
        checkButton();
        Main.getBatch().end();
        stage.draw();
    }

    private void checkButton() {
        if (button.isChecked()) {
            //gameMenu.show();
            Main.getMain().setScreen(gameMenu);
        }
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
}
