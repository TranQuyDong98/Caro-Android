package com.example.caro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caro.Model.Cell;
import com.example.caro.Model.GameBoard;
import com.example.caro.ViewModel.GameBoardViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameBoardActivity extends AppCompatActivity {

    TextView playerName1, playerName2, score;
    GridView gridView;
    CellAdapter cellAdapter;
    ArrayList<Cell> cellList;
    public static int height;
    public static int width;
    public static int numberRows;
    public static int width_item;
    public static int height_item;
    public static int numberColumns = 15;
    private GameBoardViewModel gameBoardViewModel;
    private GameBoard gameBoard;
    private String gameId;
    private int height_relativeLayout;
    private int scorePlayer1 = 0, scorePlayer2 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        gridView = (GridView)findViewById(R.id.gridview);
        gridView.setNumColumns(numberColumns);
        playerName1 = (TextView)findViewById(R.id.player1);
        playerName2 = (TextView) findViewById(R.id.player2);
        score = (TextView)findViewById(R.id.score);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width_screen_px = (int)displayMetrics.widthPixels;
        final int height_screen_px = (int)displayMetrics.heightPixels;
       /* Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width_screen_px = (int)size.x;
        int height_screen_px = (int)size.y;
        float density = displayMetrics.density;*/
        //int width_screen = (int)((width_screen_px*160) / density);
        //int height_screen = (int)((height_screen_px*160) / density);
        /*Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width_screen = (int)size.x;
        int height_screen = (int)size.y;*/
        //final int height_relativeLayout;
      /*  final RelativeLayout layout = (RelativeLayout) findViewById(R.id.relative1);
        final ViewTreeObserver observer= layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        height_relativeLayout = (int)layout.getHeight();
                        //Log.d("Log", "Height: " + layout.getHeight());
                    }
                });
*/
        //if (Build.VERSION.SDK_INT >= 16) {

        width_item = (int )(width_screen_px/* - 14*convertDpToPixels(gridView.getHorizontalSpacing(), this)*/) / numberColumns;
        //}
        height_item = width_item;

        numberRows = (int) (height_screen_px/* + convertDpToPixels(gridView.getHorizontalSpacing(), this)*/) / (height_item /*+ convertDpToPixels(gridView.getVerticalSpacing(), this)*/);
        //gameBoardViewModel = ViewModelProviders.of(this).get(GameBoardViewModel.class);

        Bundle extras = getIntent().getExtras();
        String type = extras.getString("type");
        //Log.e("Online", type);
        if (type.contains("online")){
            gameId = extras.getString("gameId");
            Log.e("gameID", gameId);
            final String playerName = extras.getString("playerName");
            final String fromName = extras.getString("fromName");
            String me = extras.getString("me");
            if (me.equals("x")){
                playerName1.setText(playerName);
                playerName2.setText(fromName);
                gameBoard = new GameBoard(playerName, fromName);
            }
            else if (me.equals("o")){
                playerName1.setText(fromName);
                playerName2.setText(playerName);
                gameBoard = new GameBoard(fromName, playerName);
            }
            FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("cells").setValue(null);
            //FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("score").setValue(null);

            cellList = new ArrayList<>();
            cellAdapter = new CellAdapter(this, cellList, gameBoard, gameId);
            gridView.setAdapter(cellAdapter);
            addData();

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    final int column= position/GameBoardActivity.numberColumns;//(int) Math.ceil((i + 1) / numberOfColumns);
                    final int row = position%GameBoardActivity.numberColumns;//((row * numberOfColumns) - i);

                    final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("currentPlayer")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() == null){
                                        return;
                                    }
                                    final String currentPlayer = dataSnapshot.getValue(String.class);

                                    FirebaseDatabase.getInstance().getReference().child("users")
                                            .child(currentUserId).child("name")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.getValue() == null){
                                                        return;
                                                    }
                                                    String name = dataSnapshot.getValue(String.class);
                                                    if (name.equals(currentPlayer)){
                                                        if (gameBoard.getCells()[column][row] == null || gameBoard.getCells()[column][row].getValue().equals("")) {
                                                            Cell cell;
                                                            if (gameBoard.getCurrentPlayer().getStatus() == "x") {
                                                                cell = new Cell("x", column, row);
                                                            } else {
                                                                cell = new Cell("o", column, row);
                                                            }
                                                            FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("cells").child(column + "_" + row).setValue(cell.getValue());
                                                            gameBoard.switchPlayer();
                                                            FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("currentPlayer")
                                                                    .setValue(gameBoard.getCurrentPlayer().getName());
                                                            gameBoard.switchPlayer();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                   /* if (currentPlayer.equals(name)){
                                        if (gameBoard.getCells()[column][row] == null || gameBoard.getCells()[column][row].getValue().equals("")) {
                                            Cell cell;
                                            if (gameBoard.getCurrentPlayer().getStatus() == "x") {
                                                cell = new Cell("x", column, row);
                                            } else {
                                                cell = new Cell("o", column, row);
                                            }
                                            FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("cells").child(column + "_" + row).setValue(cell.getValue());
                                            //gameBoard.switchPlayer();
                                            FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("currentPlayer")
                                                    .setValue(gameBoard.getCurrentPlayer().getName());
                                        }
                                    }*/
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            });

            FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("cells")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            if (dataSnapshot.getValue() == null){
                                return;
                            }
                            String key = dataSnapshot.getKey();
                            String[] str = key.split("_");
                            int column = Integer.parseInt(str[0]);
                            int row = Integer.parseInt(str[1]);
                            String value = dataSnapshot.getValue(String.class);
                            Cell cell = new Cell(value, column, row);
                            cellList.set(column * numberColumns + row, cell);
                            cellAdapter.notifyDataSetChanged();
                            gameBoard.getCells()[column][row] = cell;
                            if (gameBoard.isEndGame(column, row)) {
                                showWinner();
                                /*resetGame();
                                //FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("score").setValue()
                                FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("cells").setValue(null);
                                FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("currentPlayer")
                                        .setValue(playerName1.getText().toString());*/
                            }
                            else {
                                gameBoard.switchPlayer();
                            }

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                           /* if (dataSnapshot.getValue() == null){
                                return;
                            }*/
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void addData() {
        for (int i = 0; i < numberColumns; i++) {
            for (int j = 0; j < numberRows; j ++) {
                gameBoard.getCells()[i][j] = new Cell("", i, j);
                cellList.add(gameBoard.getCells()[i][j]);
            }
        }
        cellAdapter.notifyDataSetChanged();
    }


    private void resetGame(){
        gameBoard.reset();
        cellList.clear();
        for (int i = 0; i <(int ) GameBoardActivity.numberColumns; i++) {
            for (int j = 0; j < (int) GameBoardActivity.numberRows; j ++) {
                gameBoard.getCells()[i][j] = new Cell("", i, j);
                cellList.add(gameBoard.getCells()[i][j]);
            }
        }
        cellAdapter.notifyDataSetChanged();
    }

    private void showWinner(){
        FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("currentPlayer")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null){
                            return;
                        }
                        String currentPlayer = dataSnapshot.getValue(String.class);
                        String winner = "";
                        if (!playerName1.getText().toString().equals(currentPlayer)){
                            winner = playerName1.getText().toString();
                            scorePlayer1 ++;
                        }
                        else if (!playerName2.getText().toString().equals(currentPlayer)){
                            winner = playerName2.getText().toString();
                            scorePlayer2 ++;
                        }

                        score.setText(String.valueOf(scorePlayer1) + ":" + String.valueOf(scorePlayer2));

                        AlertDialog.Builder builder = new AlertDialog.Builder(GameBoardActivity.this);
                        builder.setTitle("RESULT!");
                        builder.setIcon(R.mipmap.ic_launcher);

                        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                resetGame();
                                //FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("score").setValue()
                                FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("cells").setValue(null);
                                FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("currentPlayer")
                                        .setValue(playerName1.getText().toString());
                            }
                        });

                        builder.create().show();
                        //Toast.makeText(GameBoardActivity.this, "Winner: " + winner, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public static int convertDpToPixels(float dp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
        return px;
    }
}
