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

                    return super.parseNetworkResponse(response);
                }


                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String>  params = new HashMap<>();
                    params.put(SharedPref.Access_Token,  SharedPref.read(SharedPref.Access_Token, null));
                    params.put(SharedPref.Client, SharedPref.read(SharedPref.Client, null));
                    params.put(SharedPref.UID, SharedPref.read(SharedPref.UID, null));

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


                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {

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

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String>  params = new HashMap<>();
                    params.put(SharedPref.Access_Token,  SharedPref.read(SharedPref.Access_Token, null));
                    params.put(SharedPref.Client, SharedPref.read(SharedPref.Client, null));
                    params.put(SharedPref.UID, SharedPref.read(SharedPref.UID, null));
                     return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {


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


                    SharedPref.write(SharedPref.Access_Token, response.headers.get("access-token"));
                    SharedPref.write(SharedPref.Client, response.headers.get("client"));
                    SharedPref.write(SharedPref.UID, response.headers.get("uid"));

                    return super.parseNetworkResponse(response);
                }


                @Override
                public Map<String, String> getHeaders() {

                    return params;
                }
            };


            queue.add(strRequest);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


}