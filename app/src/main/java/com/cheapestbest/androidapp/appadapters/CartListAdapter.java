package com.cheapestbest.androidapp.appadapters;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.SaveCoupanHelper;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.GPSTracker;
import com.cheapestbest.androidapp.apputills.MyImageLoader;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.ui.Activity.CoupanRedeeem;
import com.cheapestbest.androidapp.ui.Fragments.CoupanFragment;
import com.cheapestbest.androidapp.ui.Fragments.SavedCoupansLocationFragment;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {
    private Context context;
    private DialogHelper dialogHelper;
    private List<SaveCoupanHelper> ItemList;
    private MyImageLoader myImageLoader;
    private RelativeLayout LayoutLocations,LayoutValues;

    private GPSTracker gpsTracker;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tvName,tvDescription;
        ImageView imageViewLogo;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);

            tvName=view.findViewById(R.id.tv_p_name_savedcoupan);
            tvDescription=view.findViewById(R.id.tv_descrip_savedbrand);

            imageViewLogo=view.findViewById(R.id.p_logo_savedcoupan);
            LayoutLocations=view.findViewById(R.id.layout_location_where_savedcoupans);
            LayoutValues=view.findViewById(R.id.layout_values_save_coupan);


            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public CartListAdapter(List<SaveCoupanHelper> itemList, Context context) {
        this.ItemList = itemList;
        this.context = context;
        dialogHelper=new DialogHelper(this.context);
        gpsTracker=new GPSTracker(context);
        myImageLoader = new MyImageLoader(this.context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        SaveCoupanHelper ItemLocation = ItemList.get(position);
        holder.tvName.setText(ItemLocation.getCoupanTitle());
        holder.tvDescription.setText(ItemLocation.getCoupanDescription());
        //holder.tvOffers.setText(ItemLocation.getCoupanDiscount());
        if(ItemLocation.getCoupanImage().equals("null")){
            myImageLoader.loadImage(NetworkURLs.BaseURLImages+ItemLocation.getCoupanVendorLogo(),holder.imageViewLogo);
        }else if (!ItemLocation.getCoupanImage().equalsIgnoreCase(""))
        {
            Picasso.get().load(NetworkURLs.BaseURLImages+ItemLocation.getCoupanImage()).into(holder.imageViewLogo);
        }else {
            myImageLoader.loadImage(NetworkURLs.BaseURLImages+ItemLocation.getCoupanVendorLogo(),holder.imageViewLogo);
        }

        LayoutLocations.setOnClickListener(view -> {

            if(doesUserHavePermission()){
                if(locationServicesEnabled(context)){
                    SavedCoupansLocationFragment.SelectedLocationJsonArray=ItemLocation.getJsonArray();


                    if(SavedCoupansLocationFragment.SelectedLocationJsonArray.length()>0){

                        if(SavedCoupansLocationFragment.SelectedLocationJsonArray.length()<2){
                            if(!isEmptyString(ItemLocation.getLocationLatitude())&&!isEmptyString(ItemLocation.getLocationLongitude())) {


                                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", Float.parseFloat(ItemLocation.getLocationLatitude()), Float.parseFloat(ItemLocation.getLocationLongitude()), ItemLocation.getCoupanTitle());
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
                                CoupanFragment.listener.onCoupanFragCallBack(5);
                            }
                        }
                        else {

                            CoupanRedeeem.SelectedCoupanID=ItemLocation.getCoupanID();
                            SavedCoupansLocationFragment.BrandLogoUrl=ItemLocation.getCoupanVendorLogo();
                            if(ItemLocation.getCoupanImage().equals("null")){
                                SavedCoupansLocationFragment.CoupanLogoUrl=ItemLocation.getCoupanVendorLogo();
                            }else
                            if(!ItemLocation.getCoupanImage().equalsIgnoreCase("")||!ItemLocation.getCoupanImage().isEmpty()){
                                SavedCoupansLocationFragment.CoupanLogoUrl=ItemLocation.getCoupanImage();
                            }

                            SavedCoupansLocationFragment.Coupanname=ItemLocation.getCoupanTitle();

                            CoupanFragment.listener.onCoupanFragCallBack(2);
                        }
                    }else {
                        CoupanFragment.listener.onCoupanFragCallBack(5);
                    }

                }else {
                    dialogHelper.buildAlertMessageNoGps();
                }
            }else {
                dialogHelper.gotopermission();
            }




        });

        LayoutValues.setOnClickListener(view -> {

            SavedCoupansLocationFragment.Coupanname=ItemList.get(position).getCoupanTitle();

            SavedCoupansLocationFragment.SelectedLocationJsonArray=ItemLocation.getJsonArray();
            CoupanRedeeem.SelectedCoupanID=ItemLocation.getCoupanID();

            CoupanRedeeem.SelectedCoupanID=ItemLocation.getCoupanID();
            CoupanFragment.listener.onCoupanFragCallBack(1);

        });
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    public void removeItem(int position) {
        ItemList.remove(position);

        notifyItemRemoved(position);
    }
    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
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


    private boolean doesUserHavePermission()
    {
        int result = context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }


}
