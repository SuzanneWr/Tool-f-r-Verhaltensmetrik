package com.mygdx.bisimulationgame.datatypes;

/**
 * Ein Zustand in der Markov Chain.
 * Der Zustand hat einen Namen, dieser sollte unique in seiner Markov Chain sein.
 * Jeder Zustand hat dann noch ein label.
 * Jeder Zustand hat eine Distribution zu anderen Zuständen oder blockt.
 */
public class State {

    private String name;
    private MarkovLabel label;
    private Distribution transition;
    private boolean blocking;

    private float x;
    private float y;

    public State(){

    }

    public State(String name, MarkovLabel label, Distribution transition){
        this.name = name;
        this.transition = transition;
        this.label = label;
        blocking = false;

        x = 0;
        y = 0;
    }

    public State(String name, MarkovLabel label, boolean blocking) {
        this.name = name;
        this.label = label;
        this.transition = new Distribution();
        this.blocking = blocking;

        x = 0;
        y = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MarkovLabel getLabel() {
        return label;
    }

    public void setLabel(MarkovLabel label) {
        this.label = label;
    }

    public Distribution getTransition() {
        return transition;
    }

    public void setTransition(Distribution transition) {
        this.transition = transition;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


}
