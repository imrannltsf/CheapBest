package com.takisoft.datetimepicker.sample.network;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by apple on 04/10/2018.
 */

public interface IResult {

    public void notifySuccess(String requestType, String response);

    public void notifyError(String requestType, VolleyError error);


}