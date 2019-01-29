package com.takisoft.datetimepicker.sample.responsemodel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainDashBoradResponse {
    private String StrID;
    private String StrLogo;
    private String StrName;
    private String StrCoupans_count;
    private String StrAddress;
    private JSONArray jsonArray;
    private String StrCoverPhoto;

    public MainDashBoradResponse(String strID, String strLogo, String strName, String strCoupans_count, String strAddress, JSONArray jsonArray, String strCoverPhoto) {
        StrID = strID;
        StrLogo = strLogo;
        StrName = strName;
        StrCoupans_count = strCoupans_count;
        StrAddress = strAddress;
        this.jsonArray = jsonArray;
        StrCoverPhoto = strCoverPhoto;
    }

    public String getStrID() {
        return StrID;
    }

    public void setStrID(String strID) {
        StrID = strID;
    }

    public String getStrLogo() {
        return StrLogo;
    }

    public void setStrLogo(String strLogo) {
        StrLogo = strLogo;
    }

    public String getStrName() {
        return StrName;
    }

    public void setStrName(String strName) {
        StrName = strName;
    }

    public String getStrCoupans_count() {
        return StrCoupans_count;
    }

    public void setStrCoupans_count(String strCoupans_count) {
        StrCoupans_count = strCoupans_count;
    }

    public String getStrAddress() {
        return StrAddress;
    }

    public void setStrAddress(String strAddress) {
        StrAddress = strAddress;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String getStrCoverPhoto() {
        return StrCoverPhoto;
    }

    public void setStrCoverPhoto(String strCoverPhoto) {
        StrCoverPhoto = strCoverPhoto;
    }
}
