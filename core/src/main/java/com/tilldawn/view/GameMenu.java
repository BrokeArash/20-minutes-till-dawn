package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.Main;
import com.tilldawn.controller.GameMenuController;
import com.tilldawn.model.App;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.model.Player;
import com.tilldawn.model.enums.Keys;

public class GameMenu implements Screen, InputProcessor {

    private Stage stage;
    private GameMenuController controller;

    private TextField bulletNum;
    private TextField hpField;
    private TextField killField;
    private TextField levelField;
    private ProgressBar xpBar;
    private TextField timerField;
    private Label label;
    private boolean isPaused = false;

    private Player player = App.getGame().getPlayer();

    public GameMenu(GameMenuController controller, Skin skin) {
        this.controller = controller;
        controller.setView(this);

        int ammo = player.getWeapon().getCurrentAmmo();
        float hp  = player.getPlayerHealth();

        bulletNum = new TextField(String.valueOf(ammo), skin);
        bulletNum.setPosition(10, Gdx.graphics.getHeight() - 30);
        bulletNum.setSize(100, 30);
        bulletNum.setDisabled(true);

        hpField = new TextField(String.valueOf(hp), skin);
        hpField.setPosition(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 30);
        hpField.setSize(120, 30);
        hpField.setDisabled(true);

        killField = new TextField(String.valueOf(App.getGame().getKill()), skin);
        killField.setPosition(10, Gdx.graphics.getHeight() - 100);
        killField.setSize(100, 30);
        killField.setDisabled(true);

        levelField = new TextField(String.valueOf(player.getLevel()), skin);
        levelField.setPosition(10, Gdx.graphics.getHeight() - 200);
        levelField.setSize(100, 30);
        levelField.setDisabled(true);

        ProgressBar.ProgressBarStyle xpStyle = new ProgressBar.ProgressBarStyle();
        xpStyle.background   = skin.newDrawable("white", Color.DARK_GRAY);
        xpStyle.knobBefore   = skin.newDrawable("white", Color.GREEN);
        xpStyle.knob         = skin.newDrawable("white", Color.RED);

        float initialMaxXp = player.getLevel() * 20f;
        xpBar = new ProgressBar(0f, initialMaxXp, 1f, false, xpStyle);
        xpBar.setPosition(10, Gdx.graphics.getHeight() - 300);
        xpBar.setSize(200, 50);
        xpBar.setDisabled(true);

        timerField = new TextField("", skin);
        timerField.setPosition(Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() - 30);
        timerField.setSize(150, 30);
        timerField.setDisabled(true);


    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        stage.addActor(bulletNum);
        stage.addActor(hpField);
        stage.addActor(killField);
        stage.addActor(levelField);
        label = new Label("xp: " + player.getXp(), GameAssetsManager.getGameAssetsManager().getSkin());
        label.setPosition(3, Gdx.graphics.getHeight() - 250);
        label.setSize(150, 30);
        stage.addActor(label);
        stage.addActor(xpBar);
        stage.addActor(timerField);
        Gdx.input.setInputProcessor(this);

        App.getGame().getPlayer().setInitialPosition();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        if (!isPaused) {
            player.getWeapon().update(delta);
            App.getGame().updateTime(delta);

            Main.getBatch().begin();
            controller.updateGame();
            Main.getBatch().end();
        }

        if (!isPaused && Gdx.input.isKeyPressed(Keys.RELOAD.getKeys()) && !player.getWeapon().isReloading()) {
            player.getWeapon().startReload();
        }

        killField.setText(String.valueOf(App.getGame().getKill()));
        timerField.setText(App.getGame().getFormattedTime());

        if (App.getGame().getTimeRemaining() < 30) {
            timerField.getStyle().fontColor = Color.RED;
        } else if (App.getGame().getTimeRemaining() < 60) {
            timerField.getStyle().fontColor = Color.ORANGE;
        } else {
            timerField.getStyle().fontColor = Color.BLACK;
        }

        if (App.getGame().getTimeRemaining() <= 0) {
            handleGameOver();
        }

        bulletNum.setText(String.valueOf(player.getWeapon().getCurrentAmmo()));
        hpField.setText(String.valueOf(player.getPlayerHealth()));
        levelField.setText(String.valueOf(player.getLevel()));
        label.setText("xp: " + player.getXp());

        float newMaxXp = player.getLevel() * 20f;
        xpBar.setRange(0f, newMaxXp);
        xpBar.setValue(player.getXp());

        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }


    private void handleGameOver() {
        App.getGame().setStatus("you won");
        var currentUser = App.getCurrentUser();
        var record = new com.tilldawn.model.ScoreRecord(
            currentUser,
            App.getGame().getScore(),
            App.getGame().getKill(),
            App.getGame().getTime(),
            App.getGame().getMode()
        );
        App.getUserDatabase().saveScoreRecord(record);

        Main.getMain().changeScreen(
            new GameEndMenu(
                App.getGame().getStatus(),
                App.getGame().getScore(),
                App.getGame().getKill(),
                App.getGame().getTime(),
                GameAssetsManager.getGameAssetsManager().getSkin()
            )
        );
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause()   {}
    @Override public void resume()  {}
    @Override public void hide()    {}
    @Override public void dispose() {
        stage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            isPaused = !isPaused;
            Main.getMain().changeScreen(new PauseMenu(this));
            return true;
        }
        return false;
    }
    @Override
    public boolean keyUp(int keycode)           { return false; }
    @Override
    public boolean keyTyped(char character)     { return false; }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 stageCoords = stage.screenToStageCoordinates(new Vector2(screenX, screenY));

        InputEvent event = new InputEvent();
        event.setType(InputEvent.Type.touchDown);
        event.setStage(stage);
        event.setStageX(stageCoords.x);
        event.setStageY(stageCoords.y);
        event.setPointer(pointer);
        event.setButton(button);

        if (stage.hit(stageCoords.x, stageCoords.y, true) != null && isPaused) {
            return true;
        }

        if (!isPaused) {
            controller.getWeaponController().handleWeaponShoot(screenX, screenY);
        }

        return false;
    }

    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button)   { return false; }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override public boolean touchDragged(int screenX, int screenY, int pointer)          { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) {
        controller.getWeaponController().handleWeaponRotation(screenX, screenY);
        return false;
    }
    @Override public boolean scrolled(float amountX, float amountY)    { return false; }

    public TextField getBulletNum() {
        return bulletNum;
    }
}
