package com.cheapestbest.androidapp.apputills;


import android.content.Context;
import android.widget.ImageView;

import com.cheapestbest.androidapp.R;
import com.squareup.picasso.Picasso;

public class MyImageLoader {
    private  Context context;

    public MyImageLoader(Context activity) {
        this.context=activity;
    }


    public void loadImage(String ImgUrl, ImageView imageView){
        Picasso.get().load(ImgUrl).placeholder(R.color.grey).into(imageView);
    }
}
