package com.example.caro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caro.Model.Player;
import com.example.caro.auth.AuthViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class RegisterActivity extends AppCompatActivity {

    private EditText newAccount, edtPassword, edtEmail;
    private Button btnCreate;
    public FirebaseAuth firebaseAuth;
    private AuthViewModel authViewModel;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        newAccount = (EditText) findViewById(R.id.new_account);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtEmail = (EditText) findViewById(R.id.edtNewEmail);
        newAccount = (EditText)findViewById(R.id.new_account);
        firebaseAuth = FirebaseAuth.getInstance();
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        firebaseDatabase = FirebaseDatabase.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference usersdRef = rootRef.child("users");
       /* authViewModel.createdUserLiveData.observe(this, new Observer<Player>() {
            @Override
            public void onChanged(Player player) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Hi " + player.getName() + "!\n" + "Your account was successfully created.", Toast.LENGTH_LONG).show();
            }
        });*/
        //Intent intent = getIntent();
        init();
    }

    private void init() {
        BtnCreateListener();
    }

    private void BtnCreateListener() {
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username,  email, password;
                username = newAccount.getText().toString();
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
                    return;
                }

                //authViewModel.register(email, password);
                //DatabaseReference usersRef = rootRef.child("users");
                final DatabaseReference myRef = firebaseDatabase.getReference("users");
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<AuthResult> task) {
                                                       if (task.isSuccessful()){
                                                           final String userId = firebaseAuth.getCurrentUser().getUid();
                                                           final Player player = new Player();
                                                           player.setName(username);
                                                           player.setPassword(password);
                                                           player.setEmail(email);
                                                           player.setOnOff(1);
                                                           //player.setTokenId("reonvotnot");
                                                           final String tokenId;
                                                          /* Task<InstanceIdResult> token = */FirebaseInstanceId.getInstance().getInstanceId()
                                                                   .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                                       @Override
                                                                       public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                                           if (!task.isSuccessful()) {
                                                                               //Log.w(TAG, "getInstanceId failed", task.getException());
                                                                               return;
                                                                           }
                                                                           // Get new Instance ID token
                                                                            String  token = task.getResult().getToken();
                                                                           player.setTokenId(token);
                                                                           myRef.child(userId).setValue(player);
                                                                       }
                                                                   });
                                                          /* player.setTokenId();
                                                           myRef.child(userId).setValue(player);*/
                                                           Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                           startActivity(intent);
                                                       }
                                                   }
                                               });
               /* Player player = new Player();
                player.setName(username);
                player.setPassword(password);
                player.setEmail(email);
                player.setOnOff(1);
                myRef.push().setValue(player);
                Intent intent = new Intent(RegisterActivity.this, GameActivity.class);
                startActivity(intent);*/

               /* authViewModel.createdUserLiveData.observe(RegisterActivity.this, new Observer<Player>() {
                    @Override
                    public void onChanged(Player player) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        //Toast.makeText(getApplicationContext(), "Hi " + player.getName() + "!\n" + "Your account was successfully created.", Toast.LENGTH_LONG).show();
                    }
                });*/

                /*firebaseAuth.createUserWithEmailAndPassword(email, password
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                                    //progressBar.setVisibility(View.GONE);

                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                                    //progressBar.setVisibility(View.GONE);
                                }
                            }
                        });*/
            }
        });
    }
    private void toastMessage(String name) {
        Toast.makeText(this, "Hi " + name + "!\n" + "Your account was successfully created.", Toast.LENGTH_LONG).show();
    }

}
