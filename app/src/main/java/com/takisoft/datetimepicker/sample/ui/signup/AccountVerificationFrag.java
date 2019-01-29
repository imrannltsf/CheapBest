package com.takisoft.datetimepicker.sample.ui.signup;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.apputilss.MyImageLoader;
import com.takisoft.datetimepicker.sample.apputilss.SharedPref;
import com.takisoft.datetimepicker.sample.network.IResult;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.network.VolleyService;
import com.takisoft.datetimepicker.sample.ui.Activity.SetPasswordUser;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import spinkit.style.ChasingDots;

public class AccountVerificationFrag extends Fragment {

    private MyImageLoader myImageLoader;
    private EditText editTextCode;
    private Button buttonConfirm;
    private ImageView imageViewLoading;
    private ChasingDots mChasingDotsDrawable;
    private String UserID;
    private String response_status;
    @SuppressLint("NewApi")
    private VolleyService mVolleyService;
    private IResult mResultCallback;
    public static AccountVerificationFrag newInstance() {
        return new AccountVerificationFrag();
    }
    public static boolean newUserverify;
    private TextView textViewBack;
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

        myImageLoader=new MyImageLoader(getActivity());
        imageViewLoading= view.findViewById(R.id.image_loading_confirm);
        mChasingDotsDrawable = new ChasingDots();
        mChasingDotsDrawable.setColor(getResources().getColor(R.color.color_custom));
        imageViewLoading.setImageDrawable(mChasingDotsDrawable);
        editTextCode=view.findViewById(R.id.et_confirmcode);
        buttonConfirm=view.findViewById(R.id.btn_submit_code);
        textViewBack=view.findViewById(R.id.tv_back_confirm);


        buttonConfirm.setOnClickListener(view1 -> {
           String ss=editTextCode.getText().toString();
            if(!TextUtils.isEmpty(ss)){
                ConfirmCode = new HashMap< >();
                ConfirmCode.put("code",ss);
                AccountVerifyNewUserMethod();
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                if(AccountVerificationFrag.newUserverify){
                    AccountVerifyNewUserMethod();
                }else {
                    AccountVerifyMethod();
                }


            }else {
                Toast.makeText(getActivity(), "Please Enter Code:", Toast.LENGTH_SHORT).show();
            }
        });

        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    /*volley*/

   private void AccountVerifyMethod()
    {
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForSignUp();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        mVolleyService.postDataVolleyHeader("POSTCALL",NetworkURLs.BaseURL+NetworkURLs.VerifyNewUserURL,ConfirmCode);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("Old");
        builder.setMessage(NetworkURLs.BaseURL+NetworkURLs.VerifyNewUserURL);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();

    }

   private void initVolleyCallbackForSignUp(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                            Toast.makeText(getActivity(), "status found true", Toast.LENGTH_SHORT).show();

                            JSONObject signUpResponseModel = jsonObject.getJSONObject("data");
                            UserID= signUpResponseModel.getString("id");
                            response_status="true";

                        }else {
                            response_status="false";
                            JSONObject signUpResponseModels = jsonObject.getJSONObject("error");
                            UserID= signUpResponseModels.getString("message");

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(response_status.equalsIgnoreCase("false")){

                    myImageLoader.showErroDialog(UserID);

                    Toast.makeText(getActivity(), "Error occurer", Toast.LENGTH_SHORT).show();
                }else {

                    SharedPref.write(SharedPref.User_ID, UserID);
                    Toast.makeText(getActivity(), "Login Succssfully", Toast.LENGTH_SHORT).show();
                    Intent Send=new Intent(getActivity(),SetPasswordUser.class);
                    startActivity(Send);

                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);
                myImageLoader.showErroDialog(error.getMessage());
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
                    + " must implement AccountVerificationFrag.OnItemSelectedListener");
        }
    }


                /*for new user*/

    /*volley*/

   private void AccountVerifyNewUserMethod()
    {
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForNewUser();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        mVolleyService.postDataVolleyHeader("POSTCALL",NetworkURLs.BaseURL+NetworkURLs.VerifyNewUserURL,ConfirmCode);

      /*  AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("New");
        builder.setMessage(NetworkURLs.BaseURL+NetworkURLs.VerifyNewUserURL);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();*/
    }

   private void initVolleyCallbackForNewUser(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                            Toast.makeText(getActivity(), "status found true", Toast.LENGTH_SHORT).show();

                            JSONObject signUpResponseModel = jsonObject.getJSONObject("data");
                            UserID= signUpResponseModel.getString("id");
                            response_status="true";

                        }else {
                            response_status="false";
                            JSONObject signUpResponseModels = jsonObject.getJSONObject("error");
                            UserID= signUpResponseModels.getString("message");

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(response_status.equalsIgnoreCase("false")){

                    myImageLoader.showErroDialog(UserID);

                    Toast.makeText(getActivity(), "Error occurer status false", Toast.LENGTH_SHORT).show();
                }else {

                    SharedPref.write(SharedPref.User_ID, UserID);
                    Toast.makeText(getActivity(), "User Confirmed Succssfully", Toast.LENGTH_SHORT).show();
                    Intent Send=new Intent(getActivity(),SetPasswordUser.class);
                    startActivity(Send);
                    getActivity().finish();
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);
                myImageLoader.showErroDialog(error.getMessage());
            }
        };
    }

    public interface OnItemSelectedListener {
         void onAccountVerfyFragCallBack(int position);
    }

}
