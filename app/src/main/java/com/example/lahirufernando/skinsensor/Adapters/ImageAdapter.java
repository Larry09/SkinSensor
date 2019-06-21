package com.example.lahirufernando.skinsensor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.example.lahirufernando.skinsensor.ImageInfo;
import com.example.lahirufernando.skinsensor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private final ArrayList<String> filesPaths;
    static final int IMAGE_GRID = 1;


    public ImageAdapter(Context context, ArrayList<String> filesPaths) {
        this.context = context;

        this.filesPaths = filesPaths;
    }

    @Override
    public int getCount() {
        return filesPaths.size();
    }

    @Override
    public Object getItem(int pos) {
        return pos;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int postion, View convertView, final ViewGroup parent) {
        parent.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        View grid;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            grid = inflater.inflate(R.layout.gallery_item, parent, false);
            //ImageView gridImage =  grid.findViewById(R.id.grid_image);
            //gridImage.setId(IMAGE_GRID);


        } else {
            grid = (View) convertView;
        }
        Button info = grid.findViewById(R.id.info_btn);
        ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
        imageView.setTag(postion);
        final int pos = postion;
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageInfo.class);
                intent.putExtra("uri", "file://" + filesPaths.get(pos));

                context.startActivity(intent);
            }
        });
        //textView.setText("jhgjhgjhgj");
        Picasso.with(context).load("file://" + filesPaths.get(postion)).noFade().resize(430, 430).centerCrop().into(imageView);
        return grid;
    }/*
        ImageView imageView;
        if (convertView == null) {

            imageView = new ImageView(context);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(context).load("file://" + filesPaths.get(postion)).noFade().resize(430, 430).centerCrop().into(imageView);
        return imageView;}

*/
}