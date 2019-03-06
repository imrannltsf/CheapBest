package com.cheapestbest.androidapp.ui.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.SaveCoupanHelper;
import com.cheapestbest.androidapp.appadapters.CartListAdapter;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.GPSTracker;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import com.cheapestbest.androidapp.recyclerhelper.RecyclerTouchListener;
import com.cheapestbest.androidapp.ui.Activity.MainDashBoard;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CoupanFragment extends Fragment implements RecyclerTouchListener.RecyclerItemTouchHelperListener {
    private String StrLat,StrLong;
    public Map<String, String> LocationUser;
    public static CoupanFragment newInstance() {
        return new CoupanFragment();
    }
    public static OnItemSelectedListener listener;
    int listindex=0;
    private DialogHelper dialogHelper;
    private VolleyService mVolleyService;
    private IResult mResultCallback;
    private List<SaveCoupanHelper>Mlist=new ArrayList<>();
    private GPSTracker gpsTracker;
   private RecyclerView recyclerView;
    /*private CoupanAdapter mAdapter;*/
    private CartListAdapter mAdapter;
    private Progressbar progressbar;
    private RelativeLayout relativeLayoutEmpty;
    boolean isloadingnewdata=false;
    int pagenationCurrentcount=1;
    int TotalPaginationCount=0;
    boolean isgettingdata=false;
    public static int AllTotoalCoupon=0;
    boolean isfirsttime=false;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coupan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressbar =new Progressbar(getActivity());
        gpsTracker=new GPSTracker(getActivity());
        MainDashBoard.FragmentName="CoupanFragment";
        Typeface myTypeFace = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/roboto_bold.ttf");
        relativeLayoutEmpty=view.findViewById(R.id.layout_empty);
        relativeLayoutEmpty.setVisibility(View.GONE);
        TextView tvFontTextView = view.findViewById(R.id.tv_coupan_header);
        tvFontTextView.setTypeface(myTypeFace);
        dialogHelper=new DialogHelper(getActivity());
        if(doesUserHavePermission()){
            StrLat=String.valueOf(gpsTracker.getLatitude());
            StrLong=String.valueOf(gpsTracker.getLongitude());

        }else {
            StrLat="";
            StrLong="";
        }

        LocationUser=new HashMap< >();
        LocationUser.put("lat",StrLat);
        LocationUser.put("long", StrLong);

        recyclerView = view.findViewById(R.id.recycler_view_savedcoupans);
        mAdapter = new CartListAdapter(Mlist,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
       // recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

       /* ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
       */
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerTouchListener(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);




        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);
        GetSavedCoupansData();


        RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isloadingnewdata)
                    return;
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    //End of list
                    listindex=pastVisibleItems;

                    if(!isgettingdata){

                        isgettingdata=true;
                        if(pagenationCurrentcount<TotalPaginationCount){
                            isgettingdata=true;
                            pagenationCurrentcount=pagenationCurrentcount+1;
                            GetSavedCoupansData();
                        }else {
                           // CoupanAdapter.showmargin();
                       //   Toast.makeText(getActivity(), "current ", Toast.LENGTH_SHORT).show();
                        }
                    }else {
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
                    + " must implement CoupanFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof CartListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            //String name = Mlist.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose


            // remove the item from recycler view
            //mAdapter.removeItem(viewHolder.getAdapterPosition());

            int positions = viewHolder.getAdapterPosition();
           // String str=Mlist.get(viewHolder.getAdapterPosition()).getCoupanID();
            String str=Mlist.get(viewHolder.getAdapterPosition()).getCoupanID();
           // Toast.makeText(getActivity(), String.valueOf(str), Toast.LENGTH_SHORT).show();
            final SaveCoupanHelper deletedItem = Mlist.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            mAdapter.removeItem(positions);
            DeleteSavedCoupansData(str,positions);

        }

    }


    public interface OnItemSelectedListener {

        void onCoupanFragCallBack(int position);
    }

   private void GetSavedCoupansData()
    {

        showprogress();
        initVolleyCallbackForSavedCoupan();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        if(!doesUserHavePermission()){
            mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.GetSavedCoupanUrl+"?page="+pagenationCurrentcount);

        }else {
            if(isEmptyString(String.valueOf(gpsTracker.getLatitude()))||isEmptyString(String.valueOf(gpsTracker.getLongitude()))){
                String Str=NetworkURLs.GetSavedCoupanUrl+"?page="+pagenationCurrentcount;
                mVolleyService.getDataVolleyWithoutparam("GETCALL",Str);

            }else {
                String Str=NetworkURLs.GetSavedCoupanUrl+"?&lat="+StrLat+ "&long=" + StrLong+"&page="+pagenationCurrentcount;
                mVolleyService.getDataVolleyWithoutparam("GETCALL",Str);

            }
        }

    }

    private void initVolleyCallbackForSavedCoupan(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                hideprogress();
                if(!isgettingdata){
                    if(Mlist.size()>0){
                        Mlist.clear();
                    }
                }
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                            try {

                                JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                                JSONArray Coupansarray = DataRecivedObj.getJSONArray("coupons");
                                TotalPaginationCount=DataRecivedObj.getInt("page_count");
                                AllTotoalCoupon=DataRecivedObj.getInt("total_count");

                                if(Coupansarray.length()<1){
                                    relativeLayoutEmpty.setVisibility(View.VISIBLE);

                                }
                                for (int i = 0; i < Coupansarray.length(); i++) {


                                    JSONObject coupans = Coupansarray.getJSONObject(i);
                                    Mlist.add(new SaveCoupanHelper(coupans));

                                }
                               isgettingdata=false;


                                if(!isfirsttime){
                                   // prepareSavedCoupanData();
                                    isfirsttime=false;
                                    //recyclerView.getLayoutManager().scrollToPosition(listindex-1);
                                    mAdapter.notifyDataSetChanged();

                                }else {
                                   // recyclerView.getLayoutManager().scrollToPosition(listindex-1);
                                    mAdapter.notifyDataSetChanged();
                                 //   prepareSavedCoupanDataAgain();
                                }

                                //Toast.makeText(getActivity(), String.valueOf(Mlist.size()), Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
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
              //  dialogHelper.showDialogAlert(error.getMessage());


                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                  //  dialogHelper.showErroDialog(error_response);

                    try {
                        JSONObject response_obj=new JSONObject(error_response);

                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");

                          //  Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

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
                    /*DELETE Saved Coupan*/

    private void DeleteSavedCoupansData(String IDDel,int pos)
    {


       showprogress();
        initVolleyCallbackForDeleteCoupan(pos);
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        String DelCoupanUrl=NetworkURLs.BaseSaveCoupanUrl+IDDel+NetworkURLs.DelSaveCoupanUrl;
        mVolleyService.DeleteQuery("DELETECALL",DelCoupanUrl);
    }

    private void initVolleyCallbackForDeleteCoupan(int pp){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {


              hideprogress();
                adapternotified();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {


                            listener.onCoupanFragCallBack(3);

                          //  Mlist.remove(pp);
                            mAdapter.notifyDataSetChanged();
                            if(Mlist.size()<1){
                                relativeLayoutEmpty.setVisibility(View.VISIBLE);
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
                adapternotified();

                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
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


    private void adapternotified(){
        recyclerView.setAdapter(new CartListAdapter(Mlist,getActivity()));
        mAdapter.notifyDataSetChanged();

    }

    private void prepareSavedCoupanData(){
        recyclerView.setAdapter(new CartListAdapter(Mlist,getActivity()));
        mAdapter.notifyDataSetChanged();


    }
    private void prepareSavedCoupanDataAgain(){
       // recyclerView.setAdapter(new CoupanAdapter(Mlist,getActivity()));
      //  mAdapter.notifyDataSetChanged();


    }

    /*private ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
         //   Toast.makeText(getActivity(), "on Move", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();
            String str=Mlist.get(position).getCoupanID();
            DeleteSavedCoupansData(str,position);

        }
    };*/


    public void showprogress(){

        progressbar.ShowProgress();
        progressbar.setCancelable(false);

    }

    public void hideprogress(){
        progressbar.HideProgress();

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void setMarginToListView(ListView lv){
        TextView empty = new TextView(getActivity());
        empty.setHeight(140);
        empty.setClickable(false);
        lv.addFooterView(empty);
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
