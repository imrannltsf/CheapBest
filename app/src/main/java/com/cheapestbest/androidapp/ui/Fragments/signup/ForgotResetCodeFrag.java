package com.cheapestbest.androidapp.ui.Fragments.signup;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import com.cheapestbest.androidapp.apputills.Pinview;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgotResetCodeFrag extends Fragment {
    private Progressbar progressbar;
    private Pinview pinview1;
    private TextView tvback;

    private Button buttonConfirm;

    @SuppressLint("NewApi")
   private VolleyService mVolleyService;
    private IResult mResultCallback;
    private RelativeLayout relativeLayoutXml;
    @SuppressLint("NewApi")
    public static Map<String, String> ConfirmCode;
    @SuppressLint("NewApi")
    private DialogHelper dialogHelper;

    InputMethodManager imm;

    public static ForgotResetCodeFrag newInstance() {
        return new ForgotResetCodeFrag();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reset_password_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initthisfrag(view);

    }

    private void initthisfrag(View view) {
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        dialogHelper=new DialogHelper(getActivity());
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        progressbar =new Progressbar(getActivity());
        relativeLayoutXml=view.findViewById(R.id.forgot_xml);
        buttonConfirm=view.findViewById(R.id.btn_submit_code_reset);
        tvback=view.findViewById(R.id.tv_back_reset_pass);
        pinview1= view.findViewById(R.id.pinview1);
        pinview1.requestFocus();
        pinview1.setPinViewEventListener((pinview, fromUser) -> {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        });
        pinview1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return false;
            }
        });


      //  Toast.makeText(getActivity(), String.valueOf(pinvi), Toast.LENGTH_SHORT).show();
        pinview1.onKey(pinview1.getFocusedChild(), KeyEvent.KEYCODE_DEL, new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DEL));
       // pinview1.onKey(pinview1.getFocusedChild(), KeyEvent.KEYCODE_DEL, new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DEL));
       /* pinview1.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {

                Toast.makeText(getActivity(), String.valueOf(event.getAction()), Toast.LENGTH_SHORT).show();
               *//* if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:

                            return true;
                        default:
                            break;
                    }
                }*//*
                return false;
            }
        });*/


        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onResetCodeFragCallBack(2);
            }
        });

        buttonConfirm.setOnClickListener(view1 -> {
            String ss=pinview1.getValue();
            if(!TextUtils.isEmpty(ss)){/*
                if(isUserRegestered){
                    ConfirmCode = new HashMap< >();
                    ConfirmCode.put("reset_password_token",ss);
                }else {
                    ConfirmCode = new HashMap< >();
                    ConfirmCode.put("code",ss);
                }*/
                AccountVerificationFrag.newUserverify=false;
                if(ss.length()==6){
                    ConfirmCode = new HashMap< >();
                    ConfirmCode.put("reset_password_token",ss);
                    SendResetCodeResetRequest();
                }else {
                    showsnackmessage("Enter Valid Code:");
                }


            }else {
                showsnackmessage("Please Enter Code:");

            }
        });
    }

    /*volley*/

    private void SendResetCodeResetRequest()
    {
       showprogress();
        initVolleyCallbackForSendResetCode();
         mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.postDataVolley("POSTCALL",NetworkURLs.EmailVerifyCodeUrl,ConfirmCode);
       /* if(!isUserRegestered){
            mVolleyService.postDataVolley("POSTCALL",NetworkURLs.VerifyNewUserURL,ConfirmCode);
        }else {
            mVolleyService.postDataVolley("POSTCALL",NetworkURLs.EmailVerifyCodeUrl,ConfirmCode);
        }*/



    }

    private void initVolleyCallbackForSendResetCode(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                hideprogress();

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                            JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                            String StrMessage=DataRecivedObj.getString("message");
                            //dialogHelper.showDialogAlert(StrMessage);
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText(StrMessage)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            listener.onResetCodeFragCallBack(1);
                                        }
                                    })
                                    .show();
                            /*listener.onResetCodeFragCallBack(1);*/

                        }
                        else {
                            JSONObject DataRecivedObj = jsonObject.getJSONObject("error");
                            String StrMessage=DataRecivedObj.getString("message");
                            dialogHelper.showErroDialog(StrMessage);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {

               hideprogress();

                if(error.networkResponse != null && error.networkResponse.data != null){

                    String error_response=new String(error.networkResponse.data);
                //    dialogHelper.showErroDialog(error_response);

                    try {
                        JSONObject response_obj=new JSONObject(error_response);

                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");
                            dialogHelper.showErroDialog(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    dialogHelper.showErroDialog("Something went wrong please try again");
                }
            }

        };
    }
    private OnItemSelectedListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener)context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement ForgotResetCodeFrag.OnItemSelectedListener");
        }
    }

    public interface OnItemSelectedListener {
         void onResetCodeFragCallBack(int position);
    }


    public void showprogress(){
        progressbar.ShowProgress();
        progressbar.setCancelable(false);
    }

    public void hideprogress(){
       // pinview1.clearValue();
        progressbar.HideProgress();
    }

    private void showsnackmessage(String msg){

        Snackbar snackbar = Snackbar
                .make(relativeLayoutXml, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    private void clearPinViewChild() {
        for (int i = 0; i < pinview1.getChildCount() ; i++) {
            EditText child = (EditText) pinview1.getChildAt(i);
            child.setText("");

        }
    }
}
