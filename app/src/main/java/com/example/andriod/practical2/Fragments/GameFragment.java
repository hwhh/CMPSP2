package com.example.andriod.practical2.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andriod.practical2.Logic.Board;
import com.example.andriod.practical2.Logic.Game;
import com.example.andriod.practical2.R;
import com.example.andriod.practical2.Views.GameView;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class GameFragment extends Fragment{


    private GameView gameView;
    private Board board;
    private Game game;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        game = new Game( false);
        board  = new Board(Game.Colour.RED, game);
        gameView = (GameView) inflater.inflate(R.layout.game_fragment, container, false);
        board.addObserver(gameView);
        game.addObserver(gameView);
        return gameView;
    }

    public Board getBoard() {
        return board;
    }

    public Game getGame() {
        return game;
    }

    public void setBoard(Board board) {
        this.board = board;
        board.addObserver(gameView);
        gameView.clearCanvas();

    }

    public void setGame(Game game) {
        this.game = game;
        game.addObserver(gameView);
        gameView.clearCanvas();
    }

    public void makeBoardMove(int x){
        if (board.makeMove(x)){
            saveBoard();
        }
    }

    public void saveBoard(){
        SharedPreferences mPrefs=getContext().getSharedPreferences(getContext().getApplicationInfo().name, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=mPrefs.edit();
        Gson gson = new Gson();
        ed.putString("board", gson.toJson(board.getBoard()));
        ed.putString("turn", gson.toJson(board.getTurn()));
        ed.putString("xCord", gson.toJson(board.getxCord()));
        ed.putString("yCord", gson.toJson(board.getyCord()));

        ed.putString("gameWon", gson.toJson(game.isWon()));
        ed.putString("gameWinner", gson.toJson(game.getWinner()));

        ed.commit();//TODO Change this



    }


}


