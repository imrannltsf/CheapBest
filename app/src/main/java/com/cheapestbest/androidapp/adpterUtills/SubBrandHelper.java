package com.cheapestbest.androidapp.adpterUtills;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubBrandHelper {
    private String ProductID;
    private String ProductTitle;
    private String ProductCode;
    private String ProductImage;
    private String ProductCover;
    private String ProductDescription;
    private String ProductStatus;
    private String ProductOriginalPrice;
    private String ProductDiscount;
    private String ProductDiscountUnit;
    private String ProductStartDate;
    private String ProductEndDate;
    private String ProductAppliedToAll;
    private String ProductSummery;
    private String ProductVendorName;
    private String ProductLimit;
    private String ProductRedemtionsCount;
    private String ProductUserRedeemCount;
    private boolean IsSaved_Coupon;
    private boolean IsUnlimited;
    private JSONArray jsonArrayLocations;

    public String getProductLimit() {
        return ProductLimit;
    }

    public void setProductLimit(String productLimit) {
        ProductLimit = productLimit;
    }

    public boolean isUnlimited() {
        return IsUnlimited;
    }

    public void setUnlimited(boolean unlimited) {
        IsUnlimited = unlimited;
    }

    public SubBrandHelper(JSONObject coupans) {

        try {
            ProductID = coupans.getString("id");
            ProductTitle = coupans.getString("title");
            ProductCode = coupans.getString("code");
            ProductImage = coupans.getString("thumb");
            ProductCover = coupans.getString("cover");
            ProductDescription = coupans.getString("description");
            ProductStatus = coupans.getString("status");
            ProductOriginalPrice = coupans.getString("original_price");
            ProductDiscount = coupans.getString("discount");
            ProductDiscountUnit = coupans.getString("discount_unit");
            ProductStartDate= coupans.getString("start_date");
            ProductEndDate = coupans.getString("end_date");
            ProductAppliedToAll = coupans.getString("applied_to_all_locations");
            ProductSummery = coupans.getString("summary");
            ProductVendorName = coupans.getString("vendor_name");
            ProductLimit=coupans.getString("redemptions_limit_per_user");
            ProductRedemtionsCount=coupans.getString("redemptions_count");
            ProductUserRedeemCount=coupans.getString("user_redeemed_count");
            IsUnlimited=coupans.getBoolean("unlimited");
            IsSaved_Coupon=coupans.getBoolean("saved");
            jsonArrayLocations=coupans.getJSONArray("locations");

            setProductID(ProductID);
            setProductTitle(ProductTitle);
            setProductCode(ProductCode);
            setProductImage(ProductImage);
            setProductCover(ProductCover);
            setProductDescription(ProductDescription);
            setProductStatus(ProductStatus);
            setProductOriginalPrice(ProductOriginalPrice);
            setProductDiscount(ProductDiscount);
            setProductDiscountUnit(ProductDiscountUnit);
            setProductStartDate(ProductStartDate);
            setProductEndDate(ProductEndDate);
            setProductAppliedToAll(ProductAppliedToAll);
            setProductSummery(ProductSummery);
            setProductVendorName(ProductVendorName);
            setProductLimit(ProductLimit);
            setUnlimited(IsUnlimited);
            setSaved_Coupon(IsSaved_Coupon);
            setJsonArrayLocations(jsonArrayLocations);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getProductID() {
        return ProductID;
    }
    private void setProductID(String productID) {
        ProductID = productID;
    }
    public String getProductTitle() {
        return ProductTitle;
    }
    private void setProductTitle(String productTitle) {
        ProductTitle = productTitle;
    }
    public String getProductCode() {
        return ProductCode;
    }
    private void setProductCode(String productCode) {
        ProductCode = productCode;
    }
    public String getProductImage() {
        return ProductImage;
    }
    private void setProductImage(String productImage) {
        ProductImage = productImage;
    }
    public String getProductDescription() {
        return ProductDescription;
    }
    private void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public String getProductStatus() {
        return ProductStatus;
    }
    private void setProductStatus(String productStatus) {
        ProductStatus = productStatus;
    }
    public String getProductOriginalPrice() {
        return ProductOriginalPrice;
    }
    public void setProductOriginalPrice(String productOriginalPrice) {
        ProductOriginalPrice = productOriginalPrice;
    }

    public String getProductDiscount() {
        return ProductDiscount;
    }
    private void setProductDiscount(String productDiscount) {
        ProductDiscount = productDiscount;
    }
    public String getProductDiscountUnit() {
        return ProductDiscountUnit;
    }
    private void setProductDiscountUnit(String productDiscountUnit) {
        ProductDiscountUnit = productDiscountUnit;
    }

    public String getProductStartDate() {
        return ProductStartDate;
    }

    private void setProductStartDate(String productStartDate) {
        ProductStartDate = productStartDate;
    }

    public String getProductEndDate() {
        return ProductEndDate;
    }
    private void setProductEndDate(String productEndDate) {
        ProductEndDate = productEndDate;
    }
    public String getProductAppliedToAll() {
        return ProductAppliedToAll;
    }
    private void setProductAppliedToAll(String productAppliedToAll) {
        ProductAppliedToAll = productAppliedToAll;
    }
    public String getProductSummery() {
        return ProductSummery;
    }
    private void setProductSummery(String productSummery) {
        ProductSummery = productSummery;
    }
    public String getProductVendorName() {
        return ProductVendorName;
    }
    private void setProductVendorName(String productVendorName) {
        ProductVendorName = productVendorName;
    }
    public JSONArray getJsonArrayLocations() {
        return jsonArrayLocations;
    }
    private void setJsonArrayLocations(JSONArray jsonArrayLocations) {
        this.jsonArrayLocations = jsonArrayLocations;
    }
    public String getProductCover() {
        return ProductCover;
    }
    public void setProductCover(String productCover) {
        ProductCover = productCover;
    }


   /* public boolean isCouponSaved() {
        return IsSaved_Coupon;
    }

    public void setCouponSaved(boolean couponSaved) {
        IsSaved_Coupon = couponSaved;
    }*/

    public String getProductRedemtionsCount() {
        return ProductRedemtionsCount;
    }

    public void setProductRedemtionsCount(String productRedemtionsCount) {
        ProductRedemtionsCount = productRedemtionsCount;
    }

    public String getProductUserRedeemCount() {
        return ProductUserRedeemCount;
    }

    public void setProductUserRedeemCount(String productUserRedeemCount) {
        ProductUserRedeemCount = productUserRedeemCount;
    }

    public boolean isSaved_Coupon() {
        return IsSaved_Coupon;
    }

    public void setSaved_Coupon(boolean saved_Coupon) {
        IsSaved_Coupon = saved_Coupon;
    }
}
