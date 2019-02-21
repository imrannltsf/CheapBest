package com.cheapestbest.androidapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.MainDashBoardHelper;
import com.cheapestbest.androidapp.appadapters.DashBoardAdapter;
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


public class MainDashBoardFragment extends Fragment{
    private int previousDistanceFromFirstCellToTop;
    public  int MainDashBoardSaveIndex;
    public static MainDashBoardFragment newInstance() {
        return new MainDashBoardFragment();
    }
    public static OnItemSelectedListener listener;
    private ListView LvProducts;
    private IResult mResultCallback;
    public  Map<String, String> LocationUser;
    private GPSTracker gpsTracker;
    private String StrLat,StrLong;
    private Progressbar progressbar;
    private RelativeLayout relativeLayoutEmpty;
    private DialogHelper dialogHelper;
    private VolleyService mVolleyService;
   public DashBoardAdapter dashBoardAdapter;
   public  boolean isfromScrolled=false;
    private int pagenationCurrentcount=1;
    private int TotalPaginationCount=0;
    private int AllTotoalCoupon=0;
    private  boolean isloadeddata=false;
    private  int listindex=0;
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
       LvProducts=view.findViewById(R.id.lv_products_main_dash);

        setMarginToListView(LvProducts);
        relativeLayoutEmpty=view.findViewById(R.id.layout_empty);
        relativeLayoutEmpty.setVisibility(View.GONE);
        StrLat=String.valueOf(gpsTracker.getLatitude());
        StrLong=String.valueOf(gpsTracker.getLongitude());

        LocationUser=new HashMap< >();
        LocationUser.put("lat",StrLat);
        LocationUser.put("long", StrLong);
        GetMainDashBoardData();


        /*LvProducts.setOnTouchListener(new View.OnTouchListener() {
            float height;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                float height = event.getY();
                if(action == MotionEvent.ACTION_DOWN){
                    this.height = height;
                }else if(action == MotionEvent.ACTION_UP){
                    if(this.height < height){

                       // Toast.makeText(getActivity(), "Scrolled up", Toast.LENGTH_SHORT).show();

                    }else if(this.height > height){


                        if (LvProducts.getLastVisiblePosition() == dashBoardAdapter.getCount()) {
                            listindex=LvProducts.getLastVisiblePosition();
                            if(pagenationCurrentcount<TotalPaginationCount){
                                isfromScrolled=true;
                                isloadeddata=true;
                                pagenationCurrentcount++;
                                // pagenationCurrentcount=pagenationCurrentcount+1;
                                //  Toast.makeText(getActivity(), "after incremented"+pagenationCurrentcount, Toast.LENGTH_SHORT).show();
                                GetMainDashBoardData();
                            }else {
                                Toast.makeText(getActivity(), String.valueOf(AllTotoalCoupon), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), String.valueOf(dashBoardAdapter.getCount()), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), "current count is exced the limit", Toast.LENGTH_SHORT).show();
                            }


                        }else {
                            //   Toast.makeText(getActivity(), String.valueOf("Listview last position:"+lvProducts.getLastVisiblePosition()), Toast.LENGTH_SHORT).show();

                        }


                    }
                }
                return false;
            }
        });*/


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
                            // pagenationCurrentcount=pagenationCurrentcount+1;
                            //  Toast.makeText(getActivity(), "after incremented"+pagenationCurrentcount, Toast.LENGTH_SHORT).show();
                            GetMainDashBoardData();
                        }else {
                          /*  Toast.makeText(getActivity(), String.valueOf(AllTotoalCoupon), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), String.valueOf(dashBoardAdapter.getCount()), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), "current count is exced the limit", Toast.LENGTH_SHORT).show();*/
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

        showprogress();
        initVolleyCallbackForMainDashBoard();

         mVolleyService = new VolleyService(mResultCallback, getActivity());
     //   mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.BaseURL+NetworkURLs.MainDashBoardURL);
        if(isEmptyString(StrLat)||isEmptyString(StrLong)){
           // Toast.makeText(getActivity(),"a", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(),"a", Toast.LENGTH_SHORT).show();
          // mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.BaseURL+NetworkURLs.MainDashBoardURL);
            mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.BaseURL+NetworkURLs.MainDashBoardURL+"?page="+pagenationCurrentcount);

        }else {
        //    Toast.makeText(getActivity(),"b"+String.valueOf(LocationUser), Toast.LENGTH_SHORT).show();
           /* Toast.makeText(getActivity(),"b"+String.valueOf(LocationUser), Toast.LENGTH_SHORT).show();
            "v1/vendors.json?lat=" + String(GlobalData.latitude) + "&long=" + String(GlobalData.longitude) + "&page=" + String(current_page), completion: completion)*/
           // mVolleyService.getDataVolley("GETCALL",NetworkURLs.BaseURL+NetworkURLs.MainDashBoardURL,LocationUser);
            //mVolleyService.getDataVolley("GETCALL",NetworkURLs.BaseURL+NetworkURLs.MainDashBoardURL+"?page="+pagenationCurrentcount,LocationUser);
            /*mVolleyService.getDataVolleyWithoutParams("GETCALL",NetworkURLs.BaseURL+NetworkURLs.MainDashBoardURL+"?lat="+StrLat+ "&long=" + StrLong+"&page=" + pagenationCurrentcount);*/
            mVolleyService.getDataVolleyWithoutParams("GETCALL",NetworkURLs.BaseURL+NetworkURLs.MainDashBoardURL+"?page=" + pagenationCurrentcount+"&lat="+StrLat+ "&long=" + StrLong);

        }
        /*if(TextUtils.isEmpty(StrLat)||StrLat.equalsIgnoreCase("null")||TextUtils.isEmpty(StrLong)||StrLong.equalsIgnoreCase("null")){
            mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.BaseURL+NetworkURLs.MainDashBoardURL);
        }else {
            mVolleyService.getDataVolley("GETCALL",NetworkURLs.BaseURL+NetworkURLs.MainDashBoardURL,LocationUser);
        }*/

    }

    private void initVolleyCallbackForMainDashBoard(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

              hideprogress();
              if(!isfromScrolled){
                  if(MainDashBoard.DashBoardList.size()>0){
                      MainDashBoard.DashBoardList.clear();
                  }
              }


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
                             //   MainDashBoard.DashBoardList.add(new MainDashBoardHelper(Vendorsarray));
                                for (int i = 0; i < Vendorsarray.length(); i++) {
                                    JSONObject c = Vendorsarray.getJSONObject(i);
                                    MainDashBoard.DashBoardList.add(new MainDashBoardHelper(c));

                                }
                            }
                            dashBoardAdapter=new DashBoardAdapter(MainDashBoard.DashBoardList,getActivity());

                            LvProducts.setAdapter(new DashBoardAdapter(MainDashBoard.DashBoardList,getActivity()));
                            LvProducts.setSelection(listindex-1);
                            isloadeddata=true;

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
//                      dialogHelper.showErroDialog(error_response);

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
        empty.setHeight(120);
        empty.setClickable(false);
        lv.addFooterView(empty);
    }
    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }
}
