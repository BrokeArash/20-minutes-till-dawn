package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.Main;
import com.tilldawn.controller.LoginMenuController;
import com.tilldawn.model.App;

public class ForgetPassword implements Screen {
    private Stage stage;
    private final Skin skin;
    private final LoginMenuController controller;
    private final String username;

    private Table currentTable;
    private SelectBox<String> answerField;
    private TextField newPasswordField;
    private TextField confirmPasswordField;
    private Label errorLabel;
    private Label questionLabel;

    public ForgetPassword(LoginMenuController controller, Skin skin, String username) {
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

        questionLabel = new Label("Security Question: " + getSecurityQuestion(), skin);
        answerField = new SelectBox<>(skin);
        Array<String> answer = new Array<>();
        answer.add("Yes");
        answer.add("No (Yes)");
        answerField.setItems(answer);

        newPasswordField = new TextField("", skin);
        newPasswordField.setMessageText("New password");
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');

        confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setMessageText("Confirm new password");
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');

        showSecurityQuestionScreen();
    }

    private void showSecurityQuestionScreen() {
        Table table = new Table(skin);
        table.setFillParent(true);
        table.center();

        Label titleLabel = new Label("Password Recovery", skin, "title");
        titleLabel.setColor(Color.WHITE);

        TextButton verifyButton = new TextButton("Verify Answer", skin);
        verifyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String answer = answerField.getSelected().trim();
                if (answer.isEmpty()) {
                    errorLabel.setText("Please enter your answer");
                    return;
                }

                if (verifyAnswer(answer)) {
                    showPasswordResetScreen();
                } else {
                    errorLabel.setText("Incorrect security answer");
                }
            }
        });

        table.add(titleLabel).padBottom(30).row();
        table.add(questionLabel).padBottom(20).row();
        table.add(answerField).width(200).padBottom(20).row();
        table.add(verifyButton).width(400).padBottom(10).row();
        table.add(errorLabel).row();

        setCurrentTable(table);
    }

    private void showPasswordResetScreen() {
        Table table = new Table(skin);
        table.setFillParent(true);
        table.center();

        Label titleLabel = new Label("Reset Your Password", skin, "title");
        titleLabel.setColor(Color.WHITE);

        TextButton submitButton = new TextButton("Reset Password", skin);
        submitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resetPassword();
            }
        });

        table.add(titleLabel).padBottom(30).row();
        table.add(new Label("Enter new password:", skin)).padBottom(10).row();
        table.add(newPasswordField).width(600).padBottom(10).row();
        table.add(new Label("Confirm new password:", skin)).padBottom(10).row();
        table.add(confirmPasswordField).width(600).padBottom(20).row();
        table.add(submitButton).width(600).row();
        table.add(errorLabel).padTop(10).row();

        setCurrentTable(table);
    }

    private void setCurrentTable(Table table) {
        if (currentTable != null) {
            currentTable.remove();
        }
        currentTable = table;
        stage.addActor(table);
    }

    private String getSecurityQuestion() {
        return "zavale aghl dari ?";
    }

    private boolean verifyAnswer(String answer) {
        return App.getUserDatabase().verifySecurityAnswer(username, answer);
    }

    private void resetPassword() {
        String newPass = newPasswordField.getText();
        String confirmPass = confirmPasswordField.getText();

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            errorLabel.setText("Please fill in both fields");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            errorLabel.setText("Passwords don't match");
            return;
        }

        if (newPass.length() < 8) {
            errorLabel.setText("Password must be at least 8 characters");
            return;
        }

        if (App.getUserDatabase().updatePassword(username, newPass)) {
            errorLabel.setText("Password updated successfully!");
            errorLabel.setColor(Color.GREEN);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    Gdx.app.postRunnable(() -> {
                        Main.getMain().setScreen(new LoginMenu(controller, skin));
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
