package com.cheapestbest.androidapp.apputills;

import android.content.Context;
import android.text.TextUtils;

import com.cheapestbest.androidapp.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogHelper {
    private Context context;

    public DialogHelper(Context activity) {
        this.context=activity;
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
    public void showDialogAlertSuccess(String StrCode){
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success!")
                .setContentText(StrCode)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();

    }

}
