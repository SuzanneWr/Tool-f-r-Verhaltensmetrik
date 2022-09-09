package com.mygdx.bisimulationgame.Util;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.bisimulationgame.BisimulationGame;

public class OptionTable {

    public Table getOptionstable(BisimulationGame game, Screen screen){
        Table top = new Table();
        top.setFillParent(true);
        top.top().right().pad(20);
        TextButton mainMenu = new TextButton("main menu", game.skin);
        game.controller.backToMenuButtonController(mainMenu, screen);
        top.add(mainMenu);
        return top;
    }
}
