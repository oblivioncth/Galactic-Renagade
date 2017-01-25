package com.chrisheimlich.galaticrenegade;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends Activity implements View.OnClickListener {


    private ImageButton buttonPlay;
    private ImageButton buttonScore;
    private ImageButton buttonInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);
        buttonScore = (ImageButton) findViewById(R.id.buttonScore);
        buttonInfo = (ImageButton) findViewById(R.id.buttonInfo);

        buttonScore.setOnClickListener(this);
        buttonPlay.setOnClickListener(this);
        buttonInfo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==buttonPlay)
            startActivity(new Intent(MainActivity.this, GameActivity.class));
        if(v==buttonScore)
            startActivity(new Intent(MainActivity.this,HighScore.class));
        if(v==buttonInfo)
            startActivity(new Intent(MainActivity.this,Info.class));


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

}