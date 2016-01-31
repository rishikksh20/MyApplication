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

import java.util.List;

/**
 * Created by rishikesh on 1/2/16.
 */
public class CustomAdapter extends ArrayAdapter<Node> {

    CustomAdapter(Context context, List<Node> nodeItems)
    {
       super(context,R.layout.custom_row,nodeItems);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater nodeInflater = LayoutInflater.from(getContext());
        View customView = nodeInflater.inflate(R.layout.custom_row, parent, false);

        Node nodeItems = getItem(position);
        ImageView iv = (ImageView) customView.findViewById(R.id.imageView2);
        Bitmap bp= BitmapFactory.decodeFile(nodeItems.getImage());
        //bp = scaleDown(bp,MAX_IMAGE_SIZE,true);
        bp=scaleBitmap(bp,800,800);
        iv.setAdjustViewBounds(true);
        iv.setImageBitmap(bp);
        return customView;
    }
    // One method to scale down bitmap
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
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
