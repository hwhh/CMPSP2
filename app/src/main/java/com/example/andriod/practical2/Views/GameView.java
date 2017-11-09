package com.example.andriod.practical2.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.andriod.practical2.Fragments.GameFragment;
import com.example.andriod.practical2.Logic.Board;
import com.example.andriod.practical2.Logic.Game;
import com.example.andriod.practical2.R;

import java.util.Observer;


public class GameView extends View implements Observer{

    Paint yellowCircle, redCircle, winner, ditherPaint, line;
    float width, height, circleRadius;
    float xCord, yCord;

    Bitmap b;
    Canvas c;


    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public void clearCanvas(){
        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        this.invalidate();
        this.requestLayout();
    }

    public void init() {
        yellowCircle = new Paint();
        yellowCircle.setStyle(Paint.Style.FILL);
        yellowCircle.setColor(Color.YELLOW);

        redCircle = new Paint();
        redCircle.setStyle(Paint.Style.FILL);
        redCircle.setColor(Color.RED);

        winner = new Paint();

        ditherPaint = new Paint(Paint.DITHER_FLAG);

        line = new Paint();
        line.setColor(Color.BLACK);
        line.setStrokeWidth(10);
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
        b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        c = new Canvas(b);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(), y = event.getY();

        xCord = (float) (Math.floor((x / ((width) / 6))) * ((width) / 6) * 2);//TODO change to const and add offset for circle size
        yCord = y;

        if ((screenToCell(x) < Board.COLUMNS)) {

            AppCompatActivity context = (AppCompatActivity) getContext();
            GameFragment gameFragment = (GameFragment) context.getSupportFragmentManager().findFragmentById(R.id.game_fragment);
            if(gameFragment == null)
                gameFragment = (GameFragment) context.getSupportFragmentManager().findFragmentByTag("some_frag");
            gameFragment.makeBoardMove(screenToCell(x));
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
        //TODO CHNAGE THIS TO DRAW GRID
        float ytop = getHeight() - (circleRadius*2)*Board.ROWS;
        for (int i = 0; i < Board.COLUMNS; i++) {
            float x = Math.round(cellXToScreen(i)-(circleRadius*1.2));
            canvas.drawLine(x, ytop, x, getHeight(), line);
        }
        for (int i = 0; i < Board.ROWS-1; i++) {
            float y = Math.round(cellYToScreen(i)-(circleRadius*1.2));
            canvas.drawLine(0, y, getWidth(), y, line);
        }
        canvas.drawBitmap(b, 0, 0, ditherPaint);
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        if (o instanceof Board) {
            Board board = (Board) o;
            for (int i = 0; i < Board.ROWS; i++) {
                for (int j = 0; j < Board.COLUMNS; j++) {
                    if (board.getBoard()[i][j] == Game.Colour.RED) {
                        c.drawCircle(cellXToScreen(j), cellYToScreen(i), circleRadius, redCircle);
                    } else if (board.getBoard()[i][j] == Game.Colour.YELLOW) {
                        c.drawCircle(cellXToScreen(j), cellYToScreen(i), circleRadius, yellowCircle);
                    }
                }
            }
            int i = board.yCord, j = board.xCord;
//            if (board.getBoard()[i][j] == Game.Colour.RED) {
//                c.drawCircle(cellXToScreen(j), cellYToScreen(i), circleRadius, redCircle);
//            } else if (board.getBoard()[i][j] == Game.Colour.YELLOW) {
//                c.drawCircle(cellXToScreen(j), cellYToScreen(i), circleRadius, yellowCircle);
//            }
        } else if (o instanceof Game) {
            Game game = (Game) o;
            if (game.isWon()) {
                winner.setTextSize(100);
                winner.setColor(game.getWinner() == Game.Colour.RED ? Color.RED : Color.YELLOW);
                c.drawText((game.getWinner().toString() + " WON"), (getWidth() / 2) - 200, (100), winner);
            }
        }
        this.invalidate();
        this.requestLayout();
    }
}
