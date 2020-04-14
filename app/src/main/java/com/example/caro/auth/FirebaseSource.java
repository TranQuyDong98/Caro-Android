package com.example.caro.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.caro.Model.Player;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class FirebaseSource {
    private FirebaseAuth firebaseAuth;
    //private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    //private CollectionReference usersRef = rootRef.collection(USERS);

    public FirebaseSource() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Register with normal account
     *
     * //@param email
     * //@param password
     * //@param callback
     */
    public MutableLiveData<Player> register(String email, String passwrord){
        final MutableLiveData<Player> newUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(email, passwrord)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                //FirebaseUser user = firebaseAuth.getCurrentUser();
                               /* authenticatedUser.setCreated(true);
                                newUserMutableLiveData.setValue(authenticatedUser);*/
                            }
                           /* else {
                                logE(task.getException().getMessage());
                            }*/
                        }
                    });
        return newUserMutableLiveData;
    }

    /**
     * Login with normal account
     */
    public MutableLiveData<Player> signIn(String email, String password){
        final MutableLiveData<Player> newUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            /*boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null){
                                String name = firebaseUser.getDisplayName();
                                String email = firebaseUser.getEmail();
                                String id = firebaseUser.getUid();
                               *//* Player user = new Player(id, name, email);
                                user.setNew(isNewUser);
                                newUserMutableLiveData.setValue(user);*//*
                            }*/
                        }
                        else{
                            //ToDO
                        }
                    }
                });
        return newUserMutableLiveData;
    }

}
