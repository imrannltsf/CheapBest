package com.cheapestbest.androidapp.appadapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.VolleyService;
import com.google.android.material.snackbar.Snackbar;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.MainDashBoardHelper;
import com.cheapestbest.androidapp.apputills.MyImageLoader;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.ui.Activity.MainDashBoard;
import com.cheapestbest.androidapp.ui.Fragments.MainDashBoardFragment;
import com.cheapestbest.androidapp.ui.Fragments.SubBrandFragment;
import com.cheapestbest.androidapp.apputills.GPSTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import androidx.core.content.ContextCompat;

public class DashBoardAdapter extends BaseAdapter {
    private IResult mResultCallback;
    private Progressbar progressbar;
    private LinearLayout layoutHelper;
    public static List<MainDashBoardHelper>ItemList;
    private GPSTracker gpsTracker;
    private Context context;
    private DialogHelper dialogHelper;
    private MyImageLoader myImageLoader;
    public DashBoardAdapter(List<MainDashBoardHelper> itemList, Context context) {
        ItemList = itemList;

        this.context = context;
        gpsTracker=new GPSTracker(this.context);
        myImageLoader=new MyImageLoader( this.context);
        dialogHelper=new DialogHelper(this.context);
        progressbar =new Progressbar( this.context);
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
        TextView tvName,tvPriceUnit,tvOffers,tvdis;
        /*,tvHintVendor*/
        ImageView imageViewLogo,imageViewSaveVendor;

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
          //  tvHintVendor=view.findViewById(R.id.hint_add_vendor);
            imageViewSaveVendor=view.findViewById(R.id.img_add_vendor);
            imageViewLogo=view.findViewById(R.id.p_logo);
            relativeLayoutMove=view.findViewById(R.id.layout_values);
            relativeLayoutLocation=view.findViewById(R.id.layout_location);
            tvName.setText(ItemList.get(i).getStrName());
            tvPriceUnit.setText(ItemList.get(i).getStrNearBranchAddress());
            tvOffers.setText(String.valueOf("Total Offers:"+ItemList.get(i).getStrCoupans_count()));

            if(ItemList.get(i).isVendorSaved()){

                imageViewSaveVendor.setBackgroundResource(R.drawable.es_save);
              //  Toast.makeText(context, "Image Applied", Toast.LENGTH_SHORT).show();

            }else {
                imageViewSaveVendor.setBackgroundResource(R.drawable.now_sav);
            }
             if(haveNetworkConnection()){
                if(locationServicesEnabled(context)){
                        Log.e("","");
                }else {
                    showsnackmessage("GPS disabled");
                }
                if(!isEmptyString(ItemList.get(i).getStrDistance())){
                    if(!ItemList.get(i).getStrDistance().equals("-1")){
                        double dis=Double.parseDouble(ItemList.get(i).getStrDistance());
                        if(dis<1.0){
                            tvdis.setText(" ");
                        }else {
                            tvdis.setText(String.valueOf(ItemList.get(i).getStrDistance()+" Miles"));
                        }

                    }else {
                        tvdis.setText(" ");
                    }

                }else {
                    tvdis.setText(" ");
                }
            }else {
                showsnackmessage("Internet Connection disabled");
            }



            String img_url = ItemList.get(i).getStrLogo();

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
                    MainDashBoardFragment.listener.onDashBoardCallBack(1);
                }
            });

            relativeLayoutLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    int hasPermission = ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION);
                    if (hasPermission == PackageManager.PERMISSION_GRANTED) {

                      //  isLocationPermssionAllowed=true;
                    }else {
                      //  isLocationPermssionAllowed=false;
                      //  showsnackmessage("Location Permission is not granted");

                    }
                    String StrLat=ItemList.get(i).getStrLatitude();
                    String StrLong=ItemList.get(i).getStrLongitude();


                    if(!isEmptyString(StrLat)&&!isEmptyString(StrLong)){
                        if(Double.parseDouble(StrLat)!=0.0&&Double.parseDouble(StrLong)!=0.0){


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


                        }else {
                            showsnackmessage("Not Available Righgt Now");
                        }
                    }else{
                        showsnackmessage("Location Not Available");
                    }
                }
            });

            relativeLayoutSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(ItemList.get(i).isVendorSaved()){

                    }else {
                        MainDashBoard.VendorID=String.valueOf(ItemList.get(i).getStrID());
                        MainDashBoard.SavedPosition=i;

                        SaveWholeVendor();
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


    ///////////////////

   private void SaveWholeVendor()
    {

        showprogress();
        initVolleyCallbackForSaveVendor();
        VolleyService mVolleyService = new VolleyService(mResultCallback,context);
        mVolleyService.postDataVolleyWithHeaderWithoutParam("POSTCALL",NetworkURLs.SaveWholeVendor_PartA+MainDashBoard.VendorID+NetworkURLs.SaveWholeVendor_PartB);
    }

    private void initVolleyCallbackForSaveVendor(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                hideprogress();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {


                            JSONObject signUpResponseModel = jsonObject.getJSONObject("data");
                            String SuccessMessage = signUpResponseModel.getString("message");
                            showsnackmessage(SuccessMessage);
                            ItemList.get(MainDashBoard.SavedPosition).setVendorSaved(true);
                            notifyDataSetChanged();
                        }else {

                            JSONObject signUpResponseModels = jsonObject.getJSONObject("error");
                            String Message = signUpResponseModels.getString("message");
                            dialogHelper.showErroDialog(Message);

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                            dialogHelper.showErroDialog(String.valueOf(message));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    dialogHelper.showErroDialog("Something went wrong please try again");
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

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
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
