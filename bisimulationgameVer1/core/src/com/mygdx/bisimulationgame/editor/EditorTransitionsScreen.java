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
import com.mygdx.bisimulationgame.Util.EditorDigitFilter;
import com.mygdx.bisimulationgame.Util.OptionTable;

public class EditorTransitionsScreen implements Screen {

    final BisimulationGame game;

    private OrthographicCamera camera;
    private FitViewport fitViewport;
    private Stage stage;

    private Array<Label> stateNames;
    private Array<TextField> inputTransitionStates;
    private Array<TextField> inputTransitionProbabilities;
    private Label infoMessage;

    public EditorTransitionsScreen(BisimulationGame game){
        this.game = game;
        game.setScreenToDispose(this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.worldWidth, game.worldHeight);
        fitViewport = new FitViewport(game.worldWidth, game.worldHeight, camera);
        stage = new Stage(fitViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //Table
        Table transitionTable = new Table();
        transitionTable.setFillParent(true);
        transitionTable.top().top().pad(40);

        //Table-Elements
        infoMessage = new Label("", game.skin, "error");
        Label header = new Label("Transitions", game.skin, "title");
        Label subHeader = new Label("First textfield is for state names: StateName1,StateName2, ... \n" +
                "Second textfield is for the probability to transition to the state: probability1,probability2, ...", game.skin);
        TextButton confirm = new TextButton("confirm", game.skin);
        enterStateTransitions(confirm);

        //Arranged Table
        transitionTable.add(header).pad(5).colspan(3);
        transitionTable.row();
        transitionTable.add(subHeader).pad(5).colspan(3);
        transitionTable.row();
        transitionTable.add(confirm).pad(5).colspan(3);
        transitionTable.row();
        transitionTable.add(infoMessage).pad(5).colspan(3);
        transitionTable.row();

        stateNames = new Array<>();
        inputTransitionStates = new Array<>();
        inputTransitionProbabilities = new Array<>();
        for(int i = 0; i < game.getToEdit().getStates().size; i++){
            Label stateName = new Label(game.getToEdit().getStates().get(i).getName(), game.skin);
            stateNames.add(stateName);
            transitionTable.add(stateNames.get(i)).pad(5);
            if(game.getToEdit().getStates().get(i).isBlocking()){
                TextField inputStates = new TextField("Blocking", game.skin);
                inputStates.setDisabled(true);
                inputTransitionStates.add(inputStates);
                TextField inputProbabilities = new TextField("Blocking", game.skin);
                inputProbabilities.setDisabled(true);
                inputTransitionProbabilities.add(inputProbabilities);
            }else{
                TextField inputStates = new TextField("", game.skin);
                TextField inputProbabilities = new TextField("", game.skin);
                inputProbabilities.setTextFieldFilter(new EditorDigitFilter());
                if(game.isEditMode()){
                    inputStates.setText(game.controller.stateTransitionsToString(game.getToEdit().getStates().get(i).getTransition().getStateNames()));
                    inputProbabilities.setText(game.controller.probabilitiesTransitionsToString(game.getToEdit().getStates().get(i).getTransition().getProbabilitys()));
                }
                inputTransitionStates.add(inputStates);
                inputTransitionProbabilities.add(inputProbabilities);
            }
            transitionTable.add(inputTransitionStates.get(i)).pad(5);
            transitionTable.add(inputTransitionProbabilities.get(i)).pad(5);
            transitionTable.row();
        }

        OptionTable options = new OptionTable();
        Table top = options.getOptionstable(game, this);

        stage.addActor(transitionTable);
        stage.addActor(top);


    }

    public void enterStateTransitions(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(game.controller.addTransitions(inputTransitionStates, inputTransitionProbabilities)){
                    game.setScreen(new SaveMarkovChainScreen(game));
                }else {
                    infoMessage.setText(game.getInfoMessage());
                }
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
