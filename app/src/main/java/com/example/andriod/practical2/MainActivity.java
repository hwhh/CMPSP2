package com.example.andriod.practical2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.andriod.practical2.Fragments.GameFragment;
import com.example.andriod.practical2.Fragments.HomeFragment;
import com.example.andriod.practical2.Logic.Board;
import com.example.andriod.practical2.Logic.Game;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class MainActivity extends AppCompatActivity {

    GameFragment gameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            HomeFragment firstFragment = new HomeFragment();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }

    }

    public void onNewGame(View v) {
        Bundle args = new Bundle();
        gameFragment = (GameFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment);
        //Tablet mode
        if (gameFragment != null) {
            gameFragment.setArguments(args);
            Game newGame = new Game(false);
            Board newBoard = new Board(Game.Colour.RED, newGame);
            gameFragment.setBoard(newBoard);
            gameFragment.setGame(newGame);
        } else {
            GameFragment newFragment = new GameFragment();
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment, "new_fragment");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


    public void onLoadGame(View v){
        Gson gson = new Gson();
        JsonParser parser=new JsonParser();
        SharedPreferences mPrefs= this.getSharedPreferences(this.getApplicationInfo().name, Context.MODE_PRIVATE);
        Game game = loadGame(gson, parser, mPrefs);
        Board board = loadBoard(gson, parser, mPrefs, game);

        Bundle args = new Bundle();
        gameFragment = (GameFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment);
        if (gameFragment != null) {
            gameFragment.setArguments(args);
            gameFragment.setBoard(board);
            gameFragment.setGame(board.getGame());
            gameFragment.loadGame();
        } else {
            GameFragment newFragment = new GameFragment();
            args.putParcelable("game", game);
            args.putParcelable("board", board);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment, "new_fragment");
            transaction.addToBackStack(null);
            transaction.commit();


        }
    }


    public Board loadBoard(Gson gson, JsonParser parser, SharedPreferences mPrefs, Game game){
        JsonArray JsonBoard = parser.parse(mPrefs.getString("board", null)).getAsJsonArray();
        Game.Colour[][] gameBoard = new Game.Colour[Board.ROWS][Board.COLUMNS];
        int i = 0;
        for (JsonElement jsonElement : JsonBoard) {
            gameBoard[i++] =  gson.fromJson(jsonElement, Game.Colour[].class);
        }
        String turn = parser.parse(mPrefs.getString("turn", null)).getAsString();
        JsonPrimitive xCord = parser.parse(mPrefs.getString("xCord", null)).getAsJsonPrimitive();
        JsonPrimitive yCord = parser.parse(mPrefs.getString("yCord", null)).getAsJsonPrimitive();
        return new Board(turn.equals("YELLOW") ? Game.Colour.YELLOW : Game.Colour.RED, gameBoard, game, xCord.getAsInt(), yCord.getAsInt());
    }

    public Game loadGame(Gson gson, JsonParser parser, SharedPreferences mPrefs){
        JsonPrimitive gameWon = parser.parse(mPrefs.getString("gameWon", null)).getAsJsonPrimitive();
        JsonObject gameWinner = null;
        try {
            gameWinner = parser.parse(mPrefs.getString("gameWinner", null)).getAsJsonObject();
        }catch (Exception ignored){}
        return new Game(gson.fromJson(gameWinner, Game.Colour.class), gameWon.getAsBoolean());
    }




}
