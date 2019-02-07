package com.cheapestbest.androidapp.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.CoupanRedeemHelper;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.MyImageLoader;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CoupanRedeeem extends AppCompatActivity {

    Progressbar progressbar;
    public static String SelectedCoupanID,RedemCouponID;
    private IResult mResultCallback;
    private ImageView imageViewHeader;
    private TextView tvCountRedeem;
    private List<CoupanRedeemHelper> Coupan_DetailList=new ArrayList<>();
    TextView TvTitle,TvShortDescription,TvSummery;
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
        dialogHelper=new DialogHelper(CoupanRedeeem.this);
        myImageLoader=new MyImageLoader(CoupanRedeeem.this);
        progressbar =new Progressbar(CoupanRedeeem.this);
        imageViewHeader=findViewById(R.id.bg_header_coupan);
        //ImageView imageViewback = findViewById(R.id.img_back_coupan_redeme);
        TvTitle=findViewById(R.id.title_coupan_redeem);
       // TvtitleHeader=findViewById(R.id.title_coupan_redeem_header);
        TvShortDescription=findViewById(R.id.tv_short_des_detail_coupan);
        TvSummery=findViewById(R.id.tv_summery_coupan_redeme);
        tvCountRedeem=findViewById(R.id.tv_no_of_deal_redeem_today);
        Button btnCoupanRedeem = findViewById(R.id.btn_redeem_deal_now);

        GetSavedCoupansData();

        btnCoupanRedeem.setOnClickListener(view -> {
            GetRedeemCode();

        });
      /*  imageViewback.setOnClickListener(view -> {
            finish();
        });*/
    }


    private void GetSavedCoupansData()
    {

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

                            String CouponRedeemLimit=String.valueOf(coupanRedeemHelper.getRedemptionUserLimit());
                            Toast.makeText(CoupanRedeeem.this, "Coupon Redeem limit is:"+CouponRedeemLimit, Toast.LENGTH_SHORT).show();

                            if(CouponRedeemLimit.equalsIgnoreCase("null")||CouponRedeemLimit.equals("0")){
                            }else {
                                tvCountRedeem.setText(String.valueOf("This deal has been redeemed "+coupanRedeemHelper.getUserRedeemCount()+" out of "+coupanRedeemHelper.getRedemptionUserLimit()+" times"));

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
                    dialogHelper.showErroDialog(error_response);



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

                            dialogHelper.showDialog(Strid);
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
                            JSONObject DataObj = jsonObject.getJSONObject("error");
                            String ErrorMessage = DataObj.getString("message");
                            dialogHelper.showDialogAlert(ErrorMessage);

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
                    dialogHelper.showErroDialog(String.valueOf(error_response));
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
}
