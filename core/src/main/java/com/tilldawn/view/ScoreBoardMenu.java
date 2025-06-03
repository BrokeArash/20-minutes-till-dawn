package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.Main;
import com.tilldawn.controller.MainMenuController;
import com.tilldawn.controller.ScoreBoardMenuController;
import com.tilldawn.model.GameAssetsManager;
import com.tilldawn.model.ScoreRecord;
import com.tilldawn.model.UserDatabase;
import com.tilldawn.model.User;
import com.tilldawn.model.App;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ScoreBoardMenu extends ScreenAdapter {
    private Skin skin;
    private Stage stage;
    private Table rootTable;
    private SelectBox<String> sortType;
    private ScrollPane scrollPane;
    private Table scoresTable;
    private ScoreBoardMenuController controller;
    private TextButton backButton;
    private Array<ScoreRecord> topScorers;
    private UserDatabase userDatabase;

    public ScoreBoardMenu() {
        // Initialize database connection
        userDatabase = new UserDatabase();

        // Get skin from asset manager
        skin = GameAssetsManager.getGameAssetsManager().getSkin();
        stage = new Stage(new ScreenViewport());

        // Initialize UI components FIRST
        initializeUI();

        // THEN set up the controller
        controller = new ScoreBoardMenuController();
        controller.setView(this);

        // Set up UI layout and load scores
        setupUI();
        loadScores();

        // Set input processor
        Gdx.input.setInputProcessor(stage);
    }

    private void initializeUI() {
        // Initialize the sortType SelectBox before the controller tries to access it
        sortType = new SelectBox<>(skin);
        sortType.setItems("Score (High to Low)", "Score (Low to High)", "Date (Newest)", "Date (Oldest)");
        sortType.setSelected("Score (High to Low)"); // Set default selection

        // Initialize other UI components
        rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        scoresTable = new Table();
        scrollPane = new ScrollPane(scoresTable, skin);
        scrollPane.setScrollbarsVisible(true);
        scrollPane.setFadeScrollBars(false);

        // Initialize top scorers array
        topScorers = new Array<>();

        // Initialize back button
        backButton = new TextButton("BACK", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().changeScreen(new MainMenu(new MainMenuController(), skin));
            }
        });
    }

    private void setupUI() {
        rootTable.clear();
        rootTable.top();

        // Title
        Label titleLabel = new Label("SCOREBOARD", skin, "title");
        rootTable.add(titleLabel).padTop(20).padBottom(30).row();

        // Sort options
        Table sortTable = new Table();
        sortTable.add(new Label("Sort by: ", skin)).padRight(10);
        sortTable.add(sortType);
        rootTable.add(sortTable).padBottom(20).row();

        // Headers
        Table headerTable = new Table();
        headerTable.add(new Label("Rank", skin)).width(60).pad(5);
        headerTable.add(new Label("Player", skin)).width(150).pad(5);
        headerTable.add(new Label("Score", skin)).width(100).pad(5);
        headerTable.add(new Label("Kills", skin)).width(80).pad(5);
        headerTable.add(new Label("Time", skin)).width(80).pad(5);
        headerTable.add(new Label("Mode", skin)).width(100).pad(5);
        headerTable.add(new Label("Date", skin)).width(120).pad(5);
        rootTable.add(headerTable).padBottom(10).row();

        // Scores table in scroll pane
        rootTable.add(scrollPane).expand().fill().padBottom(20).row();

        // Back button
        rootTable.add(backButton).width(200).height(50);
    }

    private void loadScores() {
        try {
            // Get all score records from all users
            List<ScoreRecord> allScores = getAllScoreRecords();

            // Convert to Array and sort based on current selection
            topScorers.clear();
            topScorers.addAll(allScores.toArray(new ScoreRecord[0]));

            // Sort based on current selection
            sortScores();

            // Update the display
            updateScoresDisplay();

        } catch (Exception e) {
            Gdx.app.error("ScoreBoardMenu", "Error loading scores", e);
            // Show error message to user
            scoresTable.clear();
            scoresTable.add(new Label("Error loading scores", skin)).pad(20);
        }
    }

    private List<ScoreRecord> getAllScoreRecords() {
        List<ScoreRecord> allScores = new ArrayList<>();

        // Get all users from database
        Array<User> users = userDatabase.getUsers();

        // For each user, get their score records
        for (User user : users) {
            List<ScoreRecord> userScores = userDatabase.getScoreRecords(user);
            allScores.addAll(userScores);
        }

        return allScores;
    }

    private void sortScores() {
        String selectedSort = sortType.getSelected();

        switch (selectedSort) {
            case "Score (High to Low)":
                topScorers.sort(new Comparator<ScoreRecord>() {
                    @Override
                    public int compare(ScoreRecord a, ScoreRecord b) {
                        return Integer.compare(b.getScore(), a.getScore());
                    }
                });
                break;

            case "Score (Low to High)":
                topScorers.sort(new Comparator<ScoreRecord>() {
                    @Override
                    public int compare(ScoreRecord a, ScoreRecord b) {
                        return Integer.compare(a.getScore(), b.getScore());
                    }
                });
                break;

            case "Date (Newest)":
                topScorers.sort(new Comparator<ScoreRecord>() {
                    @Override
                    public int compare(ScoreRecord a, ScoreRecord b) {
                        return Long.compare(b.getTimestamp(), a.getTimestamp());
                    }
                });
                break;

            case "Date (Oldest)":
                topScorers.sort(new Comparator<ScoreRecord>() {
                    @Override
                    public int compare(ScoreRecord a, ScoreRecord b) {
                        return Long.compare(a.getTimestamp(), b.getTimestamp());
                    }
                });
                break;
        }

        // Limit to top 50 scores to avoid performance issues
        if (topScorers.size > 50) {
            Array<ScoreRecord> limitedScores = new Array<>();
            for (int i = 0; i < 50; i++) {
                limitedScores.add(topScorers.get(i));
            }
            topScorers = limitedScores;
        }
    }

    private void updateScoresDisplay() {
        scoresTable.clear();

        if (topScorers.size == 0) {
            scoresTable.add(new Label("No scores available", skin)).pad(20);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        for (int i = 0; i < topScorers.size; i++) {
            ScoreRecord record = topScorers.get(i);

            // Create row for this score
            Table rowTable = new Table();

            // Rank
            rowTable.add(new Label(String.valueOf(i + 1), skin)).width(60).pad(5);

            // Player name
            String playerName = record.getUser() != null ? record.getUser().getUsername() : "Unknown";
            rowTable.add(new Label(playerName, skin)).width(150).pad(5);

            // Score
            rowTable.add(new Label(String.valueOf(record.getScore()), skin)).width(100).pad(5);

            // Kills
            rowTable.add(new Label(String.valueOf(record.getKill()), skin)).width(80).pad(5);

            // Time (convert seconds to mm:ss format)
            int timeInSeconds = record.getTime();
            int minutes = timeInSeconds / 60;
            int seconds = timeInSeconds % 60;
            String timeString = String.format("%02d:%02d", minutes, seconds);
            rowTable.add(new Label(timeString, skin)).width(80).pad(5);

            // Game mode
            String gameMode = record.getGameMode() != null ? record.getGameMode().toString() : "Unknown";
            rowTable.add(new Label(gameMode, skin)).width(100).pad(5);

            // Date
            String dateString = dateFormat.format(new Date(record.getTimestamp()));
            rowTable.add(new Label(dateString, skin)).width(120).pad(5);

            scoresTable.add(rowTable).fillX().row();

            // Add separator line for better readability
            if (i < topScorers.size - 1) {
                scoresTable.add(new Label("", skin)).height(2).fillX().row();
            }
        }
    }

    public void refreshScores() {
        loadScores();
    }

    // Getters for controller
    public SelectBox<String> getSortType() {
        return sortType;
    }

    public TextButton getBack() {
        return backButton;
    }

    public Array<ScoreRecord> getTopScorers() {
        return topScorers;
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw stage
        stage.act(delta);
        stage.draw();

        // Check controller buttons
        if (controller != null) {
            controller.checkButton();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
    }
}
