package com.cheapestbest.androidapp.ui.Fragments.signup;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.apputills.SharedPref;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import com.cheapestbest.androidapp.ui.Activity.SetPasswordUser;
import com.cheapestbest.androidapp.apputills.Pinview;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AccountVerificationFrag extends Fragment {

    private OnItemSelectedListener listener;
    public static AccountVerificationFrag newInstance() {
        return new AccountVerificationFrag();
    }

    private RelativeLayout relativeLayoutMain;
    private Pinview pinview1;
    private Button buttonConfirm;
    private Progressbar progressbar;
    private String UserID;
    private String response_status;
    @SuppressLint("NewApi")
    private VolleyService mVolleyService;
    private IResult mResultCallback;
    private DialogHelper dialogHelper;
    public static boolean newUserverify;
    @SuppressLint("NewApi")
    public static Map<String, String> ConfirmCode;
    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_confirm_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initthisfrag(view);

    }

    private void initthisfrag(View view) {
        dialogHelper=new DialogHelper(getActivity());
        progressbar =new Progressbar(getActivity());
        relativeLayoutMain=view.findViewById(R.id.layout_confirm_user);
        pinview1= view.findViewById(R.id.pinview1);
        pinview1.setPinViewEventListener((pinview, fromUser) -> {

        });
        buttonConfirm=view.findViewById(R.id.btn_submit_code);
        buttonConfirm.setOnClickListener(view1 -> {
            String ss=pinview1.getValue();

            if(!TextUtils.isEmpty(ss)){
                ConfirmCode = new HashMap< >();
                ConfirmCode.put("code",ss);
                AccountVerifyMethod();
             /*   if(AccountVerificationFrag.newUserverify){
                    AccountVerifyNewUserMethod();
                }else {
                    AccountVerifyMethod();
                }*/

            }else {
                showsnackmessage("Please Enter Code:");

            }
        });
    }

    /*volley*/

   private void AccountVerifyMethod()
    {

            showprogress();
        initVolleyCallbackForCodeVerify();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        mVolleyService.postDataVolleyHeader("POSTCALL",NetworkURLs.VerifyNewUserURL,ConfirmCode);

    }

   private void initVolleyCallbackForCodeVerify(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                hideprogress();

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                            JSONObject signUpResponseModel = jsonObject.getJSONObject("data");
                            UserID= signUpResponseModel.getString("id");
                            response_status="true";
                            SharedPref.write(SharedPref.User_ID, UserID);
                            Intent Send=new Intent(getActivity(),SetPasswordUser.class);
                            startActivity(Send);
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
                 //   dialogHelper.showErroDialog(error_response);
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

                }
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener)context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement AccountVerificationFrag.OnItemSelectedListener");
        }
    }
    /*volley*/

   private void AccountVerifyNewUserMethod()
    {

            showprogress();
        initVolleyCallbackForNewUser();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        mVolleyService.postDataVolleyHeader("POSTCALL",NetworkURLs.BaseURL+NetworkURLs.VerifyNewUserURL,ConfirmCode);

    }

   private void initVolleyCallbackForNewUser(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                hideprogress();

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {


                            JSONObject signUpResponseModel = jsonObject.getJSONObject("data");
                            UserID= signUpResponseModel.getString("id");
                            response_status="true";

                            SharedPref.write(SharedPref.User_ID, UserID);

                            Intent Send=new Intent(getActivity(),SetPasswordUser.class);
                            startActivity(Send);
                            getActivity().finish();

                        }else {
                            response_status="false";
                            JSONObject signUpResponseModels = jsonObject.getJSONObject("error");
                            String ErrorMsg= signUpResponseModels.getString("message");
                            dialogHelper.showErroDialog(ErrorMsg);
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
                 //   dialogHelper.showErroDialog(error_response);
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

                }
            }
        };
    }

    public interface OnItemSelectedListener {
         void onAccountVerfyFragCallBack(int position);
    }


    public void showprogress(){

        progressbar.ShowProgress();
        progressbar.setCancelable(false);

    }

    public void hideprogress(){
        progressbar.HideProgress();

    }
    private void showsnackmessage(String msg){

        Snackbar snackbar = Snackbar
                .make(relativeLayoutMain, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}
