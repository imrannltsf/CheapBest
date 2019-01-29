package com.takisoft.datetimepicker.sample.adpterUtills;

public class DashBoardHelper {
    private int BrandLogo;
    private String ProductName;
    private String ProductOffers;
    private String PriceUnit;
    private String BackUnit;

    public DashBoardHelper(int brandLogo, String productName, String productOffers, String priceUnit, String backUnit) {
        BrandLogo = brandLogo;
        ProductName = productName;
        ProductOffers = productOffers;
        PriceUnit = priceUnit;
        BackUnit = backUnit;
    }

    public int getBrandLogo() {
        return BrandLogo;
    }

    public void setBrandLogo(int brandLogo) {
        BrandLogo = brandLogo;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductOffers() {
        return ProductOffers;
    }

    public void setProductOffers(String productOffers) {
        ProductOffers = productOffers;
    }

    public String getPriceUnit() {
        return PriceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        PriceUnit = priceUnit;
    }

    public String getBackUnit() {
        return BackUnit;
    }

    public void setBackUnit(String backUnit) {
        BackUnit = backUnit;
    }
}
