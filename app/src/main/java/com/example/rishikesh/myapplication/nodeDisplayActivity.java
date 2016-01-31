package com.example.rishikesh.myapplication;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Menu;
import android.view.animation.LinearInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by rishikesh on 4/1/16.
 */
public class nodeDisplayActivity extends Activity {
    // XML node names
    static final String NODE_EMP = "node";
    static final String NODE_NAME = "image";
    static final String NODE_SALARY = "audio";
    String root=null;
    File xmlFile=null;
    Bitmap bp;
    LinearLayout appLayout;
    ScrollView scrollView;
    ImageView imageV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scrollView = new ScrollView(this);

        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
        appLayout = new LinearLayout(this);
        appLayout.setOrientation(LinearLayout.VERTICAL);
        appLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));




        root = Environment.getExternalStorageDirectory().toString();
        final File myDir = new File(root + "/MyApp");
        xmlFile = new File(root,"/MyApp/imageAudioMap.xml");
        String idValue;
        XMLDOMParser parser = new XMLDOMParser();
        InputStream stream=null;
        try {
            stream = new BufferedInputStream(new FileInputStream(xmlFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Document doc = parser.getDocument(stream);

        // Get elements by name employee
        NodeList nodeList = doc.getElementsByTagName(NODE_EMP);

            /*
             * for each <employee> element get text of name, salary and
             * designation
             */
        // Here, we have only one <employee> element
        for (int i = 0; i < nodeList.getLength(); i++) {

            Element e = (Element) nodeList.item(i);
            bp= BitmapFactory.decodeFile(parser.getValue(e, NODE_NAME));
            imageV = new ImageView(this);
            imageV.setAdjustViewBounds(true);
            imageV.setImageBitmap(bp);



            appLayout.addView(imageV);


        }

        scrollView.addView(appLayout);
        setContentView(scrollView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }
}
