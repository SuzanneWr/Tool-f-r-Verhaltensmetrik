package com.mygdx.bisimulationgame.editor;

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
import com.mygdx.bisimulationgame.game.setup.SetupVsScreen;

import javax.xml.transform.Templates;

public class EditorLoadMarkovChains implements Screen {

    final BisimulationGame game;

    private OrthographicCamera camera;
    private FitViewport fitViewport;
    private Stage stage;

    private List<String> mcFileNames;
    private ScrollPane mcFileSelect;
    private Label error;

    public EditorLoadMarkovChains(BisimulationGame game){
        this.game = game;
        game.setScreenToDispose(this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.worldWidth, game.worldHeight);
        fitViewport = new FitViewport(game.worldWidth, game.worldHeight, camera);
        stage = new Stage(fitViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //Table
        Table loadTable = new Table();
        loadTable.setFillParent(true);
        loadTable.center().pad(50);

        //Table-Elements
        Label header = new Label("Markov Chain Editor.", game.skin, "title");
        error = new Label("", game.skin, "error");
        Array<String> loadFiles = game.getLoadFiles();
        mcFileNames = new List<String>(game.skin);
        mcFileNames.setItems(loadFiles);
        mcFileSelect = new ScrollPane(mcFileNames, game.skin);
        //Buttons
        TextButton createNew = new TextButton("create new", game.skin);
        createNewMarkovChain(createNew);
        TextButton edit = new TextButton("edit", game.skin);
        editMarkovChain(edit);
        TextButton delete = new TextButton("delete", game.skin);
        deleteMarkovChain(delete);

        //Arranged Tables
        loadTable.add(header).pad(10).colspan(3);
        loadTable.row();
        loadTable.add(mcFileSelect).pad(10).colspan(3).prefWidth(400).prefHeight(500);
        loadTable.row();
        loadTable.add(createNew).pad(10).prefWidth(100).prefHeight(70);
        loadTable.add(edit).pad(10).prefWidth(100).prefHeight(70);
        loadTable.add(delete).pad(10).prefWidth(100).prefHeight(70);
        loadTable.row();
        loadTable.add(error).pad(10).colspan(loadTable.getColumns());

        //main menu Table
        OptionTable options = new OptionTable();
        Table top = options.getOptionstable(game, this);

        stage.addActor(loadTable);
        stage.addActor(top);

    }

    public void createNewMarkovChain(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setEditMode(false);
                game.setScreen(new EditorLabelScreen(game));
                dispose();
            }
        });
    }

    public void editMarkovChain(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setEditMode(true);
                game.setEditFileName(mcFileNames.getSelected());
                game.setToEdit(game.controller.loadMarkovChain(mcFileNames.getSelected()));
                game.setScreen(new EditorLabelScreen(game));
                dispose();
            }
        });
    }

    public void deleteMarkovChain(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(game.controller.deleteMarkovChain(mcFileNames.getSelected())){
                    game.setScreen(new MainMenuScreen(game));
                    dispose();
                }else{
                    error.setText(game.getInfoMessage());
                }
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
