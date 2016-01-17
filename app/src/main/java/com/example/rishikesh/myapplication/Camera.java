package com.example.rishikesh.myapplication;

/**
 * Created by rishikesh on 4/1/16.
 */
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import org.apache.commons.io.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Camera extends ActionBarActivity {
    Button b1,record,b3,play,stop;
    ImageView iv;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    MediaPlayer mPlayer;
    private String imageFilePath=null;
    private String imageFileName=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameralayout);

        b1=(Button)findViewById(R.id.button);
        iv=(ImageView)findViewById(R.id.imageView);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        b3=(Button)findViewById(R.id.button3);
        b3.setEnabled(false);
        record=(Button)findViewById(R.id.button2);
        play=(Button)findViewById(R.id.button4);
        stop=(Button)findViewById(R.id.button5);

        stop.setEnabled(false);
        play.setEnabled(false);


        myAudioRecorder=new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+imageFileName+".3gp";
                    myAudioRecorder.setOutputFile(outputFile);
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                }

                catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                record.setEnabled(false);
                stop.setEnabled(true);
                b3.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   myAudioRecorder.stop();
                   myAudioRecorder.release();
                   myAudioRecorder = null;

                   stop.setEnabled(false);
                   play.setEnabled(true);


                Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException,SecurityException,IllegalStateException {
                mPlayer = new MediaPlayer();

                try {
                    mPlayer.setDataSource(outputFile);
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    mPlayer.prepare();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                mPlayer.start();
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
            }
        });



        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = null;
                Document document=null;
                InputStream is = getResources().openRawResource(R.raw.recordnode);
                try {
                    documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    document= documentBuilder.parse(is);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Element root = document.getDocumentElement();
                // server elements
                Element newNode = document.createElement("node");

                Element imagePath = document.createElement("image");
                imagePath.appendChild(document.createTextNode(imageFilePath));
                newNode.appendChild(imagePath);

                Element audioPath = document.createElement("audio");
                audioPath.appendChild(document.createTextNode(outputFile));
                newNode.appendChild(audioPath);

                root.appendChild(newNode);
                Toast.makeText(getApplicationContext(), "Save successfully",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),outputFile+" : "+imageFilePath,Toast.LENGTH_LONG).show();
                System.out.print("-------------------#####################" + imageFilePath + " : " + outputFile);
                record.setEnabled(true);
                b3.setEnabled(false);
                Intent intent = new Intent(v.getContext(), nodeDisplayActivity.class);
                startActivity(intent);


            }

        });
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bp = (Bitmap) data.getExtras().get("data");

        iv.setImageBitmap(bp);

        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        Uri tempUri = getImageUri(getApplicationContext(), bp);

        // Path of image
        imageFilePath=getRealPathFromURI(tempUri);
        imageFileName = FilenameUtils.removeExtension(imageFilePath.substring(imageFilePath.lastIndexOf("/")+1));
        // CALL THIS METHOD TO GET THE ACTUAL PATH
        File finalFile = new File(getRealPathFromURI(tempUri));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    // Save filename in the xml file

}
