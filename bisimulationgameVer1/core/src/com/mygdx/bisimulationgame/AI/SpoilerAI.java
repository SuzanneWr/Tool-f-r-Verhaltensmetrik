package com.mygdx.bisimulationgame.AI;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.mygdx.bisimulationgame.BisimulationGame;
import com.mygdx.bisimulationgame.Util.IndexPair;
import com.mygdx.bisimulationgame.datatypes.State;

import java.util.HashMap;
import java.util.Random;

public class SpoilerAI {

    final BisimulationGame game;

    private Array<Array<TextButton>> table;
    private Array<StatePair> path;

    private double changesMade;

    public SpoilerAI(BisimulationGame game){
        this.game = game;
        path = new Array<>();
    }


    public IndexPair chooseField(Array<Array<TextButton>> table){

        this.table = table;

        Array<IndexPair> winningPairs = checkBlockingAndDeviation();
        if(winningPairs.notEmpty()){
            return winningPairs.get(0);
        }

        winningPairs = checkViolatedCondition();
        if(winningPairs.notEmpty()){
            return winningPairs.get(0);
        }

        IndexPair winningPair = new IndexPair();
        boolean winningPairSet = false;
        double deviationMinusCost;
        double minDeviationMinusCost = 1;
        for(int i = 0; i < game.getDuplicatorTable().getChildrenA().size; i++){
            for(int j = 0; j < game.getDuplicatorTable().getChildrenB().size; j++){
                for(int k = 0; k < path.size; k++){
                    if(path.get(k).getA().getName().equals(game.getDuplicatorTable().getChildrenA().get(i).getName())
                    && path.get(k).getB().getName().equals(game.getDuplicatorTable().getChildrenB().get(j).getName())
                    && probabilityMeasureNotZero(i,j)){
                        deviationMinusCost = game.getDuplicatorTable().getDeviation().get(i).get(j) - path.get(k).getCost();
                        if(deviationMinusCost < minDeviationMinusCost && game.getDuplicatorTable().getDeviation().get(i).get(j) != 1){
                            minDeviationMinusCost = deviationMinusCost;
                            winningPair.setRow(i);
                            winningPair.setColumn(j);
                            winningPairSet = true;
                        }
                    }
                }
            }
        }

        if(winningPairSet){
            return winningPair;
        }


        boolean notSelected = true;
        Random random = new Random();
        IndexPair pair = new IndexPair();
        while(notSelected){
            int randomRow = random.nextInt(table.size);
            int randomCol = random.nextInt(table.get(0).size);
            if(probabilityMeasureNotZero(randomRow, randomCol)){
                pair = new IndexPair(randomRow, randomCol);
                notSelected = false;
            }
        }
        return pair;
    }

    private Array<IndexPair> checkBlockingAndDeviation(){
        Array<IndexPair> winningPairs = new Array<>();
        for(int i = 0; i < table.size; i++){
            for(int j = 0; j < table.get(i).size; j++){
                if((game.getDuplicatorTable().getChildrenA().get(i).isBlocking() ^ game.getDuplicatorTable().getChildrenB().get(j).isBlocking())
                        && game.getDuplicatorTable().getDeviation().get(i).get(j) < 1 && probabilityMeasureNotZero(i, j)){
                    IndexPair pair = new IndexPair(i, j);
                    winningPairs.add(pair);
                }
            }
        }
        return winningPairs;
    }

    private Array<IndexPair> checkViolatedCondition(){
        Array<IndexPair> winningPairs = new Array<>();
        for(int i = 0; i < table.size; i++){
            for(int j = 0; j < table.get(i).size; j++){
                if(!(game.getDuplicatorTable().getChildrenA().get(i).getLabel().equals(game.getDuplicatorTable().getChildrenB().get(j).getLabel()))
                        && game.getDuplicatorTable().getDeviation().get(i).get(j) < 1 && probabilityMeasureNotZero(i, j)){
                    IndexPair pair = new IndexPair(i, j);
                    winningPairs.add(pair);
                }
            }
        }
        return winningPairs;
    }

    private boolean probabilityMeasureNotZero(int i, int j){
        if(game.getDuplicatorTable().getProbabilityMeasure().get(i).get(j) != 0){
            return true;
        }
        return false;
    }

