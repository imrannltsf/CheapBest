package com.cheapestbest.androidapp.ui.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.SubscriptionAdaterHelper;
import com.cheapestbest.androidapp.appadapters.SubscriptionAdapter;
import java.util.ArrayList;
import java.util.List;

public class SupscriptionFragment extends Fragment {

    private List<SubscriptionAdaterHelper>ItemList=new ArrayList<>();
    public static SupscriptionFragment newInstance() {
        return new SupscriptionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_supscription, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listViewSubscription = view.findViewById(R.id.lv_subcriptItem);
        ItemList.add(new SubscriptionAdaterHelper("Lorem ipsum dolg elit,semod tinci laoreet dolore magnafdv",R.drawable.ic_tick));
        ItemList.add(new SubscriptionAdaterHelper("Lorem ipsum dolg elit,semod tinci laoreet dolore magnafdv",R.drawable.ic_tick));
        ItemList.add(new SubscriptionAdaterHelper("Lorem ipsum dolg elit,semod tinci laoreet dolore magnafdv",R.drawable.ic_tick));


        listViewSubscription.setAdapter(new SubscriptionAdapter(getActivity(),ItemList));
    }

}
