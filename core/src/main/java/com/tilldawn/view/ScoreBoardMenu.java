package com.tilldawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tilldawn.Main;
import com.tilldawn.controller.ScoreBoardMenuController;
import com.tilldawn.model.App;
import com.tilldawn.model.ScoreRecord;
import com.tilldawn.model.ScoreManager.SortType;
import com.tilldawn.model.GameAssetsManager;

import java.util.List;

public class ScoreBoardMenu implements Screen {
    private final ScoreBoardMenuController controller;
    private final Stage stage;
    private final Table table;
    private List<ScoreRecord> topScorers; // may be sorted how the client requested
    private final TextButton back;

    public ScoreBoardMenu(ScoreBoardMenuController controller, Skin skin) {
        this.controller = controller;
        controller.setView(this);

        this.stage = new Stage(new ScreenViewport());
        this.table = new Table(skin);
        table.setFillParent(true);
        this.back = new TextButton("back", skin);

        // Get the top 10, sorted by whatever SortType the client chose:
        this.topScorers = App.getScoreManager().getTopNScores(
            Math.min(10, App.getScoreManager().getAllScoresSorted().size()),
            SortType.SCORE //TODO: change
        );

        stage.addActor(table);
    }

    public ScoreBoardMenuController getController() {
        return controller;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        table.clear();
        showTops();
        table.add(back);
        stage.addActor(table);
    }

    private void showTops() {
        table.center();
        for (ScoreRecord scoreRecord : topScorers) {
            String line = String.format(
                "user: %s   score: %d   kill: %d   time: %d",
                scoreRecord.getUser().getUsername(),
                scoreRecord.getScore(),
                scoreRecord.getKill(),
                scoreRecord.getTime()
            );
            Label rowLabel = new Label(line, GameAssetsManager.getGameAssetsManager().getSkin());
            table.add(rowLabel).row();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
        Main.getBatch().begin();
        controller.checkButton();
        Main.getBatch().end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // no-op for now
    }

    @Override
    public void pause() {
        // no-op
    }

    @Override
    public void resume() {
        // no-op
    }

    @Override
    public void hide() {
        // no-op
    }

    @Override
    public void dispose() {
        // no-op
    }

    public TextButton getBack() {
        return back;
    }
}
