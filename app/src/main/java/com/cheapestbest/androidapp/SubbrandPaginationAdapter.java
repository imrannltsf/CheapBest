package com.cheapestbest.androidapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Suleiman on 19/10/16.
 */

public class SubbrandPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<SubBrandHelper> ItemList;
    private Context context;

    private boolean isLoadingAdded = false;

     private IResult mResultCallback;
    private String response_status;
    private Progressbar progressbar;
    private DialogHelper dialogHelper;
    private MyImageLoader myImageLoader;
    private GPSTracker gpsTracker;
    public  SubbrandPaginationAdapter(List<SubBrandHelper> itemList, Context context) {
        ItemList = itemList;
        this.context = context;
        gpsTracker=new GPSTracker(this.context);
        progressbar =new Progressbar(context);
        dialogHelper=new DialogHelper(this.context);
        myImageLoader=new MyImageLoader(this.context);
    }

    public List<SubBrandHelper> getBrandItems() {
        return ItemList;
    }

    public void setSubBrandItems(List<SubBrandHelper> movies) {
        this.ItemList = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.sub_brand_adapter_helper, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



        SubBrandHelper item = ItemList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                MovieVH movieVH = (MovieVH) holder;

                //movieVH.textView.setText(movie.getTitle());
                movieVH.tvName.setText(item.getProductTitle());
                // tvbackPrice.setText(ItemList.get(i).getProductOriginalPrice());
                movieVH.tvPriceUnit.setText(item.getProductDescription());


                if(isEmptyString(item.getProductImage())){
                    //  SavedCoupansLocationFragment.CoupanLogoUrl=   SubBrandFragment.BrandLogoUrl;
                    myImageLoader.loadImage(NetworkURLs.BaseURLImages+SubBrandFragment.CoverUrl, movieVH.imageViewLogo);
                } else {
                    myImageLoader.loadImage(NetworkURLs.BaseURLImages+item.getProductImage(), movieVH.imageViewLogo);
                    //  SavedCoupansLocationFragment.CoupanLogoUrl=ItemList.get(i).getProductImage();
                }

                if(item.isUnlimited()){
                    movieVH.tvLimited.setBackgroundResource(R.drawable.rounded_textview_green);
                    movieVH.tvLimited.setText("∞");
                } else {
                    if(isEmptyString(item.getProductLimit())){
                        movieVH.tvLimited.setVisibility(View.INVISIBLE);
                    }else {
                        if(isEmptyString(item.getProductLimit())){
                            movieVH.tvLimited.setVisibility(View.INVISIBLE);
                        }else {
                            movieVH.tvLimited.setText(item.getProductLimit());
                        }
                    }

                }

                if(item.isSaved_Coupon()){
                    //  tvHintSave.setText("Saved");
                    //  imageViewSaveHint.setBackgroundResource(R.drawable.issaved_coupon);
                    movieVH.layoutSave.setBackgroundResource(R.drawable.es_save);

                }else {
                    movieVH.layoutSave.setBackgroundResource(R.drawable.now_sav);
                }


                movieVH.layoutSave.setOnClickListener(view1 -> {


                    if(!item.isSaved_Coupon()){
                        SaveCoupanMethod(item.getProductID());

                    }


                });

                movieVH.LcaotionSubrand.setOnClickListener(view12 -> {
                    SavedCoupansLocationFragment.SelectedLocationJsonArray=item.getJsonArrayLocations();
                    if(SavedCoupansLocationFragment.SelectedLocationJsonArray.length()>0){

                        if(SavedCoupansLocationFragment.SelectedLocationJsonArray.length()<2){
                            if(gpsTracker.getLatitude()!=0.0&&gpsTracker.getLongitude()!=0.0&&!isEmptyString(item.getLocationLatitude())&&!isEmptyString(item.getLocationLongitude())) {
                             /*  Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                       Uri.parse("http://maps.google.com/maps?saddr=" + gpsTracker.getLatitude() + "," + gpsTracker.getLongitude() + "&daddr=" + ItemList.get(i).getLocationLatitude() + "," + ItemList.get(i).getLocationLongitude()));

                               if (intent.resolveActivity(context.getPackageManager()) != null) {
                                   context.startActivity(intent);
                               }*/
                                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", Float.parseFloat(item.getLocationLatitude()), Float.parseFloat(item.getLocationLongitude()), SubBrandFragment.VendorNmae);
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
                            CoupanRedeeem.SelectedCoupanID=item.getProductID();
                            SavedCoupansLocationFragment.BrandLogoUrl=item.getProductImage();
                            if(isEmptyString(item.getProductImage())){
                                SavedCoupansLocationFragment.CoupanLogoUrl=   SubBrandFragment.BrandLogoUrl;
                            } else {
                                SavedCoupansLocationFragment.CoupanLogoUrl=item.getProductImage();
                            }

                            SavedCoupansLocationFragment.Coupanname=item.getProductTitle();

                            SubBrandFragment.listener.onSubBrandFragCallBack(1);
                        }
                    }else {
                        SubBrandFragment.listener.onSubBrandFragCallBack(3);
                    }
                });

                movieVH.relativeLayoutValues.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SavedCoupansLocationFragment.SelectedLocationJsonArray=item.getJsonArrayLocations();
                        CoupanRedeeem.SelectedCoupanID=item.getProductID();
                        SavedCoupansLocationFragment.BrandLogoUrl=item.getProductImage();
                        if(isEmptyString(item.getProductImage())){
                            SavedCoupansLocationFragment.CoupanLogoUrl= SubBrandFragment.BrandLogoUrl;
                        }else {
                            SavedCoupansLocationFragment.CoupanLogoUrl=item.getProductImage();
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

                        SavedCoupansLocationFragment.Coupanname=item.getProductTitle();

                        SubBrandFragment.listener.onSubBrandFragCallBack(2);
                    }
                });
                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return ItemList == null ? 0 : ItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == ItemList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(SubBrandHelper mc) {
        ItemList.add(mc);
        notifyItemInserted(ItemList.size() - 1);
    }

    public void addAll(List<SubBrandHelper> mcList) {
        for (SubBrandHelper mc : mcList) {
            add(mc);
        }
    }

    public void remove(SubBrandHelper city) {
        int position = ItemList.indexOf(city);
        if (position > -1) {
            ItemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    /*public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Sub());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movies.size() - 1;
        Movie item = getItem(position);

        if (item != null) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }*/

    public SubBrandHelper getItem(int position) {
        return ItemList.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder {
        TextView tvName,tvPriceUnit,tvHintSave,tvLimited;
        RelativeLayout layoutSave,relativeLayoutValues,LcaotionSubrand;
        ImageView imageViewLogo,imageViewSaveHint;

        public MovieVH(View itemView) {
            super(itemView);
             tvName = itemView.findViewById(R.id.tv_p_name_subbrand);
             tvPriceUnit = itemView.findViewById(R.id.tv_p_price_sub_brand);
          tvHintSave = itemView.findViewById(R.id.hint_save);
             imageViewSaveHint=itemView.findViewById(R.id.klss_add);
            tvLimited=itemView.findViewById(R.id.red_limit);
             imageViewLogo = itemView.findViewById(R.id.p_logo);
             layoutSave = itemView.findViewById(R.id.layout_save_subbrand);
             LcaotionSubrand=itemView.findViewById(R.id.layout_location_subbrand);
             relativeLayoutValues=itemView.findViewById(R.id.layout_values_subbrand);

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
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
