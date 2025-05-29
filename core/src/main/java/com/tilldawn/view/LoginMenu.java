package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.controller.LoginMenuController;
import com.tilldawn.model.App;
import com.tilldawn.model.GameAssetsManager;

public class LoginMenu implements Screen {
    private Stage stage;
    private Texture backgroundTex;
    private Image background;
    private Texture backgroundTex2;
    private Image background2;
    private TextButton button;
    private final TextButton signup;
    private Table table;
    private LoginMenuController controller;
    private final TextField userField;
    private final TextField passField;
    private Label errorPass;
    private TextButton forget;


    public LoginMenu(LoginMenuController controller, Skin skin) {
        this.stage = new Stage(new ScreenViewport());
        this.backgroundTex = GameAssetsManager.getGameAssetsManager().getBackgroundTex();
        this.backgroundTex2 = GameAssetsManager.getGameAssetsManager().getBackgroundTex2();
        this.background = new Image(backgroundTex);
        this.background.setScale(0.5F, 1F);
        this.background2 = new Image(backgroundTex2);
        this.background2.setScale(0.5F, 1F);
        this.button = new TextButton("go to main menu", skin);
        this.forget = new TextButton("forget password", skin);
        this.table = new Table(skin);
        this.userField = new TextField(" enter your username", skin);
        this.passField = new TextField(" enter your password", skin);
        this.errorPass = new Label("", skin);
        this.signup = new TextButton("go to signup menu", skin);
        this.controller = controller;
        controller.setView(this);
        App.getUserDatabase().setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        background.setFillParent(true);
        stage.addActor(background);
        background2.setFillParent(true);
        background2.setX(stage.getWidth() + background2.getWidth()/2);
        stage.addActor(background2);

        table.setFillParent(true);
        table.center();
        table.add(userField).width(600).height(60).colspan(2).padBottom(20).row();
        table.add(passField).width(600).height(60).colspan(2).padBottom(20).row();
        table.add(errorPass).colspan(2).padBottom(10).row();
        table.add(button).colspan(2).padBottom(50).row();
        table.add(forget).colspan(2).padBottom(50).row();
        table.add(signup).colspan(2).padBottom(50).row();
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
        Gdx.input.setInputProcessor(null);
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

    public TextButton getButton() {
        return button;
    }

    public void setButton(TextButton button) {
        this.button = button;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public LoginMenuController getController() {
        return controller;
    }

    public void setController(LoginMenuController controller) {
        this.controller = controller;
    }

    public TextField getUserField() {
        return userField;
    }

    public TextField getPassField() {
        return passField;
    }

    public Label getErrorPass() {
        return errorPass;
    }

    public void setErrorPass(String errorPass) {
        this.errorPass.setText(errorPass);
        this.errorPass.setColor(Color.RED);
    }

    public TextButton getForget() {
        return forget;
    }

    public void setForget(TextButton forget) {
        this.forget = forget;
    }

    public TextButton getSignup() {
        return signup;
    }
}
