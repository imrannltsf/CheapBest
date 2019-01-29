package com.takisoft.datetimepicker.sample.apputilss;


import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.ui.Activity.CoupanRedeeem;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyImageLoader {
    private  Context context;

    public MyImageLoader(Context activity) {
        this.context=activity;
    }


    public void loadImage(String ImgUrl, ImageView imageView){
        Picasso.get().load(ImgUrl).into(imageView);
    }

    public void showErroDialog(String ErrorCode){
        if(TextUtils.isEmpty(ErrorCode)||ErrorCode.equalsIgnoreCase("null")){
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Some thing found null")
                    .show();
        }else {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText(ErrorCode)
                    .show();
        }

    }


    public void showDialog(String StrCode){
        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(StrCode)
                .setContentText("Here's your code.")
                .setCustomImage(R.drawable.ic_qr_code)
                .show();

    }
    public void showDialogAlert(String StrCode){
        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Note")
                .setContentText(StrCode)
                .setCustomImage(R.drawable.ic_qr_code)
                .show();

    }

}
