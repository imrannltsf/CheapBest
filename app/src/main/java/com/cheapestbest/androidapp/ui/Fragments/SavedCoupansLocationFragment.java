package com.cheapestbest.androidapp.ui.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.SavedLocationHelper;
import com.cheapestbest.androidapp.appadapters.SavedLocationsAdapter;
import com.cheapestbest.androidapp.apputills.MyImageLoader;
import com.cheapestbest.androidapp.network.NetworkURLs;
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


public class SavedCoupansLocationFragment extends Fragment {

    public static OnItemSelectedListener listener;
    public static SavedCoupansLocationFragment newInstance() {
        return new SavedCoupansLocationFragment();
    }

    private List<SavedLocationHelper>Mlist=new ArrayList<>();
    public static String BrandLogoUrl;
    public static String CoupanLogoUrl;
   // private ImageView ImgCoupan;
    private MyImageLoader myImageLoader;
    public static JSONArray SelectedLocationJsonArray;
    private TextView tvheader;
    public static String Coupanname;
    private SavedLocationsAdapter mAdapter;
    private RelativeLayout relativeLayoutEmpty;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        relativeLayoutEmpty=view.findViewById(R.id.layout_empty);
        relativeLayoutEmpty.setVisibility(View.GONE);
        myImageLoader=new MyImageLoader(getActivity());
        tvheader=view.findViewById(R.id.tv_deal_header);
        //ImgCoupan=view.findViewById(R.id.brand_logo_location);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_location);
        mAdapter = new SavedLocationsAdapter(Mlist,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutParams params = new    RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,0);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        //recyclerView.addItemDecoration(new EndOffsetItemDecoration(offsetPx));
     //   setMarginToListView(recyclerView);
        prepareLocationData();


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
           listener = (OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement SavedCoupansLocationFragment.OnItemSelectedListener");
        }
    }

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {

        void onLocationFragCallBack(int position);
    }


    private void prepareLocationData() {

       // myImageLoader.loadImage(NetworkURLs.BaseURLImages+CoupanLogoUrl,ImgCoupan);
        tvheader.setText(String.valueOf("Coupon Locations"));

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

   /* public void setMarginToListView(RecyclerView lv){
        TextView empty = new TextView(getActivity());
        empty.setHeight(100);
        lv.addFooterView(empty);
    }*/

}
