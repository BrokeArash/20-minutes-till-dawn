package com.tilldawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tilldawn.Main;
import com.tilldawn.model.*;
import com.tilldawn.model.enums.SignupEnums;
import com.tilldawn.view.LoginMenu;
import com.tilldawn.view.MainMenu;
import com.tilldawn.view.SignupMenu;

public class SignupMenuController{

    private SignupMenu view;
    private boolean passwordValid;

    public void setView(SignupMenu signupMenu) {
        this.view = signupMenu;
        setupListeners();
    }

    private void setupListeners() {
        view.getPassField().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                validatePassword();
            }
        });

        view.getPlayButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleSignup();
            }
        });

        view.getGuest().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleGuestLogin();
            }
        });

        view.getLogin().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(new LoginMenu(
                    new LoginMenuController(),
                    GameAssetsManager.getGameAssetsManager().getSkin()
                ));
            }
        });
    }

    private void handleGuestLogin() {
        UserDatabase db = App.getUserDatabase();
        User guestUser = db.createGuestUser();
        Game newGame = new Game();
        App.setGame(newGame);
        if (guestUser != null) {
            Gdx.app.log("GuestLogin", "Logged in as: " + guestUser.getUsername());
            App.setCurrentUser(guestUser);
            Main.getMain().setScreen(new MainMenu(
                new MainMenuController(),
                GameAssetsManager.getGameAssetsManager().getSkin()
            ));
        } else {
            view.setErrorUser("Failed to create guest account");
        }
    }


    private void handleSignup() {
        view.setErrorUser("");
        view.setErrorPass("");

        String username = view.getUserField().getText().trim();
        String password = view.getPassField().getText().trim();
        String securityAnswer = view.getSecBox().getSelected();

        if (username.isEmpty()) {
            view.setErrorUser("Please enter a username");
            return;
        }

        if (username.length() < 3) {
            view.setErrorUser("Username must be at least 3 characters");
            return;
        }

        if (password.isEmpty()) {
            view.setErrorPass("Please enter a password");
            return;
        }

        if (!passwordValid) {
            view.setErrorPass("Password must contain: 8+ chars, 1 uppercase, 1 number, 1 special");
            return;
        }

        try {
            UserDatabase userDatabase = App.getUserDatabase();
            if (userDatabase.registerUser(username, password, securityAnswer)) {
                Gdx.app.log("Signup", "Account created for: " + username);
                Main.getMain().changeScreen(new LoginMenu(
                    new LoginMenuController(),
                    GameAssetsManager.getGameAssetsManager().getSkin()
                ));
            } else {
                view.setErrorUser("Username already exists!");
            }
        } catch (Exception e) {
            Gdx.app.error("Signup", "Registration failed: " + e.getMessage());
            view.setErrorUser("Registration failed. Please try again.");
        }
    }


    public void validatePassword() {
        if (view != null) {
            String password = view.getPassField().getText();
            passwordValid = password.matches(SignupEnums.CHECK_PASSWORD.getPattern());
            view.setErrorPass(passwordValid ? "" : "Weak password - keep typing...");
        }
    }
}
