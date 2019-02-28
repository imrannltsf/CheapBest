package com.cheapestbest.androidapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.MainDashBoardHelper;
import com.cheapestbest.androidapp.appadapters.DashBoardAdapter;
import com.cheapestbest.androidapp.appadapters.DashBoardAdapterRecycler;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.apputills.SharedPref;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import com.cheapestbest.androidapp.ui.Activity.MainDashBoard;
import com.cheapestbest.androidapp.apputills.GPSTracker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainDashBoardFragment extends Fragment{
    RecyclerView recyclerView;
    private int previousDistanceFromFirstCellToTop;
    public  int MainDashBoardSaveIndex;
    public static MainDashBoardFragment newInstance() {
        return new MainDashBoardFragment();
    }
    public static OnItemSelectedListener listener;
    //private ListView LvProducts;
    private IResult mResultCallback;
    public  Map<String, String> LocationUser;
    private GPSTracker gpsTracker;
    private String StrLat,StrLong;
    private Progressbar progressbar;
    private RelativeLayout relativeLayoutEmpty;
    private DialogHelper dialogHelper;
    private VolleyService mVolleyService;
    public static DashBoardAdapter dashBoardAdapter;
    public  boolean isfromScrolled=false;
    private int pagenationCurrentcount=1;
    private int TotalPaginationCount=0;
    private int AllTotoalCoupon=0;
    private  boolean isloadeddata=false;
    private  int listindex=0;
    boolean isfirsttime=false;

    private DashBoardAdapterRecycler mAdapter;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_dash_board, container, false);
    }

    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPref.init(getActivity());
        progressbar =new Progressbar(getActivity());
        dialogHelper=new DialogHelper(getActivity());
        gpsTracker=new GPSTracker(getActivity());



        relativeLayoutEmpty=view.findViewById(R.id.layout_empty);
        relativeLayoutEmpty.setVisibility(View.GONE);
        StrLat=String.valueOf(gpsTracker.getLatitude());
        StrLong=String.valueOf(gpsTracker.getLongitude());

        LocationUser=new HashMap< >();
        LocationUser.put("lat",StrLat);
        LocationUser.put("long", StrLong);
     //   statusCheck();

        /*LvProducts=view.findViewById(R.id.lv_products_main_dash);
        setMarginToListView(LvProducts);
        LvProducts.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }

            public void onScrollStateChanged(AbsListView listView, int scrollState) {
                if(isloadeddata){
                    if (LvProducts.getLastVisiblePosition() == dashBoardAdapter.getCount()) {
                        isloadeddata=false;
                        listindex=LvProducts.getLastVisiblePosition();
                        if(pagenationCurrentcount<TotalPaginationCount){
                            isfromScrolled=true;
                            isloadeddata=true;
                            pagenationCurrentcount++;

                            GetMainDashBoardData();
                        }
                    }
                }
            }
        });*/
        /*recyclerView*/
        recyclerView = view.findViewById(R.id.recycler_view_vendor);
        mAdapter = new DashBoardAdapterRecycler(MainDashBoard.DashBoardList,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutParams params = new    RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,0);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //prepareLocationData();

        GetMainDashBoardData();

        RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
              /*  if (isloadeddata)
                    return;*/
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    //End of list
                    listindex=pastVisibleItems;

                    if(isloadeddata){

                            isloadeddata=false;
                           // listindex=LvProducts.getLastVisiblePosition();
                            if(pagenationCurrentcount<TotalPaginationCount){
                                isfromScrolled=true;
                               /* isloadeddata=true;*/
                                pagenationCurrentcount++;

                                GetMainDashBoardData();
                            }

                    }

                }else {

                }
            }
        };
        recyclerView.addOnScrollListener(mScrollListener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            listener = (OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MainDashBoardFragment.OnItemSelectedListener");
        }
    }

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {

        void onDashBoardCallBack(int position);
    }

   /* volley*/

   public void GetMainDashBoardData()
    {
        if(!isfromScrolled){
            showprogress();
            if(MainDashBoard.DashBoardList.size()>0){
                MainDashBoard.DashBoardList.clear();
            }
        }

        initVolleyCallbackForMainDashBoard();

         mVolleyService = new VolleyService(mResultCallback, getActivity());
     //   mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.BaseURL+NetworkURLs.MainDashBoardURL);
        if(isEmptyString(StrLat)||isEmptyString(StrLong)){

            mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.BaseURL+NetworkURLs.MainDashBoardURL+"?page="+pagenationCurrentcount);
        }else {
            mVolleyService.getDataVolleyWithoutParams("GETCALL",NetworkURLs.BaseURL+NetworkURLs.MainDashBoardURL+"?page=" + pagenationCurrentcount+"&lat="+StrLat+ "&long=" + StrLong);
        }


    }

    private void initVolleyCallbackForMainDashBoard(){
        mResultCallback = new IResult() {
            @SuppressLint("NewApi")
            @Override
            public void notifySuccess(String requestType,String response) {
                if(!isfromScrolled){
                    hideprogress();
                }

             /* if(!isfromScrolled){
                  if(MainDashBoard.DashBoardList.size()>0){
                      MainDashBoard.DashBoardList.clear();
                  }
              }*/

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                            JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                            JSONArray Vendorsarray = DataRecivedObj.getJSONArray("vendors");
                            TotalPaginationCount=DataRecivedObj.getInt("page_count");
                            AllTotoalCoupon=DataRecivedObj.getInt("total_count");
                            if(Vendorsarray.length()<1){
                                relativeLayoutEmpty.setVisibility(View.VISIBLE);
                            }else {

                                for (int i = 0; i < Vendorsarray.length(); i++) {
                                    JSONObject c = Vendorsarray.getJSONObject(i);
                                    MainDashBoard.DashBoardList.add(new MainDashBoardHelper(c));

                                }
                            }
                            /*if(!isfirsttime){
                                isfirsttime=true;
                                dashBoardAdapter=new DashBoardAdapter(MainDashBoard.DashBoardList,getActivity());
                                LvProducts.setAdapter(new DashBoardAdapter(MainDashBoard.DashBoardList,getActivity()));


                            }else {

                                int currentPosition = LvProducts.getFirstVisiblePosition();

                                LvProducts.setSelectionFromTop(currentPosition + 1, 0);
                                dashBoardAdapter.notifyDataSetChanged();
                            }*/

                            isloadeddata=true;
                            mAdapter.notifyDataSetChanged();
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
                   //  dialogHelper.showErroDialog(error_response);

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
        empty.setHeight(120);
        empty.setClickable(false);
        lv.addFooterView(empty);
    }
    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.titile_gps));
        builder.setMessage(getResources().getString(R.string.no_gps_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok_no_gps), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                       startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no_no_gps), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        //showLocationMessage();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void showLocationMessage(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.purpose_of_getting_location))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok_no_gps), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no_no_gps), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
