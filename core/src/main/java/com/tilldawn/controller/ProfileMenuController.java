package com.tilldawn.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tilldawn.Main;
import com.tilldawn.model.App;
import com.tilldawn.view.MainMenu;
import com.tilldawn.view.ProfileMenu;
import com.tilldawn.model.GameAssetsManager;

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
                Main.getMain().changeScreen(
                    new MainMenu(
                        new MainMenuController(),
                        GameAssetsManager.getGameAssetsManager().getSkin()
                    ));
            }
        });
    }

    public void onAvatarChosen(String choice, Texture texture) {
        App.getCurrentUser().setAvatar(texture);

    }
}
