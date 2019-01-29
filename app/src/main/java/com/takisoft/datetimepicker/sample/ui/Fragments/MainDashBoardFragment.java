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
import com.takisoft.datetimepicker.sample.adpterUtills.MainDashBoardHelper;
import com.takisoft.datetimepicker.sample.appadapters.DashBoardAdapter;
import com.takisoft.datetimepicker.sample.apputilss.MyImageLoader;
import com.takisoft.datetimepicker.sample.apputilss.SharedPref;
import com.takisoft.datetimepicker.sample.network.IResult;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.network.VolleyService;
import com.takisoft.datetimepicker.sample.ui.Activity.MainDashBoard;
import com.takisoft.datetimepicker.sample.ui.utills.GPSTracker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import spinkit.style.ChasingDots;


public class MainDashBoardFragment extends Fragment{
    private ListView LvProducts;
    public static OnItemSelectedListener listener;
    private IResult mResultCallback;
    private  ImageView imageViewLoading;
    private ChasingDots mChasingDotsDrawable;
    public  Map<String, String> LocationUser;
    private GPSTracker gpsTracker;
    private MyImageLoader myImageLoader;
    String StrLat,StrLong;
    public static MainDashBoardFragment newInstance() {
        return new MainDashBoardFragment();
    }

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
        myImageLoader=new MyImageLoader(getActivity());
        gpsTracker=new GPSTracker(getActivity());
        LvProducts=view.findViewById(R.id.lv_products_main_dash);
        imageViewLoading =  view.findViewById(R.id.image_loading_main);
        mChasingDotsDrawable = new ChasingDots();
        mChasingDotsDrawable.setColor(getResources().getColor(R.color.color_custom));
        imageViewLoading.setImageDrawable(mChasingDotsDrawable);
        imageViewLoading.setVisibility(View.GONE);
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
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForMainDashBoard();
        //                Only for animation
        //    private String response_status;
        VolleyService mVolleyService = new VolleyService(mResultCallback, getActivity());


   //     mVolleyService.getDataVolley("GETCALL",NetworkURLs.BaseURL+NetworkURLs.MainDashBoardURL,LocationUser);
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
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);
              if(MainDashBoard.DashBoardList.size()>0){
                  MainDashBoard.DashBoardList.clear();
              }

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                            JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                            JSONArray Vendorsarray = DataRecivedObj.getJSONArray("vendors");

                            for (int i = 0; i < Vendorsarray.length(); i++) {
                                JSONObject c = Vendorsarray.getJSONObject(i);
                                MainDashBoard.DashBoardList.add(new MainDashBoardHelper(c));

                            }
                            LvProducts.setAdapter(new DashBoardAdapter(MainDashBoard.DashBoardList,getActivity()));


                            Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.UID,"Empty UID")), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.Access_Token,"Empty Access Token")), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.Client,"Empty Client")), Toast.LENGTH_SHORT).show();

                            Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.User_ID,"Empty User ID")), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.UserEmail,"Empty Email")), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.FBLogin,"Empty Fb Login")), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.UserPassword,"Empty User Password")), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), String.valueOf(SharedPref.read(SharedPref.IsLoginUser,"Empty Login User")), Toast.LENGTH_SHORT).show();


                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);

                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
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
                            String message=error_obj.getString("message");

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            myImageLoader.showErroDialog(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
               // Toast.makeText(getActivity(), "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        };
    }

}
