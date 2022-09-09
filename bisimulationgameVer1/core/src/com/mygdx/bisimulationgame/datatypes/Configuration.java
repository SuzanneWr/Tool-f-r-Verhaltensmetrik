package com.mygdx.bisimulationgame.datatypes;

/**
 * Implementation der Configuration des Bisimulation Games.
 * Eine Configuration besteht aus einem Tripel (a, b, e).
 * Hier dargestellet durch die Variablen: stateA, stateB und deviation.
 * stateA sollte ein Zustand aus der ersten gewählten Markov Chain sein.
 * stateB sollte ein Zustand aus der zweiten gewählten Markov Chain sein.
 * deviation beschreibt die maximal zulässige Wahrscheinlichkeitsabweichung zwischen den beiden Markov Chains.
 * deviation Werte sollten nur von 0 bis 1 zulässig sein.
 */
public class Configuration {

    private State stateA;
    private State stateB;
    private double deviation;

    public Configuration(){

    }

    public State getStateA() {
        return stateA;
    }

    public void setStateA(State stateA) {
        this.stateA = stateA;
    }

    public State getStateB() {
        return stateB;
    }

    public void setStateB(State stateB) {
        this.stateB = stateB;
    }

    public double getDeviation() {
        return deviation;
    }

    public void setDeviation(double deviation) {
        this.deviation = deviation;
    }


}
