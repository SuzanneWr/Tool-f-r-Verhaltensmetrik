package com.mygdx.bisimulationgame.datatypes;

import com.badlogic.gdx.utils.Array;

public class DuplicatorTable {

    private Array<State> childrenA;
    private Array<State> childrenB;
    private Array<Array<Double>> probabilityMeasure;
    private Array<Array<Double>> deviation;

    public DuplicatorTable(Array<State> childrenA, Array<State> childrenB, Array<Array<Double>> probabilityMeasure, Array<Array<Double>> deviation){
        this.childrenA = childrenA;
        this.childrenB = childrenB;
        this.probabilityMeasure = probabilityMeasure;
        this.deviation = deviation;
    }

    public Array<State> getChildrenA() {
        return childrenA;
    }

    public void setChildrenA(Array<State> childrenA) {
        this.childrenA = childrenA;
    }

    public Array<State> getChildrenB() {
        return childrenB;
    }

    public void setChildrenB(Array<State> childrenB) {
        this.childrenB = childrenB;
    }

    public Array<Array<Double>> getProbabilityMeasure() {
        return probabilityMeasure;
    }

    public void setProbabilityMeasure(Array<Array<Double>> probabilityMeasure) {
        this.probabilityMeasure = probabilityMeasure;
    }

    public Array<Array<Double>> getDeviation() {
        return deviation;
    }

    public void setDeviation(Array<Array<Double>> deviation) {
        this.deviation = deviation;
    }
}
