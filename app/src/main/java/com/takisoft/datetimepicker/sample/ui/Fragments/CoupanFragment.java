package com.takisoft.datetimepicker.sample.ui.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.adpterUtills.SaveCoupanHelper;
import com.takisoft.datetimepicker.sample.appadapters.CoupanAdapter;
import com.takisoft.datetimepicker.sample.apputilss.MyImageLoader;
import com.takisoft.datetimepicker.sample.network.IResult;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.network.VolleyService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import spinkit.style.ChasingDots;


public class CoupanFragment extends Fragment {

    MyImageLoader myImageLoader;
 /*   private String response_status;*/
    private VolleyService mVolleyService;
    private IResult mResultCallback;
    private List<SaveCoupanHelper>Mlist=new ArrayList<>();
    private RecyclerView recyclerView;
    private CoupanAdapter mAdapter;
    public static CoupanFragment newInstance() {
        return new CoupanFragment();
    }
    public static OnItemSelectedListener listener;
    private ImageView imageViewLoading;
    private ChasingDots mChasingDotsDrawable;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coupan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myImageLoader=new MyImageLoader(getActivity());
        imageViewLoading =  view.findViewById(R.id.image_loading_coupon);
        mChasingDotsDrawable = new ChasingDots();
        mChasingDotsDrawable.setColor(getResources().getColor(R.color.color_custom));
        imageViewLoading.setImageDrawable(mChasingDotsDrawable);
        imageViewLoading.setVisibility(View.GONE);
        Typeface myTypeFace = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/roboto_bold.ttf");

        TextView tvFontTextView = view.findViewById(R.id.tv_coupan_header);
        tvFontTextView.setTypeface(myTypeFace);

        recyclerView = view.findViewById(R.id.recycler_view_savedcoupans);
        mAdapter = new CoupanAdapter(Mlist,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
       /* recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                       // CoupanRedeeem.SelectedCoupanID=Mlist.get(position).getCoupanID();
                        //listener.onCoupanFragCallBack(1);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/

        GetSavedCoupansData();
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



    public interface OnItemSelectedListener {

        void onCoupanFragCallBack(int position);
    }

   private void GetSavedCoupansData()
    {
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForSavedCoupan();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.GetSavedCoupanUrl);
    }

    private void initVolleyCallbackForSavedCoupan(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                //dismissporgress();
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                            try {

                                JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                                JSONArray Coupansarray = DataRecivedObj.getJSONArray("coupons");

                                for (int i = 0; i < Coupansarray.length(); i++) {


                                    JSONObject coupans = Coupansarray.getJSONObject(i);
                                    Mlist.add(new SaveCoupanHelper(coupans));

                                   /* String Strid = coupans.getString("id");
                                    String StrTitle = coupans.getString("title");
                                    String StrCode = coupans.getString("code");
                                    String StrImage = coupans.getString("image");
                                    String StrDescription = coupans.getString("description");
                                    String StrStatus = coupans.getString("status");
                                    String StrOriginalPrice = coupans.getString("original_price");
                                    String StrDiscount = coupans.getString("discount");
                                    String StrDiscountUnit = coupans.getString("discount_unit");
                                    String StrStartDate = coupans.getString("start_date");
                                    String StrEndDate = coupans.getString("end_date");
                                    String StrAppliedToAll = coupans.getString("applied_to_all_locations");
                                    String StrSummery = coupans.getString("summary");
                                    JSONArray LocationArray = coupans.getJSONArray("locations");
                                    int location_array_length=LocationArray.length();
                                    JSONObject c = coupans.getJSONObject("vendor");
                                    String StrVendorName = c.getString("name");
                                    String StrVendorLogo = c.getString("logo");

                                    Mlist.add(new SaveCoupanHelper(Strid,StrTitle,StrCode,StrImage,StrDescription,StrStatus,StrOriginalPrice,StrDiscount,StrDiscountUnit,StrStartDate,StrEndDate,StrAppliedToAll,StrSummery,StrVendorName,StrVendorLogo,LocationArray,location_array_length));
*/
                                }
                                prepareSavedCoupanData();
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
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);
                myImageLoader.showDialogAlert(error.getMessage());
               // Toast.makeText(getActivity(), "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();

                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    myImageLoader.showErroDialog(error_response);

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

            }
        };
    }
                    /*DELETE Saved Coupan*/

    private void DeleteSavedCoupansData(String IDDel)
    {

        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForDeleteCoupan();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        String DelCoupanUrl=NetworkURLs.BaseSaveCoupanUrl+IDDel+NetworkURLs.DelSaveCoupanUrl;
        mVolleyService.DeleteQuery("DELETECALL",DelCoupanUrl);
    }

    private void initVolleyCallbackForDeleteCoupan(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);
                adapternotified();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                            Toast.makeText(getActivity(), "Removed Succesfully", Toast.LENGTH_SHORT).show();

                            mAdapter.notifyDataSetChanged ();
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
                adapternotified();
                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
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

            }
        };
    }


    private void adapternotified(){
        recyclerView.setAdapter(new CoupanAdapter(Mlist,getActivity()));
        mAdapter.notifyDataSetChanged();

    }

    private void prepareSavedCoupanData(){
        recyclerView.setAdapter(new CoupanAdapter(Mlist,getActivity()));
        mAdapter.notifyDataSetChanged();
    }


    private ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            Toast.makeText(getActivity(), "on Move", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();
            String str=Mlist.get(position).getCoupanID();
            DeleteSavedCoupansData(str);

        }
    };
}
