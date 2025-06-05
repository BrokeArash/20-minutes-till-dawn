package com.tilldawn.model;


public class App {
    private static Game game;
    private static User currentUser;
    private static UserDatabase userDatabase;
    private static boolean SFXOn = true;
    private static boolean autoReload = false;

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

    public static boolean isSFXOn() {
        return SFXOn;
    }

    public static void setSFXOn(boolean SFXOn) {
        App.SFXOn = SFXOn;
    }

    public static boolean isAutoReload() {
        return autoReload;
    }

    public static void setAutoReload(boolean autoReload) {
        App.autoReload = autoReload;
    }
}
