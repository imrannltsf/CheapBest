package com.cheapestbest.androidapp.appadapters;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.SavedLocationHelper;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.MyImageLoader;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.ui.Fragments.SavedCoupansLocationFragment;
import com.cheapestbest.androidapp.apputills.GPSTracker;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SavedLocationsAdapter extends RecyclerView.Adapter<SavedLocationsAdapter.MyViewHolder> {

    private List<SavedLocationHelper>ItemList;
    private Context context;
    private MyImageLoader myImageLoader;
    private RelativeLayout LayoutLocations;
    private GPSTracker gpsTracker;
    private DialogHelper dialogHelper;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvAddressA,tvAddressB;
        ImageView imageViewLogo;
        private RelativeLayout layoutValues;
        MyViewHolder(View view) {
            super(view);

           // ImageView imageViewFooter = view.findViewById(R.id.view_dummy);
            tvName=view.findViewById(R.id.tv_p_name_savedlocation);
            tvAddressA=view.findViewById(R.id.tv_address_line_a);
            tvAddressB=view.findViewById(R.id.tv_address_line_b);
            imageViewLogo=view.findViewById(R.id.p_logo_saved_location);
            LayoutLocations=view.findViewById(R.id.layout_location_where_savedlocation);
            layoutValues=view.findViewById(R.id.layout_values);
           // LinearLayout layoutAdapter = view.findViewById(R.id.layout_saved_loc_adapter);
        }
    }

    public SavedLocationsAdapter(List<SavedLocationHelper> itemList,Context context) {
        this.ItemList = itemList;
        this.context=context;
        gpsTracker=new GPSTracker(context);
        myImageLoader=new MyImageLoader(this.context);
        dialogHelper=new DialogHelper(this.context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_location_adapter_helper, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SavedLocationHelper ItemLocation = ItemList.get(position);
        holder.tvName.setText(ItemLocation.getLocationTitle());
        holder.tvAddressA.setText(ItemLocation.getLocationAddress1());
        holder.tvAddressB.setText(ItemLocation.getLocationAddress2());

        if(SavedCoupansLocationFragment.CoupanLogoUrl.equals("null")){
            myImageLoader.loadImage(NetworkURLs.BaseURLImages+SavedCoupansLocationFragment.CoupanLogoUrl,holder.imageViewLogo);
        }else if (!SavedCoupansLocationFragment.CoupanLogoUrl.equalsIgnoreCase(""))
        {
            myImageLoader.loadImage(NetworkURLs.BaseURLImages+SavedCoupansLocationFragment.CoupanLogoUrl,holder.imageViewLogo);
        }else {
            myImageLoader.loadImage(NetworkURLs.BaseURLImages+SavedCoupansLocationFragment.BrandLogoUrl,holder.imageViewLogo);
        }
       LayoutLocations.setOnClickListener(view -> {

           if(locationServicesEnabled(context)){
               String StrLat=ItemLocation.getLocationLatitude();
               String StrLong=ItemLocation.getLocationLongitude();

               if(!StrLat.equalsIgnoreCase("null")&&!StrLong.equalsIgnoreCase("null")){
                   if(Double.parseDouble(StrLat)!=0.0&&Double.parseDouble(StrLong)!=0.0){
                       //  if(gpsTracker.getLatitude()!=0.0&&gpsTracker.getLongitude()!=0.0){
                       Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                               Uri.parse("http://maps.google.com/maps?saddr="+gpsTracker.getLatitude()+","+gpsTracker.getLongitude()+"&daddr="+StrLat+","+StrLong));

                       if (intent.resolveActivity(context.getPackageManager()) != null) {
                           context.startActivity(intent);
                       }
                   /*}else {
                       SavedCoupansLocationFragment.listener.onLocationFragCallBack(3);

                   }*/

                   }else {
                       SavedCoupansLocationFragment.listener.onLocationFragCallBack(2);
                   }

               }else {
                   SavedCoupansLocationFragment.listener.onLocationFragCallBack(2);

               }
           }else {
               dialogHelper.buildAlertMessageNoGps();
           }


       });
        holder.layoutValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SavedCoupansLocationFragment.listener.onLocationFragCallBack(1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }



    private static boolean locationServicesEnabled(Context context) {
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



}
