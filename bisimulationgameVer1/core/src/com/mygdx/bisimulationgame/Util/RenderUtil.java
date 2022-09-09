package com.mygdx.bisimulationgame.Util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.mygdx.bisimulationgame.datatypes.LabelledMarkovChain;

public class RenderUtil {

    //some static Colors for the labels
    private Array<Color> markovLabelColors;

    public RenderUtil(){
        Color c1 = new Color(1f, 0.627f, 0.082f, 1);
        Color c2 = new Color(0.6f, 0.054f, 0.262f, 1);
        Color c3 = new Color(0.639f, 0.905f, 0.988f, 1);
        Color c4 = new Color(0.8f, 0.960f, 0.674f, 1);
        Color c5 = new Color(0.709f, 0.552f, 0.713f, 1);
        Color c6 = new Color(0.980f, 0.650f, 1f, 1);
        Color c7 = new Color(0.988f, 0.427f, 0.670f, 1);
        Color c8 = new Color(0.752f, 0.298f, 0.992f, 1);
        Color c9 = new Color(0.286f, 0.623f, 0.407f, 1);
        Color c10 = new Color(0.490f, 0.803f, 0.521f, 1);
        markovLabelColors = new Array<>();
        markovLabelColors.add(c1);
        markovLabelColors.add(c2);
        markovLabelColors.add(c3);
        markovLabelColors.add(c4);
        markovLabelColors.add(c5);
        markovLabelColors.add(c6);
        markovLabelColors.add(c7);
        markovLabelColors.add(c8);
        markovLabelColors.add(c9);
        markovLabelColors.add(c10);
    }

    public LabelledMarkovChain setStatePositions(LabelledMarkovChain markovChain){
        float x = 0;
        float y = 0;
        for(int i = 0; i < markovChain.getStates().size; i++){
            markovChain.getStates().get(i).setX(x);
            x += 128;
            y = markovChain.getStates().get(i).getY() - 128;
            if(!(markovChain.getStates().get(i).isBlocking())){
                for(int j = 0; j < markovChain.getStates().get(i).getTransition().getStateNames().size; j++){
                    markovChain.getStateByName(markovChain.getStates().get(i).getTransition().getStateNames().get(j)).setY(y);
                }
            }
        }
        return markovChain;
    }

    public Array<LabelledMarkovChain> colorLabels(LabelledMarkovChain markovChainA, LabelledMarkovChain markovChainB){

        int colorCounter = 0;
        for(int i = 0; i < markovChainA.getLabels().size; i++){
            for(int j = 0; j < markovChainB.getLabels().size; j++){
                if(markovChainA.getLabels().get(i).equals(markovChainB.getLabels().get(j))){
                    markovChainA.getLabels().get(i).setColor(markovLabelColors.get(colorCounter));
                    markovChainA.getLabels().get(i).setColorSet(true);
                    markovChainB.getLabels().get(j).setColor(markovLabelColors.get(colorCounter));
                    markovChainB.getLabels().get(j).setColorSet(true);
                    colorCounter = (colorCounter + 1) % 10;
                }
            }
        }

        for(int i = 0; i < markovChainA.getLabels().size; i++){
            if(!markovChainA.getLabels().get(i).isColorSet()){
                markovChainA.getLabels().get(i).setColor(markovLabelColors.get(colorCounter));
                markovChainA.getLabels().get(i).setColorSet(true);
                colorCounter = (colorCounter + 1) % 10;
            }
        }

        for(int i = 0; i < markovChainB.getLabels().size; i++){
            if(!markovChainB.getLabels().get(i).isColorSet()){
                markovChainB.getLabels().get(i).setColor(markovLabelColors.get(colorCounter));
                markovChainB.getLabels().get(i).setColorSet(true);
                colorCounter = (colorCounter + 1) % 10;
            }
        }

        Color c = new Color();
        for(int i = 0; i < markovChainA.getStates().size; i++){
            c = markovChainA.getLabelByName(markovChainA.getStates().get(i).getLabel().getName()).getColor();
            markovChainA.getStates().get(i).getLabel().setColor(c);
        }

        for(int i = 0; i < markovChainB.getStates().size; i++){
            c = markovChainB.getLabelByName(markovChainB.getStates().get(i).getLabel().getName()).getColor();
            markovChainB.getStates().get(i).getLabel().setColor(c);
        }


        Array<LabelledMarkovChain> coloredMarkovChains = new Array<>();
        coloredMarkovChains.add(markovChainA);
        coloredMarkovChains.add(markovChainB);
        return coloredMarkovChains;
    }

    //TODO mappen von distribution kram
}
