package com.takisoft.datetimepicker.sample.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SignUpResponseModel {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    public List<Datum> data = new ArrayList();

    public class Datum {

        @SerializedName("id")
        public Integer id;


    }

    @SerializedName("error")
    private String error;
}
