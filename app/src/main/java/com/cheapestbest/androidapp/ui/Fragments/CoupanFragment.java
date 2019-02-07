package com.cheapestbest.androidapp.ui.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.SaveCoupanHelper;
import com.cheapestbest.androidapp.appadapters.CoupanAdapter;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
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

public class CoupanFragment extends Fragment {

    public static CoupanFragment newInstance() {
        return new CoupanFragment();
    }
    public static OnItemSelectedListener listener;

    private DialogHelper dialogHelper;
    private VolleyService mVolleyService;
    private IResult mResultCallback;
    private List<SaveCoupanHelper>Mlist=new ArrayList<>();
    private RecyclerView recyclerView;
    private CoupanAdapter mAdapter;
    private Progressbar progressbar;
    private RelativeLayout relativeLayoutEmpty;

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
        Typeface myTypeFace = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/roboto_bold.ttf");
        relativeLayoutEmpty=view.findViewById(R.id.layout_empty);
        relativeLayoutEmpty.setVisibility(View.GONE);
        TextView tvFontTextView = view.findViewById(R.id.tv_coupan_header);
        tvFontTextView.setTypeface(myTypeFace);
        dialogHelper=new DialogHelper(getActivity());
        recyclerView = view.findViewById(R.id.recycler_view_savedcoupans);
        mAdapter = new CoupanAdapter(Mlist,getActivity());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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

        showprogress();
        initVolleyCallbackForSavedCoupan();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        mVolleyService.getDataVolleyWithoutparam("GETCALL",NetworkURLs.GetSavedCoupanUrl);
    }

    private void initVolleyCallbackForSavedCoupan(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                hideprogress();

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                            try {

                                JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                                JSONArray Coupansarray = DataRecivedObj.getJSONArray("coupons");


                                if(Coupansarray.length()<1){
                                    relativeLayoutEmpty.setVisibility(View.VISIBLE);

                                }
                                for (int i = 0; i < Coupansarray.length(); i++) {


                                    JSONObject coupans = Coupansarray.getJSONObject(i);
                                    Mlist.add(new SaveCoupanHelper(coupans));

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

                hideprogress();
                dialogHelper.showDialogAlert(error.getMessage());


                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    dialogHelper.showErroDialog(error_response);

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

                }

            }
        };
    }
                    /*DELETE Saved Coupan*/

    private void DeleteSavedCoupansData(String IDDel)
    {


       showprogress();
        initVolleyCallbackForDeleteCoupan();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        String DelCoupanUrl=NetworkURLs.BaseSaveCoupanUrl+IDDel+NetworkURLs.DelSaveCoupanUrl;
        mVolleyService.DeleteQuery("DELETECALL",DelCoupanUrl);
    }

    private void initVolleyCallbackForDeleteCoupan(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {


              hideprogress();
                adapternotified();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                          //  Toast.makeText(getActivity(), "Removed Succesfully", Toast.LENGTH_SHORT).show();

                            listener.onCoupanFragCallBack(3);

                            if(Mlist.size()>0){
                                Mlist.clear();
                                GetSavedCoupansData();
                            }
                            mAdapter.notifyDataSetChanged ();
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


    public void showprogress(){

        progressbar.ShowProgress();
        progressbar.setCancelable(false);

    }

    public void hideprogress(){
        progressbar.HideProgress();

    }
}
