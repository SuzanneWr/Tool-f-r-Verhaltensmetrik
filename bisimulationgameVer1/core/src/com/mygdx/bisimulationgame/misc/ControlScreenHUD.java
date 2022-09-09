package com.mygdx.bisimulationgame.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.bisimulationgame.BisimulationGame;
import com.mygdx.bisimulationgame.Util.OptionTable;
import com.mygdx.bisimulationgame.interfaces.HUD;

public class ControlScreenHUD implements HUD {

    final BisimulationGame game;

    private Stage stage;
    private FitViewport stageViewport;

    public ControlScreenHUD(BisimulationGame game, Screen screen){
        this.game = game;

        stageViewport = new FitViewport(game.worldWidth, game.worldHeight);
        stage = new Stage(stageViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //Table
        Table optionDesciptions = new Table();
        optionDesciptions.setFillParent(true);
        optionDesciptions.center().top().pad(200);

        //Table-Elements
        Label wsad = new Label("W S A D", game.skin, "title");
        Label wsadDescription = new Label("Move Markov Chains", game.skin, "title");
        Label qe = new Label("Q E", game.skin, "title");
        Label qeDescription = new Label("Zoom in and out", game.skin, "title");
        Label rt = new Label("R T", game.skin, "title");
        Label rtDescription = new Label("Focus on current configuration states", game.skin, "title");
        Label ikjl = new Label("I K J L", game.skin, "title");
        Label ikjlDescription = new Label("Move Duplicator/Spoiler Table or Editor Lists", game.skin, "title");

        //Arranged Table
        optionDesciptions.add(wsad).pad(5);
        optionDesciptions.add(wsadDescription).pad(5);
        optionDesciptions.row().pad(5);
        optionDesciptions.add(qe).pad(5);
        optionDesciptions.add(qeDescription).pad(5);
        optionDesciptions.row().pad(5);
        optionDesciptions.add(rt).pad(5);
        optionDesciptions.add(rtDescription).pad(5);
        optionDesciptions.row().pad(5);
        optionDesciptions.add(ikjl).pad(5);
        optionDesciptions.add(ikjlDescription).pad(5);

        //main menu Table
        OptionTable opt = new OptionTable();
        Table top = opt.getOptionstable(game, screen);

        stage.addActor(optionDesciptions);
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
