package com.takisoft.datetimepicker.sample.network;

public class NetworkURLs {

    /*public static String BaseURL = "http://cbb200cf.ngrok.io/";
    public static String BaseURLImages ="http://cbb200cf.ngrok.io";*/

     public static String BaseURL = "http://cheapestbest.nltsf.com/";
    public static String BaseURLImages ="http://cheapestbest.nltsf.com";

    public static String SignUPURL="api/auth.json";
    public static String SignInURL="api/auth/sign_in.json";
    public static String VerifyNewUserURL="api/users/confirm.json";
    public static String SetPasswordUserURL="api/auth/password.json";
    public static String MainDashBoardURL="api/v1/vendors.json";
    public static String BaseSaveCoupanUrl = BaseURL+"api/v1/coupons/";
    public static String SaveCoupanUrl="/save.json";
    public static String DelSaveCoupanUrl="/remove.json";
    public static String GetSavedCoupanUrl=BaseURL+"api/v1/coupons/my_coupons.json";
    public static String GetCoupanDetailUrl=BaseURL+"api/v1/coupons/";
    public static String SignOutUrl="api/auth/sign_out.json";
    public static String UserProfile="api/v1/users/";
    public static String CoupanRedemUrl=BaseURL+"api/v1/coupons/";
    public static String SearchByManualUrl="api/v1/vendors/search.json?";
    public static String GetCouponsListUsingVendorsID=BaseURL+"api/v1/vendors/";
    public static String EmailUrlForPasswordReset=BaseURL+"api/auth/password.json";
    public static String EmailVerifyCodeUrl=BaseURL+"api/users/verify.json";
    public static String UpdateUser=BaseURL+"api/v1/users/";
    public static String FacebookLoginUrl=BaseURL+"api/users/auth/facebook.json";
}
