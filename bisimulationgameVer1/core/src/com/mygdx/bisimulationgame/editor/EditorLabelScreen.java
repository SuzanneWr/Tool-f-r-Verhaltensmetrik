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
import com.mygdx.bisimulationgame.datatypes.LabelledMarkovChain;
import com.mygdx.bisimulationgame.datatypes.MarkovLabel;


public class EditorLabelScreen implements Screen {

    final BisimulationGame game;

    private OrthographicCamera camera;
    private FitViewport menuViewport;
    private Stage stage;

    private Array<TextField> labelNameInputs;
    private Array<Label> textFieldDescription;
    private Table labelUI;
    private Label infoMessage;

    public EditorLabelScreen(final BisimulationGame game){
        this.game = game;
        game.setScreenToDispose(this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.worldWidth, game.worldHeight);
        menuViewport = new FitViewport(game.worldWidth, game.worldHeight, camera);
        stage = new Stage(menuViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //Table
        labelUI = new Table(game.skin);
        labelUI.setFillParent(true);
        labelUI.top().top().pad(40);

        //Table-Elements
        labelNameInputs = new Array<>();
        textFieldDescription = new Array<>();
        infoMessage = new Label("", game.skin, "error");
        Label labelHead = new Label("Enter Label names", game.skin, "title");
        Label uniqueInfo = new Label("Each name must be unique for this Markov Chain.", game.skin);
        //Buttons
        TextButton addLabel = new TextButton("+", game.skin);
        addNewLabelField(addLabel);
        TextButton deleteLabel = new TextButton("-", game.skin);
        deleteLabel(deleteLabel);
        TextButton confirm = new TextButton("confirm", game.skin);
        confirmLabelInput(confirm);

        //Arranged Table
        labelUI.add(labelHead).pad(10).colspan(3);
        labelUI.row();
        labelUI.add(uniqueInfo).pad(5).colspan(3);
        labelUI.row();
        labelUI.add(addLabel).pad(5);
        labelUI.add(confirm).pad(5);
        labelUI.add(deleteLabel).pad(5);
        labelUI.row();
        labelUI.add(infoMessage).pad(5).colspan(3);

        if(game.isEditMode()){
            for(int i = 0; i < game.getToEdit().getLabels().size; i++){
                Label labelName = new Label("Name: ", game.skin);
                textFieldDescription.add(labelName);
                TextField labelNameInput = new TextField(game.getToEdit().getLabels().get(i).getName(), game.skin);
                labelNameInputs.add(labelNameInput);
                labelUI.row();
                labelUI.add(labelName).pad(5);
                labelUI.add(labelNameInput).pad(5);
            }
        }

        //main menu Table
        OptionTable options = new OptionTable();
        Table top = options.getOptionstable(game, this);

        stage.addActor(top);
        stage.addActor(labelUI);
    }

    public void confirmLabelInput(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!(game.formatUtil.checkInputArrayEmpty(labelNameInputs))){
                    if(game.formatUtil.checkArrayElementsDistinct(labelNameInputs)){
                        
                        Array<MarkovLabel> labels = new Array<>();
                        for(TextField labelInput : labelNameInputs){
                            MarkovLabel newLabel = new MarkovLabel();
                            newLabel.setName(labelInput.getText());
                            labels.add(newLabel);
                        }

                        if(game.isEditMode()){
                            game.getToEdit().setLabels(labels);
                        }else {
                            LabelledMarkovChain newChain = new LabelledMarkovChain();
                            newChain.setLabels(labels);
                            game.setToEdit(newChain);
                        }

                        game.setScreen(new EditorStateScreen(game));
                        dispose();

                    }else{
                        infoMessage.setText("Each Label name has to be unique.");
                    }
                }else{
                    infoMessage.setText("Empty fields are invalid.");
                }
            }
        });
    }

    public void addNewLabelField(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Label labelName = new Label("Name: ", game.skin);
                textFieldDescription.add(labelName);
                TextField labelNameInput = new TextField("", game.skin);
                labelNameInputs.add(labelNameInput);
                labelUI.row();
                labelUI.add(labelName).pad(5);
                labelUI.add(labelNameInput).pad(5);

            }
        });
    }

    public void deleteLabel(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                labelNameInputs.get(labelNameInputs.size - 1).remove();
                labelNameInputs.removeIndex(labelNameInputs.size - 1);
                textFieldDescription.get(textFieldDescription.size - 1).remove();
                textFieldDescription.removeIndex(textFieldDescription.size - 1);
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
