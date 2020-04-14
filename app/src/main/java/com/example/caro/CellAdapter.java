package com.example.caro;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.caro.Model.Cell;
import com.example.caro.Model.GameBoard;
import com.example.caro.Model.Player;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;
import java.util.logging.Handler;

public class CellAdapter extends BaseAdapter {
    private Context context;
    private List<Cell> listCell;
    private GameBoard gameBoard;
    private Player P1, P2;
    private String gameId;
    public static int height_item;

    public CellAdapter(Context context, List<Cell> listCell, GameBoard gameBoard, String gameId) {
        this.context = context;
        this.listCell = listCell;
        //P1 = new Player("P1", "x");
        //P2 = new Player("P2", "o");
        this.gameBoard = gameBoard;
        this.gameId = gameId;
    }

    @Override
    public int getCount() {
        return listCell.size();
    }

    @Override
    public Object getItem(int i) {
        return listCell.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        //ViewHolder viewHolder = null;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view_item = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
        final TextView textView = (TextView) view_item.findViewById(R.id.item);
        textView.setHeight(GameBoardActivity.height_item);
        textView.setWidth(GameBoardActivity.width_item);
        //textView.setText(listCell.get(i).getName());
        //final int numberOfColumns = 15;
        //final int numberOfRows = (int) Math.ceil(listCell.size() / numberOfColumns);
       /* DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;*/
      /*  textView.getLayoutParams().height = MainActivity.width_screen / numberOfColumns;
        height_item = MainActivity.width_screen/numberOfColumns;
        MainActivity.numberRows = 500/height_item;*/

        final int row = position/GameBoardActivity.numberColumns;//(int) Math.ceil((i + 1) / numberOfColumns);
        final int column = position%GameBoardActivity.numberColumns;//((row * numberOfColumns) - i);

        Cell cell = listCell.get(position);
        textView.setText(cell.getValue());
        /*view_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*FirebaseDatabase.getInstance().getReference().child("games").child(gameId).child("value")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() == null){
                                    return;
                                }
                                String value = dataSnapshot.getValue(String.class);
                                textView.setText(value);
                                Log.e("Values", "O");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });*//*

                //final int row = i/GameBoardActivity.numberColumns;//(int) Math.ceil((i + 1) / numberOfColumns);
                //final int column = i%GameBoardActivity.numberColumns;//((row * numberOfColumns) - i);
                if (gameBoard.getCells()[column][row] == null || gameBoard.getCells()[column][row].getValue().equals("")){
                    //Cell cell = new Cell(null,column, row);
                    //String str;
                    Cell cell;
                    if (gameBoard.getCurrentPlayer().getStatus() == "x"){
                        cell = new Cell("x", column, row);
                    }
                    else{
                        //str = "o";
                        cell = new Cell("o", column, row);
                    }
                    //textView.setText(str);
                    //Cell cell = new Cell(str, column, row);
                    gameBoard.getCells()[column][row] = cell;
                    textView.setText(cell.getValue());
                    FirebaseDatabase.getInstance().getReference().child("games").child(gameId).setValue(cell);
                    gameBoard.getCells()[column][row] = cell;
                    if (gameBoard.isEndGame(column, row)){
                        Toast.makeText(context,"Winner: " + gameBoard.getCurrentPlayer().getName(), Toast.LENGTH_LONG).show();
                        resetGame();
                        FirebaseDatabase.getInstance().getReference().child("games").child(gameId).setValue(new Cell("x", 0, 0));
                    }
                    else{
                        gameBoard.switchPlayer();
                    }
                }
            }
        });*/

        /*FirebaseDatabase.getInstance().getReference().child("games").child(gameId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                       *//* if (dataSnapshot.getValue() == null){
                            return;
                        }
                        //String value = dataSnapshot.getValue(String.class);
                        Cell cell = dataSnapshot.getValue(Cell.class);
                        Log.e("cellAdapter", "O");*//*
                        ///gameBoard.getCells()[column][row] = cell;
                        //cellList.set(cell.getColumn() * numberColumns + cell.getRow(), cell);
                        //cellAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.getValue() == null){
                            return;
                        }
                        Cell cell = dataSnapshot.getValue(Cell.class);
                        textView.setText(cell.getValue());
                        //gameBoard.getCells()[column][row] = cell;
                        //String value = dataSnapshot.getValue(String.class);
                        //Log.e("getValues", "O");
                        //cellList.set(cell.getColumn() * numberColumns + cell.getRow(), cell);
                            //cellAdapter.notifyDataSetChanged();
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
*/


        return view_item;
    }

   /* public void onClickAtCell(int r, int c){
        if (gameBoard.getCells()[r][c] == null){
            Cell cell = new Cell(r, c);
            String str;
            if (gameBoard.getCurrentPlayer().getStatus() == "x"){
                str = "x";
            }
            else{
                str = "o";
            }
        }
    }*/


    public void resetGame(){
        gameBoard.reset();
        listCell.clear();
        for (int i = 0; i <(int ) GameBoardActivity.numberColumns; i++) {
            for (int j = 0; j < (int) GameBoardActivity.numberRows; j ++) {
                gameBoard.getCells()[i][j] = new Cell("", i, j);
                listCell.add(gameBoard.getCells()[i][j]);
            }
        }
        notifyDataSetChanged();
    }

    /*public boolean horizontalCells(int x, int y) {
        int  sequentCells = 1;
        String status = gameBoard.getCurrentPlayer().getStatus();

        //check left
        //Toast.makeText(context,"Winnervvdvir: " + gameBoard.cells[x][y].getValue(), Toast.LENGTH_LONG).show();
        for (int i = 1; i <= 4; i++){
            Cell cell = gameBoard.getCells()[x-i][y];
            if (x - i < 0) {
                break;
            }
            //Toast.makeText(context,"Winnervvdvir: " + gameBoard.cells[x][y].getValue(), Toast.LENGTH_LONG).show();
            else {
                if (cell!=null && cell.getValue().equals(status)) {
                    sequentCells++;
                } else if (cell.getValue() != status){
                    break;
                }

                if (sequentCells >= 5) {
                    return true;
                }
            }
        }

        //check right
        for (int i = 1; i <= 4; i++){
            if (x + i > gameBoard.BOARD_WIDTH){
                break;
            }
            else {
                if (gameBoard.getCells()[x + i][y].getValue().equals(status)) {
                    sequentCells++;
                } else {
                    break;
                }

                if (sequentCells >= 5) {
                    return true;
                }
            }
        }
        return false;
    }*/

    private class ViewHolder{
        TextView textView;
    }
}
