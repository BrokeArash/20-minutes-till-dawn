package com.tilldawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tilldawn.Main;
import com.tilldawn.model.App;
import com.tilldawn.model.User;
import com.tilldawn.model.UserDatabase;
import com.tilldawn.view.LoginMenu;
import com.tilldawn.view.MainMenu;
import com.tilldawn.view.ProfileMenu;
import com.tilldawn.model.GameAssetsManager;

public class ProfileMenuController {

    private UserDatabase db;
    private User currentUser = App.getCurrentUser();
    private ProfileMenu view;

    public ProfileMenuController() {
        this.db = new UserDatabase();
    }

    public void setView(ProfileMenu view) {
        this.view = view;

        view.getChangeUsernameButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showChangeUsernameDialog();
            }
        });

        view.getChangePasswordButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onChangePasswordClicked();
            }
        });

        view.getDeleteAccountButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onDeleteAccountClicked();
            }
        });

        view.getBackButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new MainMenu(new MainMenuController(), GameAssetsManager.getGameAssetsManager().getSkin()));
            }
        });
    }

    private void showChangeUsernameDialog() {
        final TextField input = new TextField("", GameAssetsManager.getGameAssetsManager().getSkin());
        final Label text = new Label("", GameAssetsManager.getGameAssetsManager().getSkin());

        Dialog dialog = new Dialog("Change Username", GameAssetsManager.getGameAssetsManager().getSkin()) {
            @Override
            protected void result(Object object) {
                if (Boolean.TRUE.equals(object)) {
                    String newUsername = input.getText();
                    if (db.updateUsername(currentUser.getUsername(), newUsername)) {
                        currentUser.setUsername(newUsername);
                        view.getError().setText("Username updated successfully.");
                        hide();
                    } else {
                        text.setText("Username is taken or invalid.");
                        view.getError().setText("Username is taken or invalid.");
                    }
                }
            }
        };

        dialog.text("Enter new username:");
        dialog.getContentTable().row();
        dialog.getContentTable().add(input).width(200);
        dialog.getContentTable().row();
        dialog.getContentTable().add(text).colspan(2);
        dialog.button("OK", true);
        dialog.button("Cancel", false);
        dialog.show(view.getStage());
    }

    public void onChangePasswordClicked() {
        final TextField currentPassword = new TextField("", GameAssetsManager.getGameAssetsManager().getSkin());
        currentPassword.setMessageText("Current Password");

        final TextField newPassword = new TextField("", GameAssetsManager.getGameAssetsManager().getSkin());
        newPassword.setMessageText("New Password");

        Dialog dialog = new Dialog("Change Password", GameAssetsManager.getGameAssetsManager().getSkin()) {
            @Override
            protected void result(Object object) {
                if (Boolean.TRUE.equals(object)) {
                    String current = currentPassword.getText();
                    String newPass = newPassword.getText();

                    if (db.verifyPassword(currentUser.getUsername(), current)) {
                        if (db.updatePassword(currentUser.getUsername(), newPass)) {
                            Gdx.app.log("ProfileMenu", "Password updated successfully.");
                            view.getError().setText("password updated successfully");
                        } else {
                            Gdx.app.error("ProfileMenu", "Failed to update password.");
                            view.getError().setText("Failed to update password.");
                        }
                    } else {
                        Gdx.app.error("ProfileMenu", "Incorrect current password.");
                        view.getError().setText("Incorrect current password.");
                    }
                }
            }
        };

        dialog.text("Enter your current and new password:");
        dialog.getContentTable().add(currentPassword).width(200).row();
        dialog.getContentTable().add(newPassword).width(200).row();

        dialog.button("Confirm", true);
        dialog.button("Cancel", false);
        dialog.show(view.getStage());
    }

    public void onDeleteAccountClicked() {
        final TextField confirmPassword = new TextField("", GameAssetsManager.getGameAssetsManager().getSkin());
        confirmPassword.setPasswordMode(true);
        confirmPassword.setPasswordCharacter('*');
        confirmPassword.setMessageText("Enter password to confirm");

        Dialog dialog = new Dialog("Delete Account", GameAssetsManager.getGameAssetsManager().getSkin()) {
            @Override
            protected void result(Object object) {
                if (Boolean.TRUE.equals(object)) {
                    String password = confirmPassword.getText();
                    if (db.deleteUser(currentUser.getUsername(), password)) {
                        Gdx.app.log("ProfileMenu", "Account deleted.");
                        Main.getMain().setScreen(new LoginMenu(new LoginMenuController(), GameAssetsManager.getGameAssetsManager().getSkin()));
                    } else {
                        Gdx.app.error("ProfileMenu", "Failed to delete account.");
                    }
                }
            }
        };

        dialog.text("This action is irreversible.");
        dialog.getContentTable().add(confirmPassword).width(200).row();
        dialog.button("Delete", true);
        dialog.button("Cancel", false);
        dialog.show(view.getStage());
    }

    public void onAvatarChosen(String choice, Texture texture) {
        App.getCurrentUser().setAvatar(texture);
    }

}
