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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.ui.Activity.MainDashBoard;
import com.google.android.material.snackbar.Snackbar;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.apputills.SharedPref;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import com.cheapestbest.androidapp.CheapBestMainLogin;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SetPasswordFragment extends Fragment {

    public static SetPasswordFragment newInstance() {
        return new SetPasswordFragment();
    }
    private OnItemSelectedListener listener;

    private DialogHelper dialogHelper;
    private LinearLayout layoutFrag;
    private EditText editTextCode,editTextRe;
    private Button buttonConfirm;
    private Progressbar progressbar;
    private String UserID;
    private TextView tvback;
    /*private String response_status;*/
    @SuppressLint("NewApi")
    private VolleyService mVolleyService;
    private IResult mResultCallback;
    @SuppressLint("NewApi")
    public static Map<String, String> ConfirmPass;
    public String StrNewPassword;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_set_password_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initthisfrag(view);

    }

    private void initthisfrag(View view) {
       // Toast.makeText(getActivity(), "Reset Code", Toast.LENGTH_SHORT).show();
        dialogHelper=new DialogHelper(getActivity());
        layoutFrag=view.findViewById(R.id.layout_set_pass);
        tvback=view.findViewById(R.id.tv_back_forgotpassword);
        progressbar =new Progressbar(getActivity());
        editTextCode=view.findViewById(R.id.et_setpassword);
        editTextRe=view.findViewById(R.id.et_setpassword_res);
        buttonConfirm=view.findViewById(R.id.btn_submit_password);
        editTextRe=view.findViewById(R.id.et_setpassword_res);
        buttonConfirm.setOnClickListener(view1 -> {
            StrNewPassword=editTextCode.getText().toString();
            String PasswordConfirm=editTextRe.getText().toString();
            if(!TextUtils.isEmpty(StrNewPassword)&&!TextUtils.isEmpty(PasswordConfirm)){

                if(StrNewPassword.equals(PasswordConfirm)){
                    ConfirmPass = new HashMap< >();
                    ConfirmPass.put("password",StrNewPassword);
                    ConfirmPass.put("password_confirmation",StrNewPassword);

                    PutMethod();
                }else {
                    showsnackmessage("Password Mis Match");
                }
            }else {
                showsnackmessage("Please Enter Password:");
            }
        });
        tvback.setOnClickListener(view12 -> {
            Intent Send=new Intent(getActivity(),CheapBestMainLogin.class);
            startActivity(Send);
            getActivity().finish();
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener)context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement SetPasswordFragment.OnItemSelectedListener");
        }
    }


    /*volley*/

   private void PutMethod()
    {

            showprogress();
        initVolleyCallbackForSignUp();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        mVolleyService.PutReqquestVolley("PUTCALL",NetworkURLs.BaseURL+NetworkURLs.SetPasswordUserURL,ConfirmPass);
    }

   private void initVolleyCallbackForSignUp(){
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
                            /*response_status="true";*/
                            SharedPref.write(SharedPref.UserPassword, StrNewPassword);
                            SharedPref.write(SharedPref.User_ID, UserID);
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Password Reset Succssfully")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            Intent Send=new Intent(getActivity(),MainDashBoard.class);
                                            startActivity(Send);
                                            getActivity().finish();
                                        }
                                    })
                                    .show();


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
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                   // dialogHelper.showErroDialog(error_response);

                    try {
                        JSONObject response_obj=new JSONObject(error_response);

                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");
                            showsnackmessage(message);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
    }

    public interface OnItemSelectedListener {
         void onPassFragCallBack(int position);
    }


    private void showsnackmessage(String msg){

        Snackbar snackbar = Snackbar
                .make(layoutFrag, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
    public void showprogress(){
        progressbar.ShowProgress();
        progressbar.setCancelable(false);

    }
    public void hideprogress(){
        progressbar.HideProgress();
    }
}
