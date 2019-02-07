package com.cheapestbest.androidapp.appadapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.adpterUtills.SubscriptionAdaterHelper;

import java.util.List;

public class SubscriptionAdapter extends BaseAdapter {
    private Context Mcontext;
    private List<SubscriptionAdaterHelper>ItemList;

    public SubscriptionAdapter(Context mcontext, List<SubscriptionAdaterHelper> itemList) {
        Mcontext = mcontext;
        ItemList = itemList;
    }

    @Override
    public int getCount() {
        return ItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return ItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null){
            LayoutInflater  inflater= (LayoutInflater) Mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                view=inflater.inflate(R.layout.subscription_adapter_helper,null);
            }
        }

        TextView textViewValue= null;
        if (view != null) {
            textViewValue = view.findViewById(R.id.tv_value_subcription);
        }
        ImageView imageView= null;
        if (view != null) {
            imageView = view.findViewById(R.id.img_tick_subscription_helper);
        }

        if (imageView != null) {
            imageView.setBackgroundResource(ItemList.get(i).getImgLogo());
        }
        if (textViewValue != null) {
            textViewValue.setText(ItemList.get(i).getStr());
        }
        return view;
    }
}
