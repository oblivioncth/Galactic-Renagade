package com.chrisheimlich.galaticrenegade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import java.util.Random;

public class Enemy {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed;
    private int minSpeed;
    private int maxSpeed;
    private int maxX;
    private int maxY;

    private Rect detectCollision;

    public Enemy(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyship);
        maxX = screenX;
        maxY = screenY;
        Random generator = new Random();
        minSpeed = generator.nextInt(6) + 10;
        maxSpeed = minSpeed * 4;
        speed = minSpeed;
        x = generator.nextInt(maxX) + bitmap.getWidth() + 10;
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

    public void setX(int x){
        this.x = x;
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

    public int getSpeed() {
        return speed;
    }

}
