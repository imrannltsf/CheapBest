package com.cheapestbest.androidapp.adpterUtills;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainDashBoardHelper {

    private String StrID;
    private String StrLogo;
    private String StrName;
    private String StrCoupans_count;
    private String StrAddress;
    private String StrCoverPhoto;
    private boolean IsVendorSaved;
    private String StrNearBranchName;
    private String StrNearBranchAddress;
    private String StrLatitude;
    private String StrLongitude;
    private String StrDistance;
    private JSONArray jsonArrayLocations;
    public String getStrNearBranchAddress() {
        return StrNearBranchAddress;
    }

    public void setStrNearBranchAddress(String strNearBranchAddress) {
        StrNearBranchAddress = strNearBranchAddress;
    }


    public JSONArray getJsonArrayLocations() {
        return jsonArrayLocations;
    }

    public void setJsonArrayLocations(JSONArray jsonArrayLocations) {
        this.jsonArrayLocations = jsonArrayLocations;
    }

    public Boolean getHasMultipleLocations() {
        return HasMultipleLocations;
    }

    public void setHasMultipleLocations(Boolean hasMultipleLocations) {
        HasMultipleLocations = hasMultipleLocations;
    }

    private Boolean HasMultipleLocations;

    public String getStrDistance() {
        return StrDistance;
    }

    public void setStrDistance(String strDistance) {
        StrDistance = strDistance;
    }

    public boolean isVendorSaved() {
        return IsVendorSaved;
    }

    public void setVendorSaved(boolean vendorSaved) {
        IsVendorSaved = vendorSaved;
    }

    public String getStrNearBranchName() {
        return StrNearBranchName;
    }

    public void setStrNearBranchName(String strNearBranchName) {
        StrNearBranchName = strNearBranchName;
    }

    public String getStrLatitude() {
        return StrLatitude;
    }

    public void setStrLatitude(String strLatitude) {
        StrLatitude = strLatitude;
    }

    public String getStrLongitude() {
        return StrLongitude;
    }

    public void setStrLongitude(String strLongitude) {
        StrLongitude = strLongitude;
    }

    public String getStrID() {
        return StrID;
    }
    private void setStrID(String strID) {
        StrID = strID;
    }
    public String getStrLogo() {
        return StrLogo;
    }
    private void setStrLogo(String strLogo) {
        StrLogo = strLogo;
    }
    public String getStrName() {
        return StrName;
    }
    private void setStrName(String strName) {
        StrName = strName;
    }
    public String getStrCoupans_count() {
        return StrCoupans_count;
    }
    private void setStrCoupans_count(String strCoupans_count) {
        StrCoupans_count = strCoupans_count;
    }
    public String getStrAddress() {
        return StrAddress;
    }
    private void setStrAddress(String strAddress) {
        StrAddress = strAddress;
    }
    public String getStrCoverPhoto() {
        return StrCoverPhoto;
    }
    private void setStrCoverPhoto(String strCoverPhoto) {
        StrCoverPhoto = strCoverPhoto;
    }

  /*  public MainDashBoardHelper(String strID, String strLogo, String strName, String strCoupans_count, String strAddress, String strCoverPhoto) {
        StrID = strID;
        StrLogo = strLogo;
        StrName = strName;
        StrCoupans_count = strCoupans_count;
        StrAddress = strAddress;
        StrCoverPhoto = strCoverPhoto;
    }*/

    public MainDashBoardHelper(JSONObject jsonObject) {

        try {
          /*  JSONArray Vendorsarray = jsonObject.getJSONArray("vendors");
            for (int i = 0; i < Vendorsarray.length(); i++) {*/
                StrID=jsonObject.getString("id");
                StrLogo = jsonObject.getString("logo");
            StrCoverPhoto = jsonObject.getString("cover");
                StrName = jsonObject.getString("name");
                StrCoupans_count = jsonObject.getString("coupons_count");
                StrAddress = jsonObject.getString("address");
              IsVendorSaved=jsonObject.getBoolean("saved");
              HasMultipleLocations=jsonObject.getBoolean("has_multiple_locations");
              if(HasMultipleLocations){
                  setHasMultipleLocations(true);
                  jsonArrayLocations=jsonObject.getJSONArray("locations");
                  setJsonArrayLocations(jsonArrayLocations);
              }

            JSONObject LocationObj = jsonObject.getJSONObject("near_by_branch");
            StrNearBranchName=LocationObj.getString("name");
            StrNearBranchAddress=LocationObj.getString("address");
            StrLatitude=LocationObj.getString("latitude");
            StrLongitude=LocationObj.getString("longitude");
            StrDistance=LocationObj.getString("distance");
                setStrID(StrID);
                setStrLogo(StrLogo);
                setStrName(StrName);
                setStrCoupans_count(StrCoupans_count);
                setStrAddress(StrAddress);
                setStrCoverPhoto(StrCoverPhoto);
                setVendorSaved(IsVendorSaved);
                setStrNearBranchName(StrNearBranchName);
                setStrNearBranchAddress(StrNearBranchAddress);
                setStrLatitude(StrLatitude);
                setStrLongitude(StrLongitude);
                setStrDistance(StrDistance);
           // }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
