package com.cheapestbest.androidapp.adpterUtills;


import org.json.JSONException;
import org.json.JSONObject;

public class SavedLocationHelper {
    private String LocationID;
    private String LocationTitle;
    private String LocationAddress1;
    private String LocationAddress2;
    private String LocationCity;
    private String LocationState;
    private String LocationCountry;
    private String LocationPostalCode;
    private String LocationLatitude;
    private String LocationLongitude;

   /* public SavedLocationHelper(String locationID, String locationTitle, String locationAddress1, String locationAddress2, String locationCity, String locationState, String locationCountry, String locationPostalCode, String locationLatitude, String locationLongitude) {
        LocationID = locationID;
        LocationTitle = locationTitle;
        LocationAddress1 = locationAddress1;
        LocationAddress2 = locationAddress2;
        LocationCity = locationCity;
        LocationState = locationState;
        LocationCountry = locationCountry;
        LocationPostalCode = locationPostalCode;
        LocationLatitude = locationLatitude;
        LocationLongitude = locationLongitude;
    }*/

    public SavedLocationHelper(JSONObject coupans) {
       /* LocationID = locationID;
        LocationTitle = locationTitle;
        LocationAddress1 = locationAddress1;
        LocationAddress2 = locationAddress2;
        LocationCity = locationCity;
        LocationState = locationState;
        LocationCountry = locationCountry;
        LocationPostalCode = locationPostalCode;
        LocationLatitude = locationLatitude;
        LocationLongitude = locationLongitude;*/

        try {
            LocationID = coupans.getString("id");
            LocationTitle = coupans.getString("name");
            LocationAddress1 = coupans.getString("address_line_1");
            LocationAddress2 = coupans.getString("address_line_2");
            LocationCity = coupans.getString("city");
            LocationState = coupans.getString("state");
            LocationCountry = coupans.getString("country");
            LocationPostalCode = coupans.getString("postal_code");
            LocationLatitude = coupans.getString("latitude");
            LocationLongitude = coupans.getString("longitude");

            setLocationID(LocationID);
            setLocationTitle(LocationTitle);
            setLocationAddress1(LocationAddress1);
            setLocationAddress2(LocationAddress2);
            setLocationCity(LocationCity);
            setLocationState(LocationState);
            setLocationCountry(LocationCountry);
            setLocationPostalCode(LocationPostalCode);
            setLocationLatitude(LocationLatitude);
            setLocationLongitude(LocationLongitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getLocationID() {
        return LocationID;
    }
    private void setLocationID(String locationID) {
        LocationID = locationID;
    }
    public String getLocationTitle() {
        return LocationTitle;
    }
    private void setLocationTitle(String locationTitle) {
        LocationTitle = locationTitle;
    }
    public String getLocationAddress1() {
        return LocationAddress1;
    }
    private void setLocationAddress1(String locationAddress1) {
        LocationAddress1 = locationAddress1;
    }

    public String getLocationAddress2() {
        return LocationAddress2;
    }
    private void setLocationAddress2(String locationAddress2) {
        LocationAddress2 = locationAddress2; }
    public String getLocationCity() {
        return LocationCity;
    }
    private void setLocationCity(String locationCity) {
        LocationCity = locationCity;
    }
    public String getLocationState() {
        return LocationState;
    }
    private void setLocationState(String locationState) {
        LocationState = locationState;
    }
    public String getLocationCountry() {
        return LocationCountry;
    }
    private void setLocationCountry(String locationCountry) {
        LocationCountry = locationCountry;
    }
    public String getLocationPostalCode() {
        return LocationPostalCode;
    }
    private void setLocationPostalCode(String locationPostalCode) {
        LocationPostalCode = locationPostalCode; }
    public String getLocationLatitude() {
        return LocationLatitude;
    }
    private void setLocationLatitude(String locationLatitude) {
        LocationLatitude = locationLatitude;
    }
    public String getLocationLongitude() {
        return LocationLongitude;
    }
    private void setLocationLongitude(String locationLongitude) {
        LocationLongitude = locationLongitude;
    }
}
