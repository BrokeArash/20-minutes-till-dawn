package com.tilldawn.model;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.HashMap;

public class App {
    private static Game game;
    private static User currentUser;
    private static UserDatabase userDatabase;
    private static ScoreManager scoreManager;

    public static void initialize() {
        userDatabase = new UserDatabase();
    }

    public static UserDatabase getUserDatabase() {
        if (userDatabase == null) {
            initialize();
        }
        return userDatabase;
    }

    public static ScoreManager getScoreManager() {
        if (scoreManager == null) {
            scoreManager = new ScoreManager();
        }
        return scoreManager;
    }

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        App.game = game;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        App.currentUser = currentUser;
    }

}
