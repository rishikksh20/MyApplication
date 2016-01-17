package com.example.rishikesh.myapplication;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nodedisplayactivity);
        TextView txtView = (TextView) findViewById(R.id.textView1);
        TextView txtView2 = (TextView) findViewById(R.id.textView2);
        XMLDOMParser parser = new XMLDOMParser();
        InputStream stream;
        stream = getResources().openRawResource(R.raw.recordnode);
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
            txtView.setText(parser.getValue(e, NODE_NAME));
            txtView2.setText(parser.getValue(e, NODE_SALARY));

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }
}
