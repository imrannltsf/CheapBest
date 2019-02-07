package com.cheapestbest.androidapp.ui.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.snackbar.Snackbar;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.apputills.SharedPref;
import com.cheapestbest.androidapp.CheapBestMainLogin;
import com.cheapestbest.androidapp.ui.Activity.MainDashBoard;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.HashMap;


public class CheapBestMainLoginFragment extends Fragment {
        private EditText etEmailLogin,etPasswordLogin;
        private String StrEmailLogin,StrPasswordLogin;

    private static final String EMAIL = "email";
    private LoginButton loginButton;
   public static CallbackManager callbackManager;
    private AccessToken accessToken;
    private LinearLayout layoutLoginMain;
    private boolean isLoggedIn;


    public static CheapBestMainLoginFragment newInstance() {
        return new CheapBestMainLoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        AppEventsLogger.activateApp(getActivity());
        return inflater.inflate(R.layout.cheap_best_main_login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initthisfrag(view);

    }

    private void initthisfrag(View view) {
        SharedPref.init(getActivity());
        layoutLoginMain=view.findViewById(R.id.login_frag_xml);
        loginButton =  view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        /*loginButton.setReadPermissions(Arrays.asList(EMAIL));*/
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();

                // Facebook Email address
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.v("LoginActivity Response ", response.toString());

                                try {

                                    CheapBestMainLogin.FbName = object.getString("name");
                                    CheapBestMainLogin.FbEmail = object.getString("email");
                                    CheapBestMainLogin.FbUID = object.getString("id");
                                  /*  CheapBestMainLogin.FbGender = object.getString("gender");
                                    CheapBestMainLogin.FbDob = object.getString("birthday");*/

                                    listener.onLoginFragCallBack(3);



                                } catch (JSONException e) {
                               //     Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn  = accessToken != null && !accessToken.isExpired();



     //   inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        TextView textViewForgotPassword = view.findViewById(R.id.tv_forgotpassword);
        etEmailLogin=view.findViewById(R.id.et_email_login);
        etPasswordLogin=view.findViewById(R.id.et_password_login);
        Button btnLogin = view.findViewById(R.id.btn_login_now);

        StrEmailLogin=SharedPref.read(SharedPref.UserEmail, "");
        StrPasswordLogin=SharedPref.read(SharedPref.UserPassword,"");
        etEmailLogin.setText(StrEmailLogin);
        etPasswordLogin.setText(StrPasswordLogin);

        etEmailLogin.setOnClickListener(view15 -> {
          /*  showKeyboard();
            etEmailLogin.setFocusableInTouchMode(true);
            etEmailLogin.setFocusable(true);
            etEmailLogin.requestFocus();*/

        });
        etPasswordLogin.setOnClickListener(view15 -> {
          /*  showKeyboard();
            etPasswordLogin.setFocusableInTouchMode(true);
            etPasswordLogin.setFocusable(true);
            etPasswordLogin.requestFocus();*/

        });

        etPasswordLogin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_DONE){
                  //  Toast.makeText(getActivity(), "Done Click", Toast.LENGTH_SHORT).show();
                    preform_loginaction();
                }
                return false;
            }
        });
        btnLogin.setOnClickListener(view12 -> {

            preform_loginaction();

           /* StrEmailLogin=etEmailLogin.getText().toString();
            StrPasswordLogin=etPasswordLogin.getText().toString();


            if(TextUtils.isEmpty(StrEmailLogin)){

                showsnackmessage("Please Enter Email For Sign In");
            }else if(TextUtils.isEmpty(StrPasswordLogin)){
                showsnackmessage("Please Enter Password For Sign In");

            }else {
                MainDashBoard.responseUid=StrEmailLogin;
                CheapBestMainLogin.SignInData = new HashMap< >();
                CheapBestMainLogin.SignInData.put("email",StrEmailLogin);
                CheapBestMainLogin.SignInData.put("password",StrPasswordLogin);
                CheapBestMainLogin.UserEmail=StrEmailLogin;
                CheapBestMainLogin.UserPass=StrPasswordLogin;
                listener.onLoginFragCallBack(2);

            }*/

        });
        textViewForgotPassword.setOnClickListener(view1 -> this.listener.onLoginFragCallBack(1));
    }

    public void preform_loginaction(){
        StrEmailLogin=etEmailLogin.getText().toString();
        StrPasswordLogin=etPasswordLogin.getText().toString();


        if(TextUtils.isEmpty(StrEmailLogin)){

            showsnackmessage("Please Enter Email For Sign In");
        }else if(TextUtils.isEmpty(StrPasswordLogin)){
            showsnackmessage("Please Enter Password For Sign In");

        }else {
          //  MainDashBoard.responseUid=StrEmailLogin;
            CheapBestMainLogin.SignInData = new HashMap< >();
            CheapBestMainLogin.SignInData.put("email",StrEmailLogin);
            CheapBestMainLogin.SignInData.put("password",StrPasswordLogin);
            CheapBestMainLogin.UserEmail=StrEmailLogin;
            CheapBestMainLogin.UserPass=StrPasswordLogin;
            listener.onLoginFragCallBack(2);

        }
    }

    private OnItemSelectedListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement CheapBestMainLoginFragment.OnItemSelectedListener");
        }
    }


    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        void onLoginFragCallBack(int position);
    }
    private void showsnackmessage(String msg){

        Snackbar snackbar = Snackbar
                .make(layoutLoginMain, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}
