package com.example.andriod.practical2.Logic;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import java.util.Observable;

public class Game extends Observable implements Parcelable{


    private Colour winner;
    private boolean won;


    public Game(boolean won) {
        this.won = won;
    }

    public Game(@Nullable Colour winner, boolean won) {
        this.winner = winner;
        this.won = won;
    }

    protected Game(Parcel in) {
        won = in.readByte() != 0;
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public Colour getWinner() {
        return winner;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(Colour colour) {
        this.won = true;
        winner = colour;
        setChanged();
        notifyObservers(won);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (won ? 1 : 0));
    }


    public enum Colour{
         RED,
         YELLOW
    }


    public static Colour switchTurn(Colour turn){
        if(turn == Colour.RED)
            return Colour.YELLOW;
        else
            return Colour.RED;
    }

}
