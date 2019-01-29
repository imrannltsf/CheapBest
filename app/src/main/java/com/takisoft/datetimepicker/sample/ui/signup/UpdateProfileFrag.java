package com.takisoft.datetimepicker.sample.ui.signup;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.apputilss.MyImageLoader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class UpdateProfileFrag extends Fragment {


    public static UpdateProfileFrag newInstance() {
        return new UpdateProfileFrag();
    }
    private Calendar myCalendar ;
    private Spinner spinner;
    private EditText etName,etDob,etEmail,etMobile;
    private String StrName,StrDob,StrEmail,StrMobile,StrGender;
    private InputMethodManager inputMethodManager;
    private MyImageLoader myImageLoader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.update_profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initfrag(view);
    }

    private void initfrag(View view) {
        myImageLoader=new MyImageLoader(getActivity());
        inputMethodManager = (InputMethodManager)Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);

        myCalendar= Calendar.getInstance();
        spinner = view.findViewById(R.id.gender_sp);

        etName=view.findViewById(R.id.et_name_signup);
        etDob=view.findViewById(R.id.et_dob_signup);
        etMobile=view.findViewById(R.id.et_mobile_signup);
        etEmail=view.findViewById(R.id.et_email_sign_up);
        Button btnSignUp = view.findViewById(R.id.btn_sign_up);

        etName.setOnClickListener(view15 -> {
            showKeyboard();
            etName.setFocusableInTouchMode(true);
            etName.setFocusable(true);
            etName.requestFocus();

        });
        etMobile.setOnClickListener(view15 -> {
            showKeyboard();
            etMobile.setFocusableInTouchMode(true);
            etMobile.setFocusable(true);
            etMobile.requestFocus();

        });
        etEmail.setOnClickListener(view15 -> {
            showKeyboard();
            etEmail.setFocusableInTouchMode(true);
            etEmail.setFocusable(true);
            etEmail.requestFocus();

        });

        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };


        etDob.setOnClickListener(view12 -> {
             etDob.setFocusableInTouchMode(true);
            etDob.setFocusable(true);
            etDob.requestFocus();
            new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        addSpinerForGender();


        btnSignUp.setOnClickListener(view13 -> {
            StrName=etName.getText().toString();
            StrEmail=etEmail.getText().toString();
            StrGender=String.valueOf(spinner.getSelectedItem()).toLowerCase();
            StrDob=etDob.getText().toString();
            StrMobile=etMobile.getText().toString();
            if(TextUtils.isEmpty(StrName)){

                myImageLoader.showErroDialog("Please Enter Name For Sign Up");

            }else if(TextUtils.isEmpty(StrDob)){

                myImageLoader.showErroDialog("Please Select Your Date Of Birth For Sign Up");

            }else if(TextUtils.isEmpty(StrGender)||StrGender.equalsIgnoreCase("Gender")){

                myImageLoader.showErroDialog("Please Select Your Gender For Sign Up");

            }else if(TextUtils.isEmpty(StrEmail)){

                myImageLoader.showErroDialog("Please Enter Email Address For Sign Up");

            }else if(TextUtils.isEmpty(StrMobile)){

                myImageLoader.showErroDialog("Please Enter Your Mobile Number For Sign Up");

            }else if(TextUtils.isEmpty(StrName)&&TextUtils.isEmpty(StrEmail)&&TextUtils.isEmpty(StrGender)||StrGender.equalsIgnoreCase("Gender")&TextUtils.isEmpty(StrDob)&TextUtils.isEmpty(StrMobile)){

                myImageLoader.showErroDialog("Enter All Required Informations For Sign Up");

            }else {
              /*  CheapBestMain.SignUpData = new HashMap< >();
                CheapBestMain.SignUpData.put("name",StrName);
                CheapBestMain.SignUpData.put("email",StrEmail);
                CheapBestMain.SignUpData.put("gender",StrGender);
                CheapBestMain.SignUpData.put("phone_number",StrMobile);
                CheapBestMain.SignUpData.put("dob",StrDob);
                CheapBestMain.SignUpData.put("password","12345678");
                listener.onSignUpFragCallBack(2);*/
            }
        });
    }
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDob.setText(sdf.format(myCalendar.getTime()));
    }
    private void addSpinerForGender()
    {
        String[] gender = {"Gender","Male", "Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_spinner_item,gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 0) {
                    ((TextView) view).setTextColor(ContextCompat.getColor(getActivity(), R.color.hintcolor));
                } else {
                    ((TextView) view).setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void showKeyboard(){
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    private OnItemSelectedListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement UpdateProfileFrag.OnItemSelectedListener");
        }
    }


    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        void onUpdateProfileFragCallBack(int position);
    }
}
