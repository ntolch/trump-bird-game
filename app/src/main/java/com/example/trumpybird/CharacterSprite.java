package com.example.trumpybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CharacterSprite {

    private Bitmap image;
    public int x, y;

    public CharacterSprite(Bitmap bmp) {
        image = bmp;
        x = 100;
        y = 100;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update() {
        y++;
    }
}
