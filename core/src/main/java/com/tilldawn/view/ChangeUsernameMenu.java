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
import com.tilldawn.controller.ProfileMenuController;
import com.tilldawn.model.App;

public class ChangeUsernameMenu implements Screen {
    private Stage stage;
    private final Skin skin;
    private final ProfileMenuController controller;
    private final String currentUsername;

    private TextField passwordField;
    private TextField newUsernameField;
    private Label errorLabel;

    public ChangeUsernameMenu(ProfileMenuController controller, Skin skin, String currentUsername) {
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

        passwordField = new TextField("", skin);
        passwordField.setMessageText("Current password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        newUsernameField = new TextField("", skin);
        newUsernameField.setMessageText("New username");

        showUsernameChangeScreen();
    }

    private void showUsernameChangeScreen() {
        Table table = new Table(skin);
        table.setFillParent(true);
        table.center();

        // Title
        Label titleLabel = new Label("Change Username", skin, "title");
        titleLabel.setColor(Color.WHITE);

        // Back button
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new ProfileMenu(controller, skin));
            }
        });

        // Submit button
        TextButton submitButton = new TextButton("Change Username", skin);
        submitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeUsername();
            }
        });

        // Layout
        table.add(titleLabel).padBottom(30).row();
        table.add(new Label("Current password:", skin)).padBottom(10).row();
        table.add(passwordField).width(600).padBottom(10).row();
        table.add(new Label("New username:", skin)).padBottom(10).row();
        table.add(newUsernameField).width(600).padBottom(20).row();
        table.add(submitButton).width(600).padBottom(10).row();
        table.add(backButton).width(600).row();
        table.add(errorLabel).padTop(10).row();

        stage.addActor(table);
    }

    private void changeUsername() {
        String password = passwordField.getText();
        String newUsername = newUsernameField.getText().trim();

        // Validation
        if (password.isEmpty() || newUsername.isEmpty()) {
            errorLabel.setText("Please fill in all fields");
            return;
        }

        if (newUsername.length() < 3) {
            errorLabel.setText("Username must be at least 3 characters");
            return;
        }

        if (newUsername.equalsIgnoreCase(currentUsername)) {
            errorLabel.setText("New username cannot be same as current");
            return;
        }

        // Verify password first
        if (!App.getUserDatabase().verifyPassword(currentUsername, password)) {
            errorLabel.setText("Password is incorrect");
            return;
        }

        // Check if username already exists
        if (App.getUserDatabase().isUsernameTaken(newUsername)) {
            errorLabel.setText("Username already taken");
            return;
        }

        // Update username in database
        if (App.getUserDatabase().updateUsername(currentUsername, newUsername)) {
            // Update current user in App
            App.getCurrentUser().setUsername(newUsername);
            errorLabel.setText("Username updated successfully!");
            errorLabel.setColor(Color.GREEN);

            // Return to profile after delay
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    Gdx.app.postRunnable(() -> {
                        Main.getMain().setScreen(new ProfileMenu(controller, skin));
                    });
                }
            }, 2);
        } else {
            errorLabel.setText("Failed to update username in database");
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
