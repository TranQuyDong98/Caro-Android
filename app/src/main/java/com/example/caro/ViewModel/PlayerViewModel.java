package com.example.caro.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.caro.Model.Player;
import com.example.caro.util.PlayerRepository;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {
    private PlayerRepository playerRespository;
    private LiveData<List<Player>> allPlayers;

    public PlayerViewModel(@NonNull Application application) {
        super(application);
        playerRespository = new PlayerRepository(application);
        allPlayers = playerRespository.getAllPlayers();
    }

    public LiveData<List<Player>> getAllPlayers(){
        return allPlayers;
    }

    public void insertPlayer(Player player){
        playerRespository.insertPlayer(player);
    }

}
