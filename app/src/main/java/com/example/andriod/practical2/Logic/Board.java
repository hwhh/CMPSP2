package com.example.andriod.practical2.Logic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Observable;

public class Board extends Observable implements Parcelable {

    public static final int COLUMNS = 7, ROWS = 6;

    private Game.Colour turn;
    private Game.Colour[][] board;
    private Game game;

    public Board(Game.Colour turn) {
        this.turn = turn;
        this.board = new Game.Colour[ROWS][COLUMNS];
    }

    protected Board(Parcel in) {
    }

    public static final Creator<Board> CREATOR = new Creator<Board>() {
        @Override
        public Board createFromParcel(Parcel in) {
            return new Board(in);
        }

        @Override
        public Board[] newArray(int size) {
            return new Board[size];
        }
    };

    public void setGame(Game game) {
        this.game = game;
    }

    public Game.Colour getTurn() {
        return turn;
    }

    public Game.Colour[][] getBoard() {
        return board;
    }

    //TODO refactor to make single for loop which takes different parameters
    public boolean checkWin(Game.Colour colour) {
        // horizontalCheck
        for (int j = 0; j < COLUMNS - 3; j++) {
            for (int i = 0; i < ROWS; i++) {
                if (board[i][j] == colour && board[i][j + 1] == colour && this.board[i][j + 2] == colour && this.board[i][j + 3] == colour) {
                    return true;
                }
            }
        }
        // verticalCheck
        for (int i = 0; i < ROWS - 3; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (board[i][j] == colour && this.board[i + 1][j] == colour && this.board[i + 2][j] == colour && this.board[i + 3][j] == colour) {
                    return true;
                }
            }
        }
        // ascendingDiagonalCheck
        for (int i = 3; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS - 3; j++) {
                if (board[i][j] == colour && this.board[i - 1][j + 1] == colour && this.board[i - 2][j + 2] == colour && this.board[i - 3][j + 3] == colour)
                    return true;
            }
        }
        // descendingDiagonalCheck
        for (int i = 3; i < ROWS; i++) {
            for (int j = 3; j < COLUMNS; j++) {
                if (board[i][j] == colour && this.board[i - 1][j - 1] == colour && this.board[i - 2][j - 2] == colour && this.board[i - 3][j - 3] == colour)
                    return true;
            }
        }
        return false;
    }


    public boolean makeMove(int xCord, Game.Colour colour) {
        if(board[ROWS-1][xCord] != null || game.isWon())
            return false;
        else {
            boolean validMove = false;
            if(board[0][xCord] == null) {
                board[0][xCord] = colour;
                validMove = true;
            }else {
                for (int y = 0; y < ROWS-1; y++) {
                    if (board[y][xCord] == null) {
                        board[y][xCord] = colour;
                        validMove = true;
                        break;
                    }
                }

            }
            if(checkWin(turn))
                game.setWon(colour);
            turn = Game.switchTurn(turn);
            return validMove;
        }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
