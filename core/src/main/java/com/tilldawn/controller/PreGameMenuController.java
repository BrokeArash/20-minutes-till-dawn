package com.tilldawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tilldawn.Main;
import com.tilldawn.model.App;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.model.Player;
import com.tilldawn.model.Weapon;
import com.tilldawn.model.enums.Hero;
import com.tilldawn.model.enums.Mode;
import com.tilldawn.model.enums.WeaponEnum;
import com.tilldawn.view.GameMenu;
import com.tilldawn.view.PreGameMenu;

public class PreGameMenuController {
    private PreGameMenu view;

    public void setView(PreGameMenu preGameMenu) {
        this.view = preGameMenu;
        setupButtonListener();
    }
    private void setupButtonListener() {
        view.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                try {
                    String hero = view.getHeroes().getSelected();
                    String weapon = view.getWeapon().getSelected();
                    String mode = view.getMode().getSelected();

                    setUserSelections(hero, weapon, mode);

                    Main.getMain().changeScreen(new GameMenu(
                        new GameMenuController(),
                        GameAssetsManager.getGameAssetsManager().getSkin()
                    ));
                } catch (Exception e) {
                    Gdx.app.error("ERROR", "Failed to handle click", e);
                }
            }
        });
    }

    private void setUserSelections(String hero, String weapon, String mode) {

        Hero hero2 = null;
        WeaponEnum weapon2 = null;

        for (Hero hero1 : Hero.values()) {
            if (hero1.getName().equalsIgnoreCase(hero)) {
                hero2 = hero1;
                break;
            }
        }

        for (WeaponEnum weapon1 : WeaponEnum.values()) {
            if (weapon1.getName().equalsIgnoreCase(weapon)) {
                weapon2 = weapon1;
                break;
            }
        }

        for (Mode mode1 : Mode.values()) {
            if (mode1.getName().equalsIgnoreCase(mode)) {
                App.getGame().setMode(mode1);
                App.getGame().startGame();
                break;
            }
        }
        Weapon newWeapon = new Weapon(weapon2);
        Player newPlayer =  new Player(hero2, newWeapon);
        App.getGame().setPlayer(newPlayer);
    }
}
