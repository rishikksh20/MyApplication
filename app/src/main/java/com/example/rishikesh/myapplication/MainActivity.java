package com.example.rishikesh.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String root=null;
    File xmlFile=null;
    static final String NODE = "node";
    static final String NODE_CATEGORY = "category";
    GridView nodeListView;
    Intent intent;
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

        // Read XML file and draw a adapeter
        root = Environment.getExternalStorageDirectory().toString();
        final File myDir = new File(root + "/MyApp");


        xmlFile = new File(root,"/MyApp/imageAudioMap.xml");
        String idValue;
       // if(xmlFile.exists()) {
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
            Set nodesSet = new HashSet();


            for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);

                nodesSet.add(parser.getValue(e, NODE_CATEGORY));
            }

            List<String> nodesItems = new ArrayList<String>(nodesSet);
            Collections.sort(nodesItems);
            ListAdapter nodeAdapter = new ThumbnailAdapter(this, nodesItems);
            nodeListView = (GridView) findViewById(R.id.gridView);
            nodeListView.setAdapter(nodeAdapter);

            nodeListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String nodeCategory = (String) (nodeListView.getItemAtPosition(position));
                            intent = new Intent(view.getContext(), nodeDisplayActivity.class);
                            intent.putExtra("category", nodeCategory);
                            startActivity(intent);
                        }
                    }


            );
       /* } else
        {
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
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

        }*/



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
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent=null;
        if (id == R.id.nav_gallery) {
            intent = new Intent(this, nodeDisplayActivity.class);
            intent.putExtra("category", "All");
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_category) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
