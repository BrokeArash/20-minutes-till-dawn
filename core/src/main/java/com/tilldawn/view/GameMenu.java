package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.Main;
import com.tilldawn.controller.GameMenuController;
import com.tilldawn.model.App;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.model.Player;

public class GameMenu implements Screen, InputProcessor {

    private Stage stage;
    private GameMenuController controller;

    // HUD fields
    private TextField bulletNum;
    private TextField hpField;
    private TextField killField;
    private TextField levelField;
    private ProgressBar xpBar;
    private TextField timerField;
    private Label label;

    // Shortcut to the player
    private Player player = App.getGame().getPlayer();

    public GameMenu(GameMenuController controller, Skin skin) {
        this.controller = controller;
        controller.setView(this);

        // Create all the TextFields, etc.
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

        // Build a custom ProgressBarStyle (since our Skin has no "default‐horizontal")
        ProgressBar.ProgressBarStyle xpStyle = new ProgressBar.ProgressBarStyle();
        // "white" is a drawable in most LibGDX skins; recolor it on the fly:
        xpStyle.background   = skin.newDrawable("white", Color.DARK_GRAY);
        xpStyle.knobBefore   = skin.newDrawable("white", Color.GREEN);
        xpStyle.knob         = skin.newDrawable("white", Color.RED);

        // Create the XP bar with the style:
        //   min = 0, max = player.getLevel() * 20  (for example)
        //   stepSize = 1f, horizontal = false
        float initialMaxXp = player.getLevel() * 20f;
        xpBar = new ProgressBar(0f, initialMaxXp, 1f, false, xpStyle);
        xpBar.setPosition(10, Gdx.graphics.getHeight() - 300);
        xpBar.setSize(200, 50);
        xpBar.setDisabled(true);

        // Timer field
        timerField = new TextField("", skin);
        timerField.setPosition(Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() - 30);
        timerField.setSize(150, 30);
        timerField.setDisabled(true);

        // Create the Stage and add all actors
        stage = new Stage(new ScreenViewport());
        stage.addActor(bulletNum);
        stage.addActor(hpField);
        stage.addActor(killField);
        stage.addActor(levelField);
        label = new Label("xp: " + player.getXp(), skin);
        label.setPosition(3, Gdx.graphics.getHeight() - 250);
        label.setSize(150, 30);
        stage.addActor(label);
        stage.addActor(xpBar);
        stage.addActor(timerField);
    }

    @Override
    public void show() {
        // Let this class handle input (so we can detect touches for shooting)
        Gdx.input.setInputProcessor(this);

        // Reset player position, etc., if needed
        App.getGame().getPlayer().setInitialPosition();
    }

    @Override
    public void render(float delta) {
        // 1) Clear
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 2) Game logic updates
        player.getWeapon().update(delta);           // weapon reloading, etc.
        App.getGame().updateTime(delta);             // timer countdown
        killField.setText(String.valueOf(App.getGame().getKill()));

        // Update timer’s color based on time left
        if (App.getGame().getTimeRemaining() < 30) {
            timerField.getStyle().fontColor = Color.RED;
        } else if (App.getGame().getTimeRemaining() < 60) {
            timerField.getStyle().fontColor = Color.ORANGE;
        } else {
            timerField.getStyle().fontColor = Color.BLACK;
        }
        timerField.setText(App.getGame().getFormattedTime());

        if (App.getGame().getTimeRemaining() <= 0) {
            handleGameOver();
        }

        // 3) Handle reload key
        if (Gdx.input.isKeyPressed(Input.Keys.R) && !player.getWeapon().isReloading()) {
            player.getWeapon().startReload();
        }

        // 4) Draw the world (via your GameMenuController)
        Main.getBatch().begin();
        controller.updateGame();
        Main.getBatch().end();

        // 5) Update all HUD elements:
        bulletNum.setText(String.valueOf(player.getWeapon().getCurrentAmmo()));
        hpField.setText(String.valueOf(player.getPlayerHealth()));
        levelField.setText(String.valueOf(player.getLevel()));
        label.setText("xp: " + player.getXp());

        // Update XP bar’s range/value in case the player leveled up:
        float newMaxXp = player.getLevel() * 20f; // or however you define XP-to-next-level
        xpBar.setRange(0f, newMaxXp);
        xpBar.setValue(player.getXp());

        // 6) Draw the Stage (HUD on top of world)
        stage.act(Math.min(delta, 1/30f));
        stage.draw();
    }

    private void handleGameOver() {
        // Save the final score and switch to a “game over” screen
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

    // ——— InputProcessor to forward clicks to WeaponController ———
    @Override
    public boolean keyDown(int keycode)         { return false; }
    @Override
    public boolean keyUp(int keycode)           { return false; }
    @Override
    public boolean keyTyped(char character)     { return false; }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        controller.getWeaponController().handleWeaponShoot(screenX, screenY);
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

    // Public getter, if another class needs to update anything in the HUD
    public TextField getBulletNum() {
        return bulletNum;
    }
}
