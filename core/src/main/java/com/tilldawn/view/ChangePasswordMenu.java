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

import java.util.concurrent.ScheduledExecutorService;

public class ChangePasswordMenu implements Screen {

    private Stage stage;
    private final Skin skin;
    private final ProfileMenuController controller;
    private final String username;

    private TextField currentPasswordField;
    private TextField newPasswordField;
    private TextField confirmPasswordField;
    private Label errorLabel;

    public ChangePasswordMenu(ProfileMenuController controller, Skin skin, String username) {
        this.controller = controller;
        this.skin = skin;
        this.username = username;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);

        currentPasswordField = new TextField("", skin);
        currentPasswordField.setMessageText("Current password");
        currentPasswordField.setPasswordMode(true);
        currentPasswordField.setPasswordCharacter('*');

        newPasswordField = new TextField("", skin);
        newPasswordField.setMessageText("New password");
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');

        confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setMessageText("Confirm new password");
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');

        showPasswordResetScreen();
    }

    private void showPasswordResetScreen() {
        Table table = new Table(skin);
        table.setFillParent(true);
        table.center();

        // Title
        Label titleLabel = new Label("Change Password", skin, "title");
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
        TextButton submitButton = new TextButton("Change Password", skin);
        submitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changePassword();
            }
        });

        // Layout
        table.add(titleLabel).padBottom(30).row();
        table.add(new Label("Current password:", skin)).padBottom(10).row();
        table.add(currentPasswordField).width(600).padBottom(10).row();
        table.add(new Label("New password:", skin)).padBottom(10).row();
        table.add(newPasswordField).width(600).padBottom(10).row();
        table.add(new Label("Confirm new password:", skin)).padBottom(10).row();
        table.add(confirmPasswordField).width(600).padBottom(20).row();
        table.add(submitButton).width(600).padBottom(10).row();
        table.add(backButton).width(600).row();
        table.add(errorLabel).padTop(10).row();

        stage.addActor(table);
    }

    private void changePassword() {
        String currentPass = currentPasswordField.getText();
        String newPass = newPasswordField.getText();
        String confirmPass = confirmPasswordField.getText();

        // Validation
        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            errorLabel.setText("Please fill in all fields");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            errorLabel.setText("New passwords don't match");
            return;
        }

        if (newPass.length() < 8) {
            errorLabel.setText("Password must be at least 8 characters");
            return;
        }

        // Verify current password first
        if (!App.getUserDatabase().verifyPassword(username, currentPass)) {
            errorLabel.setText("Current password is incorrect");
            return;
        }

        // Update password in database
        if (App.getUserDatabase().updatePassword(username, newPass)) {
            errorLabel.setText("Password updated successfully!");
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
            errorLabel.setText("Failed to update password");
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
