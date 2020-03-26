package com.example.trumpybird;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;

public class ObstacleSprite {

    private Bitmap image;
    public int xX, yY;

    public ObstacleSprite (Bitmap bmp) {
        image = bmp;
        this.yY = getRandomY();
        this.xX = getRandomX();
    }

    public int getRandomY(){
        Random r = new Random();
        return r.nextInt((GameView.screenHeight - (GameView.obstacleHeight * 2)) + GameView.obstacleHeight);
    }

    public int getRandomX(){
        Random r = new Random();
        return r.nextInt((GameView.screenWidth + GameView.obstacleWidth) + GameView.screenWidth);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, xX, yY,null);
    }

    public void update() {
        xX -= GameView.velocity;
    }
}
