package com.mygdx.bisimulationgame.datatypes;

import com.badlogic.gdx.utils.Array;

/**
 * Die Klasse Distribution beschreibt Distr(S).
 * Jeder State hat eine Distribution, welche die Übergangswahrscheinlichkeiten auf die anderen States beschreibt.
 * Diese Klasse ist als Funktion zu lesen wo stateNames.get(x) auf probabilitys.get(x) abbildet.
 * Alle probabilitys zusammen müssen 1 ergeben, außer der State blockiert.
 */
public class Distribution {

    private Array<String> stateNames;
    private Array<Double> probabilitys;

    public Distribution(){
        stateNames = new Array<>();
        probabilitys = new Array<>();
    }

    public void addTransition(String stateName, Double probability){
        stateNames.add(stateName);
        probabilitys.add(probability);
    }

    public boolean checkDistribution(){
        double sum = 0;
        for(int i = 0; i < probabilitys.size; i++){
            sum += probabilitys.get(i);
        }
        if(sum == 1){
            return true;
        }
        return false;
    }

    public Array<String> getStateNames() {
        return stateNames;
    }

    public Array<Double> getProbabilitys() {
        return probabilitys;
    }

}
