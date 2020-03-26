package com.example.trumpybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class ObstacleSprite {

    private Bitmap image;
    public int xX, yY;

    public ObstacleSprite (Bitmap bmp, int x, int y) {
        image = bmp;
        yY = y;
        xX = x;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, xX, yY,null);
    }

    public void update() {
        xX -= GameView.velocity;
    }
}
