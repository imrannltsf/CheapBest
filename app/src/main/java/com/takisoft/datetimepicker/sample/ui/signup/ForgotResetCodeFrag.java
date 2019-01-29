package com.takisoft.datetimepicker.sample.ui.signup;


import android.annotation.SuppressLint;
import android.content.Context;
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
import com.takisoft.datetimepicker.sample.network.IResult;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.network.VolleyService;
import com.takisoft.datetimepicker.sample.ui.utills.Pinview;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import spinkit.style.ChasingDots;

public class ForgotResetCodeFrag extends Fragment {
    private Pinview pinview1;
    TextView tvback;
    private MyImageLoader myImageLoader;
    //private EditText editTextCode;
    private Button buttonConfirm;
    private ImageView imageViewLoading;
    private ChasingDots mChasingDotsDrawable;
    String UserID;
    String response_status;
    @SuppressLint("NewApi")
    VolleyService mVolleyService;
    IResult mResultCallback;
    @SuppressLint("NewApi")
    public static Map<String, String> ConfirmCode;
    @SuppressLint("NewApi")

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

        myImageLoader=new MyImageLoader(getActivity());
        imageViewLoading= view.findViewById(R.id.image_loading_forgot_reset);
        mChasingDotsDrawable = new ChasingDots();

        mChasingDotsDrawable.setColor(getResources().getColor(R.color.color_custom));
        imageViewLoading.setImageDrawable(mChasingDotsDrawable);
        //editTextCode=view.findViewById(R.id.et_confirmcode_resetpass);
        buttonConfirm=view.findViewById(R.id.btn_submit_code_reset);
        tvback=view.findViewById(R.id.tv_back_reset_pass);
        pinview1= view.findViewById(R.id.pinview1);
        pinview1.setPinViewEventListener((pinview, fromUser) -> {
            //Toast.makeText(MainActivity.this, pinview.getValue(), Toast.LENGTH_SHORT).show();
        });
        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onResetCodeFragCallBack(2);
            }
        });

        buttonConfirm.setOnClickListener(view1 -> {
            String ss=pinview1.getValue();
            Toast.makeText(getActivity(),pinview1.getValue(), Toast.LENGTH_SHORT).show();
            /*if(!TextUtils.isEmpty(ss)){
                ConfirmCode = new HashMap< >();
                ConfirmCode.put("reset_password_token",ss);
                SendResetCodeResetRequest();

            }else {
                Toast.makeText(getActivity(), "Please Enter Code:", Toast.LENGTH_SHORT).show();
            }*/
        });
    }


    /*volley*/

    private void SendResetCodeResetRequest()
    {
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForSendResetCode();
         mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.PostReqquestVolleyWithoutHeader("POSTCALL",NetworkURLs.EmailVerifyCodeUrl,ConfirmCode);
    }

    private void initVolleyCallbackForSendResetCode(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                // dismissporgress();
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                            JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                            String StrMessage=DataRecivedObj.getString("message");
                            myImageLoader.showDialogAlert(StrMessage);

                            listener.onResetCodeFragCallBack(1);
                           /* Intent Send=new Intent(getActivity(),SetPasswordUser.class);
                            startActivity(Send);*/
                        }else {
                            JSONObject DataRecivedObj = jsonObject.getJSONObject("error");
                            String StrMessage=DataRecivedObj.getString("message");
                            myImageLoader.showErroDialog(StrMessage);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);
                myImageLoader.showErroDialog(error.getMessage());
                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    myImageLoader.showErroDialog(error_response);

                    try {
                        JSONObject response_obj=new JSONObject(error_response);

                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            myImageLoader.showErroDialog(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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

}
