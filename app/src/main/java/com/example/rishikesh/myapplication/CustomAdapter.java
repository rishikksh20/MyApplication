package com.example.rishikesh.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by rishikesh on 1/2/16.
 */
public class CustomAdapter extends ArrayAdapter<Node> {
    private Context context;
    private LayoutInflater inflater;

    private List<Node> imageUrls;

    public CustomAdapter(Context context, List<Node> nodeItems)
    {
       super(context,R.layout.custom_row,nodeItems);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imageUrls = nodeItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater nodeInflater = LayoutInflater.from(getContext());
        View customView = nodeInflater.inflate(R.layout.custom_row, parent, false);

        Node nodeItems = getItem(position);
        ImageView iv = (ImageView) customView.findViewById(R.id.thumbnailImageView);

        Picasso
                .with(context)
                .load(new File(nodeItems.getImage()))
                .resize(200, 200) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .into((ImageView) iv);

        return customView;
    }



}
