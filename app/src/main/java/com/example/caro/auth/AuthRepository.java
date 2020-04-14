package com.example.caro.auth;

import androidx.lifecycle.LiveData;

import com.example.caro.Model.Player;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;

public class AuthRepository {
    private FirebaseSource firebaseSource;
    //LiveData<Player> authenticatedUserLiveData;
    //LiveData<Player> createdUserLiveData;

    public AuthRepository(FirebaseSource firebaseSource){

        this.firebaseSource = firebaseSource;
    }

    public LiveData<Player> register(String email, String password){
        return firebaseSource.register(email, password);
    }

    public LiveData<Player> signIn(String email, String password){
        return firebaseSource.signIn(email, password);
    }
}
