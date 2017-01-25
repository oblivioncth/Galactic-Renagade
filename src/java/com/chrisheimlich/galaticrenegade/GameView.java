package com.chrisheimlich.galaticrenegade;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import static android.content.Context.AUDIO_SERVICE;

public class GameView extends SurfaceView implements Runnable {

    Context context;

    volatile boolean playing;
    private Thread gameThread = null;
    public Player player;
    private Enemy enemy;
    public Jet jet;
    private int debrisCount = 3;
    private Debris debris[] = new Debris[debrisCount];
    private Blast blast;

    int screenX;
    boolean jetOn = false;
    int score;
    int highScore[] = new int[10];
    private boolean isGameOver;

    SharedPreferences sharedPreferences;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private ArrayList<Star> stars = new
            ArrayList<Star>();


    //Sound Pool
    private SoundPool soundPool;
    private AudioManager audioManager;
    private static final int MAX_STREAMS = 5;
    private static final int streamType = AudioManager.STREAM_MUSIC;
    private boolean loaded;

    private int soundIdDestroy;
    private int soundIdCrash;
    private float soundVolume;

    //Media Player
    static  MediaPlayer bgMusic;


    public GameView(Context context, int screenX, int screenY, Activity gameActivity) {
        super(context);

        //More SoundPool
        audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        float currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);
        float maxVolumeIndex  = (float) audioManager.getStreamMaxVolume(streamType);

        this.soundVolume = currentVolumeIndex / maxVolumeIndex;

        // Suggests an audio stream whose volume should be changed by
        // the hardware volume controls.
        gameActivity.setVolumeControlStream(streamType);

