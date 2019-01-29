package com.takisoft.datetimepicker.sample.ui.Activity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import spinkit.style.ChasingDots;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.adpterUtills.MainDashBoardHelper;
import com.takisoft.datetimepicker.sample.appadapters.CoupanAdapter;
import com.takisoft.datetimepicker.sample.apputilss.MyImageLoader;
import com.takisoft.datetimepicker.sample.apputilss.SharedPref;
import com.takisoft.datetimepicker.sample.network.IResult;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.network.VolleyService;
import com.takisoft.datetimepicker.sample.ui.Fragments.CheapBestMainLoginFragment;
import com.takisoft.datetimepicker.sample.ui.Fragments.CoupanFragment;
import com.takisoft.datetimepicker.sample.ui.Fragments.MainDashBoardFragment;
import com.takisoft.datetimepicker.sample.ui.Fragments.SavedCoupansLocationFragment;
import com.takisoft.datetimepicker.sample.ui.Fragments.SearchDetailFragment;
import com.takisoft.datetimepicker.sample.ui.Fragments.SubBrandFragment;
import com.takisoft.datetimepicker.sample.ui.signup.ProfileFragment;
import com.takisoft.datetimepicker.sample.ui.signup.UpdateProfileFrag;
import com.takisoft.datetimepicker.sample.ui.utills.GPSTracker;
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

    public  static String responseUid;

    private RelativeLayout layout_ProdcutName_dlg,layout_location_dlg,layout_category_dlg;
    private EditText et_p_name_dlg,et_location_dlg,et_category_dlg;
    private ImageView ImgClearQuery,ImgClearLocation,ImgClearCategory;
    /**/
    private TextView TvHintProduct_dlg,TvHintLocation_dlg,TvHintCategory_dlg;
    public static int clickcounter=0;
    public static Map<String, String> LocationCorrdinates;
    GPSTracker gpsTracker;
 //  public static List<CoupanDetailList> coupanDetailList=new ArrayList<>();
 //   public static  List<MainDashBoradResponse>MainDashBoardList=new ArrayList<>();
  public static List<MainDashBoardHelper>DashBoardList=new ArrayList<>();
    public static boolean loadALLData=false;
    private  ImageView imageViewLoading;
    private ChasingDots mChasingDotsDrawable;
    private EditText etReferalCode_dlg,etLinkCode_dlg;
    private RelativeLayout LayoutReferal,layoutLink;
    private TextView TvLink,TvReferal;
    private String SelcedCategory,SelectedQuery,SelectedLocation;
    private BottomSheetMenuDialog mBottomSheetDialog;
    private Button BtnSearchVendor;
    private IResult mResultCallback;
    private MyImageLoader myImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPref.init(getApplicationContext());
        setContentView(R.layout.activity_main_dash_board);
        myImageLoader=new MyImageLoader(MainDashBoard.this);
        gpsTracker=new GPSTracker(MainDashBoard.this);
        if( MainDashBoard.clickcounter==3){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CoupanFragment.newInstance())
                    .commitNow();
        }
        else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                    .commitNow();
        }

        inintthisactivity();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void inintthisactivity() {
        imageViewLoading =  findViewById(R.id.image_loading_signout);
        mChasingDotsDrawable = new ChasingDots();
        mChasingDotsDrawable.setColor(getResources().getColor(R.color.color_custom));
        imageViewLoading.setImageDrawable(mChasingDotsDrawable);
        imageViewLoading.setVisibility(View.GONE);
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

        buttonSearch.setOnClickListener(view -> {
            Dialog dialog=new Dialog(MainDashBoard.this);
            dialog.setCancelable(false);
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
                    //Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
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
                    // Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
                } else {
                    layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);

                    TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                }
            });
            et_category_dlg.setOnFocusChangeListener((view1, hasFocus) -> {
                if (hasFocus) {
                    layout_ProdcutName_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_location_dlg.setBackgroundResource(R.drawable.rectangle_edittext_unselcetr);
                    layout_category_dlg.setBackgroundResource(R.drawable.rectangle_edittext_selcetr);
                    TvHintProduct_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintLocation_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.black));
                    TvHintCategory_dlg.setTextColor(ContextCompat.getColor(MainDashBoard.this, R.color.color_custom));
                    // Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(MainDashBoard.this, "Enter Query For Search", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(SelectedLocation)||SelectedLocation.equalsIgnoreCase("null")){
                    Toast.makeText(MainDashBoard.this, "Enter Location For Search", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(SelcedCategory)||SelcedCategory.equalsIgnoreCase("null")){
                    Toast.makeText(MainDashBoard.this, "Enter Category For Search", Toast.LENGTH_SHORT).show();
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
        });

        imageViewConnectReferal.setOnClickListener(view -> {
            Dialog dialog=new Dialog(MainDashBoard.this);
            dialog.setCancelable(false);
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
    }
    @Override
    public void onSubBrandFragCallBack(int position) {
        if(position==1) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SavedCoupansLocationFragment.newInstance())
                    .commitNow();
        }
    }
    @Override
    public void onCoupanFragCallBack(int position) {
        if(position==1){
            Intent home_intent = new Intent(MainDashBoard.this, CoupanRedeeem.class);
            overridePendingTransition(R.anim.animation_leave, R.anim.animation_enter);
            startActivity(home_intent);
            finish();
        }else if(position==2) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SavedCoupansLocationFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public void onLocationFragCallBack(int position) {
        if(position==1){
                         Intent home_intent = new Intent(MainDashBoard.this, CoupanRedeeem.class);
                        overridePendingTransition(R.anim.animation_leave, R.anim.animation_enter);
                        startActivity(home_intent);
                        finish();
        }
    }

    @Override
    public void onSwipe(int position, View v) {
        Toast.makeText(this, "When Remove Item", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Your are Login Fragment", Toast.LENGTH_SHORT).show();

        }else if(f instanceof ProfileFragment){

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                    .commitNow();
        }else if(f instanceof CoupanFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainDashBoardFragment.newInstance())
                    .commitNow();

        }else {


          //  Toast.makeText(this, "Main Menu ", Toast.LENGTH_SHORT).show();
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
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForSearch();
        VolleyService mVolleyService = new VolleyService(mResultCallback, MainDashBoard.this);
        mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.BaseURL+NetworkURLs.SearchByManualUrl+StrQuery);
    }

    private void initVolleyCallbackForSearch(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                            Toast.makeText(MainDashBoard.this, "Data Found found true", Toast.LENGTH_SHORT).show();

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
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);
                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    try {
                        JSONObject response_obj=new JSONObject(error_response);

                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");
                            Toast.makeText(MainDashBoard.this, message, Toast.LENGTH_SHORT).show();
                            myImageLoader.showErroDialog(message);
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
}
