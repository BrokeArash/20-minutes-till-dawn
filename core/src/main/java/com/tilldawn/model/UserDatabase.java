package com.tilldawn.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.tilldawn.model.enums.Mode;
import com.tilldawn.view.LoginMenu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
    private DatabaseManager dbManager;
    private LoginMenu view;

    public UserDatabase() {
        this.dbManager = DatabaseManager.getInstance();
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

        // Add to database
        if (insertUser(guestUser)) {
            return guestUser;
        }
        return null;
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

        String sql = "SELECT id, username, password, security_answer FROM users WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    Gdx.app.log("UserDatabase", "Login successful for: " + username);
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("security_answer")
                    );
                } else {
                    if (view != null) {
                        view.setErrorPass("Login failed - incorrect password for: " + username);
                    }
                    Gdx.app.log("UserDatabase", "Login failed - incorrect password for: " + username);
                    return null;
                }
            } else {
                if (view != null) {
                    view.setErrorPass("Login failed - user not found: " + username);
                }
                Gdx.app.log("UserDatabase", "Login failed - user not found: " + username);
                return null;
            }
        } catch (SQLException e) {
            Gdx.app.error("UserDatabase", "Error during login", e);
            return null;
        }
    }

    public boolean registerUser(String username, String password, String securityAnswer) {
        if (isUsernameTaken(username)) {
            return false;
        }

        User newUser = new User(username, password, securityAnswer);
        return insertUser(newUser);
    }

    private boolean insertUser(User user) {
        String sql = "INSERT INTO users (username, password, security_answer) VALUES (?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getSec());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // Get the generated ID and set it on the user
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
                Gdx.app.log("UserDatabase", "User registered: " + user.getUsername());
                return true;
            }
        } catch (SQLException e) {
            Gdx.app.error("UserDatabase", "Error registering user", e);
        }
        return false;
    }

    public boolean isUsernameTaken(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            Gdx.app.error("UserDatabase", "Error checking username", e);
        }
        return false;
    }

    public boolean verifySecurityAnswer(String username, String answer) {
        if (username == null || username.isEmpty() || answer == null || answer.isEmpty()) {
            Gdx.app.error("UserDatabase", "Invalid username or answer");
            return false;
        }

        String sql = "SELECT security_answer FROM users WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedAnswer = rs.getString("security_answer");
                boolean isCorrect = storedAnswer.equalsIgnoreCase(answer.trim());
                if (!isCorrect) {
                    Gdx.app.log("UserDatabase", "Incorrect security answer for: " + username);
                }
                return isCorrect;
            } else {
                Gdx.app.log("UserDatabase", "User not found: " + username);
                return false;
            }
        } catch (SQLException e) {
            Gdx.app.error("UserDatabase", "Error verifying security answer", e);
            return false;
        }
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

        String sql = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                Gdx.app.log("UserDatabase", "Password updated for: " + username);
                return true;
            } else {
                Gdx.app.log("UserDatabase", "User not found for password update: " + username);
                return false;
            }
        } catch (SQLException e) {
            Gdx.app.error("UserDatabase", "Error updating password", e);
            return false;
        }
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

        String sql = "UPDATE users SET username = ?, updated_at = CURRENT_TIMESTAMP WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newUsername);
            pstmt.setString(2, oldUsername);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                Gdx.app.log("UserDatabase", "Username updated from " + oldUsername + " to " + newUsername);
                return true;
            } else {
                Gdx.app.log("UserDatabase", "User not found for username update: " + oldUsername);
                return false;
            }
        } catch (SQLException e) {
            Gdx.app.error("UserDatabase", "Error updating username", e);
            return false;
        }
    }

    public boolean deleteUser(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            Gdx.app.error("UserDatabase", "Invalid delete parameters");
            return false;
        }

        // First verify the password
        if (!verifyPassword(username, password)) {
            Gdx.app.log("UserDatabase", "User not found or password incorrect: " + username);
            return false;
        }

        String sql = "DELETE FROM users WHERE LOWER(username) = LOWER(?) AND password = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                Gdx.app.log("UserDatabase", "Deleted user: " + username);
                return true;
            }
        } catch (SQLException e) {
            Gdx.app.error("UserDatabase", "Error deleting user", e);
        }
        return false;
    }

    public Array<User> getUsers() {
        Array<User> users = new Array<>();
        String sql = "SELECT id, username, password, security_answer FROM users ORDER BY username";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("security_answer")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            Gdx.app.error("UserDatabase", "Error retrieving users", e);
        }

        return users;
    }

    public boolean verifyPassword(String username, String currentPass) {
        String sql = "SELECT password FROM users WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("password").equals(currentPass);
            }
        } catch (SQLException e) {
            Gdx.app.error("UserDatabase", "Error verifying password", e);
        }
        return false;
    }

    public User findUserInDatabase(String username) {
        String sql = "SELECT id, username, password, security_answer FROM users WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("security_answer")
                );
            }
        } catch (SQLException e) {
            Gdx.app.error("UserDatabase", "Error finding user", e);
        }
        return null;
    }

    // Score-related methods that would replace the JSON serialization logic
    public boolean saveScoreRecord(ScoreRecord record) {
        String sql = "INSERT INTO score_records (user_id, score, kills, time, game_mode, timestamp) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, record.getUser().getId());
            pstmt.setInt(2, record.getScore());
            pstmt.setInt(3, record.getKill());
            pstmt.setInt(4, record.getTime());
            pstmt.setString(5, record.getGameMode().toString());
            pstmt.setLong(6, record.getTimestamp());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Gdx.app.error("UserDatabase", "Error saving score record", e);
            return false;
        }
    }

    public List<ScoreRecord> getScoreRecords(User user) {
        List<ScoreRecord> records = new ArrayList<>();
        String sql = "SELECT score, kills, time, game_mode, timestamp FROM score_records WHERE user_id = ? ORDER BY timestamp DESC";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ScoreRecord record = new ScoreRecord(
                    user,
                    rs.getInt("score"),
                    rs.getInt("kills"),
                    rs.getInt("time"),
                    Mode.valueOf(rs.getString("game_mode"))
                );
                // Set the timestamp from database
                record.setTimestamp(rs.getLong("timestamp"));
                records.add(record);
            }
        } catch (SQLException e) {
            Gdx.app.error("UserDatabase", "Error retrieving score records", e);
        }

        return records;
    }

    // Also add this method to get ALL score records from ALL users:
    public List<ScoreRecord> getAllScoreRecords() {
        List<ScoreRecord> records = new ArrayList<>();
        String sql = "SELECT sr.score, sr.kills, sr.time, sr.game_mode, sr.timestamp, " +
            "u.id as user_id, u.username, u.password, u.security_answer " +
            "FROM score_records sr " +
            "JOIN users u ON sr.user_id = u.id " +
            "ORDER BY sr.timestamp DESC";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Create user object
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("security_answer")
                );

                // Create score record
                ScoreRecord record = new ScoreRecord(
                    user,
                    rs.getInt("score"),
                    rs.getInt("kills"),
                    rs.getInt("time"),
                    Mode.valueOf(rs.getString("game_mode"))
                );

                // Set the timestamp from database
                record.setTimestamp(rs.getLong("timestamp"));
                records.add(record);
            }
        } catch (SQLException e) {
            Gdx.app.error("UserDatabase", "Error retrieving all score records", e);
        }

        return records;
    }

    public int getMaxScore(User user) {
        String sql = "SELECT MAX(score) as max_score FROM score_records WHERE user_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getId());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("max_score");
            }
        } catch (SQLException e) {
            Gdx.app.error("UserDatabase", "Error retrieving max score", e);
        }

        return 0; // Return 0 if no scores found or error occurred
    }
}
