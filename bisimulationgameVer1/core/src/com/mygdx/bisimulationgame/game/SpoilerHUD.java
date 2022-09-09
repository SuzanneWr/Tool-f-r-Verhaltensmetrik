package com.mygdx.bisimulationgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.bisimulationgame.BisimulationGame;
import com.mygdx.bisimulationgame.Util.OptionTable;
import com.mygdx.bisimulationgame.interfaces.HUD;
import com.mygdx.bisimulationgame.Util.IndexPair;


public class SpoilerHUD implements HUD {

    final BisimulationGame game;

    private Stage stage;
    private FitViewport stageViewport;
    private MarkovChainScreen screen;

    private Array<Array<TextButton>> buttonTable;

    public SpoilerHUD(final BisimulationGame game, MarkovChainScreen screen){
        this.game = game;
        this.screen = screen;

        stageViewport = new FitViewport(game.worldWidth, game.worldHeight);
        stage = new Stage(stageViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //Table
        Table spoilerTable = new Table();
        spoilerTable.setFillParent(true);
        spoilerTable.top().left().pad(20);

        //Create Buttons
        buttonTable = new Array<>();
        for(int i = 0; i < game.getDuplicatorTable().getDeviation().size; i++){
            Array<TextButton> buttonRow = new Array<>();
            for(int j = 0; j < game.getDuplicatorTable().getDeviation().get(i).size; j++){
                TextButton button = new TextButton(game.getDuplicatorTable().getProbabilityMeasure().get(i).get(j) + ", " +
                        game.getDuplicatorTable().getDeviation().get(i).get(j), game.skin);
                if(game.getDuplicatorTable().getProbabilityMeasure().get(i).get(j) == 0 || game.isVsSpoilerAI()){
                    button.setTouchable(Touchable.disabled);
                }else{
                    chooseCell(button);
                }
                buttonRow.add(button);
            }
            buttonTable.add(buttonRow);
        }

        //Table-Elements
        //Description Elements
        Label head = new Label("Spoiler turn: ", game.skin, "title");
        Label subHead = new Label("Please select a cell", game.skin);
        spoilerTable.add(head).pad(5).colspan(spoilerTable.getColumns());
        spoilerTable.row();
        spoilerTable.add(subHead).pad(5).colspan(spoilerTable.getColumns());
        spoilerTable.row();

        //Arrange Top of Spoiler Table
        String tableheadText = game.getConfiguration().getStateA().getName() + "/" + game.getConfiguration().getStateB().getName();
        Label tableHead = new Label(tableheadText, game.skin);
        spoilerTable.add(tableHead).pad(10);

        Array<Label> labelBChildren = new Array<>();
        for(int i = 0; i < game.getDuplicatorTable().getChildrenB().size; i++){
            Label childBName = new Label(game.getDuplicatorTable().getChildrenB().get(i).getName(), game.skin);
            labelBChildren.add(childBName);
            spoilerTable.add(childBName);
        }

        //Arrange Spoiler Table
        Array<Label> labelAChildren = new Array<>();
        for(int i = 0; i < buttonTable.size; i++){
            spoilerTable.row();
            Label childAName = new Label(game.getDuplicatorTable().getChildrenA().get(i).getName(), game.skin);
            labelAChildren.add(childAName);
            spoilerTable.add(childAName).pad(10);
            for(int j = 0; j < buttonTable.get(i).size; j++){
                spoilerTable.add(buttonTable.get(i).get(j)).pad(5);
            }
        }
        spoilerTable.row();

        //Current Configuration Display
        Label currentConfig = new Label("Current configuration: (" + game.getConfiguration().getStateA().getName() + ", " +
                game.getConfiguration().getStateB().getName() + ", " + game.getConfiguration().getDeviation() + ")", game.skin);
        spoilerTable.add(currentConfig).pad(5).colspan(spoilerTable.getColumns());
        spoilerTable.row();

        //Round number
        if(!(game.isUnBounded())){
            Label roundNumber = new Label("Rounds left: " + game.getnBounded(), game.skin);
            spoilerTable.add(roundNumber).pad(5).colspan(spoilerTable.getColumns());
        }

        //Tutorial Label
        if(game.isTutorialMode()){
            Table tutorialTable = new Table();
            tutorialTable.setFillParent(true);
            tutorialTable.bottom().left().pad(40);
            Label tutorialText = new Label("Now the Spoiler can choose one of the entries from the Table.\n" +
                    "The spoiler can only choose entries with a probability measure greater 0. \n" +
                    "Here it is important to consider the winning conditions of the game: \n" +
                    "1.The duplicator wins if both states are blocking. \n" +
                    "2.The duplicator wins if the chosen deviation is 1. \n" +
                    "3.The spoiler wins if exactly one state is blocking and the chosen deviation is smaller 1. \n" +
                    "4.The spoiler wins if the two states chosen have different labels and the deviation is smaller 1. \n" +
                    "5.The duplicator wins if there are no more rounds left and nobody has won yet.", game.skin, "error");
            tutorialTable.add(tutorialText).pad(20).colspan(spoilerTable.getColumns());
            stage.addActor(tutorialTable);
            game.setTutorialMode(false);
        }

        //Spoiler AI choosing
        if(game.isVsSpoilerAI()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long time = System.currentTimeMillis();
                    while (System.currentTimeMillis() < time + 1500){}
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            IndexPair picked = game.getSpoilerAI().chooseField(buttonTable);
                            AIChooseCell(picked);
                        }
                    });
                }
            }).start();
        }

        //main menu Table
        OptionTable options = new OptionTable();
        Table top = options.getOptionstable(game, screen);

        stage.addActor(spoilerTable);
        stage.addActor(top);
    }

    public void chooseCell(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                TextButton buttonClicked = (TextButton) event.getListenerActor();
                for(int i = 0; i < buttonTable.size; i++){
                    for(int j = 0; j < buttonTable.get(i).size; j++){
                        if(buttonClicked.equals(buttonTable.get(i).get(j))){
                            game.controller.roundEnd(i, j);
                            if(game.isDuplicatorWin() || game.isSpoilerWin()){
                                screen.setHud(new WinHUD(game, screen));
                            }else{
                                screen.setHud(new DupliProbabilityHUD(game, screen));
                            }
                            dispose();
                        }
                    }


                }
            }
        });
    }

    public void AIChooseCell(IndexPair pickedPair){
        int row = pickedPair.getRow();
        int column = pickedPair.getColumn();

        game.controller.roundEnd(row, column);
        if(game.isDuplicatorWin() || game.isSpoilerWin()){
            screen.setHud(new WinHUD(game, screen));
        }else{
            screen.setHud(new DupliProbabilityHUD(game, screen));
        }
        dispose();
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
