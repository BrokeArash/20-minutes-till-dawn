package com.tilldawn;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.tilldawn.controller.SignupMenuController;
import com.tilldawn.model.App;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.view.SignupMenu;

public class Main extends Game {
    private static Main main;
    private static SpriteBatch batch;
    private static boolean blackAndWhite = false;

    private static ShaderProgram grayscaleShader;

    @Override
    public void create() {
        main = this;
        batch = new SpriteBatch();

        initGrayscaleShader();

        App.initialize();
        setScreen(new SignupMenu(new SignupMenuController(), GameAssetsManager.getGameAssetsManager().getSkin()));
        GameAssetsManager.getGameAssetsManager().playMusic();
    }

    @Override
    public void render() {
        batch.setShader(blackAndWhite ? grayscaleShader : null);
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (grayscaleShader != null) {
            grayscaleShader.dispose();
        }
    }

    public void changeScreen(Screen newScreen) {
        Screen oldScreen = getScreen();
        if (oldScreen != null) {
            oldScreen.hide();
            oldScreen.dispose();
        }
        setScreen(newScreen);
    }

    public static Main getMain() {
        return main;
    }

    public static SpriteBatch getBatch() {
        return batch;
    }

    public static boolean isBlackAndWhite() {
        return blackAndWhite;
    }

    public static void setBlackAndWhite(boolean value) {
        blackAndWhite = value;
    }

    private void initGrayscaleShader() {
        ShaderProgram.pedantic = false;
        grayscaleShader = new ShaderProgram(
            Gdx.files.internal("shaders/grayscale.vert"),
            Gdx.files.internal("shaders/grayscale.frag")
        );

        if (!grayscaleShader.isCompiled()) {
            Gdx.app.error("Shader", "Grayscale shader compile error:\n" + grayscaleShader.getLog());
        }
    }
}
