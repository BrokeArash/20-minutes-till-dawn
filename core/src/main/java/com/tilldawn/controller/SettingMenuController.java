package com.tilldawn.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tilldawn.Main;
import com.tilldawn.model.App;
import com.tilldawn.model.Game;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.view.MainMenu;
import com.tilldawn.view.SettingMenu;

import java.util.Set;

public class SettingMenuController {

    private SettingMenu view;

    public void setView(SettingMenu settingMenu) {
        this.view = settingMenu;
        setupListeners();
        // No specific listeners needed here for now as UI interactions are handled in SettingMenu itself
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

        view.getMusicSelectBox().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAssetsManager.getGameAssetsManager().switchMusic(view.getMusicSelectBox().getSelectedIndex());
            }
        });

        view.getVolumeSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAssetsManager.getGameAssetsManager().setMusicVolume(view.getVolumeSlider().getValue());
            }
        });

        view.getSfx().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (view.getSfx().isChecked()) {
                    App.setIsSFXOn(false);
                } else {
                    App.setIsSFXOn(true);
                }
            }
        });
    }

}
