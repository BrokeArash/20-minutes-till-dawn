package com.tilldawn.model;

import com.badlogic.gdx.Gdx;
import java.sql.*;

public class DatabaseManager {
    private static final String DB_NAME = "tilldawn.db";
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        try {
            String dbPath = Gdx.files.local(DB_NAME).file().getAbsolutePath();
            String url = "jdbc:sqlite:" + dbPath;

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);

            connection.createStatement().execute("PRAGMA foreign_keys = ON");

            createTables();
            Gdx.app.log("DatabaseManager", "Database initialized successfully");

        } catch (Exception e) {
            Gdx.app.error("DatabaseManager", "Failed to initialize database", e);
        }
    }

    private void createTables() throws SQLException {
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username VARCHAR(50) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                security_answer VARCHAR(255) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

        String createScoreRecordsTable = """
            CREATE TABLE IF NOT EXISTS score_records (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                score INTEGER NOT NULL,
                kills INTEGER NOT NULL,
                time INTEGER NOT NULL,
                game_mode VARCHAR(50) NOT NULL,
                timestamp BIGINT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
            """;

        String createUsersIndex = "CREATE INDEX IF NOT EXISTS idx_users_username ON users(username)";
        String createScoreUserIndex = "CREATE INDEX IF NOT EXISTS idx_score_records_user_id ON score_records(user_id)";
        String createScoreTimestampIndex = "CREATE INDEX IF NOT EXISTS idx_score_records_timestamp ON score_records(timestamp)";
        String createScoreScoreIndex = "CREATE INDEX IF NOT EXISTS idx_score_records_score ON score_records(score DESC)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createScoreRecordsTable);
            stmt.execute(createUsersIndex);
            stmt.execute(createScoreUserIndex);
            stmt.execute(createScoreTimestampIndex);
            stmt.execute(createScoreScoreIndex);
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                initializeDatabase();
            }
        } catch (SQLException e) {
            Gdx.app.error("DatabaseManager", "Database connection check failed", e);
        }
        return connection;
    }
}
