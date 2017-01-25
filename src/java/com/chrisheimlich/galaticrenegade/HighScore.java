package com.chrisheimlich.galaticrenegade;

import android.content.Context;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ListView;


public class HighScore extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Integer highScore[] = new Integer[10];
    ListView scoreListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        sharedPreferences  = getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

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

        scoreListView = (ListView) findViewById(R.id.scoreList);
        ArrayAdapter<Integer> scoreAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, highScore);

        scoreListView.setAdapter(scoreAdapter);
    }
}

