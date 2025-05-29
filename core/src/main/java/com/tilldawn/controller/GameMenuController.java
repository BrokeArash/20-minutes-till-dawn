package com.tilldawn.controller;


import com.tilldawn.model.App;
import com.tilldawn.view.GameMenu;

public class GameMenuController {
    private GameMenu view;
    private PlayerController playerController;
    private WorldController worldController;
    private WeaponController weaponController;


    public void setView(GameMenu view) {
        this.view = view;
        playerController = new PlayerController(App.getGame().getPlayer());
        worldController = new WorldController(playerController);
        weaponController = new WeaponController(App.getGame().getPlayer().getWeapon());
    }

    public void updateGame() {
        if (view != null) {
            worldController.update();
            playerController.update();
            weaponController.update();
        }
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public WeaponController getWeaponController() {
        return weaponController;
    }
}
