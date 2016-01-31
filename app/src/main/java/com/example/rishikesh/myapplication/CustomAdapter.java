package com.example.rishikesh.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * Created by rishikesh on 1/2/16.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    CustomAdapter(Context context, String[] images)
    {
       super(context,R.layout.custom_row,images);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater nodeInflater = LayoutInflater.from(getContext());
        View customView = nodeInflater.inflate(R.layout.custom_row, parent, false);

        String nodeItems = getItem(position);
        ImageView iv = (ImageView) customView.findViewById(R.id.imageView2);
        Bitmap bp= BitmapFactory.decodeFile(nodeItems);
        iv.setAdjustViewBounds(true);
        iv.setImageBitmap(bp);
        return customView;
    }
}
