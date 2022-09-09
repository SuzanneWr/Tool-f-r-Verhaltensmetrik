package com.mygdx.bisimulationgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.bisimulationgame.BisimulationGame;
import com.mygdx.bisimulationgame.Util.OptionTable;
import com.mygdx.bisimulationgame.interfaces.HUD;

public class WinHUD implements HUD {

    final BisimulationGame game;

    private Stage stage;
    private FitViewport stageViewport;
    private MarkovChainScreen screen;

    public WinHUD(BisimulationGame game, MarkovChainScreen screen){
        this.game = game;
        this.screen = screen;

        stageViewport = new FitViewport(game.worldWidth, game.worldHeight);
        stage = new Stage(stageViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //Table
        Table description = new Table();
        description.setFillParent(true);
        description.top().left().pad(20);

        //Create Victorymessage
        String victorymessage = "";
        if(game.isDuplicatorWin()){
            victorymessage = "Congratulation, the duplicator wins";
        }else if(game.isSpoilerWin() && !game.isVsSpoilerAI()){
            victorymessage = "Congratulation, the spoiler wins";
        }else if(game.isSpoilerWin() && game.isVsSpoilerAI()){
            victorymessage = "The Spoiler wins";
        }

        //Table-Elements
        Label head = new Label("Game end! ", game.skin, "title");
        Label winCondition = new Label(game.getInfoMessage(), game.skin, "error");
        Label subHead = new Label(victorymessage, game.skin);
        Label configuration = new Label("Final configuration: " + game.getConfiguration().getStateA().getName() +
                ", " + game.getConfiguration().getStateB().getName() + ", " + game.getConfiguration().getDeviation(), game.skin);


        //Arranged Table
        description.add(head).pad(5);
        description.row();
        description.add(winCondition).pad(5);
        description.row();
        description.add(subHead).pad(5);
        description.row();
        description.add(configuration).pad(5);

        //main menu Table
        OptionTable options = new OptionTable();
        Table top = options.getOptionstable(game, screen);

        stage.addActor(description);
        stage.addActor(top);
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
