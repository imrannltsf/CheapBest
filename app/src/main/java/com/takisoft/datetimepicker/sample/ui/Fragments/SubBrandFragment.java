package com.takisoft.datetimepicker.sample.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.adpterUtills.SubBrandHelper;
import com.takisoft.datetimepicker.sample.appadapters.SubBrandAdapter;
import com.takisoft.datetimepicker.sample.apputilss.MyImageLoader;
import com.takisoft.datetimepicker.sample.apputilss.SharedPref;
import com.takisoft.datetimepicker.sample.network.IResult;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.network.VolleyService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import spinkit.style.ChasingDots;


public class SubBrandFragment extends Fragment {

    private List<SubBrandHelper>Mlist=new ArrayList<>();

    public static SubBrandFragment newInstance() {
        return new SubBrandFragment();
    }
    //public static JSONArray SelectedJsonArray;
    public static String BrandLogoUrl;
    public static String CoverUrl;
    @SuppressLint("StaticFieldLeak")
    public static ImageView imageViewLoading;
    public static ChasingDots mChasingDotsDrawable;
    public static String StrVendorID;
    private IResult mResultCallback;
    private ListView lvProducts;
    public static OnItemSelectedListener listener;
    MyImageLoader myImageLoader;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sub_brand, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       Toast.makeText(getActivity(), "ID of Vendor:"+StrVendorID, Toast.LENGTH_SHORT).show();
        myImageLoader=new MyImageLoader(getActivity());
        imageViewLoading =  view.findViewById(R.id.image_loading_sub_frag);
        mChasingDotsDrawable = new ChasingDots();
        mChasingDotsDrawable.setColor(getResources().getColor(R.color.color_custom));
        imageViewLoading.setImageDrawable(mChasingDotsDrawable);
        imageViewLoading.setVisibility(View.GONE);

        lvProducts= view.findViewById(R.id.lv_products_sub_brands);
        ImageView imgCover = view.findViewById(R.id.cover_photo_brand);
        ImageView imgBrandLogo = view.findViewById(R.id.brand_logo);
        myImageLoader.loadImage(NetworkURLs.BaseURLImages+SubBrandFragment.BrandLogoUrl, imgBrandLogo);
        myImageLoader.loadImage(NetworkURLs.BaseURLImages+SubBrandFragment.CoverUrl, imgCover);

       /* lvProducts.setOnItemClickListener((adapterView, view1, i, l) -> {

        });*/

      //  Toast.makeText(getActivity(), String.valueOf(MainDashBoard.responseUid), Toast.LENGTH_SHORT).show();
        GetCoupanData();
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

    private void GetCoupanData()
    {
        Toast.makeText(getActivity(), "Call Make Method", Toast.LENGTH_SHORT).show();
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);

       /* myImageLoader.showErroDialog("Uid:"+SharedPref.read(SharedPref.UID,"Empty Uid"));
        myImageLoader.showErroDialog("Acess Token:"+SharedPref.read(SharedPref.Access_Token,"Empty Access Token"));
        myImageLoader.showErroDialog("Client:"+SharedPref.read(SharedPref.Client,"Empty Client"));*/
        initVolleyCallbackForCoupons();

        VolleyService mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.GetCouponsListUsingVendorsID+StrVendorID+".json");
    }

    private void initVolleyCallbackForCoupons(){
       // Toast.makeText(getActivity(), "Call Make Method b", Toast.LENGTH_SHORT).show();
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
            //    Toast.makeText(getActivity(), "Success Response", Toast.LENGTH_SHORT).show();
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);
                if(Mlist.size()>0){
                    Mlist.clear();
                }

                if (response != null) {
                  //  Toast.makeText(getActivity(), "Success Response b", Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                            Toast.makeText(getActivity(), "Data Found found true", Toast.LENGTH_SHORT).show();

                            JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                            JSONObject VendorObj = DataRecivedObj.getJSONObject("vendor");
                            JSONArray Couponsarray = VendorObj.getJSONArray("coupons");
                          //  myImageLoader.showDialog(String.valueOf(Couponsarray));
                            for (int j = 0; j < Couponsarray.length(); j++) {

                                    JSONObject coupans;
                                    try {
                                        coupans = Couponsarray.getJSONObject(j);
                                      Mlist.add(new SubBrandHelper(coupans));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    lvProducts.setAdapter(new SubBrandAdapter(Mlist,getActivity()));
                                }
                        }else {
                            Toast.makeText(getActivity(), "Data Not found true", Toast.LENGTH_SHORT).show();

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getActivity(), "Null response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                mChasingDotsDrawable.stop();
                //Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                imageViewLoading.setVisibility(View.GONE);
                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    Toast.makeText(getActivity(), String.valueOf(error_response), Toast.LENGTH_SHORT).show();
                    myImageLoader.showErroDialog(error_response);
                    Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.UID,"Empty UID")), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.Access_Token,"Empty Access Token")), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.Client,"Empty Client")), Toast.LENGTH_SHORT).show();

                    Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.User_ID,"Empty User ID")), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.UserEmail,"Empty Email")), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.FBLogin,"Empty Fb Login")), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.UserPassword,"Empty User Password")), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.IsLoginUser,"Empty Login User")), Toast.LENGTH_SHORT).show();

                    try {
                        JSONObject response_obj=new JSONObject(error_response);
                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            myImageLoader.showErroDialog(String.valueOf(error_obj));
                            String message=error_obj.getString("message");

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            myImageLoader.showErroDialog(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(getActivity(), "network response issue", Toast.LENGTH_SHORT).show();
                }
            }

        };
    }
}
