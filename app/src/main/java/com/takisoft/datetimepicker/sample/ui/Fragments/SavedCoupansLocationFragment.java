package com.takisoft.datetimepicker.sample.ui.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.adpterUtills.SavedLocationHelper;
import com.takisoft.datetimepicker.sample.appadapters.SavedLocationsAdapter;
import com.takisoft.datetimepicker.sample.apputilss.MyImageLoader;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.recyclerhelper.RecyclerItemClickListener;
import com.takisoft.datetimepicker.sample.ui.Activity.CoupanRedeeem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SavedCoupansLocationFragment extends Fragment {

    public static SavedCoupansLocationFragment newInstance() {
        return new SavedCoupansLocationFragment();
    }
    private List<SavedLocationHelper>Mlist=new ArrayList<>();
    public static String BrandLogoUrl;
    public static String CoupanLogoUrl;
    private ImageView ImgCoupan;
    private MyImageLoader myImageLoader;
    public static JSONArray SelectedLocationJsonArray;
    private TextView tvheader;
    public static String Coupanname;
    private SavedLocationsAdapter mAdapter;
    public static OnItemSelectedListener listener;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myImageLoader=new MyImageLoader(getActivity());
        tvheader=view.findViewById(R.id.tv_deal_header);
        ImgCoupan=view.findViewById(R.id.brand_logo_location);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_location);
        mAdapter = new SavedLocationsAdapter(Mlist,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareLocationData();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener) context; // = (YourActivity) context
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

        //Toast.makeText(getActivity(), String, Toast.LENGTH_SHORT).show();
           // myImageLoader.showDialog(CoupanLogoUrl);
        myImageLoader.loadImage(NetworkURLs.BaseURLImages+CoupanLogoUrl,ImgCoupan);
        tvheader.setText(Coupanname);


        for (int j = 0; j < SelectedLocationJsonArray.length(); j++) {

            JSONObject coupans;
            try {
                coupans = SelectedLocationJsonArray.getJSONObject(j);
                Mlist.add(new SavedLocationHelper(coupans));

              /*  String Strid = coupans.getString("id");
                String StrTitle = coupans.getString("name");
                String StrAddressA = coupans.getString("address_line_1");
                String StrAddressB = coupans.getString("address_line_2");
                String StrCity = coupans.getString("city");
                String StrState = coupans.getString("state");
                String StrCountry = coupans.getString("country");
                String StrPostalCode = coupans.getString("postal_code");
                String StrLongitude = coupans.getString("latitude");
                String StrLatitude = coupans.getString("longitude");
                Mlist.add(new SavedLocationHelper(Strid,StrTitle,StrAddressA,StrAddressB,StrCity,StrState,StrCountry,StrPostalCode,StrLatitude,StrLongitude));
*/


            } catch (JSONException e) {
                e.printStackTrace();
            }

          //  LvProducts.setAdapter(new SavedLocationsAdapter(Mlist,getActivity()));

        }
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
            Toast.makeText(getActivity(), "on Swiped ", Toast.LENGTH_SHORT).show();
            //Remove swiped item from list and notify the RecyclerView
            int position = viewHolder.getAdapterPosition();
            String str=Mlist.get(position).getLocationID();
            Toast.makeText(getActivity(), String.valueOf(str), Toast.LENGTH_SHORT).show();
        }
    };
}
