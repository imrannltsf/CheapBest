package com.cheapestbest.androidapp.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.cheapestbest.androidapp.CheapBestMainLogin;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.apputills.SharedPref;

public class SplachApplication extends AppCompatActivity {
    private int SPLASH_TIME = 4000;
    boolean strStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getSharedPreferences(getPackageName(), 0).edit().clear().apply();
        setContentView(R.layout.activity_splach_application);
        SharedPref.init(getApplicationContext());

        strStatus =SharedPref.readBol(SharedPref.IsLoginUser, false);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(SPLASH_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    if(strStatus){
                        Intent Send=new Intent(SplachApplication.this,MainDashBoard.class);
                        startActivity(Send);
                        finish();
                    }else{
                        Intent Send=new Intent(SplachApplication.this,CheapBestMainLogin.class);
                        startActivity(Send);
                        finish();
                    }
                  /*  Intent Send=new Intent(SplachApplication.this,CheapBestMainLogin.class);
                    startActivity(Send);
                    finish();*/
                }
            }
        };
        timer.start();
    }
}
