package com.example.andriod.practical2.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.andriod.practical2.Logic.Board;
import com.example.andriod.practical2.Logic.Game;


public class GameView extends View {

    Paint yellowCircle, redCircle, winner;
    float width, height, circleRadius;
    float xCord, yCord;
    public static Board board;
    public static Game game;

    public static Bundle data;


    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public void init() {
        yellowCircle = new Paint();
        yellowCircle.setStyle(Paint.Style.FILL);
        yellowCircle.setColor(Color.YELLOW);

        redCircle = new Paint();
        redCircle.setStyle(Paint.Style.FILL);
        redCircle.setColor(Color.RED);

        winner = new Paint();

    }

    public void setData(Bundle data) {
        game = (Game) data.get("game");
        board = (Board) data.get("board");
        this.invalidate();
        this.requestLayout();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());
        width = ((float) w - xpad); //TODO change to const
        height = ((float) h - ypad);
        circleRadius = Math.min(((width / (Board.COLUMNS+1)) / 2), (height / (Board.ROWS+1)) / 2);
        width = width - (circleRadius * 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(), y = event.getY();

        xCord = (float) (Math.floor((x / ((width) / 6))) * ((width) / 6) * 2);//TODO change to const and add offset for circle size
        yCord = y;

        if ((screenToCell(x) < Board.COLUMNS) && board.makeMove(screenToCell(x), board.getTurn())) {
            this.invalidate();
            this.requestLayout();
        }

        return super.onTouchEvent(event);
    }

    public int screenToCell(float xCord) {
        return (int) Math.floor((xCord / ((width) / 6)));
    }

    public float cellXToScreen(int x) {
        return (x * ((width) / 12) * 2) + circleRadius;
    }

    public float cellYToScreen(int y) {
        return ((height - circleRadius) + ((-y) * (height / 6)));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < Board.ROWS; i++) {
            for (int j = 0; j < Board.COLUMNS; j++) {
                if (board.getBoard()[i][j] == Game.Colour.RED) {
                    canvas.drawCircle(cellXToScreen(j), cellYToScreen(i), circleRadius, redCircle);
                } else if (board.getBoard()[i][j] == Game.Colour.YELLOW) {
                    canvas.drawCircle(cellXToScreen(j), cellYToScreen(i), circleRadius, yellowCircle);
                }
            }
        }
        if (game.isWon()) {
            winner.setTextSize(100);
            winner.setColor(game.getWinner() == Game.Colour.RED ? Color.RED : Color.YELLOW);
            canvas.drawText((game.getWinner().toString() + " WON"), (getWidth() / 2)-200, (400), winner);
        }
    }



}
