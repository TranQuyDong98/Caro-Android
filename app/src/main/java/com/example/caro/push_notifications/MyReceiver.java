package com.example.caro.push_notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.caro.GameBoardActivity;
import com.example.caro.MainActivity;
import com.example.caro.Model.GameBoard;
import com.example.caro.Model.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyReceiver extends BroadcastReceiver {

    public static String FIREBASE_CLOUD_FUNCTIONS_BASE = "https://us-central1-caro-android.cloudfunctions.net";
    private static final String LOG_TAG = "MyReceiver";
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserId = currentUser.getUid();
        Log.e("UserId", currentUserId);
        if (intent != null){
            try{
                /*String withId = intent.getExtras().getString("fromId");
                String to = intent.getExtras().getString("fromPushId");
                String fromName = intent.getExtras().getString("fromName");
                if (intent.getAction().equals("accept")) {
                    String gameId = withId + "-" + currentUserId;
                    Log.e("gameID", gameId);
*//*
                    FirebaseDatabase.getInstance().getReference().child("games")
                            .child(gameId)
                            .setValue(null);
*//*
                    Log.e("log_tag", LOG_TAG);
                    Intent resultIntent = new Intent(context, GameBoardActivity.class);
                    resultIntent.putExtra("type", "online")
                            .putExtra("fromId", gameId)
                            .putExtra("fromPushId", gameId)
                            .putExtra("fromName", fromName);
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(resultIntent);
                }*/
                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(currentUserId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Player player = dataSnapshot.getValue(Player.class);
                                OkHttpClient client = new OkHttpClient();
                                String withId = intent.getExtras().getString("fromId");
                                String to = intent.getExtras().getString("fromPushId");
                                String fromName = intent.getExtras().getString("fromName");
                                //Log.e("Action", intent.getAction());

                                String format = String
                                        .format("%s/sendNotification?to=%s&fromPushId=%s&fromId=%s&fromName=%s&type=%s",
                                                FIREBASE_CLOUD_FUNCTIONS_BASE,
                                                to,
                                                player.getTokenId(),
                                                currentUser.getUid(),
                                                player.getName(),
                                                intent.getAction());

                                Request request = new Request.Builder().url(format).build();

                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                    }

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                                    }
                                });

                                //Log.e("Action", intent.getAction());

                                if (intent.getAction().equals("accept")){
                                   String gameId = withId + "-" + currentUserId;
                                   //Log.e("gameID", gameId);

                                    FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("currentPlayer").setValue(fromName);

                                    //Log.e("log_tag", LOG_TAG);
                                    Intent resultIntent = new Intent(context, GameBoardActivity.class);
                                    resultIntent.putExtra("type", "online")
                                            /*.putExtra("fromId", gameId)
                                            .putExtra("fromPushId", gameId)
                                            .putExtra("fromName", fromName);*/
                                                .putExtra("gameId", gameId)
                                                .putExtra("playerName", player.getName())
                                                .putExtra("fromName", fromName)
                                                .putExtra("me", "o");
                                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(resultIntent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
