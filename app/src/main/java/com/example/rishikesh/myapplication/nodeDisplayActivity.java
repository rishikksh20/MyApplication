package com.example.rishikesh.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
public class nodeDisplayActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    // XML node names
    static final String NODE = "node";
    static final String NODE_IMAGE = "image";
    static final String NODE_AUDIO = "audio";
    static final String NODE_CATEGORY = "category";
    String root=null;
    File xmlFile=null;
    Bitmap bp;
    String Category= "All";
    LinearLayout appLayout;
    ScrollView scrollView;
    ImageView imageV;
    Node nodes = null;
    Intent intent;
    GridView nodeListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        root = Environment.getExternalStorageDirectory().toString();
        final File myDir = new File(root + "/MyApp");

        Intent intentX = getIntent();
        if (null != intentX) {
            Category = intentX.getStringExtra("category");
        }


        xmlFile = new File(root,"/MyApp/imageAudioMap.xml");
        String idValue;
        if(xmlFile.exists()) {
            XMLDOMParser parser = new XMLDOMParser();
            InputStream stream = null;
            try {
                stream = new BufferedInputStream(new FileInputStream(xmlFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Document doc = parser.getDocument(stream);

            // Get elements by name employee
            NodeList nodeList = doc.getElementsByTagName(NODE);
            //List<String>images= new ArrayList<String>();
            List<Node> nodesItems = new ArrayList<Node>();

            if(!"All".equals(Category)) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    nodes = new Node();
                    Element e = (Element) nodeList.item(i);

                    if (Category.equals(parser.getValue(e, NODE_CATEGORY))) {
                        nodes.setImage(parser.getValue(e, NODE_IMAGE));
                        nodes.setAudio(parser.getValue(e, NODE_AUDIO));
                        nodes.setId(e.getAttribute("id"));
                        nodesItems.add(nodes);
                    }

                }
            }else {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    nodes = new Node();
                    Element e = (Element) nodeList.item(i);
                    nodes.setImage(parser.getValue(e, NODE_IMAGE));
                    nodes.setAudio(parser.getValue(e, NODE_AUDIO));
                    nodes.setId(e.getAttribute("id"));
                    nodesItems.add(nodes);

                }
            }
            ListAdapter nodeAdapter = new CustomAdapter(this, nodesItems);
            nodeListView = (GridView) findViewById(R.id.gridView);
            nodeListView.setAdapter(nodeAdapter);

            nodeListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Node nodeItem = (Node) (nodeListView.getItemAtPosition(position));
                            intent = new Intent(view.getContext(), ImageShow.class);
                            intent.putExtra("imageURL", nodeItem.getImage());
                            intent.putExtra("audioURL", nodeItem.getAudio());
                            intent.putExtra("id", nodeItem.getId());
                            startActivity(intent);
                        }
                    }


            );
        } else
        {
            RelativeLayout linearLayout = (RelativeLayout)findViewById(R.id.gridLayout);
            Button btn = new Button(this);
            btn.setText("Start");
            linearLayout.addView(
                    btn,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT)
            );
            btn.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           Intent intent = new Intent(v.getContext(), Camera.class);
                                           startActivity(intent);

                                       }

                                   }
            );

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            case R.id.action_settings:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_gallery) {
            if(!Category.equals("All")) {
                intent = new Intent(this, nodeDisplayActivity.class);
                intent.putExtra("category", "All");
                startActivity(intent);
            }
            return true;

        } else if (id == R.id.nav_category) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return  true;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
