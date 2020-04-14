package com.example.caro;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.DataRemovalRequest;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caro.Model.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {
    public static String FIREBASE_CLOUD_FUNCTIONS_BASE = "https://us-central1-caro-android.cloudfunctions.net";
    private List<Player>  mPlayers ;//= Collections.emptyList();
    private Context mContext;
    private LayoutInflater layoutInflater;

    public PlayerAdapter(Context mContext, List<Player> mPlayers) {
        this.mContext = mContext;
        this.layoutInflater =LayoutInflater.from(mContext);
        this.mPlayers = mPlayers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View playerView =layoutInflater.inflate(R.layout.player_item, parent, false);
        return new ViewHolder(playerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = mPlayers.get(position);
        holder.tvName.setText(player.getName());

    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private Button btnInvite;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            btnInvite = (Button) itemView.findViewById(R.id.btnInvite);
            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            final String currentUserId = firebaseUser.getUid();

            btnInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                               DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
            myRef.child("users").child(currentUserId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Player me = dataSnapshot.getValue(Player.class);
                            OkHttpClient client = new OkHttpClient();

                            Player player = mPlayers.get(getLayoutPosition());
                            //Log.e("Name", player.getName());
                            String to = player.getTokenId();

                            String format = String
                                    .format("%s/sendNotification?to=%s&fromPushId=%s&fromId=%s&fromName=%s&type=%s",
                                            FIREBASE_CLOUD_FUNCTIONS_BASE,
                                            to,
                                            me.getTokenId(),
                                            currentUserId,
                                            me.getName(),
                                            "invite");
                            Request request = new Request.Builder().url(format).build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    });

        }

        public void bindView(Player player){
            String name = player.getName();
            tvName.setText(name);
        }
    }
}
