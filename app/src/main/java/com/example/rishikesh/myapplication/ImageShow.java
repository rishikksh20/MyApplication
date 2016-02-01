package com.example.rishikesh.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    }
}
