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
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rishikesh on 4/1/16.
 */
public class nodeDisplayActivity extends Activity {
    // XML node names
    static final String NODE_EMP = "node";
    static final String NODE_NAME = "image";
    static final String NODE_AUDIO = "audio";
    String root=null;
    File xmlFile=null;
    Bitmap bp;
    LinearLayout appLayout;
    ScrollView scrollView;
    ImageView imageV;
    Node nodes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nodedisplayactivity);



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
        //List<String>images= new ArrayList<String>();
        List<Node> nodesItems = new ArrayList<Node>();


        for (int i = 0; i < nodeList.getLength(); i++) {
            nodes=new Node();
            Element e = (Element) nodeList.item(i);
            nodes.setImage(parser.getValue(e, NODE_NAME));
            nodes.setAudio(parser.getValue(e, NODE_AUDIO));
            nodesItems.add(nodes);
           // images[i] =parser.getValue(e, NODE_NAME);
           // images.add(parser.getValue(e, NODE_NAME));



        }
        ListAdapter nodeAdapter = new CustomAdapter(this, nodesItems);
        final ListView nodeListView = (ListView) findViewById(R.id.listView);
        nodeListView.setAdapter(nodeAdapter);

        nodeListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Node nodeItem = (Node) (nodeListView.getItemAtPosition(position));
                        Toast.makeText(nodeDisplayActivity.this, nodeItem.getImage(), Toast.LENGTH_SHORT).show();
                    }
                }



        );







    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }
}
