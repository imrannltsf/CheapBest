package com.cheapestbest.androidapp.ui.Fragments.signup;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;
import com.cheapestbest.androidapp.CheapBestMainLogin;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.MyImageLoader;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.apputills.SharedPref;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private RelativeLayout relativeLayoutMain;

    private AccessToken accessToken;
    private boolean isLoggedIn;
    private IResult mResultCallback;

    private TextView tvUserName;
    private TextView tvUsedCoupon;
    private TextView tvSavedCoupon;
    private TextView tvEamil;
    private TextView tvPhone;
    private TextView tvDob;
    private TextView tvJoinDate;
    private CircleImageView circleImageView;
    private ImageView imageViewLogout;
    private Button BtnUpdateProfile;
    private RelativeLayout layout_name_dlg,layout_ph_dlg,layout_dob_dlg;
    private EditText et_p_name_dlg,et_ph_dlg;
    TextView et_dob_dlg;
    private ImageView ImgClearName,ImgClearPh,ImgClearDOB;
    /**/
    private TextView TvHintName_dlg,TvHintPh_dlg,TvHintDOB_dlg;
  //  private TextView TvUserAddress;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }
    public static OnProfileSelectedListener listener;
    private static String UserID,UserName,UserEmail,UserPic,UserDOB,UserGender,UserPh,UserSavedCoupons,UserUsedCoupon,UserJoinDate;
    private MyImageLoader myImageLoader;
    private DialogHelper dialogHelper;
    private ImageView ImgUpdateUser;
    ///////////////////////////////////////
    private String MyDOB;
    private Calendar myCalendar ;
    private Spinner spinner;

    private InputMethodManager inputMethodManager;
    private String[] gender = {"Gender","Male", "Female"};
    private ArrayAdapter<String> adapter;

    private String response_status;
    @SuppressLint("NewApi")
    private VolleyService mVolleyService;

    private Progressbar progressbar;
    @SuppressLint("NewApi")
    public static Map<String, String>UpdateUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initthisfrag(view);
    }

    private void initthisfrag(View view) {

        SharedPref.init(getActivity());
        relativeLayoutMain=view.findViewById(R.id.profile_xml);
        myImageLoader=new MyImageLoader(getActivity());
        dialogHelper=new DialogHelper(getActivity());
        Typeface myTypeFace = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/roboto_bold.ttf");
        TextView tvFontTextView = view.findViewById(R.id.tv_coupan_header);
        tvFontTextView.setTypeface(myTypeFace);
        progressbar =new Progressbar(getActivity());
        ImgUpdateUser=view.findViewById(R.id.img_user_update);
        imageViewLogout=view.findViewById(R.id.img_logout_user);
        tvUserName=view.findViewById(R.id.tv_name_profile);
        tvUsedCoupon=view.findViewById(R.id.tv_used_coupon);
        tvSavedCoupon=view.findViewById(R.id.tv_saved_coupon);
        tvEamil=view.findViewById(R.id.email_profile);
        tvPhone=view.findViewById(R.id.ph_profile);
        tvDob=view.findViewById(R.id.dob_profile);
      //  TvUserAddress=view.findViewById(R.id.user_address);
        tvJoinDate=view.findViewById(R.id.join_date_profile);
        circleImageView=view.findViewById(R.id.profile_image);

        imageViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor pref = getActivity().getSharedPreferences(getActivity().getPackageName(), 0).edit();
                pref.clear();
                pref.apply();

                Intent Send=new Intent(getActivity(),CheapBestMainLogin.class);
                startActivity(Send);
                getActivity().finish();
                accessToken = AccessToken.getCurrentAccessToken();
                isLoggedIn  = accessToken != null && !accessToken.isExpired();
                 SharedPref.write(SharedPref.FBLogin,"false");
                    LoginManager.getInstance().logOut();
               /* if(isLoggedIn){
                    SharedPref.write(SharedPref.FBLogin,"false");
                    LoginManager.getInstance().logOut();
                    Toast.makeText(getActivity(), "Sign Out Succesfully", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor pref = getActivity().getSharedPreferences(getActivity().getPackageName(), 0).edit();
                    pref.clear();
                    pref.apply();
                    Intent Send=new Intent(getActivity(),CheapBestMainLogin.class);
                    startActivity(Send);
                    getActivity().finish();

                }else {

                    SignOut();

                }*/

            }
        });


        ImgUpdateUser.setOnClickListener(view14 -> {
                Dialog dialog=new Dialog(getActivity());
                dialog.setCancelable(true);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.update_profile_fragment);



           myCalendar= Calendar.getInstance();
            spinner = dialog.findViewById(R.id.gender_sp_update);
            addSpinerForGender();
            BtnUpdateProfile=dialog.findViewById(R.id.btn_update_profile);
                layout_name_dlg=dialog.findViewById(R.id.layout_name_dlg_update);
                 layout_ph_dlg=dialog.findViewById(R.id.layout_ph_dlg_update);
                layout_dob_dlg=dialog.findViewById(R.id.layout_category_name_dlg);

                et_p_name_dlg=dialog.findViewById(R.id.et_name_dlg_update_update);
                et_ph_dlg=dialog.findViewById(R.id.et_ph_dlg_update);
                et_dob_dlg=dialog.findViewById(R.id.et_dob_dlg_update);

                ImgClearName=dialog.findViewById(R.id.img_cancel_pname_dlg_update);
                ImgClearPh=dialog.findViewById(R.id.img_cancel_ph_dlg_update);
                ImgClearDOB=dialog.findViewById(R.id.img_cancel_dob_dlg_update);

                TvHintName_dlg=dialog.findViewById(R.id.name_dlg_update);
                TvHintPh_dlg=dialog.findViewById(R.id.ph_hint_dlg_update);
                TvHintDOB_dlg=dialog.findViewById(R.id.dob_hint_dlg);


                et_p_name_dlg.setText(UserName);
                et_ph_dlg.setText(UserPh);
                et_dob_dlg.setText(UserDOB);

           if ( UserGender.equalsIgnoreCase("male")) {

                spinner.setSelection(1);

            }else if(UserGender.equalsIgnoreCase("female")){
                spinner.setSelection(2);

            }

            ImgClearName.setOnClickListener(view16 -> {
                    if(!TextUtils.isEmpty(et_p_name_dlg.getText().toString())){
                        et_p_name_dlg.getText().clear();
                    }
                });
            ImgClearPh.setOnClickListener(view17 -> {
                    if(!TextUtils.isEmpty(et_ph_dlg.getText().toString())){
                        et_ph_dlg.getText().clear();
                    }
                });
            ImgClearDOB.setOnClickListener(view18 -> {

                    if(!TextUtils.isEmpty(et_dob_dlg.getText().toString())){
                        et_dob_dlg.setText(" ");
                    }
                });



            DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            };
            et_p_name_dlg.setOnFocusChangeListener((view13, hasFocus) -> {
                if (hasFocus) {
                    layout_name_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    layout_ph_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_dob_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);

                    TvHintName_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_custom));
                    TvHintPh_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    TvHintDOB_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));

                } else {
                    layout_name_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_ph_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    layout_dob_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);

                    TvHintName_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    TvHintPh_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_custom));
                    TvHintDOB_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));

                }
            });

            et_ph_dlg.setOnFocusChangeListener((view12, hasFocus) -> {
                if (hasFocus) {
                    layout_name_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_ph_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    layout_dob_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);

                    TvHintName_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    TvHintPh_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_custom));
                    TvHintDOB_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    // Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
                } else {
                    layout_name_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_ph_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_dob_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);

                    TvHintName_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    TvHintPh_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    TvHintDOB_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_custom));
                }
            });
            et_dob_dlg.setInputType(InputType.TYPE_NULL);
            et_dob_dlg.requestFocus();

               et_dob_dlg.setOnFocusChangeListener((view1, hasFocus) -> {
                    if (hasFocus) {
                        layout_name_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                        layout_ph_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                        layout_dob_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);

                        TvHintName_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                        TvHintPh_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                        TvHintDOB_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_custom));

                        et_dob_dlg.setFocusableInTouchMode(true);
                        et_dob_dlg.setFocusable(true);
                        et_dob_dlg.requestFocus();
                        new DatePickerDialog(getActivity(), date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                    } else {
                        layout_name_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                        layout_ph_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                        layout_dob_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);

                        TvHintName_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_custom));
                        TvHintPh_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                        TvHintDOB_dlg.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    }

                });
                  /* et_dob_dlg.setInputType(InputType.TYPE_NULL);
                   et_dob_dlg.requestFocus();*/
                 et_dob_dlg.setOnClickListener(view141 -> {
                     et_dob_dlg.setFocusableInTouchMode(true);
                     et_dob_dlg.setFocusable(true);
                     et_dob_dlg.requestFocus();
                     new DatePickerDialog(getActivity(), date, myCalendar
                             .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                             myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                 });




            BtnUpdateProfile.setOnClickListener(view19 -> {


                String User_name=et_p_name_dlg.getText().toString();
                String User_Ph=et_ph_dlg.getText().toString();
                String Gender=spinner.getSelectedItem().toString().toLowerCase();
                 MyDOB=et_dob_dlg.getText().toString();

                if(TextUtils.isEmpty(User_name)||User_name.equalsIgnoreCase("null")){
                    showsnackmessage("Please Fill Name");

                }else  if(TextUtils.isEmpty(User_Ph)||User_Ph.equalsIgnoreCase("null")){
                    showsnackmessage("Please Fill Mobile Number");

                }else  if(TextUtils.isEmpty(MyDOB)||MyDOB.equalsIgnoreCase("null")){
                    showsnackmessage("Please Select Date of Birth");

                }else  if(TextUtils.isEmpty(Gender)||Gender.equalsIgnoreCase("null")||Gender.equalsIgnoreCase("gender")){
                    showsnackmessage("Please Select Gender");

                }else {

                    UserDOB=MyDOB;
                    UserPh=User_Ph;
                     UserGender=Gender;
                     UserName=User_name;

                    if(check_date_validity(UserDOB)){
                        showsnackmessage("Date of birth is invalid ");
                    }else {


                    UpdateUser = new HashMap< >();
                    UpdateUser.put("user[name]",User_name);
                   // UpdateUser.put("user[display_picture]","");
                    UpdateUser.put("user[phone_number]",UserPh);
                    UpdateUser.put("user[dob]",MyDOB);
                    UpdateUser.put("user[gender]",Gender);

                    dialog.dismiss();
                    UpdateUserPutMethod();
                    }
                }

                });
           /*      dialog.setOnKeyListener((arg0, keyCode, event) -> {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        dialog.dismiss();
                    }
                    return true;
                });
*/
                dialog.show();


        });
        GetUserProfileData();


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnProfileSelectedListener){      // context instanceof YourActivity
           listener = (OnProfileSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement ProfileFragment.OnItemSelectedListener");
        }
    }


    // Define the events that the fragment will use to communicate
    public interface OnProfileSelectedListener {

        void onProfileFragCallBack(int position);
    }
    /* volley*/

    private void GetUserProfileData()
    {

      //  Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.User_ID,"0")), Toast.LENGTH_SHORT).show();
       showprogress();
        initVolleyCallbackForProfile();
        VolleyService mVolleyService = new VolleyService(mResultCallback, getActivity());
         mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.BaseURL+NetworkURLs.UserProfile+SharedPref.read(SharedPref.User_ID,"0")+".json");
          //  showsnackmessage(SharedPref.read(SharedPref.User_ID,"0"));
       // mVolleyService.getDataVolley("GETCALL",NetworkURLs.BaseURL+NetworkURLs.UserProfile+"6.json");

    }

    private void initVolleyCallbackForProfile(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                hideprogress();

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {




                            JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                            JSONObject PrfoileObj = DataRecivedObj.getJSONObject("user");


                            try {









                                if ( PrfoileObj.has("id")) {
                                    UserID=PrfoileObj.getString("id");
                                }else {
                                    UserID="";
                                }
                                if ( PrfoileObj.has("name")) {
                                    UserName=PrfoileObj.getString("name");
                                }else {
                                    UserName="";
                                }
                                if ( PrfoileObj.has("email")) {
                                    UserEmail=PrfoileObj.getString("email");
                                }else {
                                    UserEmail="";
                                }
                                if ( PrfoileObj.has("display_picture")) {
                                    UserPic=PrfoileObj.getString("display_picture");
                                }else {
                                    UserPic="";
                                }
                                if ( PrfoileObj.has("gender")) {
                                    UserGender=PrfoileObj.getString("gender");
                                }else {
                                    UserGender="";
                                }
                                if ( PrfoileObj.has("joining_date")) {
                                    UserJoinDate=PrfoileObj.getString("joining_date");
                                }else {
                                    UserJoinDate="";
                                }
                                if ( PrfoileObj.has("saved_coupons_count")) {
                                    UserSavedCoupons=PrfoileObj.getString("saved_coupons_count");
                                }else {
                                    UserSavedCoupons="";
                                }
                                if ( PrfoileObj.has("redeemed_coupons_count")) {
                                    UserUsedCoupon=PrfoileObj.getString("redeemed_coupons_count");
                                }else {
                                    UserUsedCoupon="";
                                }

                                tvUserName.setText(UserName);
                                tvEamil.setText(UserEmail);
                                tvJoinDate.setText(UserJoinDate);
                                tvSavedCoupon.setText(UserSavedCoupons);
                                tvUsedCoupon.setText(UserUsedCoupon);
                               // tvPhone.setText(UserPh);

                                     //   boolean str=SharedPref.readBol(SharedPref.FBLogin,false);

                               // Toast.makeText(getActivity(), String.valueOf(SharedPref.readBol(SharedPref.FBLogin,false)), Toast.LENGTH_SHORT).show();
                               /* if(SharedPref.readBol(SharedPref.FBLogin,false)){
                                    UserDOB="";
                                    UserPh="";
                                }else {

                                }*/


                                /*try {*/
                                    if ( PrfoileObj.has("dob")) {
                                        UserDOB=PrfoileObj.getString("dob");
                                    }else {
                                        UserDOB="";
                                    }
                                    if ( PrfoileObj.has("phone_number")) {
                                        UserPh=PrfoileObj.getString("phone_number");
                                    }else {
                                        UserPh="";
                                    }

                                   /* UserDOB=PrfoileObj.getString("dob");
                                    UserPh=PrfoileObj.getString("phone_number");*/

                               // Toast.makeText(getActivity(), UserDOB+","+UserPh, Toast.LENGTH_SHORT).show();
                                    tvDob.setText(UserDOB);
                                    tvPhone.setText(UserPh);
                              /*  } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/

                                if (!UserPic.equalsIgnoreCase("")&&!UserPic.equalsIgnoreCase("null")){

                                    myImageLoader.loadImage(NetworkURLs.BaseURLImages+UserPic,circleImageView);
                                    }else {

                                    circleImageView.setBackgroundResource(R.drawable.ic_profile_avatar);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }else {
                            dialogHelper.showErroDialog("status false");
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    dialogHelper.showErroDialog("Response null");
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {

                    hideprogress();
                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    dialogHelper.showErroDialog(error_response);

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
    private void addSpinerForGender()
    {

        adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_spinner_item,gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 0) {
                    ((TextView) view).setTextColor(ContextCompat.getColor(getActivity(), R.color.hintcolor));
                } else {
                    ((TextView) view).setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void showKeyboard(){
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void updateLabel() {
       /* String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        et_dob_dlg.setText(sdf.format(myCalendar.getTime()));*/

        String myFormat = "dd-MM-yyyy";
        String myFormatShow = "MMM dd yyyy";
        //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormatShow, Locale.US);
        MyDOB=String.valueOf(sdf.format(myCalendar.getTime()));
        et_dob_dlg.setText(sdf2.format(myCalendar.getTime()));
    }


    //////////////////Update Profile request//////////////////

    /*volley*/

    private void UpdateUserPutMethod()
    {

              showprogress();
        initVolleyCallbackForUpdateUser();
        mVolleyService = new VolleyService(mResultCallback,getActivity());



        mVolleyService.PutReqquestVolley("PUTCALL",NetworkURLs.UpdateUser+UserID+".json",UpdateUser);
    }

   private void initVolleyCallbackForUpdateUser(){
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
                            showsnackmessage("Updated Successfully");


                            tvUserName.setText(UserName);
                            tvEamil.setText(UserEmail);
                            tvDob.setText(UserDOB);
                            tvJoinDate.setText(UserJoinDate);
                            tvPhone.setText(UserPh);
                            UserPic=signUpResponseModel.getString("display_picture");
                            if (!UserPic.equalsIgnoreCase("")&&!UserPic.equalsIgnoreCase("null")){
                                // Toast.makeText(getActivity(), String.valueOf(UserPic), Toast.LENGTH_SHORT).show();
                                myImageLoader.loadImage(NetworkURLs.BaseURLImages+UserPic,circleImageView);
                            }else {
                               // Toast.makeText(getActivity(), String.valueOf(UserPic), Toast.LENGTH_SHORT).show();
                                circleImageView.setBackgroundResource(R.drawable.ic_profile_avatar);
                            }

                            SharedPref.write(SharedPref.User_ID, UserID);
                        }else {
                            response_status="false";
                            JSONObject signUpResponseModels = jsonObject.getJSONObject("error");
                            String ErrorMsg= signUpResponseModels.getString("message");
                            showsnackmessage(ErrorMsg);

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    dialogHelper.showErroDialog("Respone is null,try again later");
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

                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

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
    /////////////////////////////


    /*DELETE Saved Coupan*/

    private void SignOut()
    {
            showprogress();
        initVolleyCallbackForDeleteCoupan();
        VolleyService mVolleyService = new VolleyService(mResultCallback, getActivity());
        String DelCoupanUrl=NetworkURLs.BaseURL+NetworkURLs.SignOutUrl;
        mVolleyService.DeleteQuery("DELETECALL",DelCoupanUrl);
    }

    private void initVolleyCallbackForDeleteCoupan(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                    hideprogress();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                            //Toast.makeText(getActivity(), "Sign Out Succesfully", Toast.LENGTH_SHORT).show();
                            getActivity().getSharedPreferences(getActivity().getPackageName(), 0).edit().clear().apply();
                            Intent Send=new Intent(getActivity(),CheapBestMainLogin.class);
                            startActivity(Send);
                            getActivity().finish();

                        }else {

                            JSONObject signUpResponseModels = jsonObject.getJSONObject("error");
                            dialogHelper.showErroDialog( signUpResponseModels.getString("message"));
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
                 //   dialogHelper.showErroDialog(error_response);

                    try {
                        JSONObject response_obj=new JSONObject(error_response);

                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");

                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

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


    public boolean check_date_validity(String dd){
        boolean isvalid=false;
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date strDate = null;
        try {
            strDate = sdf.parse(dd);
            if (new Date().after(strDate)) {
                isvalid=false;
            }
            else{
                isvalid=true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isvalid;
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}


