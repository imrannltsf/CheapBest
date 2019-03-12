package com.cheapestbest.androidapp.ui.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.SavedLocationHelper;
import com.cheapestbest.androidapp.appadapters.VendorsLocationsAdapter;
import com.cheapestbest.androidapp.ui.Activity.MainDashBoard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MultipleVendorsLocationsFragment extends Fragment {

    public static OnItemSelectedListener listener;
    public static MultipleVendorsLocationsFragment newInstance() {
        return new MultipleVendorsLocationsFragment();
    }

    private List<SavedLocationHelper>Mlist=new ArrayList<>();
    public static String BrandLogoUrl;



    public static JSONArray SelectedLocationJsonArray;
    private TextView tvheader;
    private VendorsLocationsAdapter mAdapter;
    private RelativeLayout relativeLayoutEmpty;
    public static String ImageLogoVendors;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multi_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        relativeLayoutEmpty=view.findViewById(R.id.layout_empty);
        relativeLayoutEmpty.setVisibility(View.GONE);
        MainDashBoard.FragmentName="MultipleVendorsLocationsFragment";
        tvheader=view.findViewById(R.id.tv_deal_header);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_location);
        mAdapter = new VendorsLocationsAdapter(Mlist,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutParams params = new    RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,0);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareLocationData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
           listener = (OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MultipleVendorsLocationsFragment.OnItemSelectedListener");
        }
    }

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {

        void onMultipleVendorsLocationsFragCallBack(int position);
    }


    private void prepareLocationData() {


        tvheader.setText(String.valueOf("Business Locations"));

        if(SelectedLocationJsonArray.length()<1){
            relativeLayoutEmpty.setVisibility(View.VISIBLE);
        }else {
            for (int j = 0; j < SelectedLocationJsonArray.length(); j++) {

                JSONObject coupans;
                try {
                    coupans = SelectedLocationJsonArray.getJSONObject(j);
                    Mlist.add(new SavedLocationHelper(coupans));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
