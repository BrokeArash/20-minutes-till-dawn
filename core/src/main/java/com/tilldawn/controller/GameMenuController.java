package com.tilldawn.controller;

import com.tilldawn.model.App;
import com.tilldawn.view.GameMenu;

public class GameMenuController {
    private GameMenu view;
    private PlayerController playerController;
    private WorldController worldController;
    private WeaponController weaponController;
    private EnemyController enemyController;

    public void setView(GameMenu view) {
        this.view = view;
        playerController = new PlayerController(App.getGame().getPlayer());
        worldController = new WorldController(playerController);
        playerController.setWorldController(worldController);
        weaponController = new WeaponController(App.getGame().getPlayer().getWeapon(), worldController, App.getGame().getPlayer());
        enemyController = new EnemyController(playerController, worldController, weaponController);
    }

    public void updateGame() {
        if (view != null) {
            worldController.update();
            playerController.update();
            weaponController.update();
            enemyController.update();
        }
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public WeaponController getWeaponController() {
        return weaponController;
    }

    public EnemyController getEnemyController() {
        return enemyController;
    }

    public GameMenu getView() {
        return view;
    }
}
