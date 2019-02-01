package com.takisoft.datetimepicker.sample.ui.Fragments.signup;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/*import android.view.inputmethod.InputMethodManager;*/
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.ui.Activity.CheapBestMain;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class SignUpFragment extends Fragment {

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    private Calendar myCalendar ;
    private Spinner spinner;
    private EditText etName,etDob,etEmail,etMobile;
    private String StrName,StrDob,StrEmail,StrMobile,StrGender;
//    private InputMethodManager inputMethodManager;
    private RelativeLayout relativeLayoutSignUp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initthisfrag(view);

    }

    private void initthisfrag(View view) {

       // inputMethodManager = (InputMethodManager)Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        relativeLayoutSignUp=view.findViewById(R.id.layout_signup);
        myCalendar= Calendar.getInstance();
        spinner = view.findViewById(R.id.gender_sp);
        TextView tvLogin = view.findViewById(R.id.tv_login_signup);
        etName=view.findViewById(R.id.et_name_signup);
        etDob=view.findViewById(R.id.et_dob_signup);
        etMobile=view.findViewById(R.id.et_mobile_signup);
        etEmail=view.findViewById(R.id.et_email_sign_up);
        Button btnSignUp = view.findViewById(R.id.btn_sign_up);



        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        etDob.setInputType(InputType.TYPE_NULL);
        etDob.requestFocus();

        etDob.setOnClickListener(view12 -> {

            etDob.setFocusableInTouchMode(true);
            etDob.setFocusable(true);
            etDob.requestFocus();
            new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        tvLogin.setOnClickListener(view14 -> this.listener.onSignUpFragCallBack(1));
        addSpinerForGender();


        btnSignUp.setOnClickListener(view13 -> {
            StrName=etName.getText().toString();
            StrEmail=etEmail.getText().toString();
            StrGender=String.valueOf(spinner.getSelectedItem()).toLowerCase();
           // StrDob=etDob.getText().toString();
            StrMobile=etMobile.getText().toString();
            if(TextUtils.isEmpty(StrName)){

              //  myImageLoader.showErroDialog("Please Enter Name For Sign Up");
                showsnackmessage("Please Enter Name For Sign Up");
            }else if(TextUtils.isEmpty(StrDob)){
                showsnackmessage("Please Select Your Date Of Birth For Sign Up");


            }else if(TextUtils.isEmpty(StrGender)||StrGender.equalsIgnoreCase("Gender")){
                showsnackmessage("Please Select Your Gender For Sign Up");


            }else if(TextUtils.isEmpty(StrEmail)){
                showsnackmessage("Please Enter Email Address For Sign Up");


            }else if(TextUtils.isEmpty(StrMobile)){
                showsnackmessage("Please Enter Your Mobile Number For Sign Up");


            }else if(TextUtils.isEmpty(StrName)&&TextUtils.isEmpty(StrEmail)&&TextUtils.isEmpty(StrGender)||StrGender.equalsIgnoreCase("Gender")&TextUtils.isEmpty(StrDob)&TextUtils.isEmpty(StrMobile)){
                showsnackmessage("Enter All Required Informations For Sign Up");


            }else {

                if (!isEmailValid(StrEmail)){
                    showsnackmessage("Invalid Email ");
                }else {


                    CheapBestMain.SignUpData = new HashMap< >();
                    CheapBestMain.SignUpData.put("name",StrName);
                    CheapBestMain.SignUpData.put("email",StrEmail);
                    CheapBestMain.SignUpData.put("gender",StrGender);
                    CheapBestMain.SignUpData.put("phone_number",StrMobile);
                    CheapBestMain.SignUpData.put("dob",StrDob);
                    CheapBestMain.SignUpData.put("password","12345678");

                    listener.onSignUpFragCallBack(2);
                }

            }
        });
    }

    private OnItemSelectedListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener)context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement SignUpFragment.OnItemSelectedListener");
        }
    }

    public interface OnItemSelectedListener {
         void onSignUpFragCallBack(int position);
    }
    private void updateLabel() {
         String myFormat = "dd-MM-yyyy";
        String myFormatShow = "MMM dd yyyy";
         //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormatShow, Locale.US);
        StrDob=String.valueOf(sdf.format(myCalendar.getTime()));
        etDob.setText(sdf2.format(myCalendar.getTime()));
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

    private void showsnackmessage(String msg){

        Snackbar snackbar = Snackbar
                .make(relativeLayoutSignUp, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

   private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
