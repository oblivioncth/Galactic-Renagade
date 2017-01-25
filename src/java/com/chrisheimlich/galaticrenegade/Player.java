package com.chrisheimlich.galaticrenegade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Player {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int minSpeed = 12;
    private int maxSpeed = 16;
    private int speed;
    public int maxCheck = 0;
    public int maxCap = 40;
    private boolean movingRight;
    private boolean movingLeft;
    public boolean isBoosting;
    private int maxX;
    private int minX;

    private Rect detectCollision;

    public Player(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playership);
        maxX = screenX - bitmap.getWidth();
        x = screenX/2 - (bitmap.getWidth()/2);
        y = screenY - bitmap.getHeight() - 250;
        speed = minSpeed;
        minX = 0;
        movingRight = false;
        movingLeft = false;
        isBoosting = false;

        detectCollision =  new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void startMovingRight(){movingRight = true;}
    public void stopMovingRight(){movingRight = false;}
    public void startMovingLeft(){movingLeft = true;}
    public void stopMovingLeft(){movingLeft = false;}
    public void startBoosting(){isBoosting = true;}
    public void stopBoosting(){isBoosting = false;}



    public void update() {
        if(isBoosting  && speed != maxSpeed)
            speed += 1;
        else if(speed != minSpeed)
            speed -= 1;

        if(isBoosting && maxCheck != maxCap)
            maxCheck += 1;
        else if(!isBoosting && maxCheck != 0)
            maxCheck -= 1;

        if (movingRight && !movingLeft)
            x += speed;
        else if(!movingRight && movingLeft)
            x -= speed;

        if (x < minX) {
            x = minX;
        }
        if (x > maxX) {
            x = maxX;
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

    public boolean checkBoosting() {return isBoosting;}

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