package com.example.caro.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.caro.Model.Player;

public class AuthViewModel extends AndroidViewModel {
    AuthRepository authRepository;
    public  LiveData<Player> authenticatedUserLiveData;
    public LiveData<Player> createdUserLiveData;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(new FirebaseSource());
    }

    public void signIn(String email, String password){
        authenticatedUserLiveData = authRepository.signIn(email,password);
    }

    public void register(String email, String password){
        createdUserLiveData = authRepository.register(email, password);
    }
}
