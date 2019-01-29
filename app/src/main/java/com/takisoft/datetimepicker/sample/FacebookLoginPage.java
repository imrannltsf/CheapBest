package com.takisoft.datetimepicker.sample;

import androidx.appcompat.app.AppCompatActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;

public class FacebookLoginPage extends AppCompatActivity {
    private static final String EMAIL = "email";
    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessToken accessToken;
    boolean isLoggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login_page);
        loginButton =  findViewById(R.id.login_button_my);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(FacebookLoginPage.this, "Success", Toast.LENGTH_SHORT).show();
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(FacebookLoginPage.this, "Cancel", Toast.LENGTH_SHORT).show();
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(FacebookLoginPage.this, "Error", Toast.LENGTH_SHORT).show();
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
                                    String Name = object.getString("name");

                                    String FEmail = object.getString("email");
                                    Log.v("Email = ", " " + FEmail);
                                    Toast.makeText(FacebookLoginPage.this, "Name " + Name, Toast.LENGTH_LONG).show();
                                    Toast.makeText(FacebookLoginPage.this, "Email " + FEmail, Toast.LENGTH_LONG).show();

                                } catch (JSONException e) {
                                    Toast.makeText(FacebookLoginPage.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(FacebookLoginPage.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn  = accessToken != null && !accessToken.isExpired();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
