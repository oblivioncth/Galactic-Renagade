package com.chrisheimlich.galaticrenegade;

import java.util.Random;

public class Star {
    private int x;
    private int y;
    private int minSpeed;
    private int maxSpeed;
    private int speed;
    private int colorNum;

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;



    public Star(int screenX, int screenY) {
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        Random generator = new Random();
        minSpeed = generator.nextInt(10);
        maxSpeed = minSpeed * 4;
        speed = minSpeed;
        Random generator2 = new Random();
        colorNum = generator2.nextInt(18);

        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);
    }

    public void update(boolean boosting) {

        if(boosting && speed < maxSpeed)
            speed += 1;
        else if (speed > minSpeed)
            speed -= 1;

            y += speed;

        if (y > maxY) {
            y = 0;
            Random generator = new Random();
            x = generator.nextInt(maxX);
            speed = generator.nextInt(15);
        }
    }

    public float getStarWidth() {
        float minX = 0.8f;
        float maxX = 3.3f;
        Random rand = new Random();
        float finalX = rand.nextFloat() * (maxX - minX) + minX;
        return finalX;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getColorNum()
    {
        return colorNum;
    }
}
