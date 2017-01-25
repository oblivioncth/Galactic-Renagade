package com.chrisheimlich.galaticrenegade;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.text.Layout;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener{

    private GameView gameView;
    RelativeLayout mainLayout;
    ImageButton rightButtonRef;
    ImageButton leftButtonRef;
    ImageButton boostButtonRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        gameView = new GameView(this, size.x, size.y, GameActivity.this);
        setContentView(R.layout.activity_game);

        mainLayout = (RelativeLayout)findViewById(R.id.activity_game);
        mainLayout.addView(gameView);

        rightButtonRef = (ImageButton)findViewById(R.id.rightButton);
        leftButtonRef = (ImageButton)findViewById(R.id.leftButton);
        boostButtonRef = (ImageButton)findViewById(R.id.boostButton);
        rightButtonRef.bringToFront();
        leftButtonRef.bringToFront();
        boostButtonRef.bringToFront();

        rightButtonRef.setOnTouchListener(this);
        leftButtonRef.setOnTouchListener(this);
        boostButtonRef.setOnTouchListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to quit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        GameView.stopMusic();
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
            switch (arg0.getId()) {
                case R.id.leftButton: // Id of the button
                    gameView.player.startMovingLeft();
                    break;

                case R.id.rightButton: // Id of the button
                    gameView.player.startMovingRight();
                    break;

                case R.id.boostButton: // Id of the button
                    gameView.player.startBoosting();
                    break;

                default:
                    break;
            }
        }
        if (arg1.getAction() == MotionEvent.ACTION_UP) {
            switch (arg0.getId()) {
                case R.id.leftButton: // Id of the button
                    gameView.player.stopMovingLeft();
                    break;

                case R.id.rightButton: // Id of the button
                    gameView.player.stopMovingRight();
                    break;

                case R.id.boostButton: // Id of the button
                    gameView.player.stopBoosting();
                    break;

                default:
                    break;
            }
        }
        return true;
    }



}