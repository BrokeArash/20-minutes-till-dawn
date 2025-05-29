package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.Main;
import com.tilldawn.controller.LoginMenuController;
import com.tilldawn.controller.ProfileMenuController;
import com.tilldawn.model.App;

public class DeleteAccountMenu implements Screen {
    private Stage stage;
    private final Skin skin;
    private final ProfileMenuController controller;
    private final String currentUsername;

    private TextField passwordField;
    private Label errorLabel;
    private Label warningLabel;

    public DeleteAccountMenu(ProfileMenuController controller, Skin skin, String currentUsername) {
        this.controller = controller;
        this.skin = skin;
        this.currentUsername = currentUsername;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);

        warningLabel = new Label("WARNING: This action cannot be undone!", skin);
        warningLabel.setColor(Color.RED);

        passwordField = new TextField("", skin);
        passwordField.setMessageText("Enter password to confirm deletion");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        showDeleteAccountScreen();
    }

    private void showDeleteAccountScreen() {
        Table table = new Table(skin);
        table.setFillParent(true);
        table.center();

        // Title
        Label titleLabel = new Label("Delete Account", skin, "title");
        titleLabel.setColor(Color.WHITE);

        // Back button
        TextButton backButton = new TextButton("Cancel", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new ProfileMenu(controller, skin));
            }
        });

        // Delete button
        TextButton deleteButton = new TextButton("Permanently Delete Account", skin);
        deleteButton.setColor(Color.RED);
        deleteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                deleteAccount();
            }
        });

        // Layout
        table.add(titleLabel).padBottom(30).row();
        table.add(warningLabel).padBottom(20).row();
        table.add(new Label("Enter password to confirm:", skin)).padBottom(10).row();
        table.add(passwordField).width(600).padBottom(20).row();
        table.add(deleteButton).width(600).padBottom(10).row();
        table.add(backButton).width(600).row();
        table.add(errorLabel).padTop(10).row();

        stage.addActor(table);
    }

    private void deleteAccount() {
        String password = passwordField.getText();

        if (password.isEmpty()) {
            errorLabel.setText("Please enter your password");
            return;
        }

        if (App.getUserDatabase().deleteUser(currentUsername, password)) {
            errorLabel.setText("Account deleted successfully");
            errorLabel.setColor(Color.GREEN);

            // Clear current user and return to login after delay
            App.setCurrentUser(null);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    Gdx.app.postRunnable(() -> {
                        Main.getMain().setScreen(new LoginMenu(new LoginMenuController(), skin));
                    });
                }
            }, 2);
        } else {
            errorLabel.setText("Failed to delete account - wrong password?");
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(delta, 1/30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
    @Override
    public void dispose() {
        stage.dispose();
    }
}
