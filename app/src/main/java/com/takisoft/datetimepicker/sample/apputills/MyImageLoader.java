package com.takisoft.datetimepicker.sample.apputills;


import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MyImageLoader {
    private  Context context;

    public MyImageLoader(Context activity) {
        this.context=activity;
    }


    public void loadImage(String ImgUrl, ImageView imageView){
        Picasso.get().load(ImgUrl).into(imageView);
    }
}
