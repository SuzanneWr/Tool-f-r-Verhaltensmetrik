package com.mygdx.bisimulationgame.datatypes;

import com.badlogic.gdx.graphics.Color;

/**
 * Diese Klasse beschreibt ein Label von der Labelled Markov Chain.
 * In diesem Fall ist ein Label einfach nur ein String.
 * Diese Klasse kann man aber nach belieben erweitern, wenn man andere Typen für Labels benutzten möchte,
 * man muss nur darauf achten die equals Methode anzupassen.
 * Die Klasse heisst MarkovLabel und nicht nur Label, weil Label schon ein häufig benutztes Objekt für die UI ist.
 */
public class MarkovLabel {

    private String name;

    private Color color;
    private boolean colorSet;

    public MarkovLabel(){
        colorSet = false;
        color = new Color(0.368f, 0.490f, 0.250f, 1);
    }

    public MarkovLabel(String name){
        this.name = name;

        colorSet = false;
        color = new Color(0.368f, 0.490f, 0.250f, 1);
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }

        if(obj.getClass() != this.getClass()){
            return false;
        }

        final MarkovLabel other = (MarkovLabel) obj;
        if(this.name.equals(other.getName())){
            return true;
        }
        return false;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isColorSet() {
        return colorSet;
    }

    public void setColorSet(boolean colorSet) {
        this.colorSet = colorSet;
    }



    public String toString() {
        return name;
    }

}
