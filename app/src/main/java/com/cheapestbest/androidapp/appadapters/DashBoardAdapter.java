package com.cheapestbest.androidapp.appadapters;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.MainDashBoardHelper;
import com.cheapestbest.androidapp.apputills.MyImageLoader;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.ui.Activity.MainDashBoard;
import com.cheapestbest.androidapp.ui.Fragments.MainDashBoardFragment;
import com.cheapestbest.androidapp.ui.Fragments.SubBrandFragment;
import com.cheapestbest.androidapp.apputills.GPSTracker;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class DashBoardAdapter extends BaseAdapter {

    private LinearLayout layoutHelper;
    private List<MainDashBoardHelper>ItemList;
    private GPSTracker gpsTracker;
    private Context context;
    private MyImageLoader myImageLoader;
    public DashBoardAdapter(List<MainDashBoardHelper> itemList, Context context) {
        ItemList = itemList;
        this.context = context;
        gpsTracker=new GPSTracker(this.context);
        myImageLoader=new MyImageLoader( this.context);
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
                view=inflater.inflate(R.layout.dash_board_adapter_helper,null);
            }
        }
        TextView tvName,tvPriceUnit,tvOffers,tvdis,tvHintVendor;
        ImageView imageViewLogo,imageViewHintVendor;
        RelativeLayout relativeLayoutMove;
        RelativeLayout relativeLayoutLocation;
        RelativeLayout relativeLayoutSave;
        if (view != null) {
            relativeLayoutSave=view.findViewById(R.id.layout_save_vendors);
            layoutHelper=view.findViewById(R.id.layout_dashboard_adapter);
            tvName=view.findViewById(R.id.tv_p_name);
            tvPriceUnit=view.findViewById(R.id.tv_p_price);
            tvOffers=view.findViewById(R.id.tv_p_offers);
            tvdis=view.findViewById(R.id.tv_distance_loc);
            tvHintVendor=view.findViewById(R.id.hint_add_vendor);
            imageViewHintVendor=view.findViewById(R.id.img_add_vendor);
            imageViewLogo=view.findViewById(R.id.p_logo);
            relativeLayoutMove=view.findViewById(R.id.layout_values);
            relativeLayoutLocation=view.findViewById(R.id.layout_location);
            tvName.setText(ItemList.get(i).getStrName());
            tvPriceUnit.setText(ItemList.get(i).getStrNearBranchAddress());
            tvOffers.setText(String.valueOf("Total Offers:"+ItemList.get(i).getStrCoupans_count()));
       //  Toast.makeText(context, String.valueOf(ItemList.get(i).isVendorSaved()), Toast.LENGTH_SHORT).show();

            /*if(i==MainDashBoardFragment.isnowsaved){
                if(MainDashBoardFragment.isnowsaved>0&&!ItemList.get(i).isVendorSaved()){
                    relativeLayoutSave.setBackgroundResource(R.drawable.es_save);
                    MainDashBoardFragment.isnowsaved=0;
                }else {
                    relativeLayoutSave.setBackgroundResource(R.drawable.now_sav);
                }

            }else {
                if(ItemList.get(i).isVendorSaved()){

                    relativeLayoutSave.setBackgroundResource(R.drawable.es_save);
               *//* tvHintVendor.setText("Saved");
                imageViewHintVendor.setBackgroundResource(R.drawable.issaved_coupon);*//*
                    // Toast.makeText(context, "Vendor already saved", Toast.LENGTH_SHORT).show();
                }else {
                    relativeLayoutSave.setBackgroundResource(R.drawable.now_sav);
                    //    Toast.makeText(context, String.valueOf(ItemList.get(i).isVendorSaved()+"\n"+ItemList.get(i).getStrID()), Toast.LENGTH_SHORT).show();
                }
            }*/

            if(ItemList.get(i).isVendorSaved()){

                relativeLayoutSave.setBackgroundResource(R.drawable.es_save);
               /* tvHintVendor.setText("Saved");
                imageViewHintVendor.setBackgroundResource(R.drawable.issaved_coupon);*/
                // Toast.makeText(context, "Vendor already saved", Toast.LENGTH_SHORT).show();
            }else {
                relativeLayoutSave.setBackgroundResource(R.drawable.now_sav);
                //    Toast.makeText(context, String.valueOf(ItemList.get(i).isVendorSaved()+"\n"+ItemList.get(i).getStrID()), Toast.LENGTH_SHORT).show();
            }
           /* if(ItemList.get(i).isVendorSaved()){

                relativeLayoutSave.setBackgroundResource(R.drawable.es_save);
               *//* tvHintVendor.setText("Saved");
                imageViewHintVendor.setBackgroundResource(R.drawable.issaved_coupon);*//*
               // Toast.makeText(context, "Vendor already saved", Toast.LENGTH_SHORT).show();
            }else {
                relativeLayoutSave.setBackgroundResource(R.drawable.now_sav);
            //    Toast.makeText(context, String.valueOf(ItemList.get(i).isVendorSaved()+"\n"+ItemList.get(i).getStrID()), Toast.LENGTH_SHORT).show();
            }*/


            if(!isEmptyString(ItemList.get(i).getStrLatitude())&&!isEmptyString(ItemList.get(i).getStrLongitude())&&
                    gpsTracker.getLongitude()!=0.0&&gpsTracker.getLatitude()!=0.0){
                //Toast.makeText(context, String.valueOf(ItemList.get(i).getStrLatitude()+"/n"+ItemList.get(i).getStrLongitude()), Toast.LENGTH_SHORT).show();

                //Toast.makeText(context, "in this condtion", Toast.LENGTH_SHORT).show();

            Location locationA = new Location("point A");

            locationA.setLatitude(gpsTracker.getLatitude());
            locationA.setLongitude(gpsTracker.getLongitude());

            Location locationB = new Location("point B");

            locationB.setLatitude(Double.parseDouble(ItemList.get(i).getStrLatitude()));

            locationB.setLongitude(Double.parseDouble(ItemList.get(i).getStrLongitude()));

            double distance = (locationA.distanceTo(locationB)/1000)*0.621371;
                tvdis.setText(String.valueOf(new DecimalFormat("##.##").format(distance)+":Miles"));

            }else {
                tvdis.setText(" ");
            }
            String img_url = ItemList.get(i).getStrLogo();

           // tvdis.setText(String.valueOf(distance(gpsTracker.getLatitude(),gpsTracker.getLongitude(),Double.parseDouble(ItemList.get(i).getStrLatitude()),Double.parseDouble(ItemList.get(i).getStrLongitude()))));
           // myImageLoader.showDialogAlert(NetworkURLs.BaseURLImages+ItemList.get(i).getStrLogo());
            if (!img_url.equalsIgnoreCase("")){
                myImageLoader.loadImage(NetworkURLs.BaseURLImages+ItemList.get(i).getStrLogo(),imageViewLogo);
            }



            relativeLayoutMove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SubBrandFragment.StrVendorID=MainDashBoard.DashBoardList.get(i).getStrID();
                    SubBrandFragment.CoverUrl=MainDashBoard.DashBoardList.get(i).getStrCoverPhoto();
                    SubBrandFragment.BrandLogoUrl=MainDashBoard.DashBoardList.get(i).getStrLogo();
                    SubBrandFragment.VendorNmae=String.valueOf(MainDashBoard.DashBoardList.get(i).getStrName());
                 //   Toast.makeText(context, String.valueOf(MainDashBoard.DashBoardList.get(i).getStrName()), Toast.LENGTH_SHORT).show();
                    MainDashBoardFragment.listener.onDashBoardCallBack(1);
                }
            });

            relativeLayoutLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String StrLat=ItemList.get(i).getStrLatitude();
                    String StrLong=ItemList.get(i).getStrLongitude();

                    /*Toast.makeText(context, StrLat+","+StrLong, Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, gpsTracker.getLatitude()+","+gpsTracker.getLongitude(), Toast.LENGTH_SHORT).show();
                    */
                    if(!isEmptyString(StrLat)&&!isEmptyString(StrLong)){
                        if(Double.parseDouble(StrLat)!=0.0&&Double.parseDouble(StrLong)!=0.0){
                       //     if(gpsTracker.getLatitude()!=0.0&&gpsTracker.getLongitude()!=0.0){
                              /*  Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?saddr="+gpsTracker.getLatitude()+","+gpsTracker.getLongitude()+"&daddr="+StrLat+","+StrLong));

                                if (intent.resolveActivity(context.getPackageManager()) != null) {
                                    context.startActivity(intent);
                                }*/

                                /*Uri navigationIntentUri = Uri.parse("google.navigation:q=" + StrLat +"," + StrLong);//creating intent with latlng
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                                    context.startActivity(mapIntent);
                                }*/

                                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", Float.parseFloat(StrLat), Float.parseFloat(StrLong), MainDashBoard.DashBoardList.get(i).getStrName());
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setPackage("com.google.android.apps.maps");
                                try
                                {
                                    context.startActivity(intent);
                                }
                                catch(ActivityNotFoundException ex)
                                {
                                    try
                                    {
                                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                        context.startActivity(unrestrictedIntent);
                                    }
                                    catch(ActivityNotFoundException innerEx)
                                    {
                                        Toast.makeText(context, "Please install a maps application", Toast.LENGTH_LONG).show();
                                    }
                                }


                            /*}else {
                                showsnackmessage("Your location is disabled,enabled it see location");
                            }*/

                        }else {
                            showsnackmessage("not available righgt now");
                        }
                    }else{
                        showsnackmessage("Location Not Available");
                    }

                    /*if(!StrLat.equalsIgnoreCase("null")&&!StrLong.equalsIgnoreCase("null")){
                        if(Double.parseDouble(StrLat)!=0.0&&Double.parseDouble(StrLong)!=0.0){
                            if(gpsTracker.getLatitude()!=0.0&&gpsTracker.getLongitude()!=0.0){
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?saddr="+gpsTracker.getLatitude()+","+gpsTracker.getLongitude()+"&daddr="+StrLat+","+StrLong));

                                if (intent.resolveActivity(context.getPackageManager()) != null) {
                                    context.startActivity(intent);
                                }
                            }else {
                                showsnackmessage("Your location is disabled,enabled it see location");
                            }

                        }else {
                            showsnackmessage("not available righgt now");
                        }

                    }else {
                        showsnackmessage("not available righgt now");
                    }*/

                }
            });

            relativeLayoutSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(ItemList.get(i).isVendorSaved()){

                     //   Toast.makeText(context, "Vendor already saved", Toast.LENGTH_SHORT).show();
                    }else {
                        MainDashBoard.VendorID=String.valueOf(ItemList.get(i).getStrID());

                        MainDashBoardFragment.listener.onDashBoardCallBack(2);
                    }
                }
            });

            }




        return view;
    }


   private void showsnackmessage(String msg){

        Snackbar snackbar = Snackbar
                .make(layoutHelper, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }
}
