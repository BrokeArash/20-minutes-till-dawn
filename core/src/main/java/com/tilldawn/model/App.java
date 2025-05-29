package com.tilldawn.model;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;

public class App {
    private static Game game;
    private static User currentUser;
    private static UserDatabase userDatabase;

    public static void initialize() {
        userDatabase = new UserDatabase();
    }

    public static UserDatabase getUserDatabase() {
        if (userDatabase == null) {
            initialize();
        }
        return userDatabase;
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
