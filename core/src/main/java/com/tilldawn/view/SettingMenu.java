package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.Main;
import com.tilldawn.controller.SettingMenuController;
import com.tilldawn.model.App;
import com.tilldawn.model.GameAssetsManager;

public class SettingMenu implements Screen {
    private Stage stage;
    private SettingMenuController controller;
    private GameAssetsManager assetsManager;

    private TextButton backButton;
    private SelectBox<String> musicSelectBox;
    private Slider volumeSlider;
    private CheckBox sfx;
    private CheckBox bwCheckbox;
    private CheckBox autoReload;
    private TextButton keybind;

    public SettingMenu(SettingMenuController controller, Skin skin) {
        this.controller = controller;
        this.assetsManager = GameAssetsManager.getGameAssetsManager();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table(skin);
        table.setFillParent(true);
        stage.addActor(table);

        table.add(new Label("Background Music:", skin)).left();
        musicSelectBox = new SelectBox<>(skin);
        musicSelectBox.setItems("Music Track 1", "Music Track 2");
        musicSelectBox.setSelectedIndex(assetsManager.getCurrentMusicIndex());
        table.add(musicSelectBox).fillX().uniformX();
        table.row().pad(10, 0, 0, 0);

        table.add(new Label("Music Volume:", skin)).left();
        volumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        volumeSlider.setValue(assetsManager.getCurrentVolume());
        table.add(volumeSlider).fillX().uniformX();
        table.row().pad(10, 0, 0, 0);

        sfx = new CheckBox("SFX", skin);
        sfx.setChecked(true);
        table.add(sfx).colspan(2).left();
        table.row().pad(10, 0, 0, 0);

        bwCheckbox = new CheckBox("Black & White Mode", skin);
        bwCheckbox.setChecked(Main.isBlackAndWhite());
        bwCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Main.setBlackAndWhite(bwCheckbox.isChecked());
            }
        });
        autoReload = new CheckBox("auto reload", skin);
        autoReload.setChecked(false);
        autoReload.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                App.setAutoReload(autoReload.isChecked());
            }
        });


        table.add(bwCheckbox).colspan(2).left();
        table.add(autoReload).colspan(2).left();
        table.row().pad(10, 0, 0, 0);

        keybind = new TextButton("Change Keys", skin);
        table.add(keybind).colspan(2).fillX().uniformX();
        table.row().pad(10, 0, 0, 0);

        backButton = new TextButton("Back", skin);
        table.add(backButton).colspan(2).fillX().uniformX();

        controller.setView(this);
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

    @Override public void dispose() {
        stage.dispose();
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    public Stage getStage() { return stage; }
    public SettingMenuController getController() { return controller; }
    public GameAssetsManager getAssetsManager() { return assetsManager; }
    public SelectBox<String> getMusicSelectBox() { return musicSelectBox; }
    public Slider getVolumeSlider() { return volumeSlider; }
    public TextButton getBackButton() { return backButton; }
    public CheckBox getSfx() { return sfx; }
    public CheckBox getBwCheckbox() { return bwCheckbox; }
    public TextButton getKeybind() { return keybind; }
}
