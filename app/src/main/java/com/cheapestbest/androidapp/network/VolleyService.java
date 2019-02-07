package com.cheapestbest.androidapp.network;

import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cheapestbest.androidapp.apputills.SharedPref;
import java.util.HashMap;
import java.util.Map;

public class VolleyService {

   private IResult mResultCallback;
   private Context mContext;
    public VolleyService(IResult resultCallback, Context context){
        mResultCallback = resultCallback;
        mContext = context;
        SharedPref.init(mContext);
    }


    public void postDataVolley(final String requestType, String url, final Map<String, String> params){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);
            StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {

                            if(mResultCallback != null)
                                mResultCallback.notifySuccess(requestType,response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifyError(requestType,error);
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams()
                {

                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {

                            /*get headers from response*/
                    SharedPref.write(SharedPref.Access_Token, response.headers.get("access-token"));
                    SharedPref.write(SharedPref.Client, response.headers.get("client"));
                    SharedPref.write(SharedPref.UID, response.headers.get("uid"));

                    /*if(!isEmptyString(response.headers.get(SharedPref.Access_Token))){
                        SharedPref.write(SharedPref.Access_Token, response.headers.get(SharedPref.Access_Token));
                        Log.e("Myaccess_token",response.headers.get(SharedPref.Access_Token));
                    }else {
                        Log.e("Myaccess_token","Empty"+response.headers.get(SharedPref.Access_Token));
                    }
                    if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.Client)))){
                        SharedPref.write(SharedPref.Client, response.headers.get(SharedPref.Client));
                        Log.e("myclient_id",response.headers.get(SharedPref.Client));
                    }else {
                        Log.e("myclient_id","Empty:"+response.headers.get(SharedPref.Client));
                    }
                    if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.UID)))){
                        SharedPref.write(SharedPref.UID, response.headers.get(SharedPref.UID));
                        Log.e("Myuid",response.headers.get(SharedPref.UID));
                    }else {
                        Log.e("Myuid","Empty:"+response.headers.get(SharedPref.UID));
                    }
*/
                   return super.parseNetworkResponse(response);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return super.getHeaders();
                }
            };



            queue.add(strRequest);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    //////////////////////Post request with header//////////

    public void postDataVolleyHeader(final String requestType, String url, final Map<String, String> params){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);
            StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {

                            if(mResultCallback != null)
                                mResultCallback.notifySuccess(requestType,response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifyError(requestType,error);
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams()
                {

                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {

                   /* *//*get headers from response*//**/
                    SharedPref.write(SharedPref.Access_Token, response.headers.get("access-token"));
                    SharedPref.write(SharedPref.Client, response.headers.get("client"));
                    SharedPref.write(SharedPref.UID, response.headers.get("uid"));

                   /* if(!isEmptyString(response.headers.get(SharedPref.Access_Token))){
                        SharedPref.write(SharedPref.Access_Token, response.headers.get(SharedPref.Access_Token));

                    }
                    if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.Client)))){
                        SharedPref.write(SharedPref.Client, response.headers.get(SharedPref.Client));
                    }
                    if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.UID)))){
                        SharedPref.write(SharedPref.UID, response.headers.get(SharedPref.UID));
                    }*/
                    return super.parseNetworkResponse(response);
                }


                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String>  params = new HashMap<>();
                    params.put(SharedPref.Access_Token,  SharedPref.read(SharedPref.Access_Token, null));
                    params.put(SharedPref.Client, SharedPref.read(SharedPref.Client, null));
                    params.put(SharedPref.UID, SharedPref.read(SharedPref.UID, null));
                   // params.put(SharedPref.UID,MainDashBoard.responseUid);

                  /*  Log.e("hereaccess_token",SharedPref.read(SharedPref.Access_Token, null));
                    Log.e("hereuid",SharedPref.read(SharedPref.UID, null));
                    Log.e("hereclient",SharedPref.read(SharedPref.Client, null));*/
                    return params;
                }

            };



            queue.add(strRequest);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

        /*post data with out paramaters*/
        public void postDataVolleyWithHeaderWithoutParam(final String requestType, String url){
            try {
                RequestQueue queue = Volley.newRequestQueue(mContext);

                StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {

                                if(mResultCallback != null)
                                    mResultCallback.notifySuccess(requestType,response);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                if(mResultCallback != null)
                                    mResultCallback.notifyError(requestType,error);
                            }
                        })
                {
                   /* @Override
                    protected Map<String, String> getParams()
                    {

                        return params;
                    }*/

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                     /*   if(!isEmptyString(response.headers.get(SharedPref.Access_Token))){
                            SharedPref.write(SharedPref.Access_Token, response.headers.get(SharedPref.Access_Token));

                        }
                        if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.Client)))){
                            SharedPref.write(SharedPref.Client, response.headers.get(SharedPref.Client));
                        }
                        if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.UID)))){
                            SharedPref.write(SharedPref.UID, response.headers.get(SharedPref.UID));
                        }*/

                        SharedPref.write(SharedPref.Access_Token, response.headers.get("access-token"));
                        SharedPref.write(SharedPref.Client, response.headers.get("client"));
                        SharedPref.write(SharedPref.UID, response.headers.get("uid"));

                        return super.parseNetworkResponse(response);
                    }


                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String>  params = new HashMap<>();
                        params.put(SharedPref.Access_Token,  SharedPref.read(SharedPref.Access_Token, null));
                        params.put(SharedPref.Client, SharedPref.read(SharedPref.Client, null));
                       // params.put(SharedPref.UID,MainDashBoard.responseUid);
                        params.put(SharedPref.UID, SharedPref.read(SharedPref.UID, null));
                        return params;
                    }
                };


                queue.add(strRequest);

            }catch(Exception e){
                e.printStackTrace();
            }
        }

                /*get data without param*/

    public void getDataVolleyWithoutParams(final String requestType, String url){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {

                            if(mResultCallback != null)
                                mResultCallback.notifySuccess(requestType,response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifyError(requestType,error);
                        }
                    })
            {
              /*  @Override
                protected Map<String, String> getParams()
                {

                   *//* Map<String, String> data = new HashMap<>();

                    return data;*//*
                    return params;
                }*/
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String>  params = new HashMap<>();
                    params.put(SharedPref.Access_Token,  SharedPref.read(SharedPref.Access_Token, null));
                    params.put(SharedPref.Client, SharedPref.read(SharedPref.Client, null));
                    params.put(SharedPref.UID, SharedPref.read(SharedPref.UID, null));
                    // params.put(SharedPref.UID,MainDashBoard.responseUid);
                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {

                    //  Toast.makeText(mContext, String.valueOf(response.headers.get("access-token")), Toast.LENGTH_SHORT).show();

                   /* if(!isEmptyString(response.headers.get(SharedPref.Access_Token))){
                        SharedPref.write(SharedPref.Access_Token, response.headers.get(SharedPref.Access_Token));

                    }
                    if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.Client)))){
                        SharedPref.write(SharedPref.Client, response.headers.get(SharedPref.Client));
                    }
                    if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.UID)))){
                        SharedPref.write(SharedPref.UID, response.headers.get(SharedPref.UID));
                    }*/

                    // MainDashBoard.responseUid =response.headers.get("uid");

                    SharedPref.write(SharedPref.Access_Token, response.headers.get("access-token"));
                    SharedPref.write(SharedPref.Client, response.headers.get("client"));
                    SharedPref.write(SharedPref.UID, response.headers.get("uid"));
                    //params.put(SharedPref.UID,MainDashBoard.responseUid);

                    return super.parseNetworkResponse(response);

                }
            };

            queue.add(strRequest);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
                    /*get Data using param*/
        public void getDataVolley(final String requestType, String url, Map<String, String> params){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {

                            if(mResultCallback != null)
                                mResultCallback.notifySuccess(requestType,response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifyError(requestType,error);
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams()
                {

                   /* Map<String, String> data = new HashMap<>();

                    return data;*/
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String>  params = new HashMap<>();
                    params.put(SharedPref.Access_Token,  SharedPref.read(SharedPref.Access_Token, null));
                    params.put(SharedPref.Client, SharedPref.read(SharedPref.Client, null));
                    params.put(SharedPref.UID, SharedPref.read(SharedPref.UID, null));
                   // params.put(SharedPref.UID,MainDashBoard.responseUid);
                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {

                  //  Toast.makeText(mContext, String.valueOf(response.headers.get("access-token")), Toast.LENGTH_SHORT).show();

                   /* if(!isEmptyString(response.headers.get(SharedPref.Access_Token))){
                        SharedPref.write(SharedPref.Access_Token, response.headers.get(SharedPref.Access_Token));

                    }
                    if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.Client)))){
                        SharedPref.write(SharedPref.Client, response.headers.get(SharedPref.Client));
                    }
                    if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.UID)))){
                        SharedPref.write(SharedPref.UID, response.headers.get(SharedPref.UID));
                    }*/

                  // MainDashBoard.responseUid =response.headers.get("uid");

                    SharedPref.write(SharedPref.Access_Token, response.headers.get("access-token"));
                    SharedPref.write(SharedPref.Client, response.headers.get("client"));
                    SharedPref.write(SharedPref.UID, response.headers.get("uid"));
                    //params.put(SharedPref.UID,MainDashBoard.responseUid);

                    return super.parseNetworkResponse(response);

                }
            };

            queue.add(strRequest);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


                /*get Request without params*/
                public void getDataVolleyWithoutparam(final String requestType, String url){
                    try {
                        RequestQueue queue = Volley.newRequestQueue(mContext);

                        StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response)
                                    {

                                        if(mResultCallback != null)
                                            mResultCallback.notifySuccess(requestType,response);
                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error)
                                    {
                                        if(mResultCallback != null)
                                            mResultCallback.notifyError(requestType,error);
                                    }
                                })
                        {
                            @Override
                            protected Map<String, String> getParams()
                            {

                                Map<String, String> data = new HashMap<>();

                                return data;
                            }
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String>  params = new HashMap<>();
                                params.put(SharedPref.Access_Token,  SharedPref.read(SharedPref.Access_Token, null));
                                params.put(SharedPref.Client, SharedPref.read(SharedPref.Client, null));
                                params.put(SharedPref.UID, SharedPref.read(SharedPref.UID, null));
                              //  params.put("uid",MainDashBoard.responseUid);
                              return params;
                            }

                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                SharedPref.write(SharedPref.Access_Token, response.headers.get("access-token"));
                                SharedPref.write(SharedPref.Client, response.headers.get("client"));
                                SharedPref.write(SharedPref.UID, response.headers.get("uid"));


                           /*     if(!isEmptyString(response.headers.get(SharedPref.Access_Token))){
                                    SharedPref.write(SharedPref.Access_Token, response.headers.get(SharedPref.Access_Token));

                                }
                                if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.Client)))){
                                    SharedPref.write(SharedPref.Client, response.headers.get(SharedPref.Client));
                                }
                                if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.UID)))){
                                    SharedPref.write(SharedPref.UID, response.headers.get(SharedPref.UID));
                                }*/
                                return super.parseNetworkResponse(response);
                            }
                        };

                        queue.add(strRequest);

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                /*Delete Saved Request*/
    public void DeleteQuery(final String requestType, String url){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest strRequest = new StringRequest(Request.Method.DELETE, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {

                            if(mResultCallback != null)
                                mResultCallback.notifySuccess(requestType,response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifyError(requestType,error);
                        }
                    })
            {
                   /* @Override
                    protected Map<String, String> getParams()
                    {

                        return params;
                    }*/

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {

                    /*if(!isEmptyString(response.headers.get(SharedPref.Access_Token))){
                        SharedPref.write(SharedPref.Access_Token, response.headers.get(SharedPref.Access_Token));

                    }
                    if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.Client)))){
                        SharedPref.write(SharedPref.Client, response.headers.get(SharedPref.Client));
                    }
                    if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.UID)))){
                        SharedPref.write(SharedPref.UID, response.headers.get(SharedPref.UID));
                    }*/

                    SharedPref.write(SharedPref.Access_Token, response.headers.get("access-token"));
                    SharedPref.write(SharedPref.Client, response.headers.get("client"));
                    SharedPref.write(SharedPref.UID, response.headers.get("uid"));

                    return super.parseNetworkResponse(response);
                }


                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String>  params = new HashMap<>();
                    params.put(SharedPref.Access_Token,  SharedPref.read(SharedPref.Access_Token, null));
                    params.put(SharedPref.Client, SharedPref.read(SharedPref.Client, null));
                   params.put(SharedPref.UID, SharedPref.read(SharedPref.UID, null));
                   // params.put(SharedPref.UID,MainDashBoard.responseUid);
                    return params;
                }
            };


            queue.add(strRequest);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

                    /*put Request for new passowrd*/


    public void PutReqquestVolley(final String requestType, String url, final Map<String, String> params){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest strRequest = new StringRequest(Request.Method.PUT, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {

                            if(mResultCallback != null)
                                mResultCallback.notifySuccess(requestType,response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifyError(requestType,error);
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams()
                {

                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    SharedPref.write(SharedPref.Access_Token, response.headers.get("access-token"));
                    SharedPref.write(SharedPref.Client, response.headers.get("client"));
                    SharedPref.write(SharedPref.UID, response.headers.get("uid"));

                  /*  if(!isEmptyString(response.headers.get(SharedPref.Access_Token))){
                        SharedPref.write(SharedPref.Access_Token, response.headers.get(SharedPref.Access_Token));

                    }
                    if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.Client)))){
                        SharedPref.write(SharedPref.Client, response.headers.get(SharedPref.Client));
                    }
                    if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.UID)))){
                        SharedPref.write(SharedPref.UID, response.headers.get(SharedPref.UID));
                    }*/
                    return super.parseNetworkResponse(response);
                }


                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String>  params = new HashMap<>();
                    params.put(SharedPref.Access_Token,  SharedPref.read(SharedPref.Access_Token, null));
                    params.put(SharedPref.Client, SharedPref.read(SharedPref.Client, null));

                    params.put(SharedPref.UID, SharedPref.read(SharedPref.UID, null));
                   // params.put("uid",MainDashBoard.responseUid);
                    return params;
                }
            };


            queue.add(strRequest);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*Post Request for new passowrd*/


    public void PostReqquestVolleyWithoutHeader(final String requestType, String url, final Map<String, String> params){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {

                            if(mResultCallback != null)
                                mResultCallback.notifySuccess(requestType,response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifyError(requestType,error);
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams()
                {

                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                  /*  if(!isEmptyString(response.headers.get(SharedPref.Access_Token))){
                        SharedPref.write(SharedPref.Access_Token, response.headers.get(SharedPref.Access_Token));

                    }
                    if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.Client)))){
                        SharedPref.write(SharedPref.Client, response.headers.get(SharedPref.Client));
                    }
                    if(!isEmptyString(response.headers.get( response.headers.get(SharedPref.UID)))){
                        SharedPref.write(SharedPref.UID, response.headers.get(SharedPref.UID));
                    }*/


                    SharedPref.write(SharedPref.Access_Token, response.headers.get("access-token"));
                    SharedPref.write(SharedPref.Client, response.headers.get("client"));
                    SharedPref.write(SharedPref.UID, response.headers.get("uid"));

                    return super.parseNetworkResponse(response);
                }


                @Override
                public Map<String, String> getHeaders() {
                   /* Map<String, String>  params = new HashMap<>();
                    params.put("access-token",  SharedPref.read(SharedPref.Access_Token, null));
                    params.put("client", SharedPref.read(SharedPref.Client, null));
                    params.put("uid", SharedPref.read(SharedPref.UID, null));*/
                    return params;
                }
            };


            queue.add(strRequest);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }

}