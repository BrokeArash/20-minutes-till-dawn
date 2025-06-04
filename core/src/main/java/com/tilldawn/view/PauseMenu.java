package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.Main;
import com.tilldawn.controller.MainMenuController;
import com.tilldawn.model.App;
import com.tilldawn.model.GameAssetsManager;

public class PauseMenu implements Screen {

    private Stage stage;
    private Table table;
    private TextButton resume;
    private TextButton cheat;
    private TextButton abilities;
    private TextButton giveUp;
    private Label messageLabel;
    private GameMenu gameMenu;
    private Skin skin = GameAssetsManager.getGameAssetsManager().getSkin();

    public PauseMenu(GameMenu gameMenu) {
        this.gameMenu = gameMenu;
        this.stage = new Stage(new ScreenViewport());
        this.table = new Table(skin);
        this.table.setFillParent(true);

        this.resume = new TextButton("Resume", skin);
        this.cheat = new TextButton("Cheats", skin);
        this.abilities = new TextButton("Abilities", skin);
        this.giveUp = new TextButton("Give Up", skin);

        this.messageLabel = new Label("", skin);
        messageLabel.setColor(Color.RED);

        addListeners();

        table.add(resume).pad(10).row();
        table.add(cheat).pad(10).row();
        table.add(abilities).pad(10).row();
        table.add(giveUp).pad(10).row();
        table.add(messageLabel).pad(20).row();

        stage.addActor(table);
    }

    private void addListeners() {
        resume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(gameMenu);
            }
        });

        cheat.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                messageLabel.setText("No cheat available.");
            }
        });

        abilities.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                messageLabel.setText(App.getGame().getPlayer().getAbilitiesText());
            }
        });

        giveUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().changeScreen(new GameEndMenu("you gave up", App.getGame().getScore(),
                    App.getGame().getKill(), App.getGame().getTime(), GameAssetsManager.getGameAssetsManager().getSkin()));
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
    }
}
