package com.mygdx.bisimulationgame.Util;

//This class is helpful to target a specific cell in the duplicator table
// with i you can find out which childA it is
// with j you can find out which childB it is
// with both you can find out the probability measure and deviation with array.get(i).get(j)
// here named row and column, just so its easier to read
public class IndexPair {

    //i - outer for loop
    private int row;
    //j - inner for loop
    private int column;

    public IndexPair(){

    }

    public IndexPair(int row, int column){
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

}
