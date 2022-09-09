package com.mygdx.bisimulationgame.misc;

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
import com.mygdx.bisimulationgame.game.MarkovChainScreen;
import com.mygdx.bisimulationgame.game.setup.SetupVsScreen;

public class TutorialScreen implements Screen {

    final BisimulationGame game;

    private OrthographicCamera camera;
    private FitViewport fitViewport;
    private Stage stage;

    public TutorialScreen(BisimulationGame game){
        this.game = game;
        game.setScreenToDispose(this);

        //Setup
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.worldWidth, game.worldHeight);
        fitViewport = new FitViewport(game.worldWidth, game.worldHeight, camera);
        stage = new Stage(fitViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //Table
        Table loadTable = new Table();
        loadTable.setFillParent(true);
        loadTable.top().center().pad(50);

        //Table-Elements
        Label header = new Label("Bisimulation Game", game.skin, "title");
        Label text = new Label("The Bisimulation Game compares two states and tests them on a deviation e from 0 to 1.\n" +
                "With 0 meaning that both states are bisimilar. \n" +
                "There are two players in the game the duplicator and the spoiler. \n" +
                "The duplicator tries to show that both states have a smaller or equal deviation to e. \n" +
                "While the spoiler tries to prove that both states have a bigger deviation than e.\n", game.skin);
        Label credit = new Label("Uses Orange Peel UI created by Raymond \"Raeleus\" Buckley", game.skin, "peach");
        //Buttons
        TextButton continueButton = new TextButton("continue", game.skin);
        startTutorial(continueButton);

        //Arranged Table
        loadTable.add(header).pad(10);
        loadTable.row();
        loadTable.add(text).pad(10);
        loadTable.row();
        loadTable.add(credit);
        loadTable.row();
        loadTable.add(continueButton).pad(20).width(400).height(100);


        //main menu Table
        OptionTable options = new OptionTable();
        Table top = options.getOptionstable(game, this);

        stage.addActor(top);
        stage.addActor(loadTable);
    }

    public void startTutorial(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setTutorialMode(true);
                game.setVsSpoilerAI(false);
                game.controller.loadMarkovChains("easyMarkovChain1", "easyMarkovChain2");
                game.controller.setStartConfiguration("1", "1", 0.1);
                game.controller.setGameVariant(false, "5");
                game.setScreen(new MarkovChainScreen(game));
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
