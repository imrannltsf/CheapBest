package com.takisoft.datetimepicker.sample.appadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.adpterUtills.SaveCoupanHelper;
import com.takisoft.datetimepicker.sample.apputills.MyImageLoader;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.ui.Activity.CoupanRedeeem;
import com.takisoft.datetimepicker.sample.ui.Fragments.CoupanFragment;
import com.takisoft.datetimepicker.sample.ui.Fragments.SavedCoupansLocationFragment;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CoupanAdapter extends RecyclerView.Adapter<CoupanAdapter.MyViewHolder> {

    private List<SaveCoupanHelper>ItemList;
    private Context context;
    private MyImageLoader myImageLoader;
    private RelativeLayout LayoutLocations,LayoutValues;
     private static OnSwipeListener SwipeCallListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
      /*  ,tvOffers*/
        TextView tvName,tvDescription;
        ImageView imageViewLogo;


        MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_p_name_savedlocation);
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
}
