package com.tilldawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.tilldawn.Main;
import com.tilldawn.model.*;
import com.tilldawn.view.GameEndMenu;
import com.tilldawn.view.MainMenu;

import java.awt.*;

public class PlayerController {
    private Player player;
    private float speed;
    private WorldController worldController;

    public PlayerController(Player player){
        this.player = player;
        this.speed = player.getHero().getSpeed();

        player.setPosX(0);
        player.setPosY(0);

    }

    public void setWorldController(WorldController worldController) {
        this.worldController = worldController;
    }

    public void update(){
        player.getPlayerSprite().draw(Main.getBatch());

        if (player.getPlayerHealth() <= 0) {
            String status = "you lost";
            User currentUser = App.getCurrentUser();
            ScoreRecord record = new ScoreRecord(currentUser, App.getGame().getScore(), App.getGame().getKill(),
                App.getGame().getTime(), App.getGame().getMode()); // or whatever mode
            App.getUserDatabase().saveScoreRecord(record);
            Main.getMain().changeScreen(new GameEndMenu(status, App.getGame().getScore(),
                App.getGame().getKill(), App.getGame().getTime(),
                GameAssetsManager.getGameAssetsManager().getSkin()));
        }

        if(player.isPlayerIdle()){
            idleAnimation();
        }

        handlePlayerInput();
    }

    public void handlePlayerInput(){
        float currentX = player.getPosX();
        float currentY = player.getPosY();
        float newX = currentX;
        float newY = currentY;

        boolean moved = false;

        if (Gdx.input.isKeyPressed(Input.Keys.R) && !player.getWeapon().isReloading()) {
            player.getWeapon().startReload();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            newY += player.getSpeed();
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            newX += player.getSpeed();
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            newY -= player.getSpeed();
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            newX -= player.getSpeed();
            player.getPlayerSprite().flip(true, false);
            moved = true;
        }

        if (moved && worldController != null) {
            float playerWidth = player.getPlayerSprite().getWidth();
            float playerHeight = player.getPlayerSprite().getHeight();

            if (worldController.isPositionValid(newX, newY, playerWidth, playerHeight)) {
                player.setPosX(newX);
                player.setPosY(newY);
            } else {
                float clampedX = worldController.clampX(newX, playerWidth);
                float clampedY = worldController.clampY(newY, playerHeight);
                player.setPosX(clampedX);
                player.setPosY(clampedY);
            }
        }
    }


    public void idleAnimation(){
        Animation<Texture> animation = App.getGame().getPlayer().getHero().getCharacter_idle_frames();

        player.getPlayerSprite().setRegion(animation.getKeyFrame(player.getTime()));

        if (!animation.isAnimationFinished(player.getTime())) {
            player.setTime(player.getTime() + Gdx.graphics.getDeltaTime());
        }
        else {
            player.setTime(0);
        }

        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
