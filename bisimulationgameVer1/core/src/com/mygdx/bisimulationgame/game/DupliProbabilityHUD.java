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

public class DupliProbabilityHUD implements HUD {

    final BisimulationGame game;

    private Stage stage;
    private FitViewport stageViewport;
    private MarkovChainScreen screen;

    private Array<Array<TextField>> inputTable;

    private Label inputMessenger;

    public DupliProbabilityHUD(BisimulationGame game, MarkovChainScreen screen){
        this.game = game;
        this.screen = screen;

        stageViewport = new FitViewport(game.worldWidth, game.worldHeight);
        stage = new Stage(stageViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        game.controller.setupDuplicatorTable(game.getConfiguration().getStateA(), game.getConfiguration().getStateB());

        //Table
        Table duplicatorTable = new Table();
        duplicatorTable.setFillParent(true);
        duplicatorTable.top().left().pad(20);

        //Create Textfields
        inputTable = new Array<>();
        for(int i = 0; i < game.getDuplicatorTable().getChildrenA().size; i++){
            Array<TextField> inputRow = new Array<>();
            for(int j = 0; j < game.getDuplicatorTable().getChildrenB().size; j++){
                TextField input = new TextField("", game.skin);
                input.setTextFieldFilter(new DigitFilter());
                inputRow.add(input);
            }
            inputTable.add(inputRow);
        }

        //Table-Elements
        //Description Elements
        Label hudHead = new Label("Duplicator turn", game.skin, "title");
        Label hudSubHead = new Label("Please enter the probability measure", game.skin);
        duplicatorTable.add(hudHead).pad(5).colspan(duplicatorTable.getColumns());
        duplicatorTable.row();
        duplicatorTable.add(hudSubHead).pad(5).colspan(duplicatorTable.getColumns());
        duplicatorTable.row();

        //Arrange Top of Duplicator Table
        String tableheadText = game.getConfiguration().getStateA().getName() + "/" + game.getConfiguration().getStateB().getName();
        Label tableHead = new Label(tableheadText, game.skin);
        duplicatorTable.add(tableHead).pad(10).colspan(2);

        Array<Label> labelBChildren = new Array<>();
        for(int i = 0; i < game.getDuplicatorTable().getChildrenB().size; i++){
            Label childBName = new Label(game.getDuplicatorTable().getChildrenB().get(i).getName(), game.skin);
            labelBChildren.add(childBName);
            duplicatorTable.add(childBName).pad(5);
            Label probabilityChildB = new Label("("+game.getConfiguration().getStateB().getTransition().getProbabilitys().get(i)+")", game.skin, "peach");
            duplicatorTable.add(probabilityChildB).pad(5);
        }

        //Arrange Duplicator Table
        Array<Label> labelAChildren = new Array<>();
        for(int i = 0; i < inputTable.size; i++){
            duplicatorTable.row();
            Label childAName = new Label(game.getDuplicatorTable().getChildrenA().get(i).getName(), game.skin);
            labelAChildren.add(childAName);
            duplicatorTable.add(childAName).pad(5);
            Label probabilityChildA = new Label("("+game.getConfiguration().getStateA().getTransition().getProbabilitys().get(i)+")", game.skin, "peach");
            duplicatorTable.add(probabilityChildA).pad(5);
            for(int j = 0; j < inputTable.get(i).size; j++){
                duplicatorTable.add(inputTable.get(i).get(j)).pad(10).colspan(2);
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
        }

        //Button
        TextButton confirm = new TextButton("confirm", game.skin);
        enterProbabilityMeasure(confirm);
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
            Label tutorialText = new Label("First the duplicator picks a probability measure. \n" +
                    "Here it is important that the sum of the columns and rows equals the transition values of the respective child. \n" +
                    "For example in this case the two values of the first row have to add up to 0.25. \n" +
                    "And the values of the first column have to add up to 0.3. \n" +
                    "This distribution can be read like a transport plan. \n" +
                    "As in \"State 2 of Markov Chain 1 can transport 0.25 to State 2 and 3 of Markov Chain 2\" \n" +
                    "It is advisable to try to distribute more on states that are similar.", game.skin, "error");
            tutorialTable.add(tutorialText).pad(20).colspan(duplicatorTable.getColumns());
            stage.addActor(tutorialTable);
        }

        //main menu Table
        OptionTable options = new OptionTable();
        Table top = options.getOptionstable(game, screen);

        stage.addActor(duplicatorTable);
        stage.addActor(top);
    }

    public void enterProbabilityMeasure(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(game.formatUtil.checkInputFormat(inputTable)){
                    if(game.controller.checkProbabilityMeasureLogic(inputTable)){

                        for(int i = 0; i < inputTable.size; i++){
                            Array<Double> row = game.getDuplicatorTable().getProbabilityMeasure().get(i);
                            for(int j = 0; j < inputTable.get(i).size; j++){
                                row.set(j, Double.parseDouble(inputTable.get(i).get(j).getText()));
                            }
                        }

                        screen.setHud(new DupliDeviationHUD(game, screen));
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

    public Stage getStage(){
        return stage;
    }

    public void dispose(){
        stage.dispose();
    }
}
