package com.cheapestbest.androidapp.ui.Activity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.apputills.FirebaseHelper;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.MainDashBoardHelper;
import com.cheapestbest.androidapp.appadapters.CoupanAdapter;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.apputills.SharedPref;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import com.cheapestbest.androidapp.ui.Fragments.CheapBestMainLoginFragment;
import com.cheapestbest.androidapp.ui.Fragments.CoupanFragment;
import com.cheapestbest.androidapp.ui.Fragments.MainDashBoardFragment;
import com.cheapestbest.androidapp.ui.Fragments.SavedCoupansLocationFragment;
import com.cheapestbest.androidapp.ui.Fragments.SearchDetailFragment;
import com.cheapestbest.androidapp.ui.Fragments.SubBrandFragment;
import com.cheapestbest.androidapp.ui.Fragments.signup.ProfileFragment;
import com.cheapestbest.androidapp.ui.Fragments.signup.UpdateProfileFrag;
import com.cheapestbest.androidapp.apputills.GPSTracker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainDashBoard extends FragmentActivity
        implements
        MainDashBoardFragment.OnItemSelectedListener,
        CoupanFragment.OnItemSelectedListener,
        CoupanAdapter.OnSwipeListener,
        SubBrandFragment.OnItemSelectedListener,
        SavedCoupansLocationFragment.OnItemSelectedListener,
        ProfileFragment.OnProfileSelectedListener,
        SearchDetailFragment.OnItemSelectedListener,
        UpdateProfileFrag.OnItemSelectedListener{
    public static String VendorID;
   // public  static String responseUid;
    boolean doubleBackToExitPressedOnce = false;
    private RelativeLayout layout_ProdcutName_dlg,layout_location_dlg,layout_category_dlg;
    private EditText et_p_name_dlg,et_location_dlg,et_category_dlg;
    private ImageView ImgClearQuery,ImgClearLocation,ImgClearCategory;

    private TextView TvHintProduct_dlg,TvHintLocation_dlg,TvHintCategory_dlg;
   // public static int clickcounter=0;
    public static Map<String, String> LocationCorrdinates;
    GPSTracker gpsTracker;
    Progressbar progressbar;
    private DialogHelper dialogHelper;
    private FirebaseAnalytics firebaseAnalytics;
    //MainDashBoardFragment fragmentMain;
  public static List<MainDashBoardHelper>DashBoardList=new ArrayList<>();


    /*private EditText etReferalCode_dlg,etLinkCode_dlg;
    private RelativeLayout LayoutReferal,layoutLink;
    private TextView TvLink,TvReferal;*/
    private String SelcedCategory,SelectedQuery,SelectedLocation;
    private BottomSheetMenuDialog mBottomSheetDialog;
    private Button BtnSearchVendor;
    private IResult mResultCallback;

    RelativeLayout relativeLayoutMain;
    public static boolean isLocationPermssionAllowed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPref.init(getApplicationContext());
        setContentView(R.layout.activity_main_dash_board);
        relativeLayoutMain=findViewById(R.id.layout_main_board);

        gpsTracker=new GPSTracker(MainDashBoard.this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, MainDashBoardFragment.newInstance())
                .commitNow();
        inintthisactivity();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void inintthisactivity() {

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseHelper food = new FirebaseHelper();
        food.setId(1);
        // choose random food name from the list
        food.setName("Imran");
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, food.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, food.getName());
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        //Sets whether analytics collection is enabled for this app on this device.
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
        firebaseAnalytics.setMinimumSessionDuration(20000);
        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
        firebaseAnalytics.setSessionTimeoutDuration(500);
        //Sets the user ID property.
        firebaseAnalytics.setUserId(String.valueOf(food.getId()));
        //Sets a user property to a given value.
        firebaseAnalytics.setUserProperty("FirebaseHelper", food.getName());

        int hasPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            //Do smthng
            isLocationPermssionAllowed=true;
         //   Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();
        }else {
            isLocationPermssionAllowed=false;
            showsnackmessage("Location Permission is not granted");
            //Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
        }
      /*  requestLocationPermission();
        checkRunTimePermission();*/
        dialogHelper=new DialogHelper(MainDashBoard.this);
        progressbar =new Progressbar(MainDashBoard.this);
        LocationCorrdinates = new HashMap< >();
        LocationCorrdinates.put("lat",String.valueOf(gpsTracker.getLatitude()));
        LocationCorrdinates.put("long",String.valueOf(gpsTracker.getLongitude()));

        ImageView imageViewCoupan = findViewById(R.id.coupans_main_dash);
        ImageView imageViewHome = findViewById(R.id.home_main_dash);
        ImageView imageViewConnectReferal = findViewById(R.id.connect_refrence_bottomsheet);
        ImageView imageViewPerson = findViewById(R.id.person_main_dash);
        RelativeLayout relativeLayoutFooter = findViewById(R.id.layout_footer);
        RelativeLayout relativeLayoutMain = findViewById(R.id.layout_main_board);

        Fragment f =getSupportFragmentManager().findFragmentById(R.id.container);
        if(f instanceof SubBrandFragment){
           relativeLayoutFooter.setBackgroundResource(R.drawable.footer_b);
            relativeLayoutMain.setBackgroundResource(R.color.white);
        }else {
            relativeLayoutFooter.setBackgroundResource(R.drawable.footer);
            relativeLayoutMain.setBackgroundResource(R.color.main_dash_board_color);
        }
        ImageView buttonSearch = findViewById(R.id.search_main_dashboard);

        /*buttonSearch.setOnClickListener(view -> {
            Dialog dialog=new Dialog(MainDashBoard.this);
           // dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog_box);
            BtnSearchVendor=dialog.findViewById(R.id.btn_search_vendor_dlg);
            layout_ProdcutName_dlg=dialog.findViewById(R.id.layout_product_name_dlg);
            layout_location_dlg=dialog.findViewById(R.id.layout_location_name_dlg);
            layout_category_dlg=dialog.findViewById(R.id.layout_category_name_dlg);
            et_p_name_dlg=dialog.findViewById(R.id.et_p_name_dlg);
            et_location_dlg=dialog.findViewById(R.id.et_location_dlg);
            et_category_dlg=dialog.findViewById(R.id.et_category_dlg);
            ImgClearQuery=dialog.findViewById(R.id.img_cancel_pname_dlg);
            ImgClearLocation=dialog.findViewById(R.id.img_cancel_location_dlg);
          //  ImgClearCategory=dialog.findViewById(R.id.img_cancel_category_dlg);
            TvHintProduct_dlg=dialog.findViewById(R.id.search_hint_dlg);
            TvHintLocation_dlg=dialog.findViewById(R.id.location_hint_dlg);
            TvHintCategory_dlg=dialog.findViewById(R.id.hint_category_dlg);

            ImgClearQuery.setOnClickListener(view16 -> {
                if(!TextUtils.isEmpty(et_p_name_dlg.getText().toString())){
                    et_p_name_dlg.getText().clear();
                }
            });
            ImgClearLocation.setOnClickListener(view17 -> {
                if(!TextUtils.isEmpty(et_location_dlg.getText().toString())){
                    et_location_dlg.getText().clear();
                }
            });
            *//*ImgClearCategory.setOnClickListener(view18 -> {
                if(!TextUtils.isEmpty(et_category_dlg.getText().toString())){
                    et_category_dlg.getText().clear();
                }
            });*//*
            et_p_name_dlg.setOnFocusChangeListener((view13, hasFocus) -> {
                if (hasFocus) {
                    layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);

                    TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                    TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));

                } else {
                    layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);

                    TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                    TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));

                }
            });

            et_location_dlg.setOnFocusChangeListener((view12, hasFocus) -> {
                if (hasFocus) {
                    layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);

                    TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                    TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));

                } else {
                    layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);

                    TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                }
            });
            et_category_dlg.setInputType(InputType.TYPE_NULL);
            et_category_dlg.requestFocus();
            et_category_dlg.setOnClickListener(view110 -> {
                et_category_dlg.setFocusableInTouchMode(true);
                et_category_dlg.setFocusable(true);
                et_category_dlg.requestFocus();
                addcategorylist();

            });
            et_category_dlg.setOnFocusChangeListener((view1, hasFocus) -> {
                if (hasFocus) {
                    layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));

                    addcategorylist();
                } else {
                    layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);

                    TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                    TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                }
            });
            BtnSearchVendor.setOnClickListener(view19 -> {

                SelectedQuery=et_p_name_dlg.getText().toString().replace(" ","");
                SelectedLocation=et_location_dlg.getText().toString().replace(" ","");
                SelcedCategory=et_category_dlg.getText().toString().replace(" ","");
                if(TextUtils.isEmpty(SelectedQuery)||SelectedQuery.equalsIgnoreCase("null")){
                    showsnackmessage("Enter Query For Search");

                }else if(TextUtils.isEmpty(SelectedLocation)||SelectedLocation.equalsIgnoreCase("null")){
                    showsnackmessage("Enter Location For Search");

                }else if(TextUtils.isEmpty(SelcedCategory)||SelcedCategory.equalsIgnoreCase("null")){
                    showsnackmessage("Enter Category For Search");

                }else {

                    String QueryString="city="+SelectedLocation+"&"+"category="+SelectedLocation+"&"+"query="+SelcedCategory;
                    GetSearch(QueryString);
                    dialog.dismiss();
                }
            });
            dialog.setOnKeyListener((arg0, keyCode, event) -> {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            });
            dialog.show();
        });*/

        imageViewConnectReferal.setOnClickListener(view -> {

                    /*Search Using SearchBox*/

            Dialog dialog=new Dialog(MainDashBoard.this);
            // dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog_box);
            BtnSearchVendor=dialog.findViewById(R.id.btn_search_vendor_dlg);
            layout_ProdcutName_dlg=dialog.findViewById(R.id.layout_product_name_dlg);
            layout_location_dlg=dialog.findViewById(R.id.layout_location_name_dlg);
            layout_category_dlg=dialog.findViewById(R.id.layout_category_name_dlg);
            et_p_name_dlg=dialog.findViewById(R.id.et_p_name_dlg);
            et_location_dlg=dialog.findViewById(R.id.et_location_dlg);
            et_category_dlg=dialog.findViewById(R.id.et_category_dlg);
            ImgClearQuery=dialog.findViewById(R.id.img_cancel_pname_dlg);
            ImgClearLocation=dialog.findViewById(R.id.img_cancel_location_dlg);
              ImgClearCategory=dialog.findViewById(R.id.img_cancel_category_dlg);
            TvHintProduct_dlg=dialog.findViewById(R.id.search_hint_dlg);
            TvHintLocation_dlg=dialog.findViewById(R.id.location_hint_dlg);
            TvHintCategory_dlg=dialog.findViewById(R.id.hint_category_dlg);

            ImgClearQuery.setOnClickListener(view16 -> {
                if(!TextUtils.isEmpty(et_p_name_dlg.getText().toString())){
                    et_p_name_dlg.getText().clear();
                }
            });
            ImgClearLocation.setOnClickListener(view17 -> {
                if(!TextUtils.isEmpty(et_location_dlg.getText().toString())){
                    et_location_dlg.getText().clear();
                }
            });
            ImgClearCategory.setOnClickListener(view18 -> {
                if(!TextUtils.isEmpty(et_category_dlg.getText().toString())){
                    et_category_dlg.getText().clear();
                }
            });
            et_p_name_dlg.setOnFocusChangeListener((view13, hasFocus) -> {
                if (hasFocus) {
                    layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);

                    TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                    TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));

                } else {
                    layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);

                    TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                    TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));

                }
            });

            et_location_dlg.setOnFocusChangeListener((view12, hasFocus) -> {
                if (hasFocus) {
                    layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);

                    TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                    TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));

                } else {
                    layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);

                    TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                }
            });
            et_category_dlg.setInputType(InputType.TYPE_NULL);
            et_category_dlg.requestFocus();
            et_category_dlg.setOnClickListener(view110 -> {
                et_category_dlg.setFocusableInTouchMode(true);
                et_category_dlg.setFocusable(true);
                et_category_dlg.requestFocus();
                addcategorylist();

            });
            et_category_dlg.setOnFocusChangeListener((view1, hasFocus) -> {
                if (hasFocus) {
                    layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));

                    addcategorylist();
                } else {
                    layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);

                    TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                    TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                }
            });

            et_location_dlg.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // You can identify which key pressed buy checking keyCode value
                    // with KeyEvent.KEYCODE_
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        // this is for backspace
                        et_location_dlg.getText().clear();
                    }
                    return false;
                }
            });
            et_category_dlg.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // You can identify which key pressed buy checking keyCode value
                    // with KeyEvent.KEYCODE_
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        // this is for backspace
                        et_category_dlg.getText().clear();
                    }
                    return false;
                }
            });
            et_p_name_dlg.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // You can identify which key pressed buy checking keyCode value
                    // with KeyEvent.KEYCODE_
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        // this is for backspace
                        et_p_name_dlg.getText().clear();
                    }else {
                     //   Toast.makeText(MainDashBoard.this, String.valueOf(KeyEvent.KEYCODE_DEL), Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
            BtnSearchVendor.setOnClickListener(view19 -> {

                SelectedQuery=et_p_name_dlg.getText().toString().replace(" ","");
                SelectedLocation=et_location_dlg.getText().toString().replace(" ","");
                SelcedCategory=et_category_dlg.getText().toString().replace(" ","");

                if(isEmptyString(SelectedQuery)&&isEmptyString(SelectedLocation)&&isEmptyString(SelcedCategory)){
                    showsnackmessage("Enter Query For Search");
                }else {
                    String QueryString="city="+SelectedLocation+"&"+"category="+SelcedCategory+"&"+"query="+SelectedQuery;
                    GetSearch(QueryString);
                    /*if(!isEmptyString(SelectedQuery)&&!isEmptyString(SelectedLocation)&&!isEmptyString(SelcedCategory)){
                        String QueryString="city="+SelectedLocation+"&"+"category="+SelcedCategory+"&"+"query="+SelectedQuery;
                        GetSearch(QueryString);
                    }else  if(!isEmptyString(SelectedQuery)&&!isEmptyString(SelectedLocation)&&!isEmptyString(SelcedCategory)){

                    }*/

                    dialog.dismiss();
                }
               /* if(TextUtils.isEmpty(SelectedQuery)||SelectedQuery.equalsIgnoreCase("null")){
                    showsnackmessage("Enter Query For Search");

                }else if(TextUtils.isEmpty(SelectedLocation)||SelectedLocation.equalsIgnoreCase("null")){
                    showsnackmessage("Enter Location For Search");

                }else if(TextUtils.isEmpty(SelcedCategory)||SelcedCategory.equalsIgnoreCase("null")){
                    showsnackmessage("Enter Category For Search");

                }else {

                    String QueryString="city="+SelectedLocation+"&"+"category="+SelectedLocation+"&"+"query="+SelcedCategory;
                    GetSearch(QueryString);
                    dialog.dismiss();
                }*/
            });
            dialog.setOnKeyListener((arg0, keyCode, event) -> {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            });
            dialog.show();

                    /*Search Using Referal Code*/

            /*Dialog dialog=new Dialog(MainDashBoard.this);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog_coupan);
            LayoutReferal=dialog.findViewById(R.id.layout_referal_dlg);
            layoutLink=dialog.findViewById(R.id.layout_referal_link_dlg);
            TvLink=dialog.findViewById(R.id.link_hint_dlg);
            TvReferal=dialog.findViewById(R.id.referal_hint_dlg);
            etReferalCode_dlg=dialog.findViewById(R.id.et_referal_name_coupon_dlg);
            etLinkCode_dlg=dialog.findViewById(R.id.et_link_coupon_dlg);

            etReferalCode_dlg.setOnFocusChangeListener((view14, hasFocus) -> {
                if (hasFocus) {
                    LayoutReferal.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    layoutLink.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    TvReferal.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                    TvLink.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));

                } else {
                    LayoutReferal.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layoutLink.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);

                    TvReferal.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvLink.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                }
            });

            etLinkCode_dlg.setOnFocusChangeListener((view14, hasFocus) -> {
                if (hasFocus) {
                    layoutLink.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    LayoutReferal.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    TvLink.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                    TvReferal.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));

                } else {
                    layoutLink.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    LayoutReferal.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    TvLink.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvReferal.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));

                }
            });
            etReferalCode_dlg.setOnClickListener(view15 -> {
            });

            dialog.setOnKeyListener((arg0, keyCode, event) -> {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    dialog.dismiss();
                }
                return true;
            });

            dialog.show();*/

        });

        imageViewHome.setOnClickListener(view -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, MainDashBoardFragment.newInstance())
                .commitNow());

        imageViewPerson.setOnClickListener(view -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ProfileFragment.newInstance())
                .commitNow());

        imageViewCoupan.setOnClickListener(view -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, CoupanFragment.newInstance())
                .commitNow());


    }

    @Override
    public void onDashBoardCallBack(int position) {
        if(position==1){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SubBrandFragment.newInstance())
                    .commitNow();
        }else if(position==2){
           // Toast.makeText(MainDashBoard.this, String.valueOf(VendorID), Toast.LENGTH_SHORT).show();
            //Toast.makeText(MainDashBoard.this, String.valueOf("In progress"), Toast.LENGTH_SHORT).show();
           SaveWholeVendor();


        }
    }
    @Override
    public void onSubBrandFragCallBack(int position) {
        if(position==1) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SavedCoupansLocationFragment.newInstance())
                    .commitNow();
        }else if(position==2){
            Intent home_intent = new Intent(MainDashBoard.this, CoupanRedeeem.class);
            overridePendingTransition(R.anim.animation_enter_flip, R.anim.animation_out_flip);
            startActivity(home_intent);
         //   finish();

        }else if(position==3) {
            showsnackmessage("No Location Found");
        }else if(position==4){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SubBrandFragment.newInstance())
                    .commitNow();
        }
    }
    @Override
    public void onCoupanFragCallBack(int position) {
        if(position==1){
            Intent home_intent = new Intent(MainDashBoard.this, CoupanRedeeem.class);
            overridePendingTransition(R.anim.animation_enter_flip, R.anim.animation_out_flip);
            startActivity(home_intent);

        }else if(position==2) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SavedCoupansLocationFragment.newInstance())
                    .commitNow();
        }else if(position==3){
            showsnackmessage("Coupon Removed Successfully");
        }else if(position==4){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SubBrandFragment.newInstance())
                    .commitNow();
        }else if(position==5){
            showsnackmessage("No location found for this coupon");
        }

    }

    @Override
    public void onLocationFragCallBack(int position) {
        if(position==1){
                         Intent home_intent = new Intent(MainDashBoard.this, CoupanRedeeem.class);
            overridePendingTransition(R.anim.animation_enter_flip, R.anim.animation_out_flip);
                        startActivity(home_intent);
        }else if(position==2){
            showsnackmessage("No location found for this coupon");
        }else if(position==3){
            showsnackmessage("Your location is disabled,enabled it see location");
        }
    }

    @Override
    public void onSwipe(int position, View v) {
       // Toast.makeText(this, "When Remove Item", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProfileFragCallBack(int position) {

        if(position==1){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                    .commitNow();
        }else if(position==2){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, UpdateProfileFrag.newInstance())
                    .commitNow();
        }
    }
    @Override
    public void onBackPressed() {

        Fragment f =getSupportFragmentManager().findFragmentById(R.id.container);
        if(f instanceof CheapBestMainLoginFragment){
            //Toast.makeText(this, "Your are Login Fragment", Toast.LENGTH_SHORT).show();
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                doubleBackToExitPressedOnce=false;
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
        }else if(f instanceof ProfileFragment){

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                    .commitNow();
        }else if(f instanceof CoupanFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                    .commitNow();

        }else if(f instanceof SubBrandFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                    .commitNow();

        }else if(f instanceof MainDashBoardFragment){
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                doubleBackToExitPressedOnce=false;
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
        }
    }

        private void addcategorylist(){
            if (mBottomSheetDialog != null) {
                mBottomSheetDialog.dismiss();
            }
            mBottomSheetDialog = new BottomSheetBuilder(this, R.style.AppTheme_BottomSheetDialog_Custom)
                    .setMode(BottomSheetBuilder.MODE_LIST)
                    /*.setAppBarLayout(appBarLayout)*/
                    .setMenu(R.menu.menu_bottom_list_sheet)
                    .setItemClickListener(item -> {

         //               Toast.makeText(MainDashBoard.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                        SelcedCategory=item.getTitle().toString();
                        et_category_dlg.setText(SelcedCategory);
                        Log.d("Item click", item.getTitle() + "");
                        layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                        layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                        layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                        TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                        TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                        TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    })
                    .createDialog();

            mBottomSheetDialog.setOnCancelListener(dialog -> {
              //  mShowingLongDialog = false;
            });
            mBottomSheetDialog.show();
        }
                 /*Search vendor Volley Query*/

    private void GetSearch(String StrQuery)
    {

        showprogress();
        if(SearchDetailFragment.SearchDetailList.size()>0){
            SearchDetailFragment.SearchDetailList.clear();
        }
        initVolleyCallbackForSearch();
        VolleyService mVolleyService = new VolleyService(mResultCallback, MainDashBoard.this);
        mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.BaseURL+NetworkURLs.SearchByManualUrl+StrQuery);
    }

    private void initVolleyCallbackForSearch(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

               hideprogress();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                           // Toast.makeText(MainDashBoard.this, "Data Found found true", Toast.LENGTH_SHORT).show();

                            JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                            JSONArray Vendorsarray = DataRecivedObj.getJSONArray("vendors");
                            for (int i = 0; i < Vendorsarray.length(); i++) {
                                JSONObject c = Vendorsarray.getJSONObject(i);
                                SearchDetailFragment.SearchDetailList.add(new MainDashBoardHelper(c));

                            }
                            loadMySearchFragment();
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
           /* Load Search Fragment*/
    private void loadMySearchFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, SearchDetailFragment.newInstance())
                .commitNow();
    }


    @Override
    public void onSearchDeatilCallBack(int position) {

        if(position==1){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SubBrandFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public void onUpdateProfileFragCallBack(int position) {

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




    ////////////////////////////////////////Save Whole Vendor


    void SaveWholeVendor()
    {

        showprogress();
        initVolleyCallbackForSaveVendor();
        VolleyService mVolleyService = new VolleyService(mResultCallback,MainDashBoard.this);
        mVolleyService.postDataVolleyWithHeaderWithoutParam("POSTCALL",NetworkURLs.SaveWholeVendor_PartA+VendorID+NetworkURLs.SaveWholeVendor_PartB);
    }

    private void initVolleyCallbackForSaveVendor(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                hideprogress();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {


                            JSONObject signUpResponseModel = jsonObject.getJSONObject("data");
                            String SuccessMessage = signUpResponseModel.getString("message");
                            showsnackmessage(SuccessMessage);

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                                    .commitNow();

                            /*MainDashBoardFragment.isnowsaved=MainDashBoardFragment.MainDashBoardSaveIndex;
                            MainDashBoardFragment.dashBoardAdapter.notifyDataSetChanged();*/
                          //  fragmentMain.GetMainDashBoardData();


                        }else {

                            JSONObject signUpResponseModels = jsonObject.getJSONObject("error");
                            String Message = signUpResponseModels.getString("message");
                            dialogHelper.showErroDialog(Message);

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
                  //  dialogHelper.showErroDialog(String.valueOf(error_response));
                 //   Toast.makeText(MainDashBoard.this, String.valueOf(error_response), Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject response_obj=new JSONObject(error_response);

                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");
                            dialogHelper.showErroDialog(String.valueOf(message));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
    }


           /* Check Empty String Method*/

    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }
        ////////Location Permission Handler

    private void requestLocationPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Log.e("","");
                                 Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getApplicationContext(), "Permission is denied!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }


 /*   private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            //    requestPermissions(permissionArrays, 11111);
        } else {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
        }
    }*/

}