        // For Android SDK >= 21
        if (Build.VERSION.SDK_INT >= 21 ) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
        }
        // for Android SDK < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        // When Sound Pool load complete.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        this.soundIdDestroy = this.soundPool.load(context, R.raw.destroy,1);

        this.soundIdCrash = this.soundPool.load(context, R.raw.crash,1);


        player = new Player(context, screenX, screenY);
        jet = new Jet(context);

        surfaceHolder = getHolder();
        paint = new Paint();

        this.context = context;

        int starNums = 110;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }

        enemy = new Enemy(context,screenX,screenY);
        blast = new Blast(context);

        for(int i = 0; i < debrisCount; i++)
            debris[i] = new Debris(context, screenX, screenY);

        score = 0;

        this.screenX = screenX;
        isGameOver = false;

        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME",Context.MODE_PRIVATE);

        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);
        highScore[3] = sharedPreferences.getInt("score4",0);
        highScore[4] = sharedPreferences.getInt("score5",0);
        highScore[5] = sharedPreferences.getInt("score6",0);
        highScore[6] = sharedPreferences.getInt("score7",0);
        highScore[7] = sharedPreferences.getInt("score8",0);
        highScore[8] = sharedPreferences.getInt("score9",0);
        highScore[9] = sharedPreferences.getInt("score10",0);

        bgMusic = MediaPlayer.create(context,R.raw.bg);
        bgMusic.start();

    }

    @Override
    public void run() {

        while (playing) {

            update();
            draw();
            control();

        }


    }

    private void update() {

        player.update();

        if(!jetOn&& player.maxCheck == player.maxCap)
        {
            jetOn = true;
            jet.setY(player.getY() + player.getBitmap().getHeight());
        }
        if(jetOn && player.maxCheck != player.maxCap)
        {
            jetOn = false;
            jet.setY(-1000);
        }
        jet.setX(player.getX() + (player.getBitmap().getWidth()/2) - 14);


        //Off Screen
        blast.setX(-1000);
        blast.setY(-1000);

        for (Star s : stars) {

            s.update(player.checkBoosting());
        }

        enemy.update(player.checkBoosting());
        //Player Collision
        if (Rect.intersects(player.getDetectCollision(), enemy.getDetectCollision())) {
            if(jetOn)
            {
                blast.setX(enemy.getX());
                blast.setY(enemy.getY());

                playSoundDestroy();

                score += 100;
                enemy.setX(-1000);
            }
            else{
                blast.setX(player.getX());
                blast.setY(player.getY());

                playing = false;
                isGameOver = true;

                bgMusic.stop();
                playSoundCrash();

                for (int i = 0; i < 10; i++) {
                    if (highScore[i] < score) {
                        for(int j = 8; j >= i; j--) {
                            highScore[j+1] = highScore[j];
                        }
                        highScore[i] = score;
                        break;
                    }
                }

                SharedPreferences.Editor e = sharedPreferences.edit();

                for (int i = 0; i < 4; i++) {

                    int j = i + 1;
                    e.putInt("score" + j, highScore[i]);
                }
                e.apply();
            }


        }

        for(int i = 0; i < debrisCount; i++)
            debris[i].update(player.checkBoosting());
        for (int k = 0; k < debrisCount; k++) {
            if (Rect.intersects(player.getDetectCollision(), debris[k].getDetectCollision())) {

                blast.setX(player.getX());
                blast.setY(player.getY());

                playing = false;
                isGameOver = true;

                bgMusic.stop();
                playSoundCrash();

                for (int i = 0; i < 4; i++) {
                    if (highScore[i] < score) {
                        for(int j = 2; j >= i; j--) {
                            highScore[j+1] = highScore[j];
                        }
                        highScore[i] = score;
                        break;
                    }
                }

                SharedPreferences.Editor e = sharedPreferences.edit();

                for (int i = 0; i < 4; i++) {

                    int j = i + 1;
                    e.putInt("score" + j, highScore[i]);
                }
                e.apply();
                break;
            }
        }

    }


    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.WHITE);
            paint.setTextSize(20);

            for (Star s : stars) {
                int iColorNum = s.getColorNum();
                paint.setStrokeWidth(s.getStarWidth());
                if(iColorNum <= 11)
                    paint.setColor(ContextCompat.getColor(context, R.color.ccWhite));
                switch (iColorNum) {
                    case 12:  paint.setColor(ContextCompat.getColor(context, R.color.ccRed));
                        break;
                    case 13:  paint.setColor(ContextCompat.getColor(context, R.color.ccBlue));
                        break;
                    case 14:  paint.setColor(ContextCompat.getColor(context, R.color.ccGreen));
                        break;
                    case 15:  paint.setColor(ContextCompat.getColor(context, R.color.ccOrange));
                        break;
                    case 16:  paint.setColor(ContextCompat.getColor(context, R.color.ccYellow));
                        break;
                    case 17:  paint.setColor(ContextCompat.getColor(context, R.color.ccPurple));
                        break;
                    default: paint.setColor(ContextCompat.getColor(context, R.color.ccWhite));
                        break;
                }
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }
            paint.setColor(ContextCompat.getColor(context, R.color.ccWhite)); //Set back to white for other functions

            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);


            canvas.drawBitmap(
                    enemy.getBitmap(),
                    enemy.getX(),
                    enemy.getY(),
                    paint

            );

            paint.setTextSize(70);
            canvas.drawText("Score: "+score,50,100,paint);

            canvas.drawBitmap(
                    blast.getBitmap(),
                    blast.getX(),
                    blast.getY(),
                    paint
            );

            canvas.drawBitmap(
                    jet.getBitmap(),
                    jet.getX(),
                    jet.getY(),
                    paint
            );

            for(int i = 0; i < debrisCount; i++)
            {
                canvas.drawBitmap(

                        debris[i].getBitmap(),
                        debris[i].getX(),
                        debris[i].getY(),
                        paint
                );
            }

            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    //stop the music on exit
    public static void stopMusic(){

        bgMusic.stop();
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if(isGameOver){

            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){

                context.startActivity(new Intent(context,MainActivity.class));

            }

        }

        return true;

    }

    public void playSoundCrash()  {
        if(loaded)  {
            float leftVolumn = soundVolume;
            float rightVolumn = soundVolume;
            // Play sound of gunfire. Returns the ID of the new stream.
            int streamId = this.soundPool.play(this.soundIdCrash,leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }

    public void playSoundDestroy()  {
        if(loaded)  {
            float leftVolumn = soundVolume;
            float rightVolumn = soundVolume;

            // Play sound objects destroyed. Returns the ID of the new stream.
            int streamId = this.soundPool.play(this.soundIdDestroy,leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }



}