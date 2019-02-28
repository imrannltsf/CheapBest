package com.cheapestbest.androidapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.SubBrandHelper;
import com.cheapestbest.androidapp.appadapters.SubBrandAdapter;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.MyImageLoader;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class SubBrandFragment extends Fragment {

    public static OnItemSelectedListener listener;
    public static SubBrandFragment newInstance() {
        return new SubBrandFragment();
    }
    private RelativeLayout relativeLayoutEmpty;
    private List<SubBrandHelper>Mlist=new ArrayList<>();
    public static String BrandLogoUrl;
    public static String CoverUrl;
    public static String VendorNmae;
    @SuppressLint("StaticFieldLeak")
    public static String StrVendorID;
    private IResult mResultCallback;
    private ListView lvProducts;
    private MyImageLoader myImageLoader;
    private Progressbar progressbar;
    private DialogHelper dialogHelper;
    private TextView textViewName;
    private ImageView imageViewVendor;
    int pagenationCurrentcount=1;
    int TotalPaginationCount=0;
    boolean isloadeddata=false;
    SubBrandAdapter subBrandAdapter;
    int AllTotoalCoupon=0;
    int listindex=0;
    boolean isfirsttime=false;
    public  boolean isfromScrolled=false;
    public static int SelectedIndex=0;
   // private CircleImageView circleImageView;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sub_brand, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewName=view.findViewById(R.id.id_vendor_name);
        imageViewVendor=view.findViewById(R.id.img_header_logo);
        progressbar =new Progressbar(getActivity());
        myImageLoader=new MyImageLoader(getActivity());
        dialogHelper=new DialogHelper(getActivity());
        lvProducts= view.findViewById(R.id.lv_products_sub_brands);
        ImageView imgCover = view.findViewById(R.id.cover_photo_brand);
        relativeLayoutEmpty=view.findViewById(R.id.layout_empty);
        relativeLayoutEmpty.setVisibility(View.GONE);
        myImageLoader.loadImage(NetworkURLs.BaseURLImages+SubBrandFragment.CoverUrl, imgCover);
        myImageLoader.loadImage(NetworkURLs.BaseURLImages+SubBrandFragment.BrandLogoUrl, imageViewVendor);
        setMarginToListView(lvProducts);
        textViewName.setText(VendorNmae);

        GetCoupanData();

        lvProducts.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


            }

            public void onScrollStateChanged(AbsListView listView, int scrollState) {
                if(isloadeddata){
                    if (lvProducts.getLastVisiblePosition() == subBrandAdapter.getCount()) {
                        isloadeddata=false;
                        listindex=lvProducts.getLastVisiblePosition();
                        if(pagenationCurrentcount<TotalPaginationCount){
                            isfromScrolled=true;
                            isloadeddata=true;
                            pagenationCurrentcount++;
                            GetCoupanData();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            listener = (OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement SubBrandFragment.OnItemSelectedListener");
        }
    }

    public interface OnItemSelectedListener {

        void onSubBrandFragCallBack(int position);
    }

    /* volley*/

    public void GetCoupanData()
    {
      //  Toast.makeText(getActivity(), String.valueOf(StrVendorID), Toast.LENGTH_SHORT).show();
        showprogress();
        initVolleyCallbackForCoupons();
        VolleyService mVolleyService = new VolleyService(mResultCallback, getActivity());
       /* mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.GetCouponsListUsingVendorsID+StrVendorID+".json");*/
        mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.GetCouponsListUsingVendorsID+StrVendorID+".json"+"?page="+pagenationCurrentcount);
    }

    private void initVolleyCallbackForCoupons(){

        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                hideprogress();

                if(!isfromScrolled){
                    if(Mlist.size()>0){
                        Mlist.clear();
                    }
                }


                if (response != null) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                            JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                            JSONObject VendorObj = DataRecivedObj.getJSONObject("vendor");
                            JSONArray Couponsarray = VendorObj.getJSONArray("coupons");
                            TotalPaginationCount=DataRecivedObj.getInt("page_count");
                            AllTotoalCoupon=DataRecivedObj.getInt("total_count");
                            if(Couponsarray.length()<1){
                                relativeLayoutEmpty.setVisibility(View.VISIBLE);
                            }else {
                                for (int j = 0; j < Couponsarray.length(); j++) {

                                    JSONObject coupans;
                                    try {
                                        coupans = Couponsarray.getJSONObject(j);
                                        Mlist.add(new SubBrandHelper(coupans));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    subBrandAdapter=new SubBrandAdapter(Mlist,getActivity());
                                   /* lvProducts.setAdapter(new SubBrandAdapter(Mlist,getActivity()));*/
                                    lvProducts.setAdapter(subBrandAdapter);
                                    int currentPosition = lvProducts.getFirstVisiblePosition();

                                    lvProducts.setSelectionFromTop(currentPosition + 1, 0);
                                   // lvProducts.setSelection(listindex-6);
                                    if(!isfirsttime){
                                        isfirsttime=true;
                                        lvProducts.setAdapter(subBrandAdapter);
                                        lvProducts.setSelection(listindex-6);


                                        /*isloadeddata=true;*/
                                    }else {
                                        lvProducts.setSelection(listindex-6);

                                        subBrandAdapter.notifyDataSetChanged();
                                    }

                                    isloadeddata=true;
                                //    Toast.makeText(getActivity(), String.valueOf(TotalPaginationCount), Toast.LENGTH_SHORT).show();
                                }
                            }
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
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
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

    public void setMarginToListView(ListView lv){
        TextView empty = new TextView(getActivity());
        empty.setHeight(90);
        lv.addFooterView(empty);
    }
}
