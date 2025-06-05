package com.tilldawn.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tilldawn.Main;
import com.tilldawn.model.App;
import com.tilldawn.model.Game;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.view.*;

public class MainMenuController {

    private MainMenu view;

    public void setView(MainMenu menuMenu) {
        this.view = menuMenu;
        setupListeners();
    }

    private void setupListeners() {
        view.getContinueButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });

        view.getPreGameButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                App.setGame(new Game());
                Main.getMain().changeScreen(new PreGameMenu(
                    new PreGameMenuController(),
                    GameAssetsManager.getGameAssetsManager().getSkin()
                ));
            }
        });

        view.getProfileButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().changeScreen(new ProfileMenu(
                    new ProfileMenuController(),
                    GameAssetsManager.getGameAssetsManager().getSkin()
                ));
            }
        });

        view.getSettingButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().changeScreen(new SettingMenu(
                    new SettingMenuController(),
                    GameAssetsManager.getGameAssetsManager().getSkin()
                ));
            }
        });

        view.getTalentButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().changeScreen(new HintMenu(new HintMenuController(), GameAssetsManager.getGameAssetsManager().getSkin()));
            }
        });

        view.getScoreBoardButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().changeScreen(new ScoreBoardMenu());
            }
        });

        view.getExitButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().changeScreen(new LoginMenu(
                    new LoginMenuController(),
                    GameAssetsManager.getGameAssetsManager().getSkin()
                ));
            }
        });
    }
}
