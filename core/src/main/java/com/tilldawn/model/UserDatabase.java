package com.tilldawn.model;;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.tilldawn.model.enums.Mode;
import com.tilldawn.view.LoginMenu;

public class UserDatabase {

    private Array<User> users;
    private final Json json;
    private LoginMenu view;

    public UserDatabase() {
        this.json = new Json();
        json.setSerializer(ScoreRecord.class, new Json.Serializer<ScoreRecord>() {
            @Override
            public void write(Json json, ScoreRecord rec, Class knownType) {
                json.writeObjectStart();
                // Instead of writing rec.getUser(), just write the username:
                json.writeValue("username", rec.getUser().getUsername());
                // Write only the primitives you care about:
                json.writeValue("score",    rec.getScore());
                json.writeValue("kill",     rec.getKill());
                json.writeValue("time",     rec.getTime());
                json.writeValue("gameMode", rec.getGameMode().toString());
                json.writeValue("timestamp", rec.getTimestamp());
                json.writeObjectEnd();
            }

            @Override
            public ScoreRecord read(Json json, JsonValue jsonData, Class type) {
                // Reconstruct from primitives:
                String username   = jsonData.getString("username");
                int    score      = jsonData.getInt("score");
                int    kill       = jsonData.getInt("kill");
                int    time       = jsonData.getInt("time");
                String modeString = jsonData.getString("gameMode");
                Mode   gameMode   = Mode.valueOf(modeString);
                long   timestamp  = jsonData.getLong("timestamp");

                // Build a new ScoreRecord(user, score, kill, time, gameMode).
                // But first, look up the real User object so we keep the same instance:
                User user = findUserInDatabase(username);
                ScoreRecord rec = new ScoreRecord(user, score, kill, time, gameMode);
                // If you want to preserve the old timestamp, add a setter or special constructor:
                //    rec.setTimestamp(timestamp);
                return rec;
            }
        });

        this.users = new Array<>();
        loadUsers();
    }

    private User findUserInDatabase(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public void setView(LoginMenu loginMenu) {
        this.view = loginMenu;
    }

    public User createGuestUser() {
        int guestNumber = 1;
        String guestName;

        // Find the next available guest username
        do {
            guestName = "guest" + guestNumber++;
        } while (isUsernameTaken(guestName));

        // Create the guest user with default credentials
        User guestUser = new User(guestName, "guestpass", "guest");

        // Add to database and save
        users.add(guestUser);
        saveUsers();

        return guestUser;
    }

    public User loginUser(String username, String password) {
        if (username == null || username.isEmpty()) {
            Gdx.app.error("UserDatabase", "Login failed - empty username");
            return null;
        }

        if (password == null || password.isEmpty()) {
            Gdx.app.error("UserDatabase", "Login failed - empty password");
            return null;
        }

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                if (user.getPassword().equals(password)) {
                    Gdx.app.log("UserDatabase", "Login successful for: " + username);
                    return user;
                } else {
                    view.setErrorPass("Login failed - incorrect password for: " + username);
                    Gdx.app.log("UserDatabase", "Login failed - incorrect password for: " + username);
                    return null;
                }
            }
        }
        view.setErrorPass("Login failed - user not found: " + username);
        Gdx.app.log("UserDatabase", "Login failed - user not found: " + username);
        return null;
    }

    private void loadUsers() {
        try {
            if (Gdx.files.local("users.json").exists()) {
                Array<User> loadedUsers = json.fromJson(Array.class, User.class, Gdx.files.local("users.json"));
                if (loadedUsers != null) {
                    users = loadedUsers;
                }
            }
        } catch (Exception e) {
            Gdx.app.error("UserDatabase", "Error loading users", e);
            users = new Array<>();
        }
    }

    public void saveUsers() {
        try {
            json.toJson(users, Array.class, User.class, Gdx.files.local("users.json"));
            Gdx.app.log("UserDatabase", "Saved " + users.size + " users to JSON");
        } catch (Exception e) {
            Gdx.app.error("UserDatabase", "Error saving users", e);
        }
    }

    public boolean registerUser(String username, String password, String securityAnswer) {
        if (isUsernameTaken(username)) {
            return false;
        }
        users.add(new User(username, password, securityAnswer));
        saveUsers();
        return true;
    }

    public boolean isUsernameTaken(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean verifySecurityAnswer(String username, String answer) {
        if (username == null || username.isEmpty() || answer == null || answer.isEmpty()) {
            Gdx.app.error("UserDatabase", "Invalid username or answer");
            return false;
        }

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                // Compare the provided answer with the stored security answer
                boolean isCorrect = user.getSec().equalsIgnoreCase(answer.trim());
                if (!isCorrect) {
                    Gdx.app.log("UserDatabase", "Incorrect security answer for: " + username);
                }
                return isCorrect;
            }
        }

        Gdx.app.log("UserDatabase", "User not found: " + username);
        return false;
    }

    public boolean updatePassword(String username, String newPassword) {
        if (username == null || username.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            Gdx.app.error("UserDatabase", "Invalid password update parameters");
            return false;
        }

        if (newPassword.length() < 8) {
            Gdx.app.log("UserDatabase", "Password too short for: " + username);
            return false;
        }

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                // Update the password
                user.setPassword(newPassword);
                saveUsers(); // Persist the change
                Gdx.app.log("UserDatabase", "Password updated for: " + username);
                return true;
            }
        }

        Gdx.app.log("UserDatabase", "User not found for password update: " + username);
        return false;
    }

    public boolean updateUsername(String oldUsername, String newUsername) {
        if (oldUsername == null || oldUsername.isEmpty() ||
            newUsername == null || newUsername.isEmpty()) {
            Gdx.app.error("UserDatabase", "Invalid username update parameters");
            return false;
        }

        if (newUsername.length() < 3) {
            Gdx.app.log("UserDatabase", "New username too short: " + newUsername);
            return false;
        }

        if (isUsernameTaken(newUsername)) {
            Gdx.app.log("UserDatabase", "Username already taken: " + newUsername);
            return false;
        }

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(oldUsername)) {
                // Update the username
                user.setUsername(newUsername);
                saveUsers(); // Persist the change
                Gdx.app.log("UserDatabase", "Username updated from " + oldUsername + " to " + newUsername);
                return true;
            }
        }

        Gdx.app.log("UserDatabase", "User not found for username update: " + oldUsername);
        return false;
    }

    public boolean deleteUser(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            Gdx.app.error("UserDatabase", "Invalid delete parameters");
            return false;
        }

        for (int i = 0; i < users.size; i++) {
            User user = users.get(i);
            if (user.getUsername().equalsIgnoreCase(username) &&
                user.getPassword().equals(password)) {
                users.removeIndex(i);
                saveUsers();
                Gdx.app.log("UserDatabase", "Deleted user: " + username);
                return true;
            }
        }

        Gdx.app.log("UserDatabase", "User not found or password incorrect: " + username);
        return false;
    }

    public Array<User> getUsers() {
        return users;
    }

    public boolean verifyPassword(String username, String currentPass) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                if (user.getPassword().equals(currentPass)) return true;
            }
        }
        return false;
    }
}
