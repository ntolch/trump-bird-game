package com.example.trumpybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

public class CharacterSprite {

    private Bitmap image;
    public int x, y;
    public int yVelocity = 5;
    private int charHeight = GameView.characterSpriteHeight;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public CharacterSprite(Bitmap bmp) {
        image = bmp;
        this.y = getRandomY();
        x = 100;
    }

    public int getRandomY() {
        Random r = new Random();
        return r.nextInt((screenHeight - (charHeight * 2)) + charHeight);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update() {
        y += yVelocity;
        if ((y < 0)) { yVelocity = yVelocity *- 1; }
    }
}
