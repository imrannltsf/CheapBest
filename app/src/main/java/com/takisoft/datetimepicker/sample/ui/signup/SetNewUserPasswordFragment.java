package com.takisoft.datetimepicker.sample.ui.signup;


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
import com.takisoft.datetimepicker.sample.CheapBestMainLogin;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import spinkit.style.ChasingDots;

public class SetNewUserPasswordFragment extends Fragment {

    public static SetNewUserPasswordFragment newInstance() {
        return new SetNewUserPasswordFragment();
    }
    private TextView tvback;
    private MyImageLoader myImageLoader;
    private EditText editTextCode,editTextRe;
    private Button buttonConfirm;
    private ImageView imageViewLoading;
    private ChasingDots mChasingDotsDrawable;
    private String UserID;
    private String response_status;
    @SuppressLint("NewApi")
    private VolleyService mVolleyService;
    private IResult mResultCallback;
    @SuppressLint("NewApi")
    public static Map<String, String> ConfirmPass;
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
        myImageLoader=new MyImageLoader(getActivity());
        imageViewLoading=  view.findViewById(R.id.image_loading_password);
        mChasingDotsDrawable = new ChasingDots();
        mChasingDotsDrawable.setColor(getResources().getColor(R.color.color_custom));
        imageViewLoading.setImageDrawable(mChasingDotsDrawable);
        editTextCode=view.findViewById(R.id.et_setpassword);
        tvback=view.findViewById(R.id.tv_back_forgotpassword);
        buttonConfirm=view.findViewById(R.id.btn_submit_password);

        editTextRe=view.findViewById(R.id.et_setpassword_res);


        buttonConfirm.setOnClickListener(view1 -> {
            String ss=editTextCode.getText().toString();
            String sb=editTextRe.getText().toString();
            if(!TextUtils.isEmpty(ss)&&!TextUtils.isEmpty(sb)){

                if(ss.equals(sb)){
                    ConfirmPass = new HashMap< >();
                    ConfirmPass.put("password",ss);
                    ConfirmPass.put("password_confirmation",ss);

                    PutMethod();
                }else {
                    myImageLoader.showDialogAlert("Password Mis Match");
                }


            }else {
                Toast.makeText(getActivity(), "Please Enter Password:", Toast.LENGTH_SHORT).show();
            }
        });

        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Send=new Intent(getActivity(),CheapBestMainLogin.class);
                startActivity(Send);
                getActivity().finish();
            }
        });
    }


    private OnItemSelectedListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener)context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement SetNewUserPasswordFragment.OnItemSelectedListener");
        }
    }


    /*volley*/

   private void PutMethod()
    {
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForSignUp();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        mVolleyService.PutReqquestVolley("PUTCALL",NetworkURLs.BaseURL+NetworkURLs.SetPasswordUserURL,ConfirmPass);
    }

    void initVolleyCallbackForSignUp(){
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

                            SharedPref.write(SharedPref.User_ID, UserID);
                            Toast.makeText(getActivity(), "Password Reset Succssfully", Toast.LENGTH_SHORT).show();
                            Intent Send=new Intent(getActivity(),CheapBestMainLogin.class);
                            startActivity(Send);
                            getActivity().finish();

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

                    Toast.makeText(getActivity(), UserID, Toast.LENGTH_SHORT).show();
                }else {

                    SharedPref.write(SharedPref.User_ID, UserID);
                    Toast.makeText(getActivity(), "Password Reset Succssfully", Toast.LENGTH_SHORT).show();
                    Intent Send=new Intent(getActivity(),CheapBestMainLogin.class);
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
         void onPassFragCallBack(int position);
    }

}
