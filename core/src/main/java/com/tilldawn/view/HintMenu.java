package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.controller.HintMenuController;

public class HintMenu implements Screen {

    private final Stage stage;
    private final HintMenuController controller;
    private final TextButton heroes;
    private final TextButton cheats;
    private final TextButton keys;
    private final TextButton abilities;
    private final TextButton back;
    private final Label hintLabel;

    public HintMenu(HintMenuController controller, Skin skin) {
        this.controller = controller;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Buttons
        heroes = new TextButton("Heroes", skin);
        cheats = new TextButton("Cheats", skin);
        keys = new TextButton("Keys", skin);
        abilities = new TextButton("Abilities", skin);
        back = new TextButton("Back", skin);

        // Label in the middle
        hintLabel = new Label("Hover over a button to get a hint.", skin);

        // Main layout
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        // Top row: 4 buttons horizontally
        Table topButtonTable = new Table();
        topButtonTable.add(heroes).pad(10);
        topButtonTable.add(cheats).pad(10);
        topButtonTable.add(keys).pad(10);
        topButtonTable.add(abilities).pad(10);

        // Layout structure
        rootTable.top().padTop(50);
        rootTable.add(topButtonTable).center().row();
        rootTable.add(hintLabel).expandY().center().row();
        rootTable.add(back).padBottom(30).bottom();
        controller.setView(this);

        addListeners();
    }

    private void addListeners() {
        heroes.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hintLabel.setText("SHANA ->   HP: 4   SPEED: 4\n\n\n" +
                    "DIAMOND ->   HP: 7   SPEED: 1\n\n\n" +
                    "SCARLET ->   HP: 3   SPEED: 5\n\n\n" +
                    "LILITH ->   HP: 5   SPEED: 3\n\n\n" +
                    "DASHER ->   HP: 2   SPEED: 10\n\n\n");
                return true;
            }
        });

        cheats.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hintLabel.setText("CHEATS ARE FOR LOSERS\n");
                return true;
            }
        });

        keys.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hintLabel.setText("WASD for movement\n\n\n" +
                    "R for reload\n\n\n" +
                    "left click for shoot\n\n\n");
                return true;
            }
        });

        abilities.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hintLabel.setText("VITALITY ->  adds one HP\n\n\n" +
                    "DAMAGER -> adds 25% damage for 10secs\n\n\n" +
                    "PROCREASE -> adds 1 projectile\n\n\n" +
                    "AMOCREASE -> adds 5 ammo to mag\n\n\n" +
                    "SPEEDY -> doubles speed for 10 secs\n\n\n");
                return true;
            }
        });

        back.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                controller.backToMainMenu();
                return true;
            }
        });
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }

    public TextButton getHeroes() {
        return heroes;
    }

    public HintMenuController getController() {
        return controller;
    }

    public TextButton getCheats() {
        return cheats;
    }

    public TextButton getKeys() {
        return keys;
    }

    public TextButton getAbilities() {
        return abilities;
    }

    public TextButton getBack() {
        return back;
    }

    public Label getHintLabel() {
        return hintLabel;
    }
}
