package com.tilldawn.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tilldawn.model.enums.EnemyEnum;

public class GameAssetsManager {
    private static GameAssetsManager gameAssetsManager;
    private Skin skin;
    private Texture gameLogo;
    private Texture backgroundTex;
    private Texture backgroundTex2;
    private Music music1;
    private Music music2;
    private Music currentMusic;
    private Preferences preferences;
    private final String bullet;
    private Texture backgroundTexture;
    private Texture cursor;
    private Texture abbyPortrait;
    private Texture hasturPortrait;
    private Texture hinaPortrait;
    private Texture drop;
    private Sound reloadSound;
    private Sound gunshot;
    private Sound monsterAttack;
    private Sound monsterKill;
    private Sound monsterDamage;
    private Sound dropGet;

    private GameAssetsManager() {
        skin = new Skin(Gdx.files.internal("star-soldier/skin/star-soldier-ui.json"));
        gameLogo = new Texture(Gdx.files.internal("Images_grouped_1/Sprite/T/T_20Logo.png"));
        backgroundTex = new Texture(Gdx.files.internal("Images_grouped_1/Sprite/T/T_TitleLeaves.png"));
        backgroundTex2 = new Texture(Gdx.files.internal("Images_grouped_1/Sprite/T/T_TitleLeaves2.png"));

        music1 = Gdx.audio.newMusic(Gdx.files.internal("SFX/Pretty_Dungeon.mp3"));
        music2 = Gdx.audio.newMusic(Gdx.files.internal("SFX/Wasteland Combat.mp3"));

        music1.setLooping(true);
        music2.setLooping(true);

        preferences = Gdx.app.getPreferences("GameSettings");
        currentMusic = getSelectedMusic();

        float volume = preferences.getFloat("musicVolume", 0.5f);
        currentMusic.setVolume(volume);

        cursor = new Texture("Images_grouped_1/Sprite/T/T_CursorSprite.png");
        abbyPortrait =  new Texture("Images_grouped_1/Sprite/T/T_Abby_Portrait.png");
        hasturPortrait = new Texture("Images_grouped_1/Sprite/T/T_Hastur_Portrait.png");
        hinaPortrait = new Texture("Images_grouped_1/Sprite/T/T_Hina_Portrait.png");
        drop = new Texture("Images_grouped_1/Sprite/EyeMonsterProjecitle/EyeMonsterProjecitle.png");


        bullet = "Images_grouped_1/Sprite/Icon/Icon_Bullet_Storm.png";
        this.backgroundTexture = new Texture("background.png");
        reloadSound = Gdx.audio.newSound(Gdx.files.internal("SFX/AudioClip/Weapon_Shotgun_Reload.wav"));
        gunshot = Gdx.audio.newSound(Gdx.files.internal("SFX/AudioClip/single_shot.wav"));
        monsterAttack = Gdx.audio.newSound(Gdx.files.internal("SFX/AudioClip/Monster_2_Attack_Quick_01_WITH_ECHO.wav"));
        monsterKill = Gdx.audio.newSound(Gdx.files.internal("SFX/AudioClip/Monster_2_RecieveAttack_HighIntensity_01.wav"));
        dropGet = Gdx.audio.newSound(Gdx.files.internal("SFX/AudioClip/Obtain_Points_01.wav"));
        monsterDamage = Gdx.audio.newSound(Gdx.files.internal("SFX/AudioClip/characterouch2-163912.mp3"));

    }

    public static GameAssetsManager getGameAssetsManager() {
        if (gameAssetsManager == null) {
            gameAssetsManager = new GameAssetsManager();
        }
        return gameAssetsManager;
    }

    public void playMusic() {
        if (currentMusic != null && !currentMusic.isPlaying()) {
            currentMusic.play();
        }
    }

    public void stopMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.stop();
        }
    }

    public void setMusicVolume(float volume) {
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
        preferences.putFloat("musicVolume", volume);
        preferences.flush();
    }

    public void switchMusic(int musicIndex) {
        stopMusic();
        currentMusic = (musicIndex == 0) ? music1 : music2;
        currentMusic.setVolume(preferences.getFloat("musicVolume", 0.5f));
        playMusic();
        preferences.putInteger("selectedMusic", musicIndex);
        preferences.flush();
    }

    private Music getSelectedMusic() {
        int selected = preferences.getInteger("selectedMusic", 0);
        return (selected == 0) ? music1 : music2;
    }

    public int getCurrentMusicIndex() {
        return (currentMusic == music1) ? 0 : 1;
    }

    public float getCurrentVolume() {
        return preferences.getFloat("musicVolume", 0.5f);
    }

    public Skin getSkin() {
        return skin;
    }

    public Texture getGameLogo() {
        return gameLogo;
    }

    public Texture getBackgroundTex() {
        return backgroundTex;
    }

    public Texture getBackgroundTex2() {
        return backgroundTex2;
    }

    public String getBullet() {
        return bullet;
    }

    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    public Sound getReloadSound() {
        return reloadSound;
    }

    public Sound getGunshot() {
        return gunshot;
    }

    public Sound getMonsterAttack() {
        return monsterAttack;
    }

    public Sound getDropGet() {
        return dropGet;
    }

    public Sound getMonsterDamage() {
        return monsterDamage;
    }

    public Sound getMonsterKill() {
        return monsterKill;
    }

    public Texture getDrop() {
        return drop;
    }

    public void dispose() {

        for (EnemyEnum type : EnemyEnum.values()) {
            type.getTexture().dispose();
        }
    }
    public Texture getRandomAvatar() {
        int choice = (int) (Math.random() * 3);
        switch (choice) {
            case 0:
                return abbyPortrait;
            case 1:
                return hasturPortrait;
            case 2:
            default:
                return hinaPortrait;
        }
    }

    public Texture getCursor() {
        return cursor;
    }

    public Texture getAbbyPortrait() {
        return abbyPortrait;
    }

    public Texture getHasturPortrait() {
        return hasturPortrait;
    }

    public Texture getHinaPortrait() {
        return hinaPortrait;
    }
}
