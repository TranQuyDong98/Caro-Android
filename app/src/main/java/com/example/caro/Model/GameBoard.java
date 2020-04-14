package com.example.caro.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.caro.GameBoardActivity;
import com.example.caro.MainActivity;

public class GameBoard {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    public Cell[][] cells;

    public static int BOARD_HEIGHT = GameBoardActivity.numberRows;//MainActivity.numberRows;
    public static int BOARD_WIDTH = GameBoardActivity.numberColumns;

    public Player winner;
    //public MutableLiveData<Player> winner = new MutableLiveData<>();

    public GameBoard(String player1, String player2) {
        this.player1 = new Player(player1, "x");
        this.player2 = new Player(player2, "o");
        this.currentPlayer = this.player1;
        this.cells = new Cell[BOARD_WIDTH][BOARD_HEIGHT];
      /*  for (int i = 0; i < BOARD_WIDTH; i++){
            for (int j = 0; j < BOARD_HEIGHT; j++){
                this.cells[i][j] = new Cell("");
            }
        }*/
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer==(player1) ? player2 : player1;
    }

    public boolean isEndGame(int x, int y) {
        if (horizontalCells(x, y) || verticalCells(x, y) || diagonalCells(x, y)) {
           //winner.setValue(currentPlayer);
            winner = currentPlayer;
            return true;
        }

        if (isBoardFull()){
            //winner.setValue(null);
            winner = null;
            return true;
        }
        return false;
    }

    public boolean horizontalCells(int x, int y) {
        int  sequentCells = 1;
        String status = currentPlayer.getStatus();

        //check left
        for (int i = 1; i <= 4; i++){
            if (x - i < 0) {
                break;
            }
            if (cells[x-i][y].getValue().equals(status)) {
                sequentCells++;
            }
            else {
                break;
            }

            if (sequentCells >= 5) {
                return true;
            }
        }

        //check right
        for (int i = 1; i <= 4; i++){
            if (x + i >= BOARD_WIDTH){
                break;
            }
            if (cells[x + i][y].getValue().equals(status)) {
                    sequentCells++;
                } else {
                    break;
                }

            if (sequentCells >= 5) {
                return true;
            }

        }
        return false;
    }


    public boolean verticalCells(int x, int y){
        //Cell cell = currentPlayer.getCell();
        String status = currentPlayer.getStatus();
        int sequentCells = 1;

        //check top
        for (int i = 1; i <= 4 ; i++){
            if (y - i < 0){
                break;
            }
            if (cells[x][y - i].getValue().equals(status)){
                sequentCells++;
            }
            else{
                break;
            }

            if (sequentCells >= 5){
                return true;
            }
        }

        //check bottom
        for (int i = 1; i <= 4 ; i++){
            if (y - i >= BOARD_HEIGHT){
                break;
            }
            if (cells[x][y + i].getValue().equals(status)){
                sequentCells++;
            }
            else{
                break;
            }

            if (sequentCells >= 5){
                return true;
            }
        }
        return false;
    }


    public boolean diagonalCells(int x, int y){
       // Cell cell = currentPlayer.getCell();
        String status = currentPlayer.getStatus();
        int sequentCells = 1;

        //check top-left
        for (int i = 1; i <= 4; i++){
            if ( x - i < 0 || y - i < 0){
                break;
            }
            if (cells[x - i][y - i].getValue().equals(status)){
                sequentCells++;
            }
            else{
                break;
            }
            if (sequentCells >= 5){
                return true;
            }
        }

        //check top-right
        for (int i = 1; i <= 4; i++){
            if ( x + i >= BOARD_WIDTH || y - i < 0){
                break;
            }
            if (cells[x + i][y - i].getValue().equals(status)){
                sequentCells++;
            }
            else{
                break;
            }
            if (sequentCells >= 5){
                return true;
            }
        }

        //check bottom-left
        for (int i = 1; i <= 4; i++){
            if ( x - i < 0 || y + i >= BOARD_HEIGHT){
                break;
            }
            if (cells[x - i][y + i].getValue().equals(status)){
                sequentCells++;
            }
            else{
                break;
            }
            if (sequentCells >= 5){
                return true;
            }
        }

        //check bottom-right
        for (int i = 1; i <= 4; i++){
            if ( x + i >= BOARD_WIDTH || y + i >= BOARD_HEIGHT){
                break;
            }
            if (cells[x + i][y + i].getValue().equals(status)){
                sequentCells++;
            }
            else{
                break;
            }
            if (sequentCells >= 5){
                return true;
            }
        }
        return false;
    }

    public boolean isBoardFull(){
        for (int i = 0; i < BOARD_WIDTH; i++){
            for (int j = 0; j < BOARD_HEIGHT; j++){
                if (cells[i][j] == null || cells[i][j].getValue().equals("")){
                    return false;
                }
            }
        }
        return true;
    }

    public void reset(){
        currentPlayer = player1;
        cells = new Cell[BOARD_WIDTH][BOARD_HEIGHT];
    }
}
