package com.cheapestbest.androidapp.ui.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.MainDashBoardHelper;
import com.cheapestbest.androidapp.appadapters.SearchAdapter;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.GPSTracker;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import com.cheapestbest.androidapp.ui.Activity.MainDashBoard;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchDetailFragment extends Fragment{
    public static OnItemSelectedListener listener;
    public static List<MainDashBoardHelper> SearchDetailList=new ArrayList<>();
    private RelativeLayout relativeLayoutEmpty;
    public static SearchDetailFragment newInstance() {
        return new SearchDetailFragment();
    }
    private int AllTotoalCoupon=0;
    private boolean isloadeddata=false;
    private int listindex=0;
    public SearchAdapter dashBoardAdapter;
    public  boolean isfromScrolled=false;
    private DialogHelper dialogHelper;
    private IResult mResultCallback;
    private ListView lvProducts;
    private Progressbar progressbar;
    boolean isfirsttime=false;
    private GPSTracker gpsTracker;
    private String StrLat,StrLong;
    public static int savedid=0;
    public  List<NameValuePair> params = new ArrayList<>();
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_detail, container, false);
    }

    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gpsTracker=new GPSTracker(getActivity());
        dialogHelper=new DialogHelper(getActivity());
        lvProducts= view.findViewById(R.id.lv_search_detail);
        setMarginToListView(lvProducts);
        StrLat=String.valueOf(gpsTracker.getLatitude());
        StrLong=String.valueOf(gpsTracker.getLongitude());
        MainDashBoard.FragmentName="SearchDetailFragment";
        progressbar =new Progressbar(getActivity());
        relativeLayoutEmpty=view.findViewById(R.id.layout_empty);
        relativeLayoutEmpty.setVisibility(View.GONE);
        if(SearchDetailList.size()<1){
            relativeLayoutEmpty.setVisibility(View.VISIBLE);
        }else {
            dashBoardAdapter=new SearchAdapter(SearchDetailList,getActivity());
            lvProducts.setAdapter(dashBoardAdapter);
        }

        lvProducts.setOnItemClickListener((adapterView, view1, i, l) -> {


            SubBrandFragment.CoverUrl=SearchDetailList.get(i).getStrCoverPhoto();
            SubBrandFragment.BrandLogoUrl=SearchDetailList.get(i).getStrLogo();
        });

        lvProducts.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
            public void onScrollStateChanged(AbsListView listView, int scrollState) {
                if(isloadeddata){
                    if (lvProducts.getLastVisiblePosition() == dashBoardAdapter.getCount()) {
                        isloadeddata=false;
                        listindex=lvProducts.getLastVisiblePosition();
                        if(MainDashBoard.pagenationCurrentcount<MainDashBoard.TotalPaginationCount){
                            isfromScrolled=true;
                            isloadeddata=true;
                            MainDashBoard.pagenationCurrentcount++;

                            params.add(new BasicNameValuePair("city", MainDashBoard.SelectedLocation));
                            params.add(new BasicNameValuePair("category", MainDashBoard.SelcedCategory));
                            params.add(new BasicNameValuePair("query", MainDashBoard.SelectedQuery));

                            GetSearch(MainDashBoard.QueryString);
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
                    + " must implement SearchDetailFragment.OnItemSelectedListener");
        }
    }
    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        void onSearchDeatilCallBack(int position);
    }
    public void setMarginToListView(ListView lv){
        TextView empty = new TextView(getActivity());
        empty.setHeight(160);
        empty.setClickable(false);
        lv.addFooterView(empty);
    }
    ////////////////////////////////////

    private void GetSearch(String StrQuery)
    {

        showprogress();
        if(!isfromScrolled){
            if(SearchDetailFragment.SearchDetailList.size()>0){
                SearchDetailFragment.SearchDetailList.clear();
            }
        }

        initVolleyCallbackForSearch();
        VolleyService mVolleyService = new VolleyService(mResultCallback, getActivity());

        if(doesUserHavePermission()){
            if(isEmptyString(String.valueOf(gpsTracker.getLatitude()))||isEmptyString(String.valueOf(gpsTracker.getLongitude()))){
                params.add(new BasicNameValuePair("page", String.valueOf(MainDashBoard.pagenationCurrentcount)));
                String strurl=NetworkURLs.BaseURL+NetworkURLs.SearchByManualUrl;
                if(!strurl.endsWith("?"))
                    strurl += "?";
                String query = URLEncodedUtils.format(params, "utf-8");
                String Str=strurl+query;
                mVolleyService.getDataVolleyWithoutparam("GETCALL",Str);
            }else {

                params.add(new BasicNameValuePair("page", String.valueOf(MainDashBoard.pagenationCurrentcount)));
                params.add(new BasicNameValuePair("lat",String.valueOf(gpsTracker.getLatitude())));
                params.add(new BasicNameValuePair("long",String.valueOf(gpsTracker.getLongitude())));

                String strurl=NetworkURLs.BaseURL+NetworkURLs.SearchByManualUrl;
                if(!strurl.endsWith("?"))
                    strurl += "?";
                String query = URLEncodedUtils.format(params, "utf-8");
                String Str=strurl+query;
                mVolleyService.getDataVolleyWithoutparam("GETCALL",Str);



            }
        }else {
            params.add(new BasicNameValuePair("page", String.valueOf(MainDashBoard.pagenationCurrentcount)));
            String strurl=NetworkURLs.BaseURL+NetworkURLs.SearchByManualUrl;
            if(!strurl.endsWith("?"))
                strurl += "?";
            String query = URLEncodedUtils.format(params, "utf-8");
            String Str=strurl+query;
            mVolleyService.getDataVolleyWithoutparam("GETCALL",Str);
        }



      }

    private void initVolleyCallbackForSearch(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                hideprogress();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                            JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                            JSONArray Vendorsarray = DataRecivedObj.getJSONArray("vendors");
                            MainDashBoard.TotalPaginationCount=DataRecivedObj.getInt("page_count");
                            AllTotoalCoupon=DataRecivedObj.getInt("total_count");
                            for (int i = 0; i < Vendorsarray.length(); i++) {
                                JSONObject c = Vendorsarray.getJSONObject(i);
                                SearchDetailFragment.SearchDetailList.add(new MainDashBoardHelper(c));

                            }

                            isloadeddata=true;
                            if(isfromScrolled){
                                dashBoardAdapter.notifyDataSetChanged();
                            }else {
                                dashBoardAdapter=new SearchAdapter(SearchDetailList,getActivity());
                                lvProducts.setAdapter(dashBoardAdapter);
                            }

                            params.clear();
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

    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }

    private boolean doesUserHavePermission()
    {
        int result = getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
