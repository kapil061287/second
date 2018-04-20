package com.depex.okeyclick.user.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TaskDetail {
    @SerializedName("task_id")
    private String taskId;
    @SerializedName("task_key")
    private String taskKey;
    @SerializedName("created_date")
    private Date createdDate;
    @SerializedName("accepeted_date")
    private Date acceptedDate;
    @SerializedName("start_date")
    private Date startDate;
    @SerializedName("status")
    private String status;
    @SerializedName("category")
    private String category;
    @SerializedName("subcategory")
    private String subCategory;
    @SerializedName("package")
    private String packageName;
    @SerializedName("cs_name")
    private String csName;
    @SerializedName("cs_phone")
    private String csPhone;
    @SerializedName("cs_email")
    private String csEmail;
    @SerializedName("cs_latitude")
    private String csLat;
    @SerializedName("cs_longitude")
    private String csLng;
    @SerializedName("cs_address")
    private String csAddress;
    @SerializedName("sp_name")
    private String spName;
    @SerializedName("sp_phone")
    private String spPhone;
    @SerializedName("sp_email")
    private String spEmail;
    @SerializedName("sp_latitude")
    private String spLat;
    @SerializedName("sp_longitude")
    private String spLng;
    @SerializedName("sp_address")
    private String spAddress;
    @SerializedName("sp_profile")
    private String spProfilePic;

    @SerializedName("paymentStatus")
    private String paymentStatus;

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(Date acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getCsName() {
        return csName;
    }

    public void setCsName(String csName) {
        this.csName = csName;
    }

    public String getCsPhone() {
        return csPhone;
    }

    public void setCsPhone(String csPhone) {
        this.csPhone = csPhone;
    }

    public String getCsEmail() {
        return csEmail;
    }

    public void setCsEmail(String csEmail) {
        this.csEmail = csEmail;
    }

    public String getCsLat() {
        return csLat;
    }

    public void setCsLat(String csLat) {
        this.csLat = csLat;
    }

    public String getCsLng() {
        return csLng;
    }

    public void setCsLng(String csLng) {
        this.csLng = csLng;
    }

    public String getCsAddress() {
        return csAddress;
    }

    public void setCsAddress(String csAddress) {
        this.csAddress = csAddress;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getSpPhone() {
        return spPhone;
    }

    public void setSpPhone(String spPhone) {
        this.spPhone = spPhone;
    }

    public String getSpEmail() {
        return spEmail;
    }

    public void setSpEmail(String spEmail) {
        this.spEmail = spEmail;
    }

    public String getSpLat() {
        return spLat;
    }

    public void setSpLat(String spLat) {
        this.spLat = spLat;
    }

    public String getSpLng() {
        return spLng;
    }

    public void setSpLng(String spLng) {
        this.spLng = spLng;
    }

    public String getSpAddress() {
        return spAddress;
    }

    public void setSpAddress(String spAddress) {
        this.spAddress = spAddress;
    }

    public String getSpProfilePic() {
        return spProfilePic;
    }

    public void setSpProfilePic(String spProfilePic) {
        this.spProfilePic = spProfilePic;
    }

    public String toJson(){
        return new Gson().toJson(this);
    }

}
