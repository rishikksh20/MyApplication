package com.example.rishikesh.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rishikesh on 7/2/16.
 */
public class ThumbnailAdapter extends ArrayAdapter<String> {

    ThumbnailAdapter(Context context, List<String> nodeItems)
    {
        super(context,R.layout.custom_thumbnail,nodeItems);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater nodeInflater = LayoutInflater.from(getContext());
        View customView = nodeInflater.inflate(R.layout.custom_thumbnail, parent, false);

        String nodeCategory = getItem(position);
        ImageView iv = (ImageView) customView.findViewById(R.id.thumbnail);
        TextView tv =(TextView) customView.findViewById(R.id.category);
        String thumbnailURL = getthumbnailURL(nodeCategory);
        if(! thumbnailURL.equals(null)) {
            Bitmap bp = BitmapFactory.decodeFile(thumbnailURL);
            //bp = scaleDown(bp,MAX_IMAGE_SIZE,true);
            int srcWidth = bp.getWidth();
            int srcHeight = bp.getHeight();
            int dstWidth = (int)(srcWidth*0.4f);
            int dstHeight = (int)(srcHeight*0.4f);
            bp = scaleBitmap(bp, dstWidth, dstHeight);
            iv.setAdjustViewBounds(true);
            Drawable ob = new BitmapDrawable(customView.getResources(), bp);
            iv.setBackground(ob);

            tv.setText(nodeCategory);

        }else
        {
            iv.setAdjustViewBounds(true);
            iv.setImageResource(R.drawable.sorry_image_not_available);
            tv.setText(nodeCategory);
        }

        return customView;
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
     public  static String getthumbnailURL(String category)
     {
         String root=null;
         File xmlFile=null;
         Node nodes = null;
         String imageURL=null;
         String NODE = "node";
         String NODE_IMAGE = "image";
         String NODE_CATEGORY = "category";
         // Read XML file and draw a adapeter
         root = Environment.getExternalStorageDirectory().toString();
         final File myDir = new File(root + "/MyApp");


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
             Set nodesSet = new HashSet();


             for (int i = 0; i < nodeList.getLength(); i++) {
                 Element e = (Element) nodeList.item(i);

                 if(parser.getValue(e, NODE_CATEGORY).equals(category))
                    imageURL=parser.getValue(e, NODE_IMAGE);
             }
         }
         return imageURL;
     }

}
