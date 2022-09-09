package com.mygdx.bisimulationgame.game.setup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.bisimulationgame.BisimulationGame;
import com.mygdx.bisimulationgame.Util.DigitFilter;
import com.mygdx.bisimulationgame.Util.OptionTable;
import com.mygdx.bisimulationgame.interfaces.HUD;
import com.mygdx.bisimulationgame.game.DupliProbabilityHUD;
import com.mygdx.bisimulationgame.game.MarkovChainScreen;

public class SetupHUD implements HUD {

    final BisimulationGame game;

    private Stage stage;
    private FitViewport stageViewport;

    private MarkovChainScreen screen;

    private SelectBox<String> configStateA;
    private SelectBox<String> configStateB;
    private TextField deviation;
    private TextField nBounded;
    private CheckBox unbounded;
    private Label inputMessenger;

    public SetupHUD(BisimulationGame game, MarkovChainScreen screen){
        this.game = game;
        this.screen = screen;

        stageViewport = new FitViewport(game.worldWidth, game.worldHeight);
        stage = new Stage(stageViewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //Table
        Table setupHUDTable = new Table();
        setupHUDTable.setFillParent(true);
        setupHUDTable.top().left().pad(30);

        //Table-Elements
        inputMessenger = new Label("", game.skin, "error");
        Label configLabel = new Label("Initial Configuration:", game.skin, "title");

        //Configuration States
        configStateA = new SelectBox<>(game.skin);
        configStateA.setItems(game.getMarkovChainA().getStateNames());
        configStateB = new SelectBox<>(game.skin);
        configStateB.setItems(game.getMarkovChainB().getStateNames());

        //Max allowed Deviation
        Label deviationLabel = new Label("Maximum allowed deviation:", game.skin);
        deviation = new TextField("", game.skin);
        deviation.setTextFieldFilter(new DigitFilter());

        //nBounded or Unbounded
        unbounded = new CheckBox("  Unbounded game", game.skin);
        Label nBoundedLabel = new Label("Number of rounds:", game.skin);
        nBounded = new TextField("", game.skin);
        nBounded.setTextFieldFilter(new DigitFilter());

        //Button
        TextButton confirm = new TextButton("confirm", game.skin);
        setStartConfig(confirm);

        //Arranged Table
        setupHUDTable.add(configLabel).pad(10).colspan(setupHUDTable.getColumns());
        setupHUDTable.row();
        setupHUDTable.add(configStateA).pad(10).width(100);
        setupHUDTable.add(configStateB).pad(10).width(100);
        setupHUDTable.row();
        setupHUDTable.add(deviationLabel);
        setupHUDTable.add(deviation).pad(10).width(200);
        setupHUDTable.row();
        setupHUDTable.add(nBoundedLabel).pad(5);
        setupHUDTable.add(nBounded).pad(5).width(100);
        setupHUDTable.row();
        setupHUDTable.add(unbounded).colspan(2).pad(20);
        setupHUDTable.row();
        setupHUDTable.add(confirm).colspan(setupHUDTable.getColumns()).pad(35).width(250).height(60);
        setupHUDTable.row();
        setupHUDTable.add(inputMessenger).pad(5).colspan(setupHUDTable.getColumns());

        //main menu Table
        OptionTable options = new OptionTable();
        Table top = options.getOptionstable(game, screen);

        stage.addActor(setupHUDTable);
        stage.addActor(top);
    }

    public void setStartConfig(TextButton button){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(game.formatUtil.checkInputFormat(deviation)){
                    game.controller.setStartConfiguration(configStateA.getSelected(), configStateB.getSelected(), Double.parseDouble(deviation.getText()));
                    game.controller.setGameVariant(unbounded.isChecked(), nBounded.getText());
                    if(game.isVsSpoilerAI()){
                        game.getSpoilerAI().calculatePath();
                    }
                    screen.setHud(new DupliProbabilityHUD(game, screen));
                    dispose();
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
