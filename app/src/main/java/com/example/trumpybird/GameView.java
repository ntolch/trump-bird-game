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


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    CharacterSprite characterSprite;
    private  MainThread thread;

    public static int characterSpriteWidth = 210;
    public static int characterSpriteHeight = 180;

//    TODO: decrease gapHeight over time for increased difficulty
    public static int gapHeight = 400;
//    TODO: increase velocity over time for increased difficulty
    public static int velocity = 10;

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
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        characterSprite = new CharacterSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.trumpresize), characterSpriteWidth, characterSpriteHeight));
        makeLevel();
        thread.setRunning(true);
        thread.start();
    }

    public void makeLevel() {
        Bitmap bmp;
        Bitmap bmp2;
        int y, x;
        bmp = getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pipe_down_media), 500, Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        bmp2 = getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pipe_up_media), 500, Resources.getSystem().getDisplayMetrics().heightPixels / 2);

        ObstacleSprite pipe1 = new ObstacleSprite(bmp, bmp2, 0, 2000);
        ObstacleSprite pipe2 = new ObstacleSprite(bmp, bmp2, -250, 3200);
        ObstacleSprite pipe3 = new ObstacleSprite(bmp, bmp2, 250, 4500);
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
        characterSprite.y = characterSprite.y - (characterSprite.yVelocity * 25);
        return super.onTouchEvent(event);
    }

    public void update(){
        characterSprite.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            characterSprite.draw(canvas);
        }
    }

}
