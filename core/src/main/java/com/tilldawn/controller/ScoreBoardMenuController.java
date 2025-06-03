package com.tilldawn.controller;

import com.tilldawn.Main;
import com.tilldawn.model.App;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.view.GameMenu;
import com.tilldawn.view.MainMenu;
import com.tilldawn.view.ScoreBoardMenu;

public class ScoreBoardMenuController {
    private ScoreBoardMenu view;

    public void setView(ScoreBoardMenu view) {
        this.view = view;
    }

    public void checkButton() {
        if (view.getBack().isPressed()) {
            Main.getMain().changeScreen(new MainMenu(new MainMenuController(), GameAssetsManager.getGameAssetsManager().getSkin()));
        }
    }
}
