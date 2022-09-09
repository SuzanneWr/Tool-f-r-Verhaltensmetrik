package com.mygdx.bisimulationgame.AI;

import com.badlogic.gdx.utils.Array;
import com.mygdx.bisimulationgame.datatypes.State;

public class StatePair {

    private State a;
    private State b;
    private double transport;
    private double cost;
    private Array<StatePair> children;
    private Boolean childrenSet;
    private Boolean end;


    public StatePair(State a, State b){
        end = false;
        childrenSet = false;
        this.a = a;
        this.b = b;
        if(a.getLabel().equals(b.getLabel())){
            cost = 0;
        }else {
            cost = 1;
        }
        if(a.isBlocking() || b.isBlocking()){
            end = true;
            childrenSet = false;
        }else if(a.getName().equals(a.getTransition().getStateNames().get(0))
        && a.getTransition().getProbabilitys().get(0) == 1.0 &&
        b.getName().equals(b.getTransition().getStateNames().get(0))
        && b.getTransition().getProbabilitys().get(0) == 1.0){
            end = true;
            childrenSet = true;
        }

    }

    public void setChildren(Array<StatePair> children){
        if(!end && !childrenSet){
            if(children.get(0).getA().getName().equals(this.getA().getName()) &&
                    children.get(0).getB().getName().equals(this.getB().getName())){
                end = true;
            }else {
                this.children = children;
            }
        }
        childrenSet = true;
    }

    public void calculateCost(){
        double tempCost = 0;
        if(cost != 1 && children != null){
            for(int i = 0; i < children.size; i++){
                tempCost += children.get(i).getTransport() * children.get(i).getCost();
            }
            tempCost = Math.round(tempCost * 10000000000d) / 10000000000d;
            if(tempCost > cost){
                cost = tempCost;
            }
        }
    }

    public State getA() {
        return a;
    }

    public void setA(State a) {
        this.a = a;
    }

    public State getB() {
        return b;
    }

    public void setB(State b) {
        this.b = b;
    }

    public double getTransport() {
        return transport;
    }

    public void setTransport(double transport) {
        this.transport = transport;
    }

    public double getCost() {
        return cost;
    }

    public Array<StatePair> getChildren() {
        return children;
    }

    public Boolean getEnd() {
        return end;
    }

    public Boolean getChildrenSet() {
        return childrenSet;
    }

    public void setEnd(Boolean end) {
        this.end = end;
    }
}
