package com.cheapestbest.androidapp.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.CoupanRedeemHelper;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.FirebaseHelper;
import com.cheapestbest.androidapp.apputills.MyImageLoader;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import com.google.firebase.analytics.FirebaseAnalytics;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CoupanRedeeem extends AppCompatActivity {
    private FirebaseAnalytics firebaseAnalytics;
    Progressbar progressbar;
    public static String SelectedCoupanID,RedemCouponID;
    private IResult mResultCallback;
    private ImageView imageViewHeader;
    private TextView tvCountRedeem;
    private List<CoupanRedeemHelper> Coupan_DetailList=new ArrayList<>();
    TextView TvTitle,TvShortDescription,TvSummery,TvLimited;
    /*,TvtitleHeader*/
    private MyImageLoader myImageLoader;
    private DialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupan_redeeem);

        initCoupanRedeem();
    }

    private void initCoupanRedeem() {
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
        dialogHelper=new DialogHelper(CoupanRedeeem.this);
        myImageLoader=new MyImageLoader(CoupanRedeeem.this);
        progressbar =new Progressbar(CoupanRedeeem.this);
        imageViewHeader=findViewById(R.id.bg_header_coupan);
        //ImageView imageViewback = findViewById(R.id.img_back_coupan_redeme);
        TvTitle=findViewById(R.id.title_coupan_redeem);
       // TvtitleHeader=findViewById(R.id.title_coupan_redeem_header);
        TvShortDescription=findViewById(R.id.tv_short_des_detail_coupan);
        TvLimited=findViewById(R.id.tv_unlimted);
        TvLimited.setVisibility(View.INVISIBLE);
        TvSummery=findViewById(R.id.tv_summery_coupan_redeme);
        tvCountRedeem=findViewById(R.id.tv_no_of_deal_redeem_today);
        Button btnCoupanRedeem = findViewById(R.id.btn_redeem_deal_now);

        GetSavedCoupansData();

        btnCoupanRedeem.setOnClickListener(view -> {
          //  Toast.makeText(this, String.valueOf(SelectedCoupanID), Toast.LENGTH_SHORT).show();
            GetRedeemCode();

        });
      /*  imageViewback.setOnClickListener(view -> {
            finish();
        });*/
    }


    private void GetSavedCoupansData()
    {
       // Toast.makeText(CoupanRedeeem.this, String.valueOf(SelectedCoupanID), Toast.LENGTH_SHORT).show();

      showprogress();
        initVolleyCallbackForSavedCoupan();
        VolleyService mVolleyService = new VolleyService(mResultCallback, CoupanRedeeem.this);
        mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.GetCoupanDetailUrl+SelectedCoupanID+".json");

    }

    private void initVolleyCallbackForSavedCoupan(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {


               hideprogress();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                            JSONObject DataObj = jsonObject.getJSONObject("data");
                            JSONObject coupans = DataObj.getJSONObject("coupon");
                            RedemCouponID = coupans.getString("id");
                            CoupanRedeemHelper coupanRedeemHelper=new CoupanRedeemHelper(coupans);
                            Coupan_DetailList.add(coupanRedeemHelper);

                            TvTitle.setText(coupanRedeemHelper.getCoupanTitle());
                           // TvtitleHeader.setText(coupanRedeemHelper.getCoupanTitle());
                            TvShortDescription.setText(coupanRedeemHelper.getCoupanDescription());
                            TvSummery.setText(coupanRedeemHelper.getCoupanSummery());
                            String StrImage=coupanRedeemHelper.getCoupanCover();


                            if(!StrImage.equals("null")&&!StrImage.equalsIgnoreCase("")){
                                myImageLoader.loadImage(NetworkURLs.BaseURLImages+StrImage,imageViewHeader);
                             }else {
                                imageViewHeader.setImageResource(R.drawable.detailhead);

                            }




                            if(coupanRedeemHelper.isUnlimited()){
                                tvCountRedeem.setText(String.valueOf("This deal has been redeemed 0 times"));
                                TvLimited.setVisibility(View.VISIBLE);

                            }else {
                                if(isEmptyString(coupanRedeemHelper.getUserRedeemCount())){
                                  //  Toast.makeText(CoupanRedeeem.this, String.valueOf(CouponRedeemLimit), Toast.LENGTH_SHORT).show();
                                }else {
                                  /*  Toast.makeText(CoupanRedeeem.this, String.valueOf(coupanRedeemHelper.getUserRedeemCount()), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(CoupanRedeeem.this, String.valueOf(coupanRedeemHelper.getRedemptionUserLimit()), Toast.LENGTH_SHORT).show();
*/
                                        String CouponRedeemLimit=String.valueOf(coupanRedeemHelper.getRedemptionUserLimit());
                                    //Toast.makeText(CoupanRedeeem.this, String.valueOf(coupanRedeemHelper.getRedemptionUserLimit()), Toast.LENGTH_SHORT).show();
                                        tvCountRedeem.setText(String.valueOf("This deal has been redeemed "+coupanRedeemHelper.getUserRedeemCount()+" out of "+coupanRedeemHelper.getRedemptionUserLimit()+" times."));
                                    //tvCountRedeem.setText(String.valueOf("This deal has been redeemed "+coupanRedeemHelper.getUserRedeemCount()+" out of "+coupanRedeemHelper.getRedemptionUserLimit()+" times."));

                                }
                                /*if(CouponRedeemLimit.equalsIgnoreCase("null")||CouponRedeemLimit.equals("0")){
                                }else {
                                    tvCountRedeem.setText(String.valueOf("This deal has been redeemed "+coupanRedeemHelper.getUserRedeemCount()+" out of "+coupanRedeemHelper.getRedemptionUserLimit()+" times."));

                                }*/
                            }

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
    @Override
    public void onBackPressed() {

        finish();
    }
    ///////////////////////////////////Get Redeem Coode
    private void GetRedeemCode()
    {

       showprogress();

        initVolleyCallbackForRedeemCode();

        VolleyService mVolleyService = new VolleyService(mResultCallback, CoupanRedeeem.this);

        mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.CoupanRedemUrl+RedemCouponID+"/redeeming_code.json");


    }

    private void initVolleyCallbackForRedeemCode(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                hideprogress();

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {


                            JSONObject DataObj = jsonObject.getJSONObject("data");
                            String Strid = DataObj.getString("redeeming_code");

                         //   dialogHelper.showDialog(Strid);

                            new SweetAlertDialog(CoupanRedeeem.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                    .setTitleText(Strid)
                                    .setContentText("Please present your code")
                                    .setCustomImage(R.drawable.ic_qr_code)
                                    .show();
                          /*  new SweetAlertDialog(CoupanRedeeem.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText(Strid)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();*/
                        }else {
                           /* JSONObject DataObj = jsonObject.getJSONObject("error");
                            String ErrorMessage = DataObj.getString("message");*/

                            new SweetAlertDialog(CoupanRedeeem.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Offer invalid")
                                    .setContentText("This deal has already been redeemed")
                                    .show();

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
                 //   dialogHelper.showErroDialog(String.valueOf(error_response));
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

    public void showprogress(){

        progressbar.ShowProgress();
        progressbar.setCancelable(false);

    }

    public void hideprogress(){
        progressbar.HideProgress();

    }


    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }
}
