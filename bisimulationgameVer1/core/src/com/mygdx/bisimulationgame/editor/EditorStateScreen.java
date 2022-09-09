package com.mygdx.bisimulationgame.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.mygdx.bisimulationgame.Util.OptionTable;
import com.mygdx.bisimulationgame.datatypes.MarkovLabel;
import com.mygdx.bisimulationgame.datatypes.State;

public class EditorStateScreen implements Screen {

    final BisimulationGame game;

    private OrthographicCamera camera;
    private FitViewport fitViewport;
    private Stage stage;

    private Array<TextField> stateNameInputs;
    private Array<SelectBox<MarkovLabel>> stateLabelSelects;
    private Array<CheckBox> stateBlockingBoxes;
    private Array<Label> stateDescriptions;
    private Array<Label> labelDescriptions;
    private Array<Label> blockingDescriptions;
    private Label infoMessage;
    private Table table;

    public EditorStateScreen(BisimulationGame game) {
        this.game = game;
        game.setScreenToDispose(this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.worldWidth, game.worldHeight);
        fitViewport = new FitViewport(game.worldWidth, game.worldHeight, camera);
        stage = new Stage(fitViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //Table
        table = new Table();
        table.setFillParent(true);
        table.top().top().pad(40);

        //Table-Elements
        stateNameInputs = new Array<>();
        stateLabelSelects = new Array<>();
        stateBlockingBoxes = new Array<>();
        stateDescriptions = new Array<>();
        labelDescriptions = new Array<>();
        blockingDescriptions = new Array<>();
        Label head = new Label("Enter States", game.skin, "title");
        Label infoEnterStates = new Label("Choose a unique name, a Label and choose if the State is blocking or not", game.skin);
        infoMessage = new Label("", game.skin, "error");
        //Buttons
        TextButton addState = new TextButton("+", game.skin);
        addNewStateField(addState);
        TextButton deleteStateButton = new TextButton("-", game.skin);
        deleteState(deleteStateButton);
        TextButton confirm = new TextButton("confirm", game.skin);
        confirmStateInput(confirm);

        //Arranged Elements
        table.add(head).pad(10).colspan(6);
        table.row();
        table.add(infoEnterStates).pad(5).colspan(6);
        table.row();
        table.add(addState).pad(5).colspan(2);
        table.add(confirm).pad(5).colspan(2);
        table.add(deleteStateButton).pad(5).colspan(2);

        if(game.isEditMode()){
            for(int i = 0; i < game.getToEdit().getStates().size; i++){
                table.row();

                Label labelName = new Label("Name: ", game.skin);
                stateDescriptions.add(labelName);
                TextField stateNameInput = new TextField(game.getToEdit().getStates().get(i).getName(), game.skin);
                stateNameInputs.add(stateNameInput);

                Label labelLabel = new Label("Label: ", game.skin);
                labelDescriptions.add(labelLabel);
                SelectBox<MarkovLabel> labelSelect = new SelectBox<>(game.skin);
                labelSelect.setItems(game.getToEdit().getLabels());
                labelSelect.setSelected(game.getToEdit().getStates().get(i).getLabel());
                stateLabelSelects.add(labelSelect);

                Label checkBlock = new Label("Blocking: ", game.skin);
                blockingDescriptions.add(checkBlock);
                CheckBox blocking = new CheckBox("", game.skin);
                if(game.getToEdit().getStates().get(i).isBlocking()){
                    blocking.setChecked(true);
                }
                stateBlockingBoxes.add(blocking);

                table.add(labelName).pad(10);
                table.add(stateNameInput).pad(10);
                table.add(labelLabel).pad(5);
                table.add(labelSelect).pad(10);
                table.add(checkBlock).pad(10);
                table.add(blocking).pad(10);
            }
        }

        //main menu Table
        OptionTable options = new OptionTable();
        Table top = options.getOptionstable(game, this);

        stage.addActor(top);
        stage.addActor(table);
    }

    public void confirmStateInput(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!(game.formatUtil.checkInputArrayEmpty(stateNameInputs))){
                    if(game.formatUtil.checkArrayElementsDistinct(stateNameInputs)){
                        Array<State> states = new Array<>();
                        for(int i = 0; i < stateNameInputs.size; i++){
                            State newState = new State(stateNameInputs.get(i).getText(), stateLabelSelects.get(i).getSelected(), stateBlockingBoxes.get(i).isChecked());
                            states.add(newState);
                            if(game.isEditMode()){
                                if(game.getToEdit().getStateByName(newState.getName()) != null){
                                    newState.setTransition(game.getToEdit().getStateByName(newState.getName()).getTransition());
                                }
                            }
                        }
                        game.getToEdit().setStates(states);
                        game.setScreen(new EditorTransitionsScreen(game));
                        dispose();
                    }else{
                        infoMessage.setText(game.getInfoMessage());
                    }
                }else {
                    infoMessage.setText(game.getInfoMessage());
                }
            }
        });
    }

    public void addNewStateField(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.row();

                Label labelName = new Label("Name: ", game.skin);
                stateDescriptions.add(labelName);
                TextField stateNameInput = new TextField("", game.skin);
                stateNameInputs.add(stateNameInput);

                Label labelLabel = new Label("Label: ", game.skin);
                labelDescriptions.add(labelLabel);
                SelectBox<MarkovLabel> labelSelect = new SelectBox<>(game.skin);
                labelSelect.setItems(game.getToEdit().getLabels());
                stateLabelSelects.add(labelSelect);

                Label checkBlock = new Label("Blocking: ", game.skin);
                blockingDescriptions.add(checkBlock);
                CheckBox blocking = new CheckBox("", game.skin);
                stateBlockingBoxes.add(blocking);

                table.add(labelName).pad(10);
                table.add(stateNameInput).pad(10);
                table.add(labelLabel).pad(5);
                table.add(labelSelect).pad(10);
                table.add(checkBlock).pad(10);
                table.add(blocking).pad(10);
            }
        });
    }

    public void deleteState(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stateNameInputs.get(stateNameInputs.size - 1).remove();
                stateNameInputs.removeIndex(stateNameInputs.size - 1);
                stateLabelSelects.get(stateLabelSelects.size - 1).remove();
                stateLabelSelects.removeIndex(stateLabelSelects.size - 1);
                stateBlockingBoxes.get(stateBlockingBoxes.size - 1).remove();
                stateBlockingBoxes.removeIndex(stateBlockingBoxes.size - 1);
                stateDescriptions.get(stateDescriptions.size - 1).remove();
                stateDescriptions.removeIndex(stateDescriptions.size - 1);
                labelDescriptions.get(labelDescriptions.size - 1).remove();
                labelDescriptions.removeIndex(labelDescriptions.size - 1);
                blockingDescriptions.get(blockingDescriptions.size - 1).remove();
                blockingDescriptions.removeIndex(blockingDescriptions.size - 1);
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

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.position.y += 350 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.position.y -= 350 * delta;
        }

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
