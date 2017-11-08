package com.example.andriod.practical2.Logic;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {

    public Board board;
    private Colour winner;
    private boolean won;


    public Game(Board board, boolean won) {
        this.board = board;
        this.won = won;
    }


    protected Game(Parcel in) {
        board = in.readParcelable(Board.class.getClassLoader());
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

    public Board getBoard() {
        return board;
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
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(board, flags);
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

    public boolean saveGame(){
        return true;
    }

    public boolean loadGame(){
        return true;
    }



}
