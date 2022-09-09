package com.mygdx.bisimulationgame.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.bisimulationgame.BisimulationGame;
import com.mygdx.bisimulationgame.MainMenuScreen;
import com.mygdx.bisimulationgame.Util.OptionTable;


public class SaveMarkovChainScreen implements Screen {

    final BisimulationGame game;

    private OrthographicCamera camera;
    private FitViewport fitViewport;
    private Stage stage;

    private TextField markovChainName;

    public SaveMarkovChainScreen(BisimulationGame game){
        this.game = game;
        game.setScreenToDispose(this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.worldWidth, game.worldHeight);
        fitViewport = new FitViewport(game.worldWidth, game.worldHeight, camera);
        stage = new Stage(fitViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //Table
        Table saveTable = new Table();
        saveTable.setFillParent(true);
        saveTable.center().top().pad(100);

        //Table-Elements
        Label head = new Label("Save created Markov Chain", game.skin, "title");
        Label name = new Label("File name: ", game.skin);
        markovChainName = new TextField("", game.skin);
        if(game.isEditMode()){
            markovChainName.setText(game.getEditFileName());
        }
        TextButton confirm = new TextButton("confirm", game.skin);
        saveMarkovChain(confirm);

        //Arranged Table
        saveTable.add(head).pad(10).colspan(2);
        saveTable.row();
        saveTable.add(name).pad(5);
        saveTable.add(markovChainName).pad(5);
        saveTable.row();
        saveTable.add(confirm).colspan(2).pad(10);

        //main menu
        OptionTable options = new OptionTable();
        Table top = options.getOptionstable(game, this);

        stage.addActor(top);
        stage.addActor(saveTable);
    }

    public void saveMarkovChain(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getSaveUtil().saveLabelledMarkovChain(game.getToEdit(), markovChainName.getText());
                game.setScreen(new MainMenuScreen(game));
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
    public void resize(int i, int i1) {
        fitViewport.update(i, i1);
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
