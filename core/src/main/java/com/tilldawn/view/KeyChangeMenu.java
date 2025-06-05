package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.Main;
import com.tilldawn.controller.MainMenuController;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.model.enums.Keys;

public class KeyChangeMenu implements Screen {
    private Stage stage;
    private TextButton up, down, left, right, reload, back;
    private Table table;
    private Label label;
    private Skin skin = GameAssetsManager.getGameAssetsManager().getSkin();

    private TextButton selectedButton = null;
    private boolean waitingForRebind = false;

    public KeyChangeMenu() {
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        up = new TextButton("Up: " + Input.Keys.toString(Keys.UP.getKeys()), skin);
        down = new TextButton("Down: " + Input.Keys.toString(Keys.DOWN.getKeys()), skin);
        left = new TextButton("Left: " + Input.Keys.toString(Keys.LEFT.getKeys()), skin);
        right = new TextButton("Right: " + Input.Keys.toString(Keys.RIGHT.getKeys()), skin);
        reload = new TextButton("Reload: " + Input.Keys.toString(Keys.RELOAD.getKeys()), skin);
        back = new TextButton("Back", skin);

        label = new Label("Click a button to rebind a key", skin);
        table = new Table(skin);
        table.setFillParent(true);
        table.center();

        table.add(label).colspan(2).padBottom(20).row();
        table.add(up).pad(10).row();
        table.add(down).pad(10).row();
        table.add(left).pad(10).row();
        table.add(right).pad(10).row();
        table.add(reload).pad(10).row();
        table.add(back).pad(20).row();

        stage.addActor(table);

        addListeners();
    }

    private void addListeners() {
        up.addListener(createRebindListener(up, Keys.UP));
        down.addListener(createRebindListener(down, Keys.DOWN));
        left.addListener(createRebindListener(left, Keys.LEFT));
        right.addListener(createRebindListener(right, Keys.RIGHT));
        reload.addListener(createRebindListener(reload, Keys.RELOAD));

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().changeScreen(new MainMenu(new MainMenuController(), skin));
            }
        });
    }

    private ClickListener createRebindListener(final TextButton button, final Keys keyEnum) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                waitingForRebind = true;
                selectedButton = button;
                label.setText("Press a key to bind to: " + button.getText().toString());
                Gdx.app.log("Rebinding", "Waiting to rebind: " + keyEnum.name());
            }
        };
    }

    private void handleKeyRebinding() {
        if (waitingForRebind && selectedButton != null) {
            for (int key = 0; key < 256; key++) {
                if (Gdx.input.isKeyJustPressed(key)) {
                    Keys keyEnum = getKeyEnumForButton(selectedButton);
                    if (keyEnum != null) {
                        keyEnum.setKeys(key);
                        selectedButton.setText(keyEnum.name().substring(0, 1) +
                            keyEnum.name().substring(1).toLowerCase() + ": " + Input.Keys.toString(key));
                        label.setText("Rebound to: " + Input.Keys.toString(key));
                        waitingForRebind = false;
                        selectedButton = null;
                        break;
                    }
                }
            }
        }
    }

    private Keys getKeyEnumForButton(TextButton button) {
        if (button == up) return Keys.UP;
        if (button == down) return Keys.DOWN;
        if (button == left) return Keys.LEFT;
        if (button == right) return Keys.RIGHT;
        if (button == reload) return Keys.RELOAD;
        return null;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleKeyRebinding();
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }
}
