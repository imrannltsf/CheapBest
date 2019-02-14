package com.cheapestbest.androidapp.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.MainDashBoardHelper;
import com.cheapestbest.androidapp.appadapters.DashBoardAdapter;
import com.cheapestbest.androidapp.ui.Activity.MainDashBoard;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchDetailFragment extends Fragment{
    private OnItemSelectedListener listener;
    public static List<MainDashBoardHelper> SearchDetailList=new ArrayList<>();
    private RelativeLayout relativeLayoutEmpty;
    public static SearchDetailFragment newInstance() {
        return new SearchDetailFragment();
    }

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_detail, container, false);
    }

    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView lvProducts = view.findViewById(R.id.lv_search_detail);
        setMarginToListView(lvProducts);
        relativeLayoutEmpty=view.findViewById(R.id.layout_empty);
        relativeLayoutEmpty.setVisibility(View.GONE);
        if(SearchDetailList.size()<1){
            relativeLayoutEmpty.setVisibility(View.VISIBLE);
        }else {
            lvProducts.setAdapter(new DashBoardAdapter(SearchDetailList,getActivity()));
        }

        lvProducts.setOnItemClickListener((adapterView, view1, i, l) -> {


            SubBrandFragment.CoverUrl=MainDashBoard.DashBoardList.get(i).getStrCoverPhoto();
            SubBrandFragment.BrandLogoUrl=MainDashBoard.DashBoardList.get(i).getStrLogo();
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            listener = (OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement SearchDetailFragment.OnItemSelectedListener");
        }
    }
    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        void onSearchDeatilCallBack(int position);
    }
    public void setMarginToListView(ListView lv){
        TextView empty = new TextView(getActivity());
        empty.setHeight(160);
        empty.setClickable(false);
        lv.addFooterView(empty);
    }
}
