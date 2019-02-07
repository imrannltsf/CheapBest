package com.cheapestbest.androidapp.adpterUtills;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SaveCoupanHelper {
    private String CoupanID;
    private String CoupanTitle;
    private String CoupanCode;
    private String CoupanImage;
    private String CoupanCover;
    private String CoupanDescription;
    private String CoupanStatus;
    private String CoupanOriginalPrice;
    private String CoupanDiscount;
    private String CoupanDiscountUnit;
    private String CoupanStartDate;
    private String CoupanEndDate;
    private String CoupanAppliedToAll;
    private String CoupanSummery;
    private String CoupanVendorName;
    private String CoupanVendorLogo;
    private JSONArray jsonArray;
  /*  private String StrVendorName;
    private String StrVendorLogo;*/



    /* public SaveCoupanHelper(String coupanID, String coupanTitle, String coupanCode, String coupanImage, String coupanDescription, String coupanStatus, String coupanOriginalPrice, String coupanDiscount, String coupanDiscountUnit, String coupanStartDate, String coupanEndDate, String coupanAppliedToAll, String coupanSummery, String coupanVendorName, String coupanVendorLogo, JSONArray jsonArray, int lengthOfLocation) {
        CoupanID = coupanID;
        CoupanTitle = coupanTitle;
        CoupanCode = coupanCode;
        CoupanImage = coupanImage;
        CoupanDescription = coupanDescription;
        CoupanStatus = coupanStatus;
        CoupanOriginalPrice = coupanOriginalPrice;
        CoupanDiscount = coupanDiscount;
        CoupanDiscountUnit = coupanDiscountUnit;
        CoupanStartDate = coupanStartDate;
        CoupanEndDate = coupanEndDate;
        CoupanAppliedToAll = coupanAppliedToAll;
        CoupanSummery = coupanSummery;
        CoupanVendorName = coupanVendorName;
        CoupanVendorLogo = coupanVendorLogo;
        this.jsonArray = jsonArray;
        LengthOfLocation = lengthOfLocation;
    }*/

    public SaveCoupanHelper(JSONObject coupans) {
     /*   CoupanID = coupanID;
        CoupanTitle = coupanTitle;
        CoupanCode = coupanCode;
        CoupanImage = coupanImage;
        CoupanDescription = coupanDescription;
        CoupanStatus = coupanStatus;
        CoupanOriginalPrice = coupanOriginalPrice;
        CoupanDiscount = coupanDiscount;
        CoupanDiscountUnit = coupanDiscountUnit;
        CoupanStartDate = coupanStartDate;
        CoupanEndDate = coupanEndDate;
        CoupanAppliedToAll = coupanAppliedToAll;
        CoupanSummery = coupanSummery;
        CoupanVendorName = coupanVendorName;
        CoupanVendorLogo = coupanVendorLogo;
        this.jsonArray = jsonArray;
        LengthOfLocation = lengthOfLocation;*/


        try {
            CoupanID = coupans.getString("id");
            CoupanTitle = coupans.getString("title");
            CoupanCode = coupans.getString("code");
            CoupanImage = coupans.getString("thumb");
            CoupanCover = coupans.getString("cover");
            CoupanDescription = coupans.getString("description");
            CoupanStatus = coupans.getString("status");
            CoupanOriginalPrice = coupans.getString("original_price");
            CoupanDiscount = coupans.getString("discount");
            CoupanDiscountUnit = coupans.getString("discount_unit");
            CoupanStartDate = coupans.getString("start_date");
            CoupanEndDate = coupans.getString("end_date");
            CoupanAppliedToAll = coupans.getString("applied_to_all_locations");
            CoupanSummery = coupans.getString("summary");
            jsonArray = coupans.getJSONArray("locations");
            LengthOfLocation=jsonArray.length();
            JSONObject c = coupans.getJSONObject("vendor");
            CoupanVendorName = c.getString("name");
            CoupanVendorLogo = c.getString("logo");

            setCoupanID(CoupanID);
            setCoupanTitle(CoupanTitle);
            setCoupanCode(CoupanCode);
            setCoupanImage(CoupanImage);
            setCoupanCover(CoupanCover);
            setCoupanDescription(CoupanDescription);
            setCoupanStatus(CoupanStatus);
            setCoupanOriginalPrice(CoupanOriginalPrice);
            setCoupanDiscount(CoupanDiscount);
            setCoupanDiscountUnit(CoupanDiscount);
            setCoupanStartDate(CoupanStartDate);
            setCoupanEndDate(CoupanEndDate);
            setCoupanAppliedToAll(CoupanAppliedToAll);
            setCoupanSummery(CoupanSummery);
            setJsonArray(jsonArray);
            setLengthOfLocation(LengthOfLocation);
            setCoupanVendorName(CoupanVendorName);
            setCoupanVendorLogo(CoupanVendorLogo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getLengthOfLocation() {
        return LengthOfLocation;
    }

    private void setLengthOfLocation(int lengthOfLocation) {
        LengthOfLocation = lengthOfLocation;
    }

    private int LengthOfLocation;



    public JSONArray getJsonArray() {
        return jsonArray;
    }
    private void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
    public String getCoupanID() {
        return CoupanID;
    }
    private void setCoupanID(String coupanID) {
        CoupanID = coupanID;
    }
    public String getCoupanTitle() {
        return CoupanTitle;
    }
    private void setCoupanTitle(String coupanTitle) {
        CoupanTitle = coupanTitle;
    }
    public String getCoupanCode() {
        return CoupanCode;
    }
    private void setCoupanCode(String coupanCode) {
        CoupanCode = coupanCode;
    }
    public String getCoupanImage() {
        return CoupanImage;
    }
    private void setCoupanImage(String coupanImage) {
        CoupanImage = coupanImage;
    }
    public String getCoupanDescription() {
        return CoupanDescription;
    }

    private void setCoupanDescription(String coupanDescription) {
        CoupanDescription = coupanDescription;
    }

    public String getCoupanStatus() {
        return CoupanStatus;
    }
    private void setCoupanStatus(String coupanStatus) {
        CoupanStatus = coupanStatus;
    }
    public String getCoupanOriginalPrice() {
        return CoupanOriginalPrice;
    }
    private void setCoupanOriginalPrice(String coupanOriginalPrice) {
        CoupanOriginalPrice = coupanOriginalPrice;
    }

    public String getCoupanDiscount() {
        return CoupanDiscount;
    }
    private void setCoupanDiscount(String coupanDiscount) {
        CoupanDiscount = coupanDiscount;
    }
    public String getCoupanDiscountUnit() {
        return CoupanDiscountUnit;
    }
    private void setCoupanDiscountUnit(String coupanDiscountUnit) {
        CoupanDiscountUnit = coupanDiscountUnit;
    }

    public String getCoupanStartDate() {
        return CoupanStartDate;
    }
    private void setCoupanStartDate(String coupanStartDate) {
        CoupanStartDate = coupanStartDate;
    }
    public String getCoupanEndDate() {
        return CoupanEndDate;
    }
    private void setCoupanEndDate(String coupanEndDate) {
        CoupanEndDate = coupanEndDate;
    }
    public String getCoupanAppliedToAll() {
        return CoupanAppliedToAll;
    }
    private void setCoupanAppliedToAll(String coupanAppliedToAll) {
        CoupanAppliedToAll = coupanAppliedToAll;
    }

    public String getCoupanSummery() {
        return CoupanSummery;
    }
    private void setCoupanSummery(String coupanSummery) {
        CoupanSummery = coupanSummery;
    }
    public String getCoupanVendorName() {
        return CoupanVendorName;
    }
    private void setCoupanVendorName(String coupanVendorName) {
        CoupanVendorName = coupanVendorName;
    }

    public String getCoupanVendorLogo() {
        return CoupanVendorLogo;
    }
    private void setCoupanVendorLogo(String coupanVendorLogo) {
        CoupanVendorLogo = coupanVendorLogo;
    }
    public String getCoupanCover() {
        return CoupanCover;
    }
    public void setCoupanCover(String coupanCover) {
        CoupanCover = coupanCover;
    }


}
