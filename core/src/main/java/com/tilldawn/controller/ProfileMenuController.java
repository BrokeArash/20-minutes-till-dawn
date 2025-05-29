package com.tilldawn.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tilldawn.Main;
import com.tilldawn.model.App;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.view.*;

public class ProfileMenuController {
    private ProfileMenu view;

    public void setView(ProfileMenu profileMenu) {
        this.view = profileMenu;
        setupListeners();
    }

    private void setupListeners() {
        view.getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().changeScreen(new MainMenu(
                    new MainMenuController(),
                    GameAssetsManager.getGameAssetsManager().getSkin()
                ));
            }
        });

        view.getChangeUsernameButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().changeScreen(new ChangeUsernameMenu(
                    new ProfileMenuController(),
                    GameAssetsManager.getGameAssetsManager().getSkin(),
                    App.getCurrentUser().getUsername()
                ));
            }
        });

        view.getChangePasswordButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new ChangePasswordMenu(
                    new ProfileMenuController(),
                    GameAssetsManager.getGameAssetsManager().getSkin(),
                    App.getCurrentUser().getUsername()
                ));
            }
        });

        view.getDeleteAccountButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new DeleteAccountMenu(
                    new ProfileMenuController(),
                    GameAssetsManager.getGameAssetsManager().getSkin(),
                    App.getCurrentUser().getUsername()
                ));
            }
        });
    }
}
