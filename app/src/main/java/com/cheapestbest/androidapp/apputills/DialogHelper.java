package com.cheapestbest.androidapp.apputills;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;

import com.cheapestbest.androidapp.CheapBestMainLogin;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.ui.Activity.MainDashBoard;

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



    public void showWarningDIalog(String ShowMessage,String StrConfirmText,String StrCancelText){
        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(R.drawable.logo_header)
                 .setTitleText("Login Required!")
                .setContentText(ShowMessage)
                .setConfirmText(StrConfirmText)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent Send=new Intent(context,CheapBestMainLogin.class);
                        context.startActivity(Send);
                    }
                }).setCancelText(StrCancelText).
                setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    //////////////////////location permission missing////////////

    public void gotopermission(){

        new SweetAlertDialog(context,  SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(R.drawable.logo_header)
                .setTitleText("Location Permission Denied!")
                .setContentText("You have to enable location permission for this")
                .setConfirmText("Enable")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);

                    }
                }).setCancelText("Cancel").
                setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();

    }

    ////////////////////////////no gps///////////

    public void buildAlertMessageNoGps() {

        new SweetAlertDialog(context,  SweetAlertDialog.WARNING_TYPE)
                //.setCustomImage(R.drawable.logo_header)
                .setTitleText("GPS Service Disabled!")
                .setContentText(context.getResources().getString(R.string.no_gps_message))
                .setConfirmText(context.getString(R.string.ok_no_gps))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                }).setCancelText(context.getResources().getString(R.string.no_no_gps)).
                setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();

       /* final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.titile_gps));
        builder.setMessage(context.getString(R.string.no_gps_message))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.ok_no_gps), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.no_no_gps), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        //showLocationMessage();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();*/
    }

}
