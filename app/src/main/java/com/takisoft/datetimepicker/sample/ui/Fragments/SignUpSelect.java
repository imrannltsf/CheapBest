package com.takisoft.datetimepicker.sample.ui.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.takisoft.datetimepicker.sample.R;

public class SignUpSelect extends Fragment {

    public static SignUpSelect newInstance() {
        return new SignUpSelect();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_select, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initfrag(view);
    }

    private void initfrag(View view) {
        Button buttonSignUser = view.findViewById(R.id.sign_up_user);
        Button buttonSignVendor = view.findViewById(R.id.btn_vendor);
        buttonSignUser.setOnClickListener(view1 -> this.listener.onSingUpFragCallBack(1));
        buttonSignVendor.setOnClickListener(view12 -> this.listener.onSingUpFragCallBack(2));
    }

    private OnItemSelectedListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement SignUpSelect.OnItemSelectedListener");
        }
    }


    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        void onSingUpFragCallBack(int position);
    }
}
