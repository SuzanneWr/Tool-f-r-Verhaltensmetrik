package com.mygdx.bisimulationgame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.bisimulationgame.AI.SpoilerAI;
import com.mygdx.bisimulationgame.interfaces.HUD;
import com.mygdx.bisimulationgame.Util.RenderUtil;
import com.mygdx.bisimulationgame.Util.SaveUtil;
import com.mygdx.bisimulationgame.datatypes.*;

public class Controller {

    final BisimulationGame game;

    private SaveUtil saveUtil;
    private RenderUtil renderUtil;

    public Controller(BisimulationGame game){
        this.game = game;
        saveUtil = new SaveUtil();
        renderUtil = new RenderUtil();
    }

    public void setupSpoilerAI(){
        game.setVsSpoilerAI(true);
        SpoilerAI spoilerAI = new SpoilerAI(game);
        game.setSpoilerAI(spoilerAI);
    }



    public void loadMarkovChains(String fileNameA, String fileNameB){
        LabelledMarkovChain markovChainA =  saveUtil.loadLabelledMarkovChain(fileNameA);
        game.setMarkovChainA(markovChainA);
        LabelledMarkovChain markovChainB = saveUtil.loadLabelledMarkovChain(fileNameB);
        game.setMarkovChainB(markovChainB);
    }

    public LabelledMarkovChain loadMarkovChain(String fileName){
        LabelledMarkovChain markovChain = saveUtil.loadLabelledMarkovChain(fileName);
        return markovChain;
    }

    public boolean deleteMarkovChain(String fileName){
        boolean success = saveUtil.deleteFile(fileName);
        if(!success){
            game.setInfoMessage("Couldn't delete file.");
        }
        return success;
    }

    public void resetGame(){
        game.setUnBounded(false);
        game.setDuplicatorWin(false);
        game.setSpoilerWin(false);
        game.setVsSpoilerAI(false);
    }