    public void calculatePath(){

        State a = game.getConfiguration().getStateA();
        State b = game.getConfiguration().getStateB();
        StatePair root = new StatePair(a, b);
        root.setTransport(1);
        path.add(root);

        changesMade = 1;
        int pathIndex = 0;
        double currentCost = 0.0;
        while(root.getCost() < game.getConfiguration().getDeviation() && changesMade != 0){
            changesMade = 0;
            pathIndex = path.size;
            for(int i = 0; i < pathIndex; i++){
                if(!path.get(i).getEnd() && !path.get(i).getChildrenSet()){
                    //System.out.println("Setting Children for StatePair " + path.get(i).getA().getName() + ", " + path.get(i).getB().getName() + " Status:");
                    Array<StatePair> newAdditions = calculateCoupling(path.get(i).getA(), path.get(i).getB());
                    for(int k = 0; k < newAdditions.size; k++){
                        newAdditions.set(k, alreadyInPath(newAdditions.get(k)));
                    }
                    path.get(i).setChildren(newAdditions);
                }
            }

            currentCost = root.getCost();
            for(int i = path.size - 1; i > -1; i--){
                path.get(i).calculateCost();
            }

            if(currentCost < root.getCost()){
                changesMade++;
            }
        }

        for(int i = 0; i < path.size; i++){
            System.out.println("Final: State a: " + path.get(i).getA().getName() +
                    " State b: " + path.get(i).getB().getName() +
                    " Transport: " + path.get(i).getTransport() +
                    " Cost: " + path.get(i).getCost() +
                    " ChildrenSet: " + path.get(i).getChildrenSet() +
                    " End: " + path.get(i).getEnd());
        }

    }

    private Array<StatePair> calculateCoupling(State a, State b){
        HashMap<String, Double> probabilityLeftA = new HashMap<>();
        HashMap<String, Double> probabilityLeftB = new HashMap<>();
        for(int i = 0; i < a.getTransition().getStateNames().size; i++){
            probabilityLeftA.put(a.getTransition().getStateNames().get(i), a.getTransition().getProbabilitys().get(i));
        }
        for(int i = 0; i < b.getTransition().getStateNames().size; i++){
            probabilityLeftB.put(b.getTransition().getStateNames().get(i), b.getTransition().getProbabilitys().get(i));
        }

        //statepair combos
        Array<StatePair> children = new Array<>();
        for(int i = 0; i < a.getTransition().getStateNames().size; i++){
            for(int j = 0; j < b.getTransition().getStateNames().size; j++){
                State aChild = game.getMarkovChainA().getStateByName(a.getTransition().getStateNames().get(i));
                State bChild = game.getMarkovChainB().getStateByName(b.getTransition().getStateNames().get(j));
                StatePair pair = new StatePair(aChild, bChild);
                if(aChild.getLabel().equals(bChild.getLabel())){
                    double transportValue = min(a.getTransition().getProbabilitys().get(i), b.getTransition().getProbabilitys().get(j));
                    transportValue = Math.round(transportValue * 10000000000d) / 10000000000d;
                    pair.setTransport(transportValue);
                    probabilityLeftA.put(aChild.getName(), (probabilityLeftA.get(aChild.getName()) - transportValue));
                    probabilityLeftB.put(bChild.getName(), (probabilityLeftB.get(bChild.getName()) - transportValue));
                }
                children.add(pair);
            }
        }

        int childrenSize = children.size;
        for(int i = 0; i < childrenSize; i++){
            double aLeftTransport = probabilityLeftA.get(children.get(i).getA().getName());
            double bLeftTransport = probabilityLeftB.get(children.get(i).getB().getName());
            if(aLeftTransport > 0 && bLeftTransport > 0){
                double transportValue = min(aLeftTransport, bLeftTransport);
                transportValue = Math.round(transportValue * 10000000000d) / 10000000000d;
                children.get(i).setTransport(transportValue);
                probabilityLeftA.put(children.get(i).getA().getName(), (probabilityLeftA.get(children.get(i).getA().getName()) - transportValue));
                probabilityLeftB.put(children.get(i).getB().getName(), (probabilityLeftB.get(children.get(i).getB().getName()) - transportValue));
            }
        }

        return children;
    }

    private StatePair alreadyInPath(StatePair addition){
        for(int i = 0; i < path.size; i++){
            if(addition.getA().getName().equals(path.get(i).getA().getName()) && addition.getB().getName().equals(path.get(i).getB().getName())
                    /*&& (path.get(i).getChildrenSet() || path.get(i).getEnd())*/){
                if(path.get(i).getChildrenSet()){
                    path.get(i).setEnd(true);
                }
                //System.out.println(" Already in the Path.");
                return path.get(i);
            }
        }
        //System.out.println(addition.getA().getName() + ", " + addition.getB().getName() + " added to Path.");
        changesMade++;
        path.add(addition);
        return addition;
    }


    private double min(double i, double j){
        if(i < j){
            return i;
        }
        return j;
    }


}
