package com.example.caro.Model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Player.class}, version = 1, exportSchema = false)
public abstract class PlayerDatabase extends RoomDatabase {
    public abstract PlayerDAO playerDAO();
    private static PlayerDatabase INSTANCE;

    public static String DATABASE_NAME = "player-db";

    public static PlayerDatabase getInstance(Context context){
        if (INSTANCE == null) {
            synchronized (PlayerDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlayerDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
