package com.example.trumpybird;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    CharacterSprite characterSprite;
    private  MainThread thread;
    ObstacleSprite obstacle1;
    ObstacleSprite obstacle2;
    ObstacleSprite obstacle3;
    ArrayList<ObstacleSprite> obstacles = new ArrayList();


    public static int characterSpriteWidth = 210;
    public static int characterSpriteHeight = 180;

    public static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    public static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

    public static int obstacleWidth = 200;
    public static int obstacleHeight = screenHeight / 5;


/*
     TODO: obstacles to add: global warming, Roseanne Barr, specific journalists
     TODO: add points for passing each obstacle
     TODO: add points for running into Putin (put glow around bonus points like Putin and money)
    */

    //    TODO: increase velocity over time for increased difficulty
    public static int velocity = 5;

    public GameView(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        characterSprite = new CharacterSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.trumpresize), characterSpriteWidth, characterSpriteHeight));
        Log.i("\n\n**NEW HEAD CREATED", String.valueOf(characterSprite.getRandomY()));

        Bitmap bmp;
        bmp = getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.news_recording), obstacleWidth, obstacleHeight);

        obstacle1 = new ObstacleSprite(bmp);
        obstacle2 = new ObstacleSprite(bmp);
        obstacle3 = new ObstacleSprite(bmp);

        obstacles.add(obstacle1);
        obstacles.add(obstacle2);
        obstacles.add(obstacle3);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("\n*ON TOUCH EVENT", String.valueOf(event.getAction()));
        characterSprite.y = characterSprite.y - (characterSprite.yVelocity * 22);
        return super.onTouchEvent(event);
    }

    public void logic() {
        ArrayList<ObstacleSprite> obstacles = new ArrayList();
        obstacles.add(obstacle1);
        obstacles.add(obstacle2);
        obstacles.add(obstacle3);

        for (int i = 0; i < obstacles.size(); i++) {
            //Detect if the character is touching one of the pipes
            if (characterSprite.y < obstacles.get(i).yY + obstacleHeight
                    && characterSprite.x + characterSpriteWidth > obstacles.get(i).xX
                    && characterSprite.x < obstacles.get(i).xX + obstacleWidth) {
                resetLevel();
            } else if (characterSprite.y + characterSpriteHeight > obstacles.get(i).yY
                    && characterSprite.x + characterSpriteHeight > obstacles.get(i).xX
                    && characterSprite.x < obstacles.get(i).xX + obstacleWidth) {
                resetLevel();
            }

            //Detect if the pipe has gone off the left of the
            //screen and regenerate further ahead
            if (obstacles.get(i).xX + obstacleWidth < 0) {
                Random r = new Random();
                int value1 = r.nextInt(500);
                int value2 = r.nextInt(screenHeight);
                obstacles.get(i).xX = screenWidth + value1 + 1000;
                obstacles.get(i).yY = value2 + 150;
            }
        }

        //Detect if the character has gone off the top or below the bottom of the screen
        if (characterSprite.y + characterSpriteHeight + 50 < 0) { resetLevel(); }
        if (characterSprite.y > screenHeight - characterSpriteHeight) { resetLevel(); }
    }

    public void resetLevel() {
        Log.i("\n\n*RESET", "bumped into obstacle");
        characterSprite.y = characterSprite.getRandomY();
        obstacle1.getRandomX();
        obstacle1.getRandomY();
        obstacle2.getRandomX();
        obstacle2.getRandomY();
        obstacle3.getRandomX();
        obstacle3.getRandomY();
    }

    public void update(){
        logic();
        Log.i("\n\n**NEW HEAD update", String.valueOf(characterSprite.getRandomY()));
        characterSprite.update();
        for (ObstacleSprite obstacle : obstacles) { obstacle.update(); }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            for (ObstacleSprite obstacle : obstacles) { obstacle.draw(canvas); }
            characterSprite.draw(canvas);
        }
    }

}
