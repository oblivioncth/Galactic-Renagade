package com.chrisheimlich.galaticrenegade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Blast {

    private Bitmap bitmap;

    private int x;
    private int y;

    public Blast(Context context) {
        //getting blast image from drawable resource
        bitmap = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.blast);

        x = -1000;
        y = -1000;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
