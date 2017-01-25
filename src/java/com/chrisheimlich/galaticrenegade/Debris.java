package com.chrisheimlich.galaticrenegade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;


public class Debris {

    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed;
    private int minSpeed;
    private int maxSpeed;

    private int maxX;
    private int minX;

    private int maxY;
    private int minY;

    private Rect detectCollision;


    public Debris(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.debris);
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        Random generator = new Random();
        minSpeed = generator.nextInt(6) + 10;
        maxSpeed = minSpeed * 4;
        speed = maxSpeed;
        x = generator.nextInt(maxX) + bitmap.getWidth();
        y = 0;

        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(boolean boosting) {
        if(boosting && speed < maxSpeed)
            speed += 1;
        else if (speed > minSpeed)
            speed -= 1;

        y += speed;

        if (y > maxY) {
            Random generator = new Random();
            speed = generator.nextInt(10) + 10;
            x = generator.nextInt(maxX) - bitmap.getWidth();
            y = 0;
        }

        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }



    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
