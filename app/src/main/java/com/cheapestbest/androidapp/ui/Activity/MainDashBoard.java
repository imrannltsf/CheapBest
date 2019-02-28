package com.cheapestbest.androidapp.ui.Activity;

import am.appwise.components.ni.NoInternetDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.MainDashBoardHelper;
/*import com.cheapestbest.androidapp.appadapters.CoupanAdapter;*/
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
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
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
    NoInternetDialog noInternetDialog;
    public static String VendorID;
    public static String SuccessMessage;
   // public  static String responseUid;
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
    //MainDashBoardFragment fragmentMain;
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
     //   noInternetDialog = new NoInternetDialog.Builder(MainDashBoard.this).build();

        if(haveNetworkConnection()){
            if(!locationServicesEnabled(MainDashBoard.this)){
                buildAlertMessageNoGps();
               // showsnackmessage("GPS Service Disabled On Your Mobile");
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
        }else if(f instanceof MultipleVendorsLocationsFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                    .commitNow();
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

    private void GetSearch()
    {

        showprogress();
        if(SearchDetailFragment.SearchDetailList.size()>0){
            SearchDetailFragment.SearchDetailList.clear();
        }
        initVolleyCallbackForSearch();
        VolleyService mVolleyService = new VolleyService(mResultCallback, MainDashBoard.this);
       //String Str=NetworkURLs.BaseURL+NetworkURLs.SearchByManualUrl+StrQuery+"&page=" + pagenationCurrentcount+"lat="+String.valueOf(gpsTracker.getLatitude())+ "&long=" + String.valueOf(gpsTracker.getLongitude());


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

                           /* MainDashBoard.DashBoardList.get(MainDashBoard.SavedPosition).setVendorSaved(true);
                            MainDashBoardFragment.dashBoardAdapter.notifyDataSetChanged();*/


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
       // noInternetDialog.onDestroy();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainDashBoard.this);
        builder.setTitle(getResources().getString(R.string.titile_gps));
        builder.setMessage(getResources().getString(R.string.no_gps_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok_no_gps), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no_no_gps), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                       // showLocationMessage();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

   /* private void showLocationMessage(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainDashBoard.this);
        builder.setMessage(getString(R.string.purpose_of_getting_location))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok_no_gps), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no_no_gps), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
*/

    private String getScreenDimension(){
        String str;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        double wi=(double)width/(double)dm.xdpi;
        double hi=(double)height/(double)dm.ydpi;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);
            str=String.valueOf(screenInches);
        return str;
    }
}
