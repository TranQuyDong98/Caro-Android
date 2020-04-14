package com.example.caro.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.caro.Model.Cell;
import com.example.caro.Model.GameBoard;
import com.example.caro.Model.Player;

public class GameBoardViewModel extends AndroidViewModel {
    private Cell[][] cells;
    private GameBoard gameBoard;

    public GameBoardViewModel(@NonNull Application application) {
        super(application);

    }
    /*public LiveData<Player> getWinner(){
        return gameBoard.winner;
    }*/
}
