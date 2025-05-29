package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.controller.SignupMenuController;
import com.tilldawn.model.GameAssetsManager;

public class SignupMenu implements Screen {

    private Stage stage;
    private Texture backgroundTex;
    private Image background;
    private Texture backgroundTex2;
    private Image background2;
    private final TextButton playButton;
    private final Texture gameLogoTex;
    private final Image gameLogo;
    private final TextField userField;
    private Label errorUser;
    private final TextField passField;
    private Label errorPass;
    private final Label security;
    private final SelectBox<String> secBox;
    private final TextButton guest;
    private final TextButton login;
    public Table table;
    private final SignupMenuController controller;


    public SignupMenu(SignupMenuController controller, Skin skin) {
        this.playButton = new TextButton("enter", skin);
        this.gameLogoTex = GameAssetsManager.getGameAssetsManager().getGameLogo();
        this.gameLogo = new Image(gameLogoTex);
        this.backgroundTex = GameAssetsManager.getGameAssetsManager().getBackgroundTex();
        this.backgroundTex2 = GameAssetsManager.getGameAssetsManager().getBackgroundTex2();
        this.background = new Image(backgroundTex);
        this.background.setScale(0.5F, 1F);
        this.background2 = new Image(backgroundTex2);
        this.background2.setScale(0.5F, 1F);
        this.userField = new TextField(" enter your username", skin);
        this.errorUser = new Label("", skin);
        this.passField = new TextField(" enter your password", skin);
        this.errorPass = new Label("", skin);
        this.security = new Label("zavale aghl dari ?", skin);
        this.secBox = new SelectBox<>(skin);
        Array<String> answers = new Array<>();
        answers.add("yes");
        answers.add("no (yes)");
        this.secBox.setItems(answers);
        this.guest = new TextButton("login as a guest", skin);
        this.login = new TextButton("go to login menu", skin);
        this.table = new Table(skin);
        this.controller = controller;
        controller.setView(this);
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
        table.add(gameLogo);
        table.row().pad(10, 0, 10, 0);
        table.add(userField).width(600).height(60).padBottom(10).colspan(2);
        table.row().pad(10, 0, 10, 0);
        table.add(errorUser).colspan(2).padBottom(10);
        table.row().pad(10, 0, 10, 0);
        table.add(passField).width(600).height(60).colspan(2).padBottom(20);
        table.row().pad(10, 0, 10, 0);
        table.add(errorPass).colspan(2).padBottom(10).row();
        table.row().pad(10, 0, 10, 0);
        table.add(security).colspan(2).padBottom(10).row();
        table.row().pad(10, 0, 10, 0);
        table.add(secBox).colspan(2).padBottom(50);
        table.row().pad(10, 0, 10, 0);
        table.add(playButton).colspan(2).padBottom(10);
        table.row().pad(10, 0, 10, 0);
        table.add(guest).colspan(2).padBottom(10);
        table.row().pad(10, 0, 10, 0);
        table.add(login).colspan(2).padBottom(10);
        table.row().pad(10, 0, 10, 0);
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

    public TextButton getPlayButton() {
        return playButton;
    }

    public Texture getGameLogoTex() {
        return gameLogoTex;
    }



    public TextField getUserField() {
        return userField;
    }

    public Table getTable() {
        return table;
    }

    public TextField getPassField() {
        return passField;
    }

    public Label getSecurity() {
        return security;
    }

    public SelectBox<String> getSecBox() {
        return secBox;
    }

    public SignupMenuController getController() {
        return controller;
    }

    public Label getErrorPass() {
        return errorPass;
    }

    public void setErrorPass(String errorPass) {
        this.errorPass.setText(errorPass);
        this.errorPass.setColor(Color.RED);
    }

    public Label getErrorUser() {
        return errorUser;
    }

    public void setErrorUser(String errorUser) {
        this.errorUser.setText(errorUser);
        this.errorUser.setColor(Color.RED);
    }

    public TextButton getGuest() {
        return guest;
    }

    public TextButton getLogin() {
        return login;
    }
}
