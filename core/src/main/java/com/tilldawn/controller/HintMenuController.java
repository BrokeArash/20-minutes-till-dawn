package com.tilldawn.controller;

import com.tilldawn.Main;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.view.HintMenu;
import com.tilldawn.view.MainMenu;

public class HintMenuController {
    private HintMenu view;

    public void setView(HintMenu view) {
        this.view = view;
    }


    public void backToMainMenu() {
        Main.getMain().changeScreen(new MainMenu(new MainMenuController(), GameAssetsManager.getGameAssetsManager().getSkin()));
    }
}
