package com.takisoft.datetimepicker.sample.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.android.volley.VolleyError;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.adpterUtills.MainDashBoardHelper;
import com.takisoft.datetimepicker.sample.appadapters.DashBoardAdapter;
import com.takisoft.datetimepicker.sample.apputills.DialogHelper;
import com.takisoft.datetimepicker.sample.apputills.MyImageLoader;
import com.takisoft.datetimepicker.sample.apputills.Progressbar;
import com.takisoft.datetimepicker.sample.apputills.SharedPref;
import com.takisoft.datetimepicker.sample.network.IResult;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.network.VolleyService;
import com.takisoft.datetimepicker.sample.ui.Activity.MainDashBoard;
import com.takisoft.datetimepicker.sample.apputills.GPSTracker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class MainDashBoardFragment extends Fragment{

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

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_dash_board, container, false);
    }

    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPref.init(getActivity());
        progressbar =new Progressbar(getActivity());
        dialogHelper=new DialogHelper(getActivity());
        gpsTracker=new GPSTracker(getActivity());
        LvProducts=view.findViewById(R.id.lv_products_main_dash);
        relativeLayoutEmpty=view.findViewById(R.id.layout_empty);
        relativeLayoutEmpty.setVisibility(View.GONE);
        StrLat=String.valueOf(gpsTracker.getLatitude());
        StrLong=String.valueOf(gpsTracker.getLongitude());

        LocationUser=new HashMap< >();
        LocationUser.put("lat",StrLat);
        LocationUser.put("long", StrLong);
        GetMainDashBoardData();

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

   private void GetMainDashBoardData()
    {

        showprogress();
        initVolleyCallbackForMainDashBoard();

        VolleyService mVolleyService = new VolleyService(mResultCallback, getActivity());


        mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.BaseURL+NetworkURLs.MainDashBoardURL);
       /* if(TextUtils.isEmpty(StrLat)||StrLat.equalsIgnoreCase("null")||TextUtils.isEmpty(StrLong)||StrLong.equalsIgnoreCase("null")){
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
              if(MainDashBoard.DashBoardList.size()>0){
                  MainDashBoard.DashBoardList.clear();
              }

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                            JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                            JSONArray Vendorsarray = DataRecivedObj.getJSONArray("vendors");

                            if(Vendorsarray.length()<1){
                                relativeLayoutEmpty.setVisibility(View.VISIBLE);
                            }else {
                                for (int i = 0; i < Vendorsarray.length(); i++) {
                                    JSONObject c = Vendorsarray.getJSONObject(i);
                                    MainDashBoard.DashBoardList.add(new MainDashBoardHelper(c));

                                }
                            }

                            LvProducts.setAdapter(new DashBoardAdapter(MainDashBoard.DashBoardList,getActivity()));

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
}
