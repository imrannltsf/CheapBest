/*
package com.takisoft.datetimepicker.sample.appadapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.adpterUtills.SaveCoupanHelper;
import com.takisoft.datetimepicker.sample.apputills.MyImageLoader;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.ui.Activity.CoupanRedeeem;
import com.takisoft.datetimepicker.sample.ui.Fragments.CoupanFragment;
import com.takisoft.datetimepicker.sample.ui.Fragments.SavedCoupansLocationFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MyImageLoader myImageLoader;
    private RelativeLayout LayoutLocations,LayoutValues;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;
    private List<SaveCoupanHelper> ItemList;
    private List<SaveCoupanHelper> stringArrayList;
   // private Activity activity;
    private Context context;
   */
/* public RecyclerViewAdapter(Activity activity, ArrayList<String> strings) {
        this.activity = activity;
        this.stringArrayList = strings;
    }*//*


    public RecyclerViewAdapter(List<SaveCoupanHelper> itemList, Context context) {
        this.ItemList = itemList;
        this.context = context;
        myImageLoader = new MyImageLoader(this.context);
        this.stringArrayList = itemList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //Inflating recycle view item layout
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupan_adapter_helper, parent, false);
            return new ItemViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            //Inflating header view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_header_recyler, parent, false);
            return new HeaderViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            //Inflating footer view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_footer_recycler, parent, false);
            return new FooterViewHolder(itemView);
        } else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headerTitle.setText("Header View");
            headerHolder.headerTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "You clicked at Header View!", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.footerText.setText("Footer View");
            footerHolder.footerText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "You clicked at Footer View", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (holder instanceof ItemViewHolder) {
           ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            */
/* itemViewHolder.itemText.setText("Recycler Item " + position);
            itemViewHolder.itemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "You clicked at item " + position, Toast.LENGTH_SHORT).show();
                }
            });*//*


            SaveCoupanHelper ItemLocation = ItemList.get(position);
            itemViewHolder.tvName.setText(ItemLocation.getCoupanTitle());
            itemViewHolder.tvDescription.setText(ItemLocation.getCoupanDescription());
            //holder.tvOffers.setText(ItemLocation.getCoupanDiscount());
            if(ItemLocation.getCoupanImage().equals("null")){
                myImageLoader.loadImage(NetworkURLs.BaseURLImages+ItemLocation.getCoupanVendorLogo(),itemViewHolder.imageViewLogo);
            }else if (!ItemLocation.getCoupanImage().equalsIgnoreCase(""))
            {
                Picasso.get().load(NetworkURLs.BaseURLImages+ItemLocation.getCoupanImage()).into(itemViewHolder.imageViewLogo);
            }else {
                myImageLoader.loadImage(NetworkURLs.BaseURLImages+ItemLocation.getCoupanVendorLogo(),itemViewHolder.imageViewLogo);
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
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == stringArrayList.size() + 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size() + 2;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        public HeaderViewHolder(View view) {
            super(view);
            headerTitle = view.findViewById(R.id.header_text);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView footerText;

        public FooterViewHolder(View view) {
            super(view);
            footerText = view.findViewById(R.id.footer_text);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemText;

        TextView tvName,tvDescription;
        ImageView imageViewLogo;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_p_name_savedlocation);
            tvName=itemView.findViewById(R.id.tv_p_name_savedcoupan);
            tvDescription=itemView.findViewById(R.id.tv_descrip_savedbrand);
            // tvOffers=view.findViewById(R.id.tv_discount_savedcoupan);
            imageViewLogo=itemView.findViewById(R.id.p_logo_savedcoupan);
            LayoutLocations=itemView.findViewById(R.id.layout_location_where_savedcoupans);
            LayoutValues=itemView.findViewById(R.id.layout_values_save_coupan);
        }
    }
}
*/
