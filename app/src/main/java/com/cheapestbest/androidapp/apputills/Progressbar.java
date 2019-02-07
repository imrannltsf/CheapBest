package com.cheapestbest.androidapp.apputills;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.cheapestbest.androidapp.R;


public class Progressbar extends Dialog  {

    public Context c;
    public Dialog d;

    public Progressbar(Context a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress_dialog);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void ShowProgress()
    {
        show();

    }

    public void HideProgress()
    {

        dismiss();
    }

  /*  public void ShowConfirmation()
    {
        show();

    }

    public void HideConfirmation()
    {

        dismiss();
    }*/


}