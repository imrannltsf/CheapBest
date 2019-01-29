package com.takisoft.datetimepicker.sample.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.takisoft.datetimepicker.sample.CheapBestMainLogin;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.apputilss.SharedPref;

public class SplachApplication extends AppCompatActivity {
    private int SPLASH_TIME = 5000;
    String strStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  getSharedPreferences(getPackageName(), 0).edit().clear().apply();

        setContentView(R.layout.activity_splach_application);
        SharedPref.init(getApplicationContext());

        strStatus =SharedPref.read(SharedPref.IsLoginUser, "false");
       // Toast.makeText(SplachApplication.this, strStatus, Toast.LENGTH_SHORT).show();
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(SPLASH_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    if(strStatus.equalsIgnoreCase("true")){
                        Intent Send=new Intent(SplachApplication.this,MainDashBoard.class);
                        startActivity(Send);
                        finish();
                    }else{
                        Intent Send=new Intent(SplachApplication.this,CheapBestMainLogin.class);
                        startActivity(Send);
                        finish();
                    }
                 /*   Intent Send=new Intent(SplachApplication.this,CheapBestMainLogin.class);
                    startActivity(Send);
                    finish();*/
                }
            }
        };
        timer.start();
    }
}
