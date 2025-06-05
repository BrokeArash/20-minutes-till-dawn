package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.controller.ProfileMenuController;
import com.tilldawn.model.App;
import com.tilldawn.model.GameAssetsManager;

public class ProfileMenu implements Screen {

    private Stage stage;
    private ProfileMenuController controller;
    private GameAssetsManager assetsManager;

    private Table table;
    private SelectBox<String> avatarSelectBox;
    private Image    avatarPreview;
    private TextButton backButton;
    private TextButton changeUsernameButton;
    private TextButton changePasswordButton;
    private TextButton deleteAccountButton;
    private Label error;

    public ProfileMenu(ProfileMenuController controller, Skin skin) {
        this.controller = controller;
        this.assetsManager = GameAssetsManager.getGameAssetsManager();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table(skin);
        table.setFillParent(true);
        table.pad(20);
        stage.addActor(table);

        changeUsernameButton = new TextButton("Change Username", skin);
        changePasswordButton = new TextButton("Change Password", skin);
        deleteAccountButton   = new TextButton("Delete Account", skin);
        backButton            = new TextButton("Back", skin);
        error = new Label("", skin);

        avatarSelectBox = new SelectBox<>(skin);
        avatarSelectBox.setItems("Abby", "Hastur", "Hina");
        avatarSelectBox.setSelected("Abby");

        Texture initialTex = getTextureForChoice( avatarSelectBox.getSelected() );
        if (avatarPreview != null) {
            assetsManager.dispose();
        }

        avatarPreview = new Image(new TextureRegionDrawable(initialTex));
        avatarPreview.setSize(100, 100);

        controller.setView(this);

        avatarSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String choice = avatarSelectBox.getSelected();
                Texture tex = getTextureForChoice(choice);

                avatarPreview.setDrawable(new TextureRegionDrawable(tex));

                controller.onAvatarChosen(choice, tex);
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        table.clear();

        table.add(new Label("Choose Avatar:", assetsManager.getSkin()))
            .padRight(10).left();
        table.add(avatarSelectBox).width(150).height(40).padRight(20);
        table.add(avatarPreview).size(100, 100).left().row();

        table.padBottom(30).row();

        table.add(changeUsernameButton).colspan(2).fillX().uniformX().padBottom(10).row();
        table.add(changePasswordButton).colspan(2).fillX().uniformX().padBottom(10).row();
        table.add(deleteAccountButton).colspan(2).fillX().uniformX().padBottom(30).row();
        table.add(backButton).colspan(2).fillX().uniformX().padBottom(30).row();
        table.add(error).fillX().uniformX();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide()  {}
    @Override public void dispose() { stage.dispose(); }

    public TextButton getBackButton()            { return backButton; }
    public TextButton getChangeUsernameButton()  { return changeUsernameButton; }
    public TextButton getChangePasswordButton()  { return changePasswordButton; }
    public TextButton getDeleteAccountButton()   { return deleteAccountButton; }

    private Texture getTextureForChoice(String choice) {
        switch (choice) {
            case "Abby":   return assetsManager.getAbbyPortrait();
            case "Hastur": return assetsManager.getHasturPortrait();
            case "Hina":   return assetsManager.getHinaPortrait();
            default:       return assetsManager.getAbbyPortrait();
        }
    }

    public Stage getStage() {
        return stage;
    }

    public Image getAvatarPreview() {
        return avatarPreview;
    }

    public SelectBox<String> getAvatarSelectBox() {
        return avatarSelectBox;
    }

    public void setAvatarSelectBox(SelectBox<String> avatarSelectBox) {
        this.avatarSelectBox = avatarSelectBox;
    }

    public void setAvatarPreview(Image avatarPreview) {
        this.avatarPreview = avatarPreview;
    }

    public Label getError() {
        return error;
    }
}
