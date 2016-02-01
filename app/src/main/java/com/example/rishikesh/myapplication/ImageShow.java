package com.example.rishikesh.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageShow extends AppCompatActivity {
    ImageView showImage;
    Button play,pause,stop;
    String audioURL=null;
    String imageURL=null;
    Boolean flag;
    MediaPlayer mp;
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
        pause.setEnabled(false);
        stop.setEnabled(false);
        flag=false;
        //creating media player
        mp=new MediaPlayer();
        try{
            //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
            mp.setDataSource(audioURL);

            mp.prepare();
        }catch(Exception e){e.printStackTrace();}

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag)
                {
                    flag=false;
                    mp=new MediaPlayer();
                    try{
                        //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
                        mp.setDataSource(audioURL);

                        mp.prepare();
                    }catch(Exception e){e.printStackTrace();}

                }
                mp.start();
                play.setEnabled(false);
                pause.setEnabled(true);
                stop.setEnabled(true);
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.pause();
                play.setEnabled(true);
                pause.setEnabled(false);
                stop.setEnabled(true);
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();

                play.setEnabled(true);
                pause.setEnabled(false);
                stop.setEnabled(false);
                flag=true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.action_favorite:
                Intent intent = new Intent(this, Camera.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
