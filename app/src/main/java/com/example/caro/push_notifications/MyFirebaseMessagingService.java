package com.example.caro.push_notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.strictmode.IntentReceiverLeakedViolation;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.caro.GameBoardActivity;
import com.example.caro.MainActivity;
import com.example.caro.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import static androidx.core.app.NotificationCompat.PRIORITY_MAX;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String INVITE = "invite";
    private static final String LOG_TAG = "MyFirebaseMessaging";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String fromPushId = remoteMessage.getData().get("fromPushId");
        final String fromId = remoteMessage.getData().get("fromId");
        final String fromName = remoteMessage.getData().get("fromName");
        String type = remoteMessage.getData().get("type");
        //Log.e("Type", type);

        //need to collapse
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserId = currentUser.getUid();

        if (type.equals("invite")){
            handleInviteEvent(fromPushId, fromId, fromName);
        }
        else if (type.equals("accept")){
            FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String playerName = dataSnapshot.getValue(String.class);
                        Intent intent = new Intent(getApplicationContext(), GameBoardActivity.class);
                        Log.e("LOG_TAG", LOG_TAG);
                        intent.putExtra("type", "online")
                                .putExtra("gameId", currentUserId + "-" + fromId)
                                .putExtra("playerName", playerName)
                                .putExtra("fromName", fromName)
                                .putExtra("me", "x");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        }
        else if (type.equals("reject")){
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "invite")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setPriority(PRIORITY_MAX)
                    .setContentTitle(fromName + " rejected your invite!");

            Notification notification = mBuilder.build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Intent resultIntent = new Intent(this, GameBoardActivity.class);
            resultIntent.putExtra("type", "online");
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);

            if (notificationManager == null) {
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel("reject", "reject", importance);
                notificationManager.createNotificationChannel(mChannel);
            }
            notificationManager.notify(1, notification);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("TokenId", token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = firebaseUser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("users");
        myRef.child(currentUserId).child("tokenId").setValue(token);
    }

    private void handleInviteEvent(String fromPushId, String fromId, String fromName) {
        Intent rejectIntent = new Intent(getApplicationContext(), MyReceiver.class);
        rejectIntent.setAction("reject");
        rejectIntent.putExtra("fromId", fromId);
        rejectIntent.putExtra("fromPushId", fromPushId);
        rejectIntent.putExtra("fromName", fromName);
        PendingIntent rejectPendingIntent = PendingIntent.getBroadcast(this, 0, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent acceptIntent = new Intent(getApplicationContext(), MyReceiver.class);
        acceptIntent.setAction("accept");
        acceptIntent.putExtra("fromId",fromId);
        acceptIntent.putExtra("fromPushId", fromPushId);
        acceptIntent.putExtra("fromName", fromName);
        //acceptIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent resultIntent = new Intent(this, GameBoardActivity.class);
        resultIntent.putExtra("type", "online")
                    .putExtra("fromId", fromId)
                    .putExtra("fromPushId", fromPushId)
                    .putExtra("fromName", fromName);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int notificationId = new Random().nextInt();

        String channelId = getString(R.string.channel_id);
        NotificationCompat.Builder build = new NotificationCompat.Builder(this, INVITE)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(PRIORITY_MAX)
                .setContentTitle(fromName + " invites you to play!")
                .addAction(R.drawable.accept, "Accept",acceptPendingIntent)
                .setVibrate(new long[3000])
                .setChannelId(INVITE)
                .addAction(R.drawable.reject, "Reject", rejectPendingIntent)
                .setContentIntent(resultPendingIntent);

        Notification notification = build.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(INVITE, INVITE, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(notificationId, notification);
    }

}
