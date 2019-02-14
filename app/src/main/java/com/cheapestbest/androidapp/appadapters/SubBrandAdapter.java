package com.cheapestbest.androidapp.appadapters;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.SubBrandHelper;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.GPSTracker;
import com.cheapestbest.androidapp.apputills.MyImageLoader;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import com.cheapestbest.androidapp.ui.Activity.CoupanRedeeem;
import com.cheapestbest.androidapp.ui.Fragments.SavedCoupansLocationFragment;
import com.cheapestbest.androidapp.ui.Fragments.SubBrandFragment;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.Locale;

public class SubBrandAdapter extends BaseAdapter {
    private List<SubBrandHelper>ItemList;
    private Context context;
    private IResult mResultCallback;
    private String response_status;
    private Progressbar progressbar;
    private DialogHelper dialogHelper;
    private MyImageLoader myImageLoader;
    private GPSTracker gpsTracker;
    public  SubBrandAdapter(List<SubBrandHelper> itemList, Context context) {
        ItemList = itemList;
        this.context = context;
        gpsTracker=new GPSTracker(this.context);
        progressbar =new Progressbar(context);
        dialogHelper=new DialogHelper(this.context);
        myImageLoader=new MyImageLoader(this.context);
    }

    @Override
    public int getCount() {
        return ItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                view=inflater.inflate(R.layout.sub_brand_adapter_helper,null);
            }
        }
        if (view != null) {
          /*  try {*/
                TextView tvName = view.findViewById(R.id.tv_p_name_subbrand);
                TextView tvPriceUnit = view.findViewById(R.id.tv_p_price_sub_brand);
                TextView tvHintSave = view.findViewById(R.id.hint_save);
                 ImageView imageViewSaveHint=view.findViewById(R.id.klss_add);
                TextView tvLimited=view.findViewById(R.id.red_limit);
                ImageView imageViewLogo = view.findViewById(R.id.p_logo);
                RelativeLayout layoutSave = view.findViewById(R.id.layout_save_subbrand);
                RelativeLayout LcaotionSubrand=view.findViewById(R.id.layout_location_subbrand);
               RelativeLayout relativeLayoutValues=view.findViewById(R.id.layout_values_subbrand);
                tvName.setText(ItemList.get(i).getProductTitle());
               // tvbackPrice.setText(ItemList.get(i).getProductOriginalPrice());
                tvPriceUnit.setText(ItemList.get(i).getProductDescription());


            if(isEmptyString(ItemList.get(i).getProductImage())){
              //  SavedCoupansLocationFragment.CoupanLogoUrl=   SubBrandFragment.BrandLogoUrl;
                myImageLoader.loadImage(NetworkURLs.BaseURLImages+SubBrandFragment.CoverUrl, imageViewLogo);
            } else {
                myImageLoader.loadImage(NetworkURLs.BaseURLImages+ItemList.get(i).getProductImage(), imageViewLogo);
              //  SavedCoupansLocationFragment.CoupanLogoUrl=ItemList.get(i).getProductImage();
            }

                if(ItemList.get(i).isUnlimited()){
                    tvLimited.setBackgroundResource(R.drawable.rounded_textview_green);
                    tvLimited.setText("∞");
                } else {
                    if(isEmptyString(ItemList.get(i).getProductLimit())){
                        tvLimited.setVisibility(View.INVISIBLE);
                    }else {
                        if(isEmptyString(ItemList.get(i).getProductLimit())){
                            tvLimited.setVisibility(View.INVISIBLE);
                        }else {
                            tvLimited.setText(ItemList.get(i).getProductLimit());
                        }
                    }

                }

                if(ItemList.get(i).isSaved_Coupon()){
                  //  tvHintSave.setText("Saved");
                  //  imageViewSaveHint.setBackgroundResource(R.drawable.issaved_coupon);
                    layoutSave.setBackgroundResource(R.drawable.es_save);

                }else {
                    layoutSave.setBackgroundResource(R.drawable.now_sav);
                }


                layoutSave.setOnClickListener(view1 -> {


                    if(!ItemList.get(i).isSaved_Coupon()){
                        SaveCoupanMethod(ItemList.get(i).getProductID());

                    }


                });

                LcaotionSubrand.setOnClickListener(view12 -> {
                    SavedCoupansLocationFragment.SelectedLocationJsonArray=ItemList.get(i).getJsonArrayLocations();
                   if(SavedCoupansLocationFragment.SelectedLocationJsonArray.length()>0){

                       if(SavedCoupansLocationFragment.SelectedLocationJsonArray.length()<2){
                           if(!isEmptyString(ItemList.get(i).getLocationLatitude())&&!isEmptyString(ItemList.get(i).getLocationLongitude())) {
                             /*  Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                       Uri.parse("http://maps.google.com/maps?saddr=" + gpsTracker.getLatitude() + "," + gpsTracker.getLongitude() + "&daddr=" + ItemList.get(i).getLocationLatitude() + "," + ItemList.get(i).getLocationLongitude()));

                               if (intent.resolveActivity(context.getPackageManager()) != null) {
                                   context.startActivity(intent);
                               }*/
                               String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", Float.parseFloat(ItemList.get(i).getLocationLatitude()), Float.parseFloat(ItemList.get(i).getLocationLongitude()), SubBrandFragment.VendorNmae);
                               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                               intent.setPackage("com.google.android.apps.maps");
                               try
                               {
                                   context.startActivity(intent);
                               }
                               catch(ActivityNotFoundException ex) {
                                   try {
                                       Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                       context.startActivity(unrestrictedIntent);
                                   } catch (ActivityNotFoundException innerEx) {
                                       Toast.makeText(context, "Please install a maps application", Toast.LENGTH_LONG).show();
                                   }
                               }
                           }else {
                               SubBrandFragment.listener.onSubBrandFragCallBack(3);
                           }
                       }
                       else {
                           CoupanRedeeem.SelectedCoupanID=ItemList.get(i).getProductID();
                           SavedCoupansLocationFragment.BrandLogoUrl=ItemList.get(i).getProductImage();
                           if(isEmptyString(ItemList.get(i).getProductImage())){
                               SavedCoupansLocationFragment.CoupanLogoUrl=   SubBrandFragment.BrandLogoUrl;
                           } else {
                               SavedCoupansLocationFragment.CoupanLogoUrl=ItemList.get(i).getProductImage();
                           }

                           SavedCoupansLocationFragment.Coupanname=ItemList.get(i).getProductTitle();

                           SubBrandFragment.listener.onSubBrandFragCallBack(1);
                       }
                    }else {
                       SubBrandFragment.listener.onSubBrandFragCallBack(3);
                   }
                });

            relativeLayoutValues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SavedCoupansLocationFragment.SelectedLocationJsonArray=ItemList.get(i).getJsonArrayLocations();
                    CoupanRedeeem.SelectedCoupanID=ItemList.get(i).getProductID();
                    SavedCoupansLocationFragment.BrandLogoUrl=ItemList.get(i).getProductImage();
                    if(isEmptyString(ItemList.get(i).getProductImage())){
                        SavedCoupansLocationFragment.CoupanLogoUrl= SubBrandFragment.BrandLogoUrl;
                    }else {
                        SavedCoupansLocationFragment.CoupanLogoUrl=ItemList.get(i).getProductImage();
                    }
                    /*if(ItemList.get(i).getProductImage().equalsIgnoreCase("null")){
                        SavedCoupansLocationFragment.CoupanLogoUrl=   SubBrandFragment.BrandLogoUrl;
                    }else
                    if(!ItemList.get(i).getProductImage().equalsIgnoreCase("")&&
                            !ItemList.get(i).getProductImage().isEmpty()
                            && !ItemList.get(i).getProductImage().equalsIgnoreCase("null")){
                        //  Toast.makeText(context, "Condition b", Toast.LENGTH_SHORT).show();
                        SavedCoupansLocationFragment.CoupanLogoUrl=ItemList.get(i).getProductImage();
                    }*/

                    SavedCoupansLocationFragment.Coupanname=ItemList.get(i).getProductTitle();

                    SubBrandFragment.listener.onSubBrandFragCallBack(2);
                }
            });
           /* } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
        return view;
    }
    ///////////////////////////////////////////For Save Coupan////////////////////
    /*volley*/

    private void SaveCoupanMethod(String Coupan)
    {
            showprogress();
        initVolleyCallbackForCoupanSaved();
        VolleyService mVolleyService = new VolleyService(mResultCallback, context);
        String MYSaveCoupanUrl=NetworkURLs.BaseSaveCoupanUrl+Coupan+NetworkURLs.SaveCoupanUrl;
        mVolleyService.postDataVolleyWithHeaderWithoutParam("POSTCALL",MYSaveCoupanUrl);
    }

    private void initVolleyCallbackForCoupanSaved(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

             hideprogress();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                            Toast.makeText(context, "Coupan Saved Successfully", Toast.LENGTH_SHORT).show();
                            response_status="true";
                        }else {
                            response_status="false";
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(response_status.equalsIgnoreCase("false")){

                    Toast.makeText(context, "Unable to Save Coupan", Toast.LENGTH_SHORT).show();
                }else {
                    SubBrandFragment.listener.onSubBrandFragCallBack(4);
                    Toast.makeText(context, "Coupon saved successfully", Toast.LENGTH_SHORT).show();
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

                            /*SubBrandFragment fragment = new SubBrandFragment();
                            ((SubBrandFragment) fragment).GetCoupanData();*/



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
