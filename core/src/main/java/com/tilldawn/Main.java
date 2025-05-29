package com.tilldawn;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tilldawn.controller.SignupMenuController;
import com.tilldawn.model.App;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.view.SignupMenu;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private static Main main;
    private static SpriteBatch batch;


    @Override
    public void create() {
        main = this;
        batch = new SpriteBatch();

        App.initialize();  //initialize database
        setScreen(new SignupMenu(new SignupMenuController(), GameAssetsManager.getGameAssetsManager().getSkin()));
        GameAssetsManager.getGameAssetsManager().playMusic();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
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
}
