package com.mygdx.bisimulationgame.datatypes;

import com.badlogic.gdx.utils.Array;

/**
 * Diese Klasse repräsentiert eine Labelled Markov Chain.
 * Eine Labelled Markov Chain hat erstmal eine endliche Anzahl an States und dann eine endliche Anzahl an Labels.
 * Diese Klasse gibt die möglichkeit States bei namen herauszufinden, da bei der Distribution States nur mit namen gespeichert werden,
 * um Zyklen zu verhindern.
 */
public class LabelledMarkovChain {

    private Array<State> states;
    private Array<MarkovLabel> labels;

    public LabelledMarkovChain(){

    }

    public LabelledMarkovChain(Array<State> states, Array<MarkovLabel> labels){
        this.states = states;
        this.labels = labels;
    }

    public Array<State> getStates() {
        return states;
    }

    public void setStates(Array<State> states) {
        this.states = states;
    }

    public Array<MarkovLabel> getLabels() {
        return labels;
    }

    public void setLabels(Array<MarkovLabel> labels) {
        this.labels = labels;
    }

    public Array<String> getStateNames(){
        Array<String> stateNameList = new Array<>();
        for(State state : states){
            stateNameList.add(state.getName());
        }
        return stateNameList;
    }

    public Array<String> getLabelNames(){
        Array<String> labelNameList = new Array<>();
        for(MarkovLabel label : labels){
            labelNameList.add(label.getName());
        }
        return labelNameList;
    }

    public State getStateByName(String name){
        for(int i = 0; i < states.size; i++){
            if(states.get(i).getName().equals(name)){
                return states.get(i);
            }
        }
        return null;
    }

    public MarkovLabel getLabelByName(String name){
        for(int i = 0; i < labels.size; i++){
            if(labels.get(i).getName().equals(name)){
                return labels.get(i);
            }
        }
        return null;
    }

}
