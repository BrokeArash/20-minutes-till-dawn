package com.tilldawn.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tilldawn.Main;
import com.tilldawn.model.App;
import com.tilldawn.view.MainMenu;
import com.tilldawn.view.ProfileMenu;
import com.tilldawn.model.GameAssetsManager;

public class ProfileMenuController {
    private ProfileMenu view;

    public void setView(ProfileMenu profileMenu) {
        this.view = profileMenu;
        setupListeners();
    }

    private void setupListeners() {
        // “Back” button returns to MainMenu:
        view.getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().changeScreen(
                    new MainMenu(
                        new MainMenuController(),
                        GameAssetsManager.getGameAssetsManager().getSkin()
                    ));
            }
        });

        // (You already have ChangeUsername, ChangePassword, DeleteAccount wiring.)
    }

    /**
     * Called whenever the user chooses a new avatar in the SelectBox.
     * @param choice   The string chosen (“Abby” / “Hastur” / “Hina”)
     * @param texture  The corresponding Texture instance from GameAssetsManager
     */
    public void onAvatarChosen(String choice, Texture texture) {
        // 1) Update the in-memory User object
        App.getCurrentUser().setAvatar(texture);

        // 2) (Optional) Persist to database, e.g. store “avatar name” or path.
        //    If you want to keep that choice between sessions, you might do:
        //    App.getUserDatabase().updateAvatarPath(App.getCurrentUser().getId(), choice);
        //
        //    Then when you reload the user next time, you read the stored avatar name and reload that texture.
    }
}
