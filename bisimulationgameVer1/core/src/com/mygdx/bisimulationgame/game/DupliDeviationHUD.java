package com.mygdx.bisimulationgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.bisimulationgame.BisimulationGame;
import com.mygdx.bisimulationgame.Util.DigitFilter;
import com.mygdx.bisimulationgame.Util.OptionTable;
import com.mygdx.bisimulationgame.interfaces.HUD;

//TODO mit der anderen Duplicator klasse zusammenfassen (?)
public class DupliDeviationHUD implements HUD {

    final BisimulationGame game;

    private Stage stage;
    private FitViewport stageViewport;
    private MarkovChainScreen screen;

    private Array<Array<TextField>> inputTable;

    public Label inputMessenger;

    public DupliDeviationHUD(BisimulationGame game, MarkovChainScreen screen){
        this.game = game;
        this.screen = screen;

        stageViewport = new FitViewport(game.worldWidth, game.worldHeight);
        stage = new Stage(stageViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //Table
        Table duplicatorTable = new Table();
        duplicatorTable.setFillParent(true);
        duplicatorTable.top().left().pad(20);

        //Create TextFields
        inputTable = new Array<>();
        for(int i = 0; i < game.getDuplicatorTable().getDeviation().size; i++){
            Array<TextField> inputRow = new Array<>();
            for(int j = 0; j < game.getDuplicatorTable().getDeviation().get(i).size; j++){
                TextField input = new TextField("", game.skin);
                input.setTextFieldFilter(new DigitFilter());
                inputRow.add(input);
            }
            inputTable.add(inputRow);
        }

        //Table-Elements
        //Description Elements
        Label head = new Label("Duplicator turn ", game.skin, "title");
        Label subHead = new Label("Please enter the deviation", game.skin);
        duplicatorTable.add(head).pad(5).colspan(duplicatorTable.getColumns());
        duplicatorTable.row();
        duplicatorTable.add(subHead).pad(5).colspan(duplicatorTable.getColumns());
        duplicatorTable.row();

        //Create Top of Duplicator Table
        String tableheadtext = game.getConfiguration().getStateA().getName() + "/" + game.getConfiguration().getStateB().getName();
        Label tableHead = new Label(tableheadtext, game.skin);
        duplicatorTable.add(tableHead).colspan(2).pad(10);

        Array<Label> labelBChildren = new Array<>();
        for(int i = 0; i < game.getDuplicatorTable().getChildrenB().size; i++){
            Label childBName = new Label(game.getDuplicatorTable().getChildrenB().get(i).getName(), game.skin);
            labelBChildren.add(childBName);
            duplicatorTable.add(childBName).colspan(2);
        }

        //Create Duplicator Table
        Array<Label> labelAChildren = new Array<>();
        Array<Label> probabilityMeasures = new Array<>();
        for(int i = 0; i < inputTable.size; i++){
            duplicatorTable.row();
            Label childAName = new Label(game.getDuplicatorTable().getChildrenA().get(i).getName(), game.skin);
            labelAChildren.add(childAName);
            duplicatorTable.add(childAName).pad(5);
            for(int j = 0; j < inputTable.get(i).size; j++){
                Label probability = new Label(game.getDuplicatorTable().getProbabilityMeasure().get(i).get(j)+"", game.skin, "peach");
                probabilityMeasures.add(probability);
                duplicatorTable.add(probability).pad(5);
                duplicatorTable.add(inputTable.get(i).get(j)).pad(5);
            }
        }

        //Current Configuration Display
        Label currentConfig = new Label("Current configuration: (" + game.getConfiguration().getStateA().getName() + ", " +
                game.getConfiguration().getStateB().getName() + ", " + game.getConfiguration().getDeviation() + ")", game.skin);
        duplicatorTable.row();
        duplicatorTable.add(currentConfig).pad(5).colspan(duplicatorTable.getColumns());
        duplicatorTable.row();

        //Round number
        if(!(game.isUnBounded())){
            Label roundNumber = new Label("Rounds left: " + game.getnBounded(), game.skin);
            duplicatorTable.add(roundNumber).pad(5).colspan(duplicatorTable.getColumns());
            duplicatorTable.row();
        }

        //Button
        TextButton confirm = new TextButton("confirm", game.skin);
        enterDeviation(confirm);
        duplicatorTable.row();
        duplicatorTable.add(confirm).pad(20).colspan(duplicatorTable.getColumns()).width(250).height(60);

        //Feedback Label
        duplicatorTable.row();
        inputMessenger = new Label("", game.skin, "error");
        duplicatorTable.add(inputMessenger).pad(5).colspan(duplicatorTable.getColumns());

        //Tutorial Label
        if(game.isTutorialMode()){
            Table tutorialTable = new Table();
            tutorialTable.setFillParent(true);
            tutorialTable.bottom().left().pad(40);
            Label tutorialText = new Label("Then the Duplicator has to decide a deviation for the previous filled out Table.\n" +
                    "The deviation weighted with the probability measure has to be less or equal to our current deviation e. \n" +
                    "In this case: " + game.getDuplicatorTable().getProbabilityMeasure().get(0).get(0) + " * (1,1) " +
                    game.getDuplicatorTable().getProbabilityMeasure().get(0).get(1) + " * (1,2) " +
                    game.getDuplicatorTable().getProbabilityMeasure().get(1).get(0) + " * (2,1) " +
                    game.getDuplicatorTable().getProbabilityMeasure().get(1).get(1) + " * (2,2) < 0.1 \n" +
                    "With (row, column) from this Table.\n", game.skin, "error");
            tutorialTable.add(tutorialText).pad(20).colspan(duplicatorTable.getColumns());
            stage.addActor(tutorialTable);
        }

        //main menu Table
        OptionTable options = new OptionTable();
        Table top = options.getOptionstable(game, screen);

        stage.addActor(duplicatorTable);
        stage.addActor(top);

    }


    public void enterDeviation(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(game.formatUtil.checkInputFormat(inputTable)){
                    if(game.controller.checkDeviationLogic(inputTable)){

                        for(int i = 0; i < game.getDuplicatorTable().getDeviation().size; i++){
                            Array<Double> row = game.getDuplicatorTable().getDeviation().get(i);
                            for(int j = 0; j < game.getDuplicatorTable().getDeviation().get(i).size; j++){
                                row.set(j, Double.parseDouble(inputTable.get(i).get(j).getText()));
                            }
                        }

                        screen.setHud(new SpoilerHUD(game, screen));
                        dispose();

                    }else{
                        inputMessenger.setText(game.getInfoMessage());
                    }
                }else{
                    inputMessenger.setText(game.getInfoMessage());
                }

            }
        });
    }


    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
