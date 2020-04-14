package com.example.caro.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "player_table")
public class Player  implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @Ignore
    private String status;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "tokenId")
    private String tokenId;

    @ColumnInfo(name = "onOff")
    private int onOff;
    @Ignore
    public boolean isAuthenticated;
    @Ignore
    boolean isNew, isCreated;


    public Player(){}

    @Ignore
    public Player(int  id, String name, String password, String email, String tokenId, int onOff) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.tokenId = tokenId;
        this.onOff = onOff;
    }

    public Player(String name, String status) {
        this.name = name;
        this.status = status;
        //this.cell = cell;
    }

   /* public Cell getCell() {
        return cell;
    }*/

    public static final Parcelable.Creator<Player>  CREATOR = new Parcelable.Creator<Player>(){

        @Override
        public Player createFromParcel(Parcel parcel) {
            return new Player(parcel);
        }

        @Override
        public Player[] newArray(int i) {
            return new Player[i];
        }
    };

    private Player(Parcel parcel) {
        id = parcel.readInt();
        name = parcel.readString();
        password = parcel.readString();
        email = parcel.readString();
        tokenId = parcel.readString();
        onOff = parcel.readInt();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(password);
        parcel.writeString(email);
        parcel.writeString(tokenId);
        parcel.writeInt(onOff);
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getPassword(){
        return this.password;
    }

    public String getEmail() {
        return email;
    }

    public String getTokenId() {
        return tokenId;
    }

    public int getOnOff(){
        return this.onOff;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }


    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setOnOff(int onOff){
        this.onOff = onOff;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
