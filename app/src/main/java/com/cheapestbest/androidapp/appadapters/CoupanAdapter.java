package com.cheapestbest.androidapp.appadapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.SaveCoupanHelper;
import com.cheapestbest.androidapp.apputills.GPSTracker;
import com.cheapestbest.androidapp.apputills.MyImageLoader;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.ui.Activity.CoupanRedeeem;
import com.cheapestbest.androidapp.ui.Fragments.CoupanFragment;
import com.cheapestbest.androidapp.ui.Fragments.SavedCoupansLocationFragment;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CoupanAdapter extends RecyclerView.Adapter<CoupanAdapter.MyViewHolder> {

    private List<SaveCoupanHelper>ItemList;
    private Context context;
    private MyImageLoader myImageLoader;
    private RelativeLayout LayoutLocations,LayoutValues;
     private static OnSwipeListener SwipeCallListener;

     private GPSTracker gpsTracker;

    public class MyViewHolder extends RecyclerView.ViewHolder {
      /*  ,tvOffers*/
        TextView tvName,tvDescription;
        ImageView imageViewLogo;


        MyViewHolder(View view) {
            super(view);

           tvName=view.findViewById(R.id.tv_p_name_savedcoupan);
            tvDescription=view.findViewById(R.id.tv_descrip_savedbrand);

           // tvOffers=view.findViewById(R.id.tv_discount_savedcoupan);
            imageViewLogo=view.findViewById(R.id.p_logo_savedcoupan);
            LayoutLocations=view.findViewById(R.id.layout_location_where_savedcoupans);
            LayoutValues=view.findViewById(R.id.layout_values_save_coupan);
        }
    }

    public CoupanAdapter(List<SaveCoupanHelper> itemList, Context context) {
        this.ItemList = itemList;
        this.context = context;
        gpsTracker=new GPSTracker(context);
        myImageLoader = new MyImageLoader(this.context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coupan_adapter_helper, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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
            SavedCoupansLocationFragment.SelectedLocationJsonArray=ItemLocation.getJsonArray();

            if(SavedCoupansLocationFragment.SelectedLocationJsonArray.length()>0){

                if(SavedCoupansLocationFragment.SelectedLocationJsonArray.length()<2){
                    if(!isEmptyString(ItemLocation.getLocationLatitude())&&!isEmptyString(ItemLocation.getLocationLongitude())) {
                       /* Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=" + gpsTracker.getLatitude() + "," + gpsTracker.getLongitude() + "&daddr=" + ItemLocation.getLocationLatitude() + "," + ItemLocation.getLocationLongitude()));

                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(intent);
                        }*/

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
                  /*  CoupanRedeeem.SelectedCoupanID=ItemList.get(i).getProductID();
                    SavedCoupansLocationFragment.BrandLogoUrl=ItemList.get(i).getProductImage();
                    if(isEmptyString(ItemList.get(i).getProductImage())){
                        SavedCoupansLocationFragment.CoupanLogoUrl=   SubBrandFragment.BrandLogoUrl;
                    } else {
                        SavedCoupansLocationFragment.CoupanLogoUrl=ItemLocation.getCoupanVendorLogo();
                    }

                    SavedCoupansLocationFragment.Coupanname=ItemLocation.getCoupanTitle();

                    CoupanFragment.listener.onCoupanFragCallBack(2);*/
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


          /* CoupanRedeeem.SelectedCoupanID=ItemLocation.getCoupanID();
            SavedCoupansLocationFragment.BrandLogoUrl=ItemLocation.getCoupanVendorLogo();
            if(ItemLocation.getCoupanImage().equals("null")){
                SavedCoupansLocationFragment.CoupanLogoUrl=ItemLocation.getCoupanVendorLogo();
            }else
            if(!ItemLocation.getCoupanImage().equalsIgnoreCase("")||!ItemLocation.getCoupanImage().isEmpty()){
                SavedCoupansLocationFragment.CoupanLogoUrl=ItemLocation.getCoupanImage();
            }

            SavedCoupansLocationFragment.Coupanname=ItemLocation.getCoupanTitle();

            CoupanFragment.listener.onCoupanFragCallBack(2);*/

        });

        LayoutValues.setOnClickListener(view -> {

            SavedCoupansLocationFragment.Coupanname=ItemList.get(position).getCoupanTitle();

            SavedCoupansLocationFragment.SelectedLocationJsonArray=ItemLocation.getJsonArray();
            CoupanRedeeem.SelectedCoupanID=ItemLocation.getCoupanID();


            if(SavedCoupansLocationFragment.SelectedLocationJsonArray.length()>1){
                SavedCoupansLocationFragment.BrandLogoUrl=ItemLocation.getCoupanVendorLogo();
                if(ItemLocation.getCoupanImage().equals("null")){
                    SavedCoupansLocationFragment.CoupanLogoUrl=ItemLocation.getCoupanVendorLogo();
                }else
                if(!ItemLocation.getCoupanImage().equalsIgnoreCase("")||!ItemLocation.getCoupanImage().isEmpty()){
                    SavedCoupansLocationFragment.CoupanLogoUrl=ItemLocation.getCoupanImage();
                }

                SavedCoupansLocationFragment.Coupanname=ItemLocation.getCoupanTitle();

                CoupanFragment.listener.onCoupanFragCallBack(2);
            }else {
            CoupanRedeeem.SelectedCoupanID=ItemLocation.getCoupanID();
            CoupanFragment.listener.onCoupanFragCallBack(1);
            }

        });

    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }



    public interface OnSwipeListener {
        void onSwipe(int position, View v);
    }

    public void setOnSwipe(OnSwipeListener myClickListener) {
        CoupanAdapter.SwipeCallListener = myClickListener;
    }

    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }


}
