package com.chrisheimlich.galaticrenegade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Info extends AppCompatActivity implements View.OnClickListener{

    private ImageButton buttonShare;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        buttonShare = (ImageButton) findViewById(R.id.buttonShare);

        buttonShare.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v==buttonShare) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "You should download Galatic Renegade. It's awesome!");
            sendIntent.setType("text/plain");
            Intent chooser = Intent.createChooser(sendIntent, "Send advert via");

            if (sendIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            }
        }
    }


}
