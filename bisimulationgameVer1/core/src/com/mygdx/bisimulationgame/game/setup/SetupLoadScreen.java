package com.mygdx.bisimulationgame.game.setup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.bisimulationgame.BisimulationGame;
import com.mygdx.bisimulationgame.MainMenuScreen;
import com.mygdx.bisimulationgame.Util.OptionTable;
import com.mygdx.bisimulationgame.game.MarkovChainScreen;

public class SetupLoadScreen implements Screen {

    final BisimulationGame game;

    private OrthographicCamera camera;
    private FitViewport menuViewport;
    private Stage stage;

    private List<String> MCAFileList;
    private List<String> MCBFileList;
    private ScrollPane MCAFileSelect;
    private ScrollPane MCBFileSelect;
    private TextButton confirm;

    public SetupLoadScreen(BisimulationGame game){
        this.game = game;
        game.setScreenToDispose(this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.worldWidth, game.worldHeight);
        menuViewport = new FitViewport(game.worldWidth, game.worldHeight, camera);
        stage = new Stage(menuViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //Table
        Table loadTable = new Table(game.skin);
        loadTable.setFillParent(true);
        loadTable.top().pad(50);

        //Table-Elements
        Label loadScreenHeader = new Label("Please select 2 Markov Chain Files to load.", game.skin);
        //Buttons
        confirm = new TextButton("Confirm", game.skin);
        setupSetupScreen(confirm);
        //LoadPanels
        Array<String> loadFiles = game.getLoadFiles();
        MCAFileList = new List<>(game.skin);
        MCAFileList.setItems(loadFiles);
        MCAFileSelect = new ScrollPane(MCAFileList, game.skin);
        MCBFileList = new List<>(game.skin);
        MCBFileList.setItems(loadFiles);
        MCBFileSelect = new ScrollPane(MCBFileList, game.skin);

        //Arranged Table
        loadTable.add(loadScreenHeader).pad(20).colspan(loadTable.getColumns());
        loadTable.row();
        loadTable.add(MCAFileSelect).pad(25).width(400).height(500);
        loadTable.add(MCBFileSelect).pad(25).width(400).height(500);
        loadTable.row();
        loadTable.add(confirm).width(500).height(100).pad(50).colspan(loadTable.getColumns());

        //main menu Table
        OptionTable options = new OptionTable();
        Table top = options.getOptionstable(game, this);

        stage.addActor(loadTable);
        stage.addActor(top);
    }

    public void setupSetupScreen(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.controller.loadMarkovChains(MCAFileList.getSelected(), MCBFileList.getSelected());
                game.setScreen(new MarkovChainScreen(game));
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
