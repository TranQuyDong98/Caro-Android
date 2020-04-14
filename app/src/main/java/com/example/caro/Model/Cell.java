package com.example.caro.Model;

public class Cell {


    private String value;
    //Player player;
    private int row, column;

    public Cell(){

    }

    public Cell(String value, int column, int row){
        this.value = value;
        this.row = row;
        this.column = column;

    }
   /* public Cell(String value, Player player) {
        this.value = value;
        this.player = player;
    }*/

   /* public Cell (String value) {
        this.value = value;
    }*/

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    /*public boolean equals(Cell cell) {
        if (value.equals(cell.value))
            return true;
        else
            return false;
    }*/
}
