package com.cheapestbest.androidapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    @SuppressLint("StaticFieldLeak")
    public static String StrVendorID;
    private IResult mResultCallback;
    private ListView lvProducts;
    private MyImageLoader myImageLoader;
    private Progressbar progressbar;
    private DialogHelper dialogHelper;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sub_brand, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressbar =new Progressbar(getActivity());
        myImageLoader=new MyImageLoader(getActivity());
        dialogHelper=new DialogHelper(getActivity());
        lvProducts= view.findViewById(R.id.lv_products_sub_brands);
        ImageView imgCover = view.findViewById(R.id.cover_photo_brand);
        relativeLayoutEmpty=view.findViewById(R.id.layout_empty);
        relativeLayoutEmpty.setVisibility(View.GONE);
        myImageLoader.loadImage(NetworkURLs.BaseURLImages+SubBrandFragment.CoverUrl, imgCover);
        setMarginToListView(lvProducts);

        Toast.makeText(getActivity(), String.valueOf(StrVendorID), Toast.LENGTH_SHORT).show();
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
        showprogress();
        initVolleyCallbackForCoupons();
        VolleyService mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.GetCouponsListUsingVendorsID+StrVendorID+".json");
    }

    private void initVolleyCallbackForCoupons(){

        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                hideprogress();
                if(Mlist.size()>0){
                    Mlist.clear();
                }

                if (response != null) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                            JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                            JSONObject VendorObj = DataRecivedObj.getJSONObject("vendor");
                            JSONArray Couponsarray = VendorObj.getJSONArray("coupons");

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
                                    lvProducts.setAdapter(new SubBrandAdapter(Mlist,getActivity()));
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

                    dialogHelper.showErroDialog(error_response);
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
        empty.setHeight(100);
        lv.addFooterView(empty);
    }
}
