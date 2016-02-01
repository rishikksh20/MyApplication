package com.example.rishikesh.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageShow extends AppCompatActivity {
    ImageView showImage;
    Button play,pause,stop;
    String audioURL=null;
    String imageURL=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        showImage=(ImageView)findViewById(R.id.showImage);
        play=(Button)findViewById(R.id.playButton);
        pause=(Button)findViewById(R.id.pauseButton);
        stop=(Button)findViewById(R.id.stopButton);
        Intent intent = getIntent();
        if (null != intent) {
            imageURL= intent.getStringExtra("imageURL");
            audioURL= intent.getStringExtra("audioURL");

        }
        Bitmap bp= BitmapFactory.decodeFile(imageURL);

        showImage.setImageBitmap(bp);


        //creating media player
        final MediaPlayer mp=new MediaPlayer();
        try{
            //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
            mp.setDataSource(audioURL);

            mp.prepare();
        }catch(Exception e){e.printStackTrace();}

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.pause();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
            }
        });

    }
}