    public void backToMenuButtonController(TextButton button, final HUD hud){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                resetGame();
                hud.dispose();
            }
        });
    }

    public void backToMenuButtonController(TextButton button, final Screen screen){
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                resetGame();
                screen.dispose();
            }
        });
    }

    public void setupMarkovChainForRender(){
        game.setMarkovChainA(renderUtil.setStatePositions(game.getMarkovChainA()));
        game.setMarkovChainB(renderUtil.setStatePositions(game.getMarkovChainB()));
        Array<LabelledMarkovChain> coloredMarkovChains = renderUtil.colorLabels(game.getMarkovChainA(), game.getMarkovChainB());
        game.setMarkovChainA(coloredMarkovChains.get(0));
        game.setMarkovChainB(coloredMarkovChains.get(1));

        float offset = 0;
        for(int i = 0; i < game.getMarkovChainA().getStates().size; i++){
            if(offset < game.getMarkovChainA().getStates().get(i).getX()){
                offset = game.getMarkovChainA().getStates().get(i).getX();
            }
        }
        offset += 64;
        for(int i = 0; i < game.getMarkovChainB().getStates().size; i++){
            game.getMarkovChainB().getStates().get(i).setX(game.getMarkovChainB().getStates().get(i).getX() + offset);
        }
    }

    public void setStartConfiguration(String stateA, String stateB, double deviation){
        Configuration config = new Configuration();
        config.setStateA(game.getMarkovChainA().getStateByName(stateA));
        config.setStateB(game.getMarkovChainB().getStateByName(stateB));
        config.setDeviation(deviation);
        game.setConfiguration(config);
    }

    public void setGameVariant(boolean unbounded, String nBounded){
        if(nBounded.equals("") || unbounded){
            game.setUnBounded(true);
        }else{
            game.setnBounded(Integer.parseInt(nBounded));
        }
    }


    public boolean checkProbabilityMeasureLogic(Array<Array<TextField>> table){
        String infomessage = "";
        //first check the one for the children of a
        //adding all the values of each row and checking if it is the same as the probability
        State childOfA;
        double neededProbability;
        double inputProbability;
        for(int i = 0; i < table.size; i++){
            childOfA = game.getDuplicatorTable().getChildrenA().get(i);
            neededProbability = game.getConfiguration().getStateA().getTransition().getProbabilitys().get(i);
            inputProbability = 0;
            for(int j = 0; j < table.get(i).size; j++){
                inputProbability += Double.parseDouble(table.get(i).get(j).getText());
            }
            inputProbability = Math.round(inputProbability * 10000000000d) / 10000000000d;
            if(!(inputProbability == neededProbability)){
                infomessage += "Row of child " + childOfA.getName() + " doesn't add up to " + neededProbability;
                game.setInfoMessage(infomessage);
                return false;
            }
        }

        //same procedure but here we count the 'columns'
        State childOfB;
        neededProbability = 0;
        inputProbability = 0;
        for(int j = 0; j < table.get(0).size; j++){
            childOfB = game.getDuplicatorTable().getChildrenB().get(j);
            neededProbability = game.getConfiguration().getStateB().getTransition().getProbabilitys().get(j);
            inputProbability = 0;
            for(int i = 0; i < table.size; i++) {
                inputProbability += Double.parseDouble(table.get(i).get(j).getText());
            }
            inputProbability = Math.round(inputProbability * 10000000000d) / 10000000000d;
            if(!(inputProbability == neededProbability)){
                infomessage += "Column of child " + childOfB.getName() + " doesn't add up to " + neededProbability;
                game.setInfoMessage(infomessage);
                return false;
            }
        }

        return true;
    }

    public boolean checkDeviationLogic(Array<Array<TextField>> table){
        double tableDeviation = 0.0;
        String infoMessage = "Deviation is too high: \n";
        for(int i = 0; i < table.size; i++){
            for(int j = 0; j < table.get(i).size; j++){
                tableDeviation += game.getDuplicatorTable().getProbabilityMeasure().get(i).get(j) * Double.parseDouble(table.get(i).get(j).getText());
                //create the info Message
                infoMessage += game.getDuplicatorTable().getProbabilityMeasure().get(i).get(j) + " * " + table.get(i).get(j).getText();
                if(!(i == table.size - 1 && j == table.get(i).size - 1)){
                    infoMessage += " + \n";
                }
            }
        }
        tableDeviation = Math.round(tableDeviation * 10000000000d) / 10000000000d;

        infoMessage += " \n = " + tableDeviation + " > " + game.getConfiguration().getDeviation();

        if(tableDeviation > game.getConfiguration().getDeviation()){
            game.setInfoMessage(infoMessage);
            return false;
        }

        return true;
    }

    public void setupDuplicatorTable(State a, State b){
        Array<State> childrenA = new Array<>();
        for(int i = 0; i < a.getTransition().getStateNames().size; i++){
            State childA = game.getMarkovChainA().getStateByName(a.getTransition().getStateNames().get(i));
            childrenA.add(childA);
        }

        Array<State> childrenB = new Array<>();
        for(int i = 0; i < b.getTransition().getStateNames().size; i++){
            State childB = game.getMarkovChainB().getStateByName(b.getTransition().getStateNames().get(i));
            childrenB.add(childB);
        }

        Array<Array<Double>> probabilityMeasure = new Array<>();
        Array<Array<Double>> deviation = new Array<>();
        for(int i = 0; i < childrenA.size; i++){
            Array<Double> rowDeviation = new Array<>();
            Array<Double> rowProbability = new Array<>();
            for(int j = 0; j < childrenB.size; j++){
                rowDeviation.add(-1.0);
                rowProbability.add(-1.0);
            }
            probabilityMeasure.add(rowProbability);
            deviation.add(rowDeviation);
        }

        DuplicatorTable duplicatorTable = new DuplicatorTable(childrenA, childrenB, probabilityMeasure, deviation);
        game.setDuplicatorTable(duplicatorTable);
    }

    public void checkWinConditions(){
        if(game.getConfiguration().getStateA().isBlocking() && game.getConfiguration().getStateB().isBlocking()){
            game.setInfoMessage("Both states are blocking!");
            game.setDuplicatorWin(true);
        }else if(game.getConfiguration().getDeviation() == 1.0){
            game.setInfoMessage("Deviation equals 1!");
            game.setDuplicatorWin(true);
        }else if((game.getConfiguration().getStateA().isBlocking() ^ game.getConfiguration().getStateB().isBlocking()) && game.getConfiguration().getDeviation() < 1.0){
            game.setInfoMessage("One state is blocking and the deviation is than smaller 1!");
            game.setSpoilerWin(true);
        }else if(!(game.getConfiguration().getStateA().getLabel().equals(game.getConfiguration().getStateB().getLabel())) && game.getConfiguration().getDeviation() < 1){
            game.setInfoMessage("Both states have different labels and the deviation is smaller than 1!");
            game.setSpoilerWin(true);
        }else if((game.getnBounded() < 1) && (!game.isUnBounded())){
            game.setInfoMessage("No more Rounds left!");
            game.setDuplicatorWin(true);
        }
    }

    public void roundEnd(int childAIndex, int childBIndex){

        game.getConfiguration().setStateA(game.getDuplicatorTable().getChildrenA().get(childAIndex));
        game.getConfiguration().setStateB(game.getDuplicatorTable().getChildrenB().get(childBIndex));
        game.getConfiguration().setDeviation(game.getDuplicatorTable().getDeviation().get(childAIndex).get(childBIndex));

        checkWinConditions();
        game.setnBounded((game.getnBounded())-1);
    }

    public boolean addTransitions(Array<TextField> stateNamesList, Array<TextField> probabilitiesList){

        for(int i = 0; i < stateNamesList.size; i++){
            if(!game.getToEdit().getStates().get(i).isBlocking()){

                String stateNames = stateNamesList.get(i).getText();
                System.out.println(stateNames);
                String[] stateList = stateNames.split(",");

                String probabilities = probabilitiesList.get(i).getText();
                System.out.println(probabilities);
                String[] probyList = probabilities.split(",");

                if(stateList.length != probyList.length){
                    game.setInfoMessage("Number of states has to match number of probabilities.");
                    return false;
                }

                Double[] probabilityValues = new Double[stateList.length];
                double sum = 0.0;
                for(int j = 0; j < stateList.length; j++){

                    if(game.getToEdit().getStateByName(stateList[j]) == null){
                        game.setInfoMessage("State name not found.");
                        return false;
                    }

                    if(game.formatUtil.checkInputFormat(probyList[j])){
                        probabilityValues[j] = Double.parseDouble(probyList[j]);
                        sum += probabilityValues[j];
                    }else{
                        return false;
                    }

                }

                sum = Math.round(sum * 10000000000d) / 10000000000d;

                if(sum != 1.0){
                    game.setInfoMessage("Probabilities have to add up to 1.");
                    return false;
                }

                Distribution distribution = new Distribution();
                for(int j = 0; j < probabilityValues.length; j++){
                    distribution.addTransition(stateList[j], probabilityValues[j]);
                }
                game.getToEdit().getStates().get(i).setTransition(distribution);
            }

        }

        return true;
    }

    public String stateTransitionsToString(Array<String> stateNames){
        String stateNameTextfield = "";
        for(int i = 0; i < stateNames.size; i++){
            if(i == stateNames.size-1){
                stateNameTextfield += stateNames.get(i);
            }else{
                stateNameTextfield += stateNames.get(i)+",";
            }
        }
        return stateNameTextfield;
    }

    public String probabilitiesTransitionsToString(Array<Double> probabilities){
        String probabilitiesTextfield = "";
        for(int i = 0; i < probabilities.size; i++){
            if(i == probabilities.size-1){
                probabilitiesTextfield += probabilities.get(i)+"";
            }else{
                probabilitiesTextfield += probabilities.get(i)+",";
            }
        }
        return probabilitiesTextfield;
    }

}
