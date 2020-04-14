package com.example.caro.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface PlayerDAO {
   @Insert
    void insertPlayer(Player player);

   @Query("select * from player_table")
    LiveData<List<Player>> getAllPlayers();

}
