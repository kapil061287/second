package com.depex.okeyclick.user.model;


import com.google.gson.annotations.SerializedName;

public class Coupon {

    @SerializedName("coupon_id")
    private String couponId;
    @SerializedName("coupon_title")
    private String couponTitle;
    @SerializedName("coupon_key")
    private String couponKey;
    @SerializedName("discount_type")
    private String discountType;
    @SerializedName("flat_discount")
    private String flatDiscount;
    @SerializedName("from_date")
    private String fromDate;
    @SerializedName("to_date")
    private String toDate;
    @SerializedName("coupon_description")
    private String couponDesc;

    public String getCouponKey() {
        return couponKey;
    }

    public void setCouponKey(String couponKey) {
        this.couponKey = couponKey;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getFlatDiscount() {
        return flatDiscount;
    }

    public void setFlatDiscount(String flatDiscount) {
        this.flatDiscount = flatDiscount;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getCouponDesc() {
        return couponDesc;
    }

    public void setCouponDesc(String couponDesc) {
        this.couponDesc = couponDesc;
    }
}
