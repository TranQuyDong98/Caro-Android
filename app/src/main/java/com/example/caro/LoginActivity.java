package com.example.caro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProviders;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caro.auth.AuthViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static androidx.core.app.NotificationCompat.PRIORITY_MAX;

public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD = 100;
    public static final int REQUEST_CODE_EDIT = 101;
    private Button btnNewAccount;
    private Button btnLogin;
    private Button btnResetPassword;
    private EditText edtEmail, edtPassword;
    FirebaseAuth firebaseAuth;
    AuthViewModel authViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        init();
    }

    public void init(){
        BtnNewAccountListener();
        loginWithEmailAndPassword();
        setBtnResetPassword();
    }
    public void BtnNewAccountListener() {
        btnNewAccount = (Button) findViewById(R.id.create_account);
        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                //intent.putExtra("add_edit", "addAccount");
                startActivity(intent);
            }
        });
    }

    public void loginWithEmailAndPassword(){
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
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

                /*authViewModel.signIn(email, password);
                Intent intent = new Intent(MainActivity.this, GameBoardActivity.class);
                startActivity(intent);*/

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                                    //progressBar.setVisibility(View.GONE);

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                                    //progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }

    public void setBtnResetPassword(){
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }*/
}
