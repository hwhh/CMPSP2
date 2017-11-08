package com.example.andriod.practical2.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andriod.practical2.Logic.Board;
import com.example.andriod.practical2.Logic.Game;
import com.example.andriod.practical2.R;
import com.example.andriod.practical2.Views.GameView;


public class GameFragment extends Fragment {



    private Board board;
    private Game game;

    GameView gameView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        gameView = new GameView(getContext(), null);
        setGameData();
        return inflater.inflate(R.layout.game_fragment, container, false);
    }

    public void setGameData(){
        board = new Board(Game.Colour.RED);
        game = new Game(board, false);
        board.setGame(game);
        Bundle b  =new Bundle();
        b.putParcelable("board", board);
        b.putParcelable("game", game);
        gameView.setData(b);
    }

    public void updateGameView() {
        setGameData();
    }
}
