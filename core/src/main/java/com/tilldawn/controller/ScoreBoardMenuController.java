package com.tilldawn.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tilldawn.Main;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.view.MainMenu;
import com.tilldawn.view.ScoreBoardMenu;

public class ScoreBoardMenuController {
    private ScoreBoardMenu view;

    public void setView(ScoreBoardMenu view) {
        this.view = view;
        setupListeners();
    }

    public void checkButton() {
        if (view.getBack().isPressed()) {
            Main.getMain().changeScreen(new MainMenu(new MainMenuController(),
                GameAssetsManager.getGameAssetsManager().getSkin()));
        }
    }

    private void setupListeners() {
        // Sort type change listener
        view.getSortType().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Refresh the scores display when sort type changes
                view.refreshScores();
            }
        });
    }
}
