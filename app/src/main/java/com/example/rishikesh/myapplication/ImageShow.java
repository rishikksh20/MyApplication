package com.example.rishikesh.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ImageShow extends AppCompatActivity {
    ImageView showImage;
    Button play, pause, stop;
    String audioURL = null;
    String imageURL = null;
    String id = null;
    Boolean flag;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        showImage = (ImageView) findViewById(R.id.showImage);
        play = (Button) findViewById(R.id.playButton);
        pause = (Button) findViewById(R.id.pauseButton);
        stop = (Button) findViewById(R.id.stopButton);
        Intent intent = getIntent();
        if (null != intent) {
            imageURL = intent.getStringExtra("imageURL");
            audioURL = intent.getStringExtra("audioURL");
            id = intent.getStringExtra("id");

        }
        Bitmap bp = BitmapFactory.decodeFile(imageURL);

        showImage.setImageBitmap(bp);
        pause.setEnabled(false);
        stop.setEnabled(false);
        flag = false;
        //creating media player
        mp = new MediaPlayer();
        try {
            //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
            mp.setDataSource(audioURL);

            mp.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    flag = false;
                    mp = new MediaPlayer();
                    try {
                        //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
                        mp.setDataSource(audioURL);

                        mp.prepare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                flag = true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_show, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_favorite:
                Intent intent = new Intent(this, Camera.class);
                startActivity(intent);
                return true;
            case R.id.action_delete:
                if (deleteNode(imageURL)) {
                    Intent intent2 = new Intent(this, nodeDisplayActivity.class);
                    startActivity(intent2);
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean deleteNode(String url) {
        String root = Environment.getExternalStorageDirectory().toString();
        final File myDir = new File(root + "/MyApp");


        File xmlFile = new File(root, "/MyApp/imageAudioMap.xml");
        String idValue;
        if (xmlFile.exists()) {
            XMLDOMParser parser = new XMLDOMParser();
            InputStream stream = null;
            try {
                stream = new BufferedInputStream(new FileInputStream(xmlFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Document doc = parser.getDocument(stream);
            // Get the root element
            org.w3c.dom.Node nodes = doc.getFirstChild();
            // Get elements by name employee
            NodeList nodeList = doc.getElementsByTagName("node");
            //List<String>images= new ArrayList<String>();
            List<Node> nodesItems = new ArrayList<Node>();


            for (int i = 0; i < nodeList.getLength(); i++) {

                Element e = (Element) nodeList.item(i);
                if(id.equals(e.getAttribute("id")))
                {
                    nodes.removeChild(e);
                }

            }
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = null;
            try {
                transformer = transformerFactory.newTransformer();
            } catch (TransformerConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            try {
                transformer.transform(source, result);
                return true;
            } catch (TransformerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return false;
    }
}
