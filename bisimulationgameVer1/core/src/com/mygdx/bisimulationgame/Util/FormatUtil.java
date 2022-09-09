package com.mygdx.bisimulationgame.Util;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;
import com.mygdx.bisimulationgame.BisimulationGame;

import java.util.HashSet;
import java.util.Set;

public class FormatUtil {

    final BisimulationGame game;

    public FormatUtil(BisimulationGame game){
        this.game = game;
    }

    public boolean checkInputFormat(TextField field){
        if(field.getText().equals("")){
            game.setInfoMessage("Please enter a value from 0 to 1.");
            return false;
        }

        if(field.getText().equals(".")){
            game.setInfoMessage("Please enter a value from 0 to 1.");
            return false;
        }

        //https://stackoverflow.com/questions/275944/how-do-i-count-the-number-of-occurrences-of-a-char-in-a-string/35242882
        int countDots = field.getText().length() - field.getText().replace(".", "").length();
        if(countDots > 1){
            game.setInfoMessage("Please enter a value from 0 to 1.");
            return false;
        }

        double inputNumber = Double.parseDouble(field.getText());
        if(inputNumber > 1 || inputNumber < 0){
            game.setInfoMessage("Please enter a value from 0 to 1.");
            return false;
        }

        return true;
    }

    public boolean checkInputFormat(String text){
        if(text.equals("")){
            game.setInfoMessage("Please enter a value from 0 to 1.");
            return false;
        }

        if(text.equals(".")){
            game.setInfoMessage("Please enter a value from 0 to 1.");
            return false;
        }

        //https://stackoverflow.com/questions/275944/how-do-i-count-the-number-of-occurrences-of-a-char-in-a-string/35242882
        int countDots = text.length() - text.replace(".", "").length();
        if(countDots > 1){
            game.setInfoMessage("Please enter a value from 0 to 1.");
            return false;
        }

        double inputNumber = Double.parseDouble(text);
        if(inputNumber > 1 || inputNumber < 0){
            game.setInfoMessage("Please enter a value from 0 to 1.");
            return false;
        }

        return true;
    }

    public boolean checkInputFormat(Array<Array<TextField>> table){
        for(int i = 0; i < table.size; i++){
            for(int j = 0; j < table.get(i).size; j++){
                if(!(checkInputFormat(table.get(i).get(j)))){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkInputArrayEmpty(Array<TextField> field){
        for(int i = 0; i < field.size; i++){
            if(field.get(i).getText().equals("")){
                game.setInfoMessage("Please enter a value from 0 to 1.");
                return true;
            }
        }
        return false;
    }

    public boolean checkArrayElementsDistinct(Array<TextField> field){
        Set<String> distinctValues = new HashSet<>();

        for(int i = 0; i < field.size; i++){
            distinctValues.add(field.get(i).getText());
        }

        return distinctValues.size() == field.size;
    }

}
