package com.example.rishikesh.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.ic_launcher);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);

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
       /* Bitmap bp = BitmapFactory.decodeFile(imageURL);
        int srcWidth = bp.getWidth();
        int srcHeight = bp.getHeight();
        int dstWidth = (int)(srcWidth*0.8f);
        int dstHeight = (int)(srcHeight*0.8f);
        bp = scaleBitmap(bp, dstWidth, dstHeight);
        showImage.setImageBitmap(bp);*/
        Picasso
                .with(getApplicationContext())
                .load(new File(imageURL))
                .fit()
                .into((ImageView) showImage);
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
                AlertDialog.Builder a_builder = new AlertDialog.Builder(ImageShow.this);
                a_builder.setMessage("Do you want to Delete this File !!!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteNode();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }) ;
                AlertDialog alert = a_builder.create();
                alert.setTitle("Delete Alert !!!");
                alert.show();


                return true;
            case android.R.id.home:
                finish();
              /*  Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }*/
                return true;
            case R.id.action_settings:
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Exit Application?");
                alertDialogBuilder
                        .setMessage("Click yes to exit!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        moveTaskToBack(true);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(1);
                                    }
                                })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean deleteNode() {
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
            File imageFile = new File(imageURL);
            File audioFile = new File(audioURL);
            try {
                if(imageFile.delete() && audioFile.delete()) {
                    transformer.transform(source, result);
                    Intent intent2 = new Intent(this, nodeDisplayActivity.class);
                    startActivity(intent2);
                    return true;
                }else
                {
                    Toast.makeText(getApplicationContext(), "Problem in deleting file !!!", Toast.LENGTH_LONG).show();
                }
            } catch (TransformerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return false;
    }
    // Another methos to scale down bitmap
    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }
}
