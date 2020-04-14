package com.example.caro.util;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.caro.Model.Player;
import com.example.caro.Model.PlayerDAO;
import com.example.caro.Model.PlayerDatabase;

import java.util.List;

public class PlayerRepository {
    private PlayerDAO playerDAO;
    private LiveData<List<Player>> allPlayers;

    public PlayerRepository(Application application) {
        PlayerDatabase playerDatabase = PlayerDatabase.getInstance(application);
        playerDAO = playerDatabase.playerDAO();
        allPlayers = playerDAO.getAllPlayers();
    }

    public LiveData<List<Player>> getAllPlayers(){
        return allPlayers;
    }

    public void insertPlayer(Player player){
        new InsertAsyncTask(playerDAO).execute(player);
    }

    private static class InsertAsyncTask extends AsyncTask<Player, Void, Void> {
        private PlayerDAO ayncTaskDAO;
        public InsertAsyncTask(PlayerDAO ayncTaskDAO) {
            this.ayncTaskDAO = ayncTaskDAO;
        }

        @Override
        protected Void doInBackground(Player... players) {
            ayncTaskDAO.insertPlayer(players[0]);
            return null;
        }
    }
}
