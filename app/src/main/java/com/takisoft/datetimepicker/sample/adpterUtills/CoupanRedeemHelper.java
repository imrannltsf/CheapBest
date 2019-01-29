package com.takisoft.datetimepicker.sample.adpterUtills;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CoupanRedeemHelper {
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
    private String RedemptionValue;

    public String getRedemptionValue() {
        return RedemptionValue;
    }

    public void setRedemptionValue(String redemptionValue) {
        RedemptionValue = redemptionValue;
    }

    private JSONArray jsonArray;
    private int LengthOfLocation;

    public CoupanRedeemHelper(JSONObject coupans) {
      /*  CoupanID = coupanID;
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
            CoupanSummery= coupans.getString("summary");
            RedemptionValue=coupans.getString("redemptions_count");
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
            setCoupanDiscountUnit(CoupanDiscountUnit);
            setCoupanStartDate(CoupanStartDate);
            setCoupanEndDate(CoupanEndDate);
            setCoupanAppliedToAll(CoupanAppliedToAll);
            setCoupanSummery(CoupanSummery);
            setRedemptionValue(RedemptionValue);
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
