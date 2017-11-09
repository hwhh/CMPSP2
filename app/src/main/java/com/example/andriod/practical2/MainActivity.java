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
    //TODO read ​(review), ​update ​(play), ​and ​delete ​(restart)
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
            // If the frag is not available, we're in the one-pane layout and must swap frags...
            // Create fragment and give it an argument for the selected article
            GameFragment newFragment = new GameFragment();
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment, "some_frag");
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
//
    }

    //TODO Refactor into one function
    public void onLoadGame(View v){
        Gson gson = new Gson();
        JsonParser parser=new JsonParser();
        SharedPreferences mPrefs= this.getSharedPreferences(this.getApplicationInfo().name, Context.MODE_PRIVATE);

        JsonPrimitive gameWon = parser.parse(mPrefs.getString("gameWon", null)).getAsJsonPrimitive();
        JsonObject gameWinner = null;
        try {
            gameWinner = parser.parse(mPrefs.getString("gameWinner", null)).getAsJsonObject();
        }catch (Exception ignored){}

        JsonArray JsonBoard = parser.parse(mPrefs.getString("board", null)).getAsJsonArray();
        Game.Colour[][] gameBoard = new Game.Colour[Board.ROWS][Board.COLUMNS];
        int i = 0;
        for (JsonElement jsonElement : JsonBoard) {
            gameBoard[i++] =  gson.fromJson(jsonElement, Game.Colour[].class);
        }
        String turn = parser.parse(mPrefs.getString("turn", null)).getAsString();
        JsonPrimitive xCord = parser.parse(mPrefs.getString("xCord", null)).getAsJsonPrimitive();
        JsonPrimitive yCord = parser.parse(mPrefs.getString("yCord", null)).getAsJsonPrimitive();

        Game game = new Game(gson.fromJson(gameWinner, Game.Colour.class), gameWon.getAsBoolean());
        Board board = new Board(turn.equals("YELLOW") ? Game.Colour.YELLOW : Game.Colour.RED, gameBoard, game, xCord.getAsInt(), yCord.getAsInt());

        Bundle args = new Bundle();
        gameFragment = (GameFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment);
        if (gameFragment != null) {
            gameFragment.setArguments(args);
            gameFragment.setBoard(board);
            gameFragment.setGame(board.getGame());
        } else {

            GameFragment newFragment = new GameFragment(game, board);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment, "some_frag");
            transaction.addToBackStack(null);
            transaction.commit();

        }

    }




}
