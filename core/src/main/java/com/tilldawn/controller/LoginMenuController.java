package com.tilldawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tilldawn.Main;
import com.tilldawn.model.App;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.model.User;
import com.tilldawn.model.UserDatabase;
import com.tilldawn.view.ForgetPassword;
import com.tilldawn.view.LoginMenu;
import com.tilldawn.view.MainMenu;
import com.tilldawn.view.SignupMenu;

public class LoginMenuController {
    private LoginMenu view;

    public void setView(LoginMenu loginMenu) {
        this.view = loginMenu;
        setupListeners();
    }

    private void setupListeners() {
        view.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleLogin();
            }
        });

        view.getForget().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = view.getUserField().getText().trim();

                if (username.isEmpty()) {
                    view.setErrorPass("Enter your username first");
                    return;
                }

                UserDatabase db = App.getUserDatabase();
                if (!db.isUsernameTaken(username)) {
                    view.setErrorPass("Username not found");
                    return;
                }

                Main.getMain().changeScreen(new ForgetPassword(
                    LoginMenuController.this,
                    GameAssetsManager.getGameAssetsManager().getSkin(),
                    username
                ));
            }
        });

        view.getSignup().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().changeScreen(new SignupMenu(
                    new SignupMenuController(),
                    GameAssetsManager.getGameAssetsManager().getSkin()
                ));
            }
        });
    }

    private void handleLogin() {
        String username = view.getUserField().getText().trim();
        String password = view.getPassField().getText().trim();

        UserDatabase db = App.getUserDatabase();
        User user = db.loginUser(username, password);

        if (user != null) {
            Gdx.app.log("Login", "Successfully logged in as: " + user.getUsername());
            App.setCurrentUser(user);
            Main.getMain().changeScreen(new MainMenu(
                new MainMenuController(),
                GameAssetsManager.getGameAssetsManager().getSkin()
            ));
        } else {
            Gdx.app.log("Login", "Failed login attempt for: " + username);
        }
    }
}
