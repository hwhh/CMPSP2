package com.example.andriod.practical2.Logic;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import java.util.Observable;

public class Game extends Observable{


    private Colour winner;
    private boolean won;


    public Game(boolean won) {
        this.won = won;
    }

    public Game(@Nullable Colour winner, boolean won) {
        this.winner = winner;
        this.won = won;
    }

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
