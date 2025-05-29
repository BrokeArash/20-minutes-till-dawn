package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.controller.PreGameMenuController;

public class PreGameMenu implements Screen {
    private Stage stage;
    private SelectBox<String> heroes;
    private SelectBox<String> weapon;
    private SelectBox<String> mode;
    private TextButton button;
    private Table table;
    private PreGameMenuController controller;
    private Skin skin;

    public PreGameMenu(PreGameMenuController controller, Skin skin) {
        this.stage = new Stage(new ScreenViewport());
        this.controller = controller;
        this.skin = skin;
        this.heroes = new SelectBox<>(skin);
        Array<String> heroArray = new Array<>();
        heroArray.add("Shana");
        heroArray.add("Diamond");
        heroArray.add("Scarlet");
        heroArray.add("Lilith");
        heroArray.add("Dasher");
        this.heroes.setItems(heroArray);
        this.weapon = new SelectBox<>(skin);
        Array<String> weapons = new Array<>();
        weapons.add("Revolver");
        weapons.add("Shotgun");
        weapons.add("SMGs dual");
        this.weapon.setItems(weapons);
        this.mode = new SelectBox<>(skin);
        Array<String> modes = new Array<>();
        modes.add("20 minutes");
        modes.add("10 minutes");
        modes.add("5 minutes");
        modes.add("2 minutes");
        this.mode.setItems(modes);
        this.button = new TextButton("start game", skin);
        this.table = new Table(skin);
        controller.setView(this);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        // Clear previous actors
        table.clear();
        stage.clear();

        table.setFillParent(true);

        // Title at top center
        table.add(new Label("PRE-GAME SETTINGS", skin, "title"))
            .colspan(2).padBottom(40).row();

        // Hero selection
        table.add(new Label("HERO:", skin)).right().padRight(10).width(150);
        table.add(heroes).width(300).left().padBottom(20).row();

        // Weapon selection
        table.add(new Label("WEAPON:", skin)).right().padRight(10).width(150);
        table.add(weapon).width(300).left().padBottom(20).row();

        // Mode selection
        table.add(new Label("MODE:", skin)).right().padRight(10).width(150);
        table.add(mode).width(300).left().padBottom(40).row();

        // Start button centered
        table.add(button)
            .colspan(2)
            .width(400)
            .height(60)
            .padTop(20)
            .fillX();

        stage.addActor(table);
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

    public SelectBox<String> getHeroes() {
        return heroes;
    }

    public SelectBox<String> getWeapon() {
        return weapon;
    }

    public SelectBox<String> getMode() {
        return mode;
    }

    public TextButton getButton() {
        return button;
    }
}
