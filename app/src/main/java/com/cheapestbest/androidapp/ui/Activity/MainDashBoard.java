package com.cheapestbest.androidapp.ui.Activity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.cheapestbest.androidapp.ui.Fragments.MultipleVendorsLocationsFragment;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.MainDashBoardHelper;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.apputills.SharedPref;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import com.cheapestbest.androidapp.ui.Fragments.CoupanFragment;
import com.cheapestbest.androidapp.ui.Fragments.MainDashBoardFragment;
import com.cheapestbest.androidapp.ui.Fragments.SavedCoupansLocationFragment;
import com.cheapestbest.androidapp.ui.Fragments.SearchDetailFragment;
import com.cheapestbest.androidapp.ui.Fragments.SubBrandFragment;
import com.cheapestbest.androidapp.ui.Fragments.signup.ProfileFragment;
import com.cheapestbest.androidapp.ui.Fragments.signup.UpdateProfileFrag;
import com.cheapestbest.androidapp.apputills.GPSTracker;
import com.karumi.dexter.listener.single.PermissionListener;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MainDashBoard extends FragmentActivity
        implements
        MainDashBoardFragment.OnItemSelectedListener,
        CoupanFragment.OnItemSelectedListener,
        /* CoupanAdapter.OnSwipeListener,*/
        SubBrandFragment.OnItemSelectedListener,
        SavedCoupansLocationFragment.OnItemSelectedListener,
        ProfileFragment.OnProfileSelectedListener,
        SearchDetailFragment.OnItemSelectedListener,
        UpdateProfileFrag.OnItemSelectedListener,
        MultipleVendorsLocationsFragment.OnItemSelectedListener{

    public static String VendorID;
    public static String SuccessMessage;
   // ProgressDialog progressDialog;
    boolean doubleBackToExitPressedOnce = false;
    private RelativeLayout layout_ProdcutName_dlg,layout_location_dlg,layout_category_dlg;
    private EditText et_p_name_dlg,et_location_dlg,et_category_dlg;
    private ImageView ImgClearQuery,ImgClearLocation,ImgClearCategory;
    public  static String QueryString;
    private TextView TvHintProduct_dlg,TvHintLocation_dlg,TvHintCategory_dlg;
    // public static int clickcounter=0;
    public static Map<String, String> LocationCorrdinates;
    public static Map<String, String> HashSearch;
    GPSTracker gpsTracker;
    Progressbar progressbar;
    private DialogHelper dialogHelper;
    private FirebaseAnalytics firebaseAnalytics;
    public static int pagenationCurrentcount=1;
    public static int TotalPaginationCount=0;
    public static int AllTotoalCoupon=0;
    public static List<MainDashBoardHelper>DashBoardList=new ArrayList<>();
    public static String SelcedCategory,SelectedQuery,SelectedLocation;
    private BottomSheetMenuDialog mBottomSheetDialog;
    private Button BtnSearchVendor;
    private IResult mResultCallback;
    RelativeLayout relativeLayoutMain;
    public static boolean isLocationPermssionAllowed;
    Properties propsSearch = new Properties();
    public static int SavedPosition=0;
    public static List<NameValuePair> params = new ArrayList<>();
    public static boolean isFromSettings;
  //  public static boolean LoadDataWithDelay=false;
    public static String FragmentName;
    public static String StrLat,StrLong;
    public static boolean IsFromMainMenu=false;
    public static boolean IsFromLaunching=false;
    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;

    public  static boolean isfromHomeButton=false;
    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    public static boolean isShowMessage;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPref.init(getApplicationContext());
        setContentView(R.layout.activity_main_dash_board);


        relativeLayoutMain=findViewById(R.id.layout_main_board);
        requestLocationPermission();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
        checkLocationUpdate();
        gpsTracker=new GPSTracker(MainDashBoard.this);
        IsFromLaunching=true;

        inintthisactivity();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void inintthisactivity() {

        //   noInternetDialog = new NoInternetDialog.Builder(MainDashBoard.this).build();
        dialogHelper=new DialogHelper(MainDashBoard.this);
        if(haveNetworkConnection()){
            if(!locationServicesEnabled(MainDashBoard.this)){
                Log.d("", "onKey: ");
            }
        }else {

            showsnackmessage("You don't have internet connection");
        }


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

            isLocationPermssionAllowed=true;
        }else {
            isLocationPermssionAllowed=false;
            showsnackmessage("Location Permission is not granted");

        }

        dialogHelper=new DialogHelper(MainDashBoard.this);
        // dialogHelper.showDialog(String.valueOf(getScreenDimension()));
        progressbar =new Progressbar(MainDashBoard.this);
        LocationCorrdinates = new HashMap< >();
        LocationCorrdinates.put("lat",String.valueOf(gpsTracker.getLatitude()));
        LocationCorrdinates.put("long",String.valueOf(gpsTracker.getLongitude()));

        ImageView imageViewCoupan = findViewById(R.id.coupans_main_dash);
        ImageView imageViewHome = findViewById(R.id.home_main_dash);
        ImageView imageViewSearchReferal = findViewById(R.id.search_refrence_bottomsheet);
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

        buttonSearch.setOnClickListener(view -> {
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
                        Log.d("", "onKey: ");
                        //   Toast.makeText(MainDashBoard.this, String.valueOf(KeyEvent.KEYCODE_DEL), Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
            BtnSearchVendor.setOnClickListener(view19 -> {

                SelectedQuery=et_p_name_dlg.getText().toString().trim();
                SelectedLocation=et_location_dlg.getText().toString().trim();
                SelcedCategory=et_category_dlg.getText().toString().trim();


                if(isEmptyString(SelectedQuery)&&isEmptyString(SelectedLocation)&&isEmptyString(SelcedCategory)){
                    showsnackmessage("Enter Query For Search");
                }else {


                    QueryString="city="+SelectedLocation+"&"+"category="+SelcedCategory+"&"+"query="+SelectedQuery;
                    params.add(new BasicNameValuePair("city", SelectedLocation));
                    params.add(new BasicNameValuePair("category", SelcedCategory));
                    params.add(new BasicNameValuePair("query", SelectedQuery));



                    GetSearch();



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
        });

        imageViewSearchReferal.setOnClickListener(view -> {



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

                SelectedQuery=et_p_name_dlg.getText().toString().trim();
                SelectedLocation=et_location_dlg.getText().toString().trim();
                SelcedCategory=et_category_dlg.getText().toString().trim();

                if(isEmptyString(SelectedQuery)&&isEmptyString(SelectedLocation)&&isEmptyString(SelcedCategory)){
                    showsnackmessage("Enter Query For Search");
                }else {
                    //  SelcedCategory="";

                    QueryString="city="+SelectedLocation+"&"+"category="+SelcedCategory+"&"+"query="+SelectedQuery;

                    params.add(new BasicNameValuePair("city", SelectedLocation));
                    params.add(new BasicNameValuePair("category", SelcedCategory));
                    params.add(new BasicNameValuePair("query", SelectedQuery));


                    GetSearch();

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


        });
        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsFromMainMenu=true;
                //  isFromSettings=true;
                isfromHomeButton=true;
                //checkLocationUpdate();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, MainDashBoardFragment.newInstance())
                        .commitNow();

            }
        });



        imageViewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean strStatus =SharedPref.readBol(SharedPref.IsLoginUser, false);

                if(strStatus){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, ProfileFragment.newInstance())
                            .commitNow();
                }else {

                    dialogHelper.showWarningDIalog(getResources().getString(R.string.no_login_alert_msg),"Login",getResources().getString(R.string.dialog_cancel));

                }

            }
        });
        imageViewCoupan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean strStatus =SharedPref.readBol(SharedPref.IsLoginUser, false);
                if(strStatus){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, CoupanFragment.newInstance())
                            .commitNow();
                }else {
                    dialogHelper.showWarningDIalog(getResources().getString(R.string.no_login_alert_msg),"Login",getResources().getString(R.string.dialog_cancel));

                }
            }
        });


    }

    @Override
    public void onDashBoardCallBack(int position) {
        if(position==1){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SubBrandFragment.newInstance())
                    .commitNow();
        }
        else if(position==2){

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MultipleVendorsLocationsFragment.newInstance())
                    .commitNow();
        }else if(position==3){
            showsnackmessage("Not Available Righgt Now");
        }else if(position==4){
            showsnackmessage("Location Not Available");
        }else if(position==5){
            /*if(isEmptyString(SuccessMessage)){
                showsnackmessage("Coupons Successfully Added");
            }else{
                showsnackmessage(SuccessMessage);
            }*/
            showsnackmessage("Coupons Successfully Added");


        }else if(position==6){
            showsnackmessage("Already Added");
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
            showsnackmessage("Coupons Successfully Added");
        }else if(position==5){
            showsnackmessage("Already Added");

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
        }else if(position==6){
            showsnackmessage("Coupon are already saved");
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

    /*@Override
    public void onSwipe(int position, View v) {
       // Toast.makeText(this, "When Remove Item", Toast.LENGTH_SHORT).show();
    }*/

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

    /* @Override
     public void onBackPressed() {
         super.onBackPressed();
     }
 */
    @Override
    public void onBackPressed() {



        Fragment f =getSupportFragmentManager().findFragmentById(R.id.container);
        if(f instanceof MainDashBoardFragment){



            //Toast.makeText(this, "Your are Login Fragment", Toast.LENGTH_SHORT).show();
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                doubleBackToExitPressedOnce=false;
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            //    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
        }else if(f instanceof ProfileFragment){
            //  Toast.makeText(this, "ProfileFragment", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                    .commitNow();
        }else if(f instanceof CoupanFragment){
            //   Toast.makeText(this, "CoupanFragment", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                    .commitNow();

        }else if(f instanceof SubBrandFragment){
            //  Toast.makeText(this, "SubBrandFragment", Toast.LENGTH_SHORT).show();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                    .commitNow();

        }else if(f instanceof MultipleVendorsLocationsFragment){
            // Toast.makeText(this, "MultipleVendorsLocationsFragment", Toast.LENGTH_SHORT).show();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                    .commitNow();
        }else if(f instanceof SearchDetailFragment){
            // Toast.makeText(this, "MultipleVendorsLocationsFragment", Toast.LENGTH_SHORT).show();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                    .commitNow();
        }else {
            //    Toast.makeText(this, "All else", Toast.LENGTH_SHORT).show();

        }
        //  super.onBackPressed();
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

    private void GetSearch()
    {

        showprogress();
        if(SearchDetailFragment.SearchDetailList.size()>0){
            SearchDetailFragment.SearchDetailList.clear();
        }
        initVolleyCallbackForSearch();
        VolleyService mVolleyService = new VolleyService(mResultCallback, MainDashBoard.this);
        //String Str=NetworkURLs.BaseURL+NetworkURLs.SearchByManualUrl+StrQuery+"&page=" + pagenationCurrentcount+"lat="+String.valueOf(gpsTracker.getLatitude())+ "&long=" + String.valueOf(gpsTracker.getLongitude());


        if(doesUserHavePermission()){
            if(isEmptyString(String.valueOf(gpsTracker.getLatitude()))||isEmptyString(String.valueOf(gpsTracker.getLongitude()))){
                params.add(new BasicNameValuePair("page", String.valueOf(pagenationCurrentcount)));
                String strurl=NetworkURLs.BaseURL+NetworkURLs.SearchByManualUrl;
                if(!strurl.endsWith("?"))
                    strurl += "?";
                String query = URLEncodedUtils.format(params, "utf-8");
                String Str=strurl+query;
                mVolleyService.getDataVolleyWithoutparam("GETCALL",Str);
            }else {

                params.add(new BasicNameValuePair("page", String.valueOf(pagenationCurrentcount)));
                params.add(new BasicNameValuePair("lat",String.valueOf(gpsTracker.getLatitude())));
                params.add(new BasicNameValuePair("long",String.valueOf(gpsTracker.getLongitude())));

                String strurl=NetworkURLs.BaseURL+NetworkURLs.SearchByManualUrl;
                if(!strurl.endsWith("?"))
                    strurl += "?";
                String query = URLEncodedUtils.format(params, "utf-8");
                String Str=strurl+query;
                mVolleyService.getDataVolleyWithoutparam("GETCALL",Str);



            }
        }else {
            params.add(new BasicNameValuePair("page", String.valueOf(pagenationCurrentcount)));
            String strurl=NetworkURLs.BaseURL+NetworkURLs.SearchByManualUrl;
            if(!strurl.endsWith("?"))
                strurl += "?";
            String query = URLEncodedUtils.format(params, "utf-8");
            String Str=strurl+query;
            mVolleyService.getDataVolleyWithoutparam("GETCALL",Str);
        }



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

                            params.clear();
                            JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                            JSONArray Vendorsarray = DataRecivedObj.getJSONArray("vendors");

                            TotalPaginationCount=DataRecivedObj.getInt("page_count");
                            AllTotoalCoupon=DataRecivedObj.getInt("total_count");


                            if(Vendorsarray.length()>0){
                                for (int i = 0; i < Vendorsarray.length(); i++) {
                                    JSONObject c = Vendorsarray.getJSONObject(i);
                                    SearchDetailFragment.SearchDetailList.add(new MainDashBoardHelper(c));

                                }

                            }else {
                                //     Toast.makeText(MainDashBoard.this, "Array Lenght is less than zero", Toast.LENGTH_SHORT).show();
                            }

                            loadMySearchFragment();

                        }else{
                            //    Toast.makeText(MainDashBoard.this, "not found", Toast.LENGTH_SHORT).show();
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
                    //  dialogHelper.showErroDialog(error_response);
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
        }else if(position==2){

            showsnackmessage("Not Available Righgt Now");
        }else if(position==3){
            showsnackmessage("Location Not Available");
        }else if(position==4){
            if(isEmptyString(SuccessMessage)){
                showsnackmessage("Coupons Successfully Added");
            }else {
                showsnackmessage(SuccessMessage);
            }
        }else if(position==5){
            showsnackmessage("Already Added");
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
                    String error_response=new String(error.networkResponse.data);
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
                        /*if (report.areAllPermissionsGranted()) {
                         *//* mRequestingLocationUpdates = true;
                            startLocationUpdates();*//*
                            Log.e("","");
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                                    .commitNow();

                             Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }else{

                        }*/
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, MainDashBoardFragment.newInstance())
                                .commitNow();
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
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    @Override
    public void onMultipleVendorsLocationsFragCallBack(int position) {

        if(position==1){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SubBrandFragment.newInstance())
                    .commitNow();
        }else if(position==2){
            showsnackmessage("No Location Found");
        }

    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static boolean locationServicesEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean net_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.e("Location Enabled","Exception gps_enabled");
        }

        try {
            net_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Log.e("Location Enabled","Exception network_enabled");
        }
        return gps_enabled || net_enabled;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            stopLocationUpdates();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {


        super.onResume();


        if(locationServicesEnabled(MainDashBoard.this)){
            if(doesUserHavePermission()){
                mRequestingLocationUpdates = true;
                startLocationUpdates();
            }

            // checkLocationUpdate();
        }else {

            if(!isShowMessage){
                isShowMessage=true;
                dialogHelper.buildAlertMessageNoGps();
            }else {
                //  Toast.makeText(this, String.valueOf(isShowMessage), Toast.LENGTH_SHORT).show();
            }

        }


    }

    private boolean doesUserHavePermission()
    {
        int result = checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            StrLat=String.valueOf(mCurrentLocation.getLatitude());
            StrLong=String.valueOf(mCurrentLocation.getLongitude());
            if(isFromSettings){
                isFromSettings=false;
                if(FragmentName.equals("MainDashBoardFragment")){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, MainDashBoardFragment.newInstance())
                            .commitNow();
                }

            }

        }


    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:



                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                        }

                        updateLocationUI();
                    }
                });
    }


    public  void checkLocationUpdate(){
        Dexter.withActivity(MainDashBoard.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

}

