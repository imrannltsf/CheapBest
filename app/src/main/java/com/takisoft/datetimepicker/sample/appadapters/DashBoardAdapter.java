package com.takisoft.datetimepicker.sample.appadapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.adpterUtills.MainDashBoardHelper;
import com.takisoft.datetimepicker.sample.apputilss.MyImageLoader;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.ui.Activity.MainDashBoard;
import com.takisoft.datetimepicker.sample.ui.Fragments.MainDashBoardFragment;
import com.takisoft.datetimepicker.sample.ui.Fragments.SubBrandFragment;
import com.takisoft.datetimepicker.sample.ui.utills.GPSTracker;

import java.text.DecimalFormat;
import java.util.List;

public class DashBoardAdapter extends BaseAdapter {
 //  private List<MainDashBoradResponse>ItemList;
 private List<MainDashBoardHelper>ItemList;
    GPSTracker gpsTracker;
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
        TextView tvName = null,tvPriceUnit,tvOffers,tvdis;
        ImageView imageViewLogo;
        RelativeLayout relativeLayoutMove;
        RelativeLayout relativeLayoutLocation;
        if (view != null) {
            tvName=view.findViewById(R.id.tv_p_name);
            tvPriceUnit=view.findViewById(R.id.tv_p_price);
            tvOffers=view.findViewById(R.id.tv_p_offers);
            tvdis=view.findViewById(R.id.tv_distance_loc);
            imageViewLogo=view.findViewById(R.id.p_logo);
            relativeLayoutMove=view.findViewById(R.id.layout_values);
            relativeLayoutLocation=view.findViewById(R.id.layout_location);
            tvName.setText(ItemList.get(i).getStrName());
            tvPriceUnit.setText(ItemList.get(i).getStrNearBranchAddress());
            tvOffers.setText(String.valueOf("Total Offers:"+ItemList.get(i).getStrCoupans_count()));
          //  tvbackPrice.setText(ItemList.get(i).getStrLogo());
            //String img_url = ItemList.get(i).getStrLogo();

            if(!ItemList.get(i).getStrLatitude().equalsIgnoreCase("null")&&
                    !ItemList.get(i).getStrLongitude().equalsIgnoreCase("null")&&
                    gpsTracker.getLongitude()!=0.0&&gpsTracker.getLatitude()!=0.0){


            Location locationA = new Location("point A");

            locationA.setLatitude(gpsTracker.getLatitude());
            locationA.setLongitude(gpsTracker.getLongitude());

            Location locationB = new Location("point B");

            locationB.setLatitude(Double.parseDouble(ItemList.get(i).getStrLatitude()));

            locationB.setLongitude(Double.parseDouble(ItemList.get(i).getStrLongitude()));

            double distance = (locationA.distanceTo(locationB)/1000)*0.621371;
                tvdis.setText(String.valueOf(new DecimalFormat("##.##").format(distance)+":Miles"));
           // tvdis.setText(String.valueOf(distance)+" : miles");
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

                    MainDashBoardFragment.listener.onDashBoardCallBack(1);
                }
            });

            relativeLayoutLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String StrLat=ItemList.get(i).getStrLatitude();
                    String StrLong=ItemList.get(i).getStrLongitude();

                   // Uri.parse("http://maps.google.com/maps?saddr="+gpsTracker.getLatitude()+","+gpsTracker.getLongitude()+"&daddr="+StrLat+","+StrLong);


                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr="+gpsTracker.getLatitude()+","+gpsTracker.getLongitude()+"&daddr="+StrLat+","+StrLong));

                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                    Toast.makeText(context, StrLat+","+StrLong, Toast.LENGTH_SHORT).show();

                }
            });
        }
       /* if (tvName != null) {
            tvName.setOnClickListener(view1 -> {
                SubBrandFragment.StrVendorID=ItemList.get(i).getStrID();
                 //SubBrandFragment.SelectedJsonArray =ItemList.get(i).getJsonArray();
                SubBrandFragment.CoverUrl=ItemList.get(i).getStrCoverPhoto();
                SubBrandFragment.BrandLogoUrl=ItemList.get(i).getStrCoverPhoto();
                Toast.makeText(context, String.valueOf(SubBrandFragment.SelectedJsonArray), Toast.LENGTH_SHORT).show();
            });
        }
*/

        return view;
    }


    public float distance (double lat_a, double lng_a, double lat_b, double lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }
}
