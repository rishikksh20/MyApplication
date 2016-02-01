package com.example.rishikesh.myapplication;

/**
 * Created by rishikesh on 4/1/16.
 */
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Camera extends ActionBarActivity {
    Button b1,record,b3,play,stop;
    ImageView iv;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    MediaPlayer mPlayer;
    String root=null;
    String timeStamp;
    File imageDir;
    File image;
    File xmlFile = null;
    DocumentBuilder documentBuilder = null;
    Document document=null;
    String xmlData = "<nodes> \n </nodes>";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private String imageFilePath=null;
    private String imageFileName=null;
    Bitmap bp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameralayout);
        // Creating External Folders for my files

        root = Environment.getExternalStorageDirectory().toString();
        final File myDir = new File(root + "/MyApp");
        if(!myDir.exists())
            myDir.mkdirs();
        imageDir = new File(root + "/MyApp/images");
        if(!imageDir.exists())
            imageDir.mkdirs();
        final File audioDir = new File(root + "/MyApp/audio");
        if(!audioDir.exists())
            audioDir.mkdirs();



        b1=(Button)findViewById(R.id.button);
        iv=(ImageView)findViewById(R.id.imageView);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                imageFileName= "myApp" + timeStamp ;
                image = new File(imageDir,imageFileName+".png" );
                Uri uriSavedImage = Uri.fromFile(image);
                imageFilePath= image.getAbsolutePath();

                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
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

                    outputFile = audioDir.getAbsolutePath() + "/"+imageFileName+".mp3";
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
                xmlFile = new File(root,"/MyApp/imageAudioMap.xml");
                if(! xmlFile.exists()) {
                    try {
                        xmlFile.createNewFile();
                        FileOutputStream fos =new FileOutputStream(xmlFile);
                        fos.write(xmlData.getBytes());
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                 InputStream is=null;

                    try {
                        is = new BufferedInputStream(new FileInputStream(xmlFile));
                    }catch(FileNotFoundException e){

                    }
                    finally {

                        }

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
                newNode.setAttribute("id",imageFileName);
                Element imagePath = document.createElement("image");
                imagePath.appendChild(document.createTextNode(imageFilePath));
                newNode.appendChild(imagePath);

                Element audioPath = document.createElement("audio");
                audioPath.appendChild(document.createTextNode(outputFile));
                newNode.appendChild(audioPath);

                root.appendChild(newNode);

                // write the content into xml file
                try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(xmlFile);

                    transformer.transform(source, result);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }


                Toast.makeText(getApplicationContext(), "Save successfully",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),outputFile+" : "+imageFilePath,Toast.LENGTH_LONG).show();
                System.out.print("-------------------#####################" + imageFilePath + " : " + outputFile);
                record.setEnabled(true);
                b3.setEnabled(false);
                Intent intent = new Intent(v.getContext(), nodeDisplayActivity.class);
                startActivity(intent);

                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        });
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        //Bitmap bp = (Bitmap) data.getExtras().get("data");

        bp= BitmapFactory.decodeFile(image.getAbsolutePath());

        iv.setImageBitmap(bp);
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



    // Save filename in the xml file

}
