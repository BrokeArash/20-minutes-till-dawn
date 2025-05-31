package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.Main;
import com.tilldawn.controller.LoginMenuController;
import com.tilldawn.controller.MainMenuController;
import com.tilldawn.controller.PreGameMenuController;
import com.tilldawn.model.App;
import com.tilldawn.model.Game;
import com.tilldawn.model.GameAssetsManager;

import javax.sound.midi.MidiUnavailableException;

public class GameEndMenu implements Screen {
    private Stage stage;
    private TextButton play;
    private TextButton end;
    private Label gameStatus;
    private Label score;
    private Label kill;
    private Label time;

    private Table table;

    public GameEndMenu(String status, int score, int kill, int time, Skin skin) {
        this.stage = new Stage(new ScreenViewport());
        this.play = new TextButton("play again", skin);
        this.end = new TextButton("end", skin);
        this.gameStatus = new Label(status, skin);
        this.score = new Label(String.valueOf(score), skin);
        this.kill = new Label(String.valueOf(kill), skin);
        this.time = new Label(String.valueOf(time), skin);
        this.table = new Table(skin);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        table.setFillParent(true);
        table.add(gameStatus).colspan(2).padBottom(30).padLeft(500).row();
        table.add(score).colspan(2).padBottom(30).padLeft(500).row();
        table.add(kill).colspan(2).padBottom(30).padLeft(500).row();
        table.add(time).colspan(2).padBottom(30).padLeft(500).row();
        table.add(play).colspan(2).padBottom(30).padLeft(500).row();
        table.add(end).colspan(2).padBottom(30).padLeft(500).row();
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
        Main.getBatch().begin();
        handleButton();
        Main.getBatch().end();
        stage.draw();
    }

    private void handleButton() {
        if (getTextButton().isPressed()) {
            App.setGame(new Game());
            Main.getMain().changeScreen(new PreGameMenu(new PreGameMenuController(), GameAssetsManager.getGameAssetsManager().getSkin()));
        }

        if (getEnd().isPressed()) {
            if (!App.getCurrentUser().getUsername().equalsIgnoreCase("guestpass")) {
                Main.getMain().changeScreen(new LoginMenu(new LoginMenuController(), GameAssetsManager.getGameAssetsManager().getSkin()));
            } else {
                Main.getMain().changeScreen(new MainMenu(new MainMenuController(), GameAssetsManager.getGameAssetsManager().getSkin()));
            }
        }
    }

    public TextButton getTextButton() {
        return play;
    }

    public TextButton getEnd() {
        return end;
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
}
