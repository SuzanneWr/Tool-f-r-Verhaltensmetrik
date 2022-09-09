package com.mygdx.bisimulationgame;

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
import com.mygdx.bisimulationgame.editor.EditorLoadMarkovChains;
import com.mygdx.bisimulationgame.game.setup.SetupVsScreen;
import com.mygdx.bisimulationgame.misc.ControlScreen;
import com.mygdx.bisimulationgame.misc.TutorialScreen;

public class MainMenuScreen implements Screen {

    final BisimulationGame game;

    private OrthographicCamera camera;
    private FitViewport menuViewport;
    private Stage stage;

    private TextButton startButton;
    private TextButton editorButton;
    private TextButton controlsButton;
    private TextButton tutorialButton;


    public MainMenuScreen(BisimulationGame game){
        this.game = game;
        game.setScreenToDispose(this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.worldWidth, game.worldHeight);
        menuViewport = new FitViewport(game.worldWidth, game.worldHeight, camera);
        stage = new Stage(menuViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //Table
        Table menuTable = new Table(game.skin);
        menuTable.setFillParent(true);
        menuTable.top().pad(100);
        menuTable.setBackground("panel-orange");

        //Table-Elements
        Label headline = new Label("Bisimulation Game", game.skin, "title");
        //Button
        startButton = new TextButton("Start Game", game.skin);
        startGame(startButton);
        editorButton = new TextButton("Markov Chain Editor", game.skin);
        startEditor(editorButton);
        controlsButton = new TextButton("Controls", game.skin);
        startControls(controlsButton);
        tutorialButton = new TextButton("Tutorial", game.skin);
        startTutorial(tutorialButton);

        //Arranged Table
        menuTable.add(headline).pad(50);
        menuTable.row();
        menuTable.add(startButton).pad(20).width(400).height(100);
        menuTable.row();
        menuTable.add(controlsButton).pad(20).width(400).height(100);
        menuTable.row();
        menuTable.add(editorButton).pad(20).width(400).height(100);
        menuTable.row();
        menuTable.add(tutorialButton).pad(20).width(400).height(100);

        stage.addActor(menuTable);
    }

    public void startGame(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new SetupVsScreen(game));
                dispose();
            }
        });
    }

    public void startEditor(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new EditorLoadMarkovChains(game));
                dispose();
            }
        });
    }

    public void startControls(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ControlScreen(game));
                dispose();
            }
        });
    }

    public void startTutorial(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new TutorialScreen(game));
                dispose();
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(game.bgColor, game.bgColor, game.bgColor, 1);
        camera.update();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        menuViewport.update(width, height);
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
