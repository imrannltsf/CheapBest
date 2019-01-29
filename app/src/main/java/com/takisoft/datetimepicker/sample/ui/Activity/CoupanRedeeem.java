package com.takisoft.datetimepicker.sample.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import spinkit.style.ChasingDots;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.adpterUtills.CoupanRedeemHelper;
import com.takisoft.datetimepicker.sample.apputilss.MyImageLoader;
import com.takisoft.datetimepicker.sample.network.IResult;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.network.VolleyService;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CoupanRedeeem extends AppCompatActivity {

    private ImageView imageViewLoading;
    private ChasingDots mChasingDotsDrawable;
    public static String SelectedCoupanID,RedemCouponID;
    private IResult mResultCallback;
    private ImageView imageViewHeader;
    private TextView tvCountRedeem;
    private List<CoupanRedeemHelper> Coupan_DetailList=new ArrayList<>();
    TextView TvTitle,TvShortDescription,TvSummery,TvtitleHeader;
    private MyImageLoader myImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupan_redeeem);


        initCoupanRedeem();

    }

    private void initCoupanRedeem() {
        myImageLoader=new MyImageLoader(CoupanRedeeem.this);
        mChasingDotsDrawable = new ChasingDots();
        mChasingDotsDrawable.setColor(getResources().getColor(R.color.color_custom));
        imageViewLoading = findViewById(R.id.image_loading_redeem);
        imageViewLoading.setImageDrawable(mChasingDotsDrawable);
        imageViewLoading.setVisibility(View.GONE);
        imageViewHeader=findViewById(R.id.bg_header_coupan);
        ImageView imageViewback = findViewById(R.id.img_back_coupan_redeme);
        TvTitle=findViewById(R.id.title_coupan_redeem);
        TvtitleHeader=findViewById(R.id.title_coupan_redeem_header);
        TvShortDescription=findViewById(R.id.tv_short_des_detail_coupan);
        TvSummery=findViewById(R.id.tv_summery_coupan_redeme);
        tvCountRedeem=findViewById(R.id.tv_no_of_deal_redeem_today);
        Button btnCoupanRedeem = findViewById(R.id.btn_redeem_deal_now);

        GetSavedCoupansData();
       // btnCoupanRedeem.setOnClickListener(view -> GetRedeemCode());
        btnCoupanRedeem.setOnClickListener(view -> {
            GetRedeemCode();
            Toast.makeText(CoupanRedeeem.this, "Redeem Coupan ID:"+String.valueOf(RedemCouponID), Toast.LENGTH_SHORT).show();
        });
        imageViewback.setOnClickListener(view -> {
            MainDashBoard.clickcounter=3;
            Intent Send=new Intent(CoupanRedeeem.this,MainDashBoard.class);
            startActivity(Send);
            finish();
        });
    }


    private void GetSavedCoupansData()
    {
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForSavedCoupan();
        VolleyService mVolleyService = new VolleyService(mResultCallback, CoupanRedeeem.this);
        mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.GetCoupanDetailUrl+SelectedCoupanID+".json");

    }

    private void initVolleyCallbackForSavedCoupan(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                            Toast.makeText(CoupanRedeeem.this, "Data Found found true", Toast.LENGTH_SHORT).show();

                            JSONObject DataObj = jsonObject.getJSONObject("data");
                            JSONObject coupans = DataObj.getJSONObject("coupon");
                            RedemCouponID = coupans.getString("id");
                            CoupanRedeemHelper coupanRedeemHelper=new CoupanRedeemHelper(coupans);
                            Coupan_DetailList.add(coupanRedeemHelper);




                            /*String StrTitle = coupans.getString("title");
                            String StrCode = coupans.getString("code");
                            String StrImage = coupans.getString("image");
                            String StrDescription = coupans.getString("description");
                            String StrStatus = coupans.getString("status");
                            String StrOriginalPrice = coupans.getString("original_price");
                            String StrDiscount = coupans.getString("discount");
                            String StrDiscountUnit = coupans.getString("discount_unit");
                            String StrStartDate = coupans.getString("start_date");
                            String StrEndDate = coupans.getString("end_date");
                            String StrAppliedToAll = coupans.getString("applied_to_all_locations");
                            String StrSummery = coupans.getString("summary");
                            JSONArray LocationArray = coupans.getJSONArray("locations");
                            int location_array_length=LocationArray.length();
                            JSONObject c = coupans.getJSONObject("vendor");
                            String StrVendorName = c.getString("name");
                            String StrVendorLogo = c.getString("logo");
                            TvTitle.setText(StrTitle);
                            TvShortDescription.setText(StrDescription);
                            TvSummery.setText(StrSummery);
                           Coupan_DetailList.add(new CoupanRedeemHelper(RedemCouponID,StrTitle,StrCode,StrImage,StrDescription,StrStatus,StrOriginalPrice,StrDiscount,StrDiscountUnit,StrStartDate,StrEndDate,StrAppliedToAll,StrSummery,StrVendorName,StrVendorLogo,LocationArray,location_array_length));
*/
                          //  Toast.makeText(CoupanRedeeem.this, "Title:"+coupanRedeemHelper.getCoupanTitle(), Toast.LENGTH_SHORT).show();

                            TvTitle.setText(coupanRedeemHelper.getCoupanTitle());
                            TvtitleHeader.setText(coupanRedeemHelper.getCoupanTitle());
                            TvShortDescription.setText(coupanRedeemHelper.getCoupanDescription());
                            TvSummery.setText(coupanRedeemHelper.getCoupanSummery());
                            String StrImage=coupanRedeemHelper.getCoupanCover();

                            tvCountRedeem.setText("This coupon has been redeem "+coupanRedeemHelper.getRedemptionValue()+" times");

                          //  RedemCouponID=coupanRedeemHelper.getCoupanID();

                            if(!StrImage.equals("null")&&!StrImage.equalsIgnoreCase("")){
                                myImageLoader.loadImage(NetworkURLs.BaseURLImages+StrImage,imageViewHeader);
                            }else {
                                imageViewHeader.setImageResource(R.drawable.detailhead);
                                Toast.makeText(CoupanRedeeem.this, "Header Image Not Found", Toast.LENGTH_SHORT).show();
                              //  myImageLoader.loadImage(NetworkURLs.BaseURLImages+SubBrandFragment.BrandLogoUrl,imageViewHeader);

                            }
                          // prepareSavedCoupanData();
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
                    myImageLoader.showErroDialog("Error While Getting Deal");
                Toast.makeText(CoupanRedeeem.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }
    @Override
    public void onBackPressed() {
        MainDashBoard.clickcounter=3;
        Intent Send=new Intent(CoupanRedeeem.this,MainDashBoard.class);
        startActivity(Send);
        finish();
    }
    ///////////////////////////////////Get Redeem Coode
    private void GetRedeemCode()
    {
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);

        initVolleyCallbackForRedeemCode();

        VolleyService mVolleyService = new VolleyService(mResultCallback, CoupanRedeeem.this);

        mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.CoupanRedemUrl+RedemCouponID+"/redeeming_code.json");


    }

    private void initVolleyCallbackForRedeemCode(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);


                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                            Toast.makeText(CoupanRedeeem.this, "Data Found found true", Toast.LENGTH_SHORT).show();

                            JSONObject DataObj = jsonObject.getJSONObject("data");
                            String Strid = DataObj.getString("redeeming_code");

                            myImageLoader.showDialog(Strid);
                        }else {
                            JSONObject DataObj = jsonObject.getJSONObject("error");
                            String Strid = DataObj.getString("message");
                            myImageLoader.showDialogAlert(Strid);

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
                    myImageLoader.showErroDialog(String.valueOf(error_response));
                    try {
                        JSONObject response_obj=new JSONObject(error_response);

                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");

                            Toast.makeText(CoupanRedeeem.this, message, Toast.LENGTH_SHORT).show();

                            myImageLoader.showErroDialog(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        };
    }
}
