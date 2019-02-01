package com.takisoft.datetimepicker.sample.appadapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.adpterUtills.SubBrandHelper;
import com.takisoft.datetimepicker.sample.apputills.DialogHelper;
import com.takisoft.datetimepicker.sample.apputills.MyImageLoader;
import com.takisoft.datetimepicker.sample.apputills.Progressbar;
import com.takisoft.datetimepicker.sample.network.IResult;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.network.VolleyService;
import com.takisoft.datetimepicker.sample.ui.Activity.CoupanRedeeem;
import com.takisoft.datetimepicker.sample.ui.Fragments.SavedCoupansLocationFragment;
import com.takisoft.datetimepicker.sample.ui.Fragments.SubBrandFragment;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class SubBrandAdapter extends BaseAdapter {
    private List<SubBrandHelper>ItemList;
    private Context context;
    private IResult mResultCallback;
    private String response_status;
    private MyImageLoader myImageLoader;
    private Progressbar progressbar;
    private DialogHelper dialogHelper;
    public  SubBrandAdapter(List<SubBrandHelper> itemList, Context context) {
        ItemList = itemList;
        this.context = context;
        progressbar =new Progressbar(context);
        myImageLoader=new MyImageLoader(this.context);
        dialogHelper=new DialogHelper(this.context);
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
               // TextView tvbackPrice = view.findViewById(R.id.tv_p_sub_detail_sub_brand);
                TextView tvLimited=view.findViewById(R.id.red_limit);
               // ImageView imageViewLogo = view.findViewById(R.id.p_logo);
                RelativeLayout layoutSave = view.findViewById(R.id.layout_save_subbrand);
                RelativeLayout LcaotionSubrand=view.findViewById(R.id.layout_location_subbrand);
               RelativeLayout relativeLayoutValues=view.findViewById(R.id.layout_values_subbrand);
                tvName.setText(ItemList.get(i).getProductTitle());
               // tvbackPrice.setText(ItemList.get(i).getProductOriginalPrice());
                tvPriceUnit.setText(ItemList.get(i).getProductDescription());

                if(ItemList.get(i).isUnlimited()){
                    tvLimited.setVisibility(View.INVISIBLE);
                }else if(Integer.parseInt(ItemList.get(i).getProductLimit())==0){
                    tvLimited.setVisibility(View.INVISIBLE);
                }else {
                    tvLimited.setText(ItemList.get(i).getProductLimit());
                }


                layoutSave.setOnClickListener(view1 -> {
                  //  String ProductID=ItemList.get(i).getProductID();

                    SaveCoupanMethod(ItemList.get(i).getProductID());
                });

                LcaotionSubrand.setOnClickListener(view12 -> {
                    SavedCoupansLocationFragment.SelectedLocationJsonArray=ItemList.get(i).getJsonArrayLocations();

                   if(SavedCoupansLocationFragment.SelectedLocationJsonArray.length()>0){
                        CoupanRedeeem.SelectedCoupanID=ItemList.get(i).getProductID();
                        SavedCoupansLocationFragment.BrandLogoUrl=ItemList.get(i).getProductImage();
                        if(ItemList.get(i).getProductImage().equalsIgnoreCase("null")){
                            SavedCoupansLocationFragment.CoupanLogoUrl=   SubBrandFragment.BrandLogoUrl;
                        }else
                        if(!ItemList.get(i).getProductImage().equalsIgnoreCase("null")
                                &&!ItemList.get(i).getProductImage().isEmpty()
                                &&!ItemList.get(i).getProductImage().equalsIgnoreCase("")){

                            SavedCoupansLocationFragment.CoupanLogoUrl=ItemList.get(i).getProductImage();
                        }

                        SavedCoupansLocationFragment.Coupanname=ItemList.get(i).getProductTitle();

                        SubBrandFragment.listener.onSubBrandFragCallBack(1);
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
                    if(ItemList.get(i).getProductImage().equalsIgnoreCase("null")){
                        SavedCoupansLocationFragment.CoupanLogoUrl=   SubBrandFragment.BrandLogoUrl;
                    }else
                    if(!ItemList.get(i).getProductImage().equalsIgnoreCase("")&&
                            !ItemList.get(i).getProductImage().isEmpty()
                            && !ItemList.get(i).getProductImage().equalsIgnoreCase("null")){
                        //  Toast.makeText(context, "Condition b", Toast.LENGTH_SHORT).show();
                        SavedCoupansLocationFragment.CoupanLogoUrl=ItemList.get(i).getProductImage();
                    }

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
                    Toast.makeText(context, "Coupan Saved Successfully", Toast.LENGTH_SHORT).show();
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

    public void showprogress(){

        progressbar.ShowProgress();
        progressbar.setCancelable(false);

    }

    public void hideprogress(){
        progressbar.HideProgress();

    }
}
