package com.mygdx.bisimulationgame.game.setup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.bisimulationgame.BisimulationGame;
import com.mygdx.bisimulationgame.Util.OptionTable;

public class SetupVsScreen implements Screen {

    final BisimulationGame game;

    private OrthographicCamera camera;
    private FitViewport fitViewport;
    private Stage stage;

    private TextButton vsPlayer;
    private TextButton vsSpoiler;

    public SetupVsScreen(BisimulationGame game){
        this.game = game;
        game.setScreenToDispose(this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.worldWidth, game.worldHeight);
        fitViewport = new FitViewport(game.worldWidth, game.worldHeight, camera);
        stage = new Stage(fitViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //Table
        Table menuTable = new Table(game.skin);
        menuTable.setFillParent(true);
        menuTable.top().pad(100);

        //Table-Elements
        Label head = new Label("Choose opponent", game.skin, "title");
        //Buttons
        vsPlayer = new TextButton("Player vs. Player", game.skin);
        startGameVsPlayer(vsPlayer);
        vsSpoiler = new TextButton("Player vs. Spoiler AI", game.skin);
        startGameVsAI(vsSpoiler);

        //Arranged Table
        menuTable.add(head).pad(50);
        menuTable.row();
        menuTable.add(vsPlayer).pad(50).width(400).height(100);
        menuTable.row();
        menuTable.add(vsSpoiler).pad(50).width(400).height(100);

        //main menu button
        OptionTable options = new OptionTable();
        Table top = options.getOptionstable(game, this);

        stage.addActor(top);
        stage.addActor(menuTable);
    }

    public void startGameVsPlayer(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setVsSpoilerAI(false);
                game.setScreen(new SetupLoadScreen(game));
                dispose();
            }
        });
    }

    public void startGameVsAI(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.controller.setupSpoilerAI();
                game.setScreen(new SetupLoadScreen(game));
                dispose();
            }
        });
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(game.bgColor, game.bgColor, game.bgColor, 1);
        camera.update();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
