package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.controller.MainMenuController;
import com.tilldawn.model.App;

public class MainMenu implements Screen {
    private Stage stage;
    private TextButton settingButton;
    private TextButton profileButton;
    private TextButton preGameButton;
    private TextButton scoreBoardButton;
    private TextButton talentButton;
    private TextButton continueButton;
    private TextButton exitButton;
    private Label username;
    private Label point;
    private Image avatar;


    private Table table;
    private MainMenuController controller;

    public MainMenu(MainMenuController mainMenuController, Skin skin) {
        this.stage = new Stage(new ScreenViewport());
        this.settingButton = new TextButton("setting menu", skin);
        this.profileButton = new TextButton("profile menu", skin);
        this.preGameButton = new TextButton("pre game menu", skin);
        this.scoreBoardButton = new TextButton("scoreboard menu", skin);
        this.talentButton = new TextButton("talent menu", skin);
        this.continueButton = new TextButton("continue", skin);
        this.exitButton = new TextButton("exit", skin);
        this.username = new Label(App.getCurrentUser().getUsername(), skin);
        int maxScore = App.getUserDatabase().getMaxScore(App.getCurrentUser());
        this.point = new Label(maxScore + " points", skin);
        this.table = new Table(skin);
        this.controller = mainMenuController;
        this.avatar = new Image(App.getCurrentUser().getAvatar());
        controller.setView(this);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        table.top().padTop(20);

        Table headerTable = new Table();
        headerTable.add(avatar).width(80).height(80).padRight(20);
        Table userInfo = new Table();
        userInfo.add(username).align(Align.left).row();
        userInfo.add(point).align(Align.left);
        headerTable.add(userInfo).align(Align.left).expandX().left();

        table.add(headerTable).colspan(2).padBottom(20).expandX().row();

        TextButton[] buttons = {
            continueButton, preGameButton, profileButton,
            settingButton, talentButton, scoreBoardButton, exitButton
        };

        for (TextButton button : buttons) {
            table.add(button)
                .width(300)
                .height(70)
                .padBottom(20)
                .colspan(2)
                .center()
                .row();
        }

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

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TextButton getSettingButton() {
        return settingButton;
    }

    public void setSettingButton(TextButton settingButton) {
        this.settingButton = settingButton;
    }

    public TextButton getProfileButton() {
        return profileButton;
    }

    public void setProfileButton(TextButton profileButton) {
        this.profileButton = profileButton;
    }

    public TextButton getPreGameButton() {
        return preGameButton;
    }

    public void setPreGameButton(TextButton preGameButton) {
        this.preGameButton = preGameButton;
    }

    public TextButton getScoreBoardButton() {
        return scoreBoardButton;
    }

    public void setScoreBoardButton(TextButton scoreBoardButton) {
        this.scoreBoardButton = scoreBoardButton;
    }

    public TextButton getTalentButton() {
        return talentButton;
    }

    public void setTalentButton(TextButton talentButton) {
        this.talentButton = talentButton;
    }

    public TextButton getContinueButton() {
        return continueButton;
    }

    public void setContinueButton(TextButton continueButton) {
        this.continueButton = continueButton;
    }

    public TextButton getExitButton() {
        return exitButton;
    }

    public void setExitButton(TextButton exitButton) {
        this.exitButton = exitButton;
    }

    public Label getUsername() {
        return username;
    }

    public void setUsername(Label username) {
        this.username = username;
    }

    public Label getPoint() {
        return point;
    }

    public void setPoint(Label point) {
        this.point = point;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public MainMenuController getController() {
        return controller;
    }

    public void setController(MainMenuController controller) {
        this.controller = controller;
    }
}
