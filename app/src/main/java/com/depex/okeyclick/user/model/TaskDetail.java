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

    @SerializedName("payment_status")
    private String paymentStatus;

    @SerializedName("cs_profile")
    private String csProfilePic;

    @SerializedName("cs_id")
    private String csId;

    @SerializedName("sp_id")
    private String spId;

    @SerializedName("payment_method")
    private String paymentMethod;

    @SerializedName("city_tax")
    private String cityTax;

    @SerializedName("subcategory_price")
    private String subCategoryPrice;

    @SerializedName("package_price")
    private String packagePrice;

    @SerializedName("work_duration")
    private String workDuration;

    @SerializedName("base_fare")
    private String baseFare;

    @SerializedName("subtotal")
    private String subTotal;

    @SerializedName("admin_commission")
    private String adminCommission;

    @SerializedName("total")
    private String total;

    @SerializedName("charge_id")
    private String chargeId;

    @SerializedName("transaction_id")
    private String transactionId;







    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getSpId() {
        return spId;
    }

    public String getCsId() {
        return csId;
    }

    public void setCsId(String csId) {
        this.csId = csId;
    }



    public String getCsProfilePic() {
        return csProfilePic;
    }

    public void setCsProfilePic(String csProfilePic) {
        this.csProfilePic = csProfilePic;
    }

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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCityTax() {
        return cityTax;
    }

    public void setCityTax(String cityTax) {
        this.cityTax = cityTax;
    }

    public String getSubCategoryPrice() {
        return subCategoryPrice;
    }

    public void setSubCategoryPrice(String subCategoryPrice) {
        this.subCategoryPrice = subCategoryPrice;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(String workDuration) {
        this.workDuration = workDuration;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getAdminCommission() {
        return adminCommission;
    }

    public void setAdminCommission(String adminCommission) {
        this.adminCommission = adminCommission;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskDetail detail = (TaskDetail) o;

        if (taskId != null ? !taskId.equals(detail.taskId) : detail.taskId != null) return false;
        if (taskKey != null ? !taskKey.equals(detail.taskKey) : detail.taskKey != null)
            return false;
        if (createdDate != null ? !createdDate.equals(detail.createdDate) : detail.createdDate != null)
            return false;
        if (acceptedDate != null ? !acceptedDate.equals(detail.acceptedDate) : detail.acceptedDate != null)
            return false;
        if (startDate != null ? !startDate.equals(detail.startDate) : detail.startDate != null)
            return false;
        if (status != null ? !status.equals(detail.status) : detail.status != null) return false;
        if (category != null ? !category.equals(detail.category) : detail.category != null)
            return false;
        if (subCategory != null ? !subCategory.equals(detail.subCategory) : detail.subCategory != null)
            return false;
        if (packageName != null ? !packageName.equals(detail.packageName) : detail.packageName != null)
            return false;
        if (csName != null ? !csName.equals(detail.csName) : detail.csName != null) return false;
        if (csPhone != null ? !csPhone.equals(detail.csPhone) : detail.csPhone != null)
            return false;
        if (csEmail != null ? !csEmail.equals(detail.csEmail) : detail.csEmail != null)
            return false;
        if (csLat != null ? !csLat.equals(detail.csLat) : detail.csLat != null) return false;
        if (csLng != null ? !csLng.equals(detail.csLng) : detail.csLng != null) return false;
        if (csAddress != null ? !csAddress.equals(detail.csAddress) : detail.csAddress != null)
            return false;
        if (spName != null ? !spName.equals(detail.spName) : detail.spName != null) return false;
        if (spPhone != null ? !spPhone.equals(detail.spPhone) : detail.spPhone != null)
            return false;
        if (spEmail != null ? !spEmail.equals(detail.spEmail) : detail.spEmail != null)
            return false;
        if (spLat != null ? !spLat.equals(detail.spLat) : detail.spLat != null) return false;
        if (spLng != null ? !spLng.equals(detail.spLng) : detail.spLng != null) return false;
        if (spAddress != null ? !spAddress.equals(detail.spAddress) : detail.spAddress != null)
            return false;
        if (spProfilePic != null ? !spProfilePic.equals(detail.spProfilePic) : detail.spProfilePic != null)
            return false;
        if (paymentStatus != null ? !paymentStatus.equals(detail.paymentStatus) : detail.paymentStatus != null)
            return false;
        if (csProfilePic != null ? !csProfilePic.equals(detail.csProfilePic) : detail.csProfilePic != null)
            return false;
        if (csId != null ? !csId.equals(detail.csId) : detail.csId != null) return false;
        if (spId != null ? !spId.equals(detail.spId) : detail.spId != null) return false;
        if (paymentMethod != null ? !paymentMethod.equals(detail.paymentMethod) : detail.paymentMethod != null)
            return false;
        if (cityTax != null ? !cityTax.equals(detail.cityTax) : detail.cityTax != null)
            return false;
        if (subCategoryPrice != null ? !subCategoryPrice.equals(detail.subCategoryPrice) : detail.subCategoryPrice != null)
            return false;
        if (packagePrice != null ? !packagePrice.equals(detail.packagePrice) : detail.packagePrice != null)
            return false;
        if (workDuration != null ? !workDuration.equals(detail.workDuration) : detail.workDuration != null)
            return false;
        if (baseFare != null ? !baseFare.equals(detail.baseFare) : detail.baseFare != null)
            return false;
        if (subTotal != null ? !subTotal.equals(detail.subTotal) : detail.subTotal != null)
            return false;
        if (adminCommission != null ? !adminCommission.equals(detail.adminCommission) : detail.adminCommission != null)
            return false;
        if (total != null ? !total.equals(detail.total) : detail.total != null) return false;
        if (chargeId != null ? !chargeId.equals(detail.chargeId) : detail.chargeId != null)
            return false;
        return transactionId != null ? transactionId.equals(detail.transactionId) : detail.transactionId == null;
    }

    @Override
    public int hashCode() {
        int result = taskId != null ? taskId.hashCode() : 0;
        result = 31 * result + (taskKey != null ? taskKey.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (acceptedDate != null ? acceptedDate.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (subCategory != null ? subCategory.hashCode() : 0);
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (csName != null ? csName.hashCode() : 0);
        result = 31 * result + (csPhone != null ? csPhone.hashCode() : 0);
        result = 31 * result + (csEmail != null ? csEmail.hashCode() : 0);
        result = 31 * result + (csLat != null ? csLat.hashCode() : 0);
        result = 31 * result + (csLng != null ? csLng.hashCode() : 0);
        result = 31 * result + (csAddress != null ? csAddress.hashCode() : 0);
        result = 31 * result + (spName != null ? spName.hashCode() : 0);
        result = 31 * result + (spPhone != null ? spPhone.hashCode() : 0);
        result = 31 * result + (spEmail != null ? spEmail.hashCode() : 0);
        result = 31 * result + (spLat != null ? spLat.hashCode() : 0);
        result = 31 * result + (spLng != null ? spLng.hashCode() : 0);
        result = 31 * result + (spAddress != null ? spAddress.hashCode() : 0);
        result = 31 * result + (spProfilePic != null ? spProfilePic.hashCode() : 0);
        result = 31 * result + (paymentStatus != null ? paymentStatus.hashCode() : 0);
        result = 31 * result + (csProfilePic != null ? csProfilePic.hashCode() : 0);
        result = 31 * result + (csId != null ? csId.hashCode() : 0);
        result = 31 * result + (spId != null ? spId.hashCode() : 0);
        result = 31 * result + (paymentMethod != null ? paymentMethod.hashCode() : 0);
        result = 31 * result + (cityTax != null ? cityTax.hashCode() : 0);
        result = 31 * result + (subCategoryPrice != null ? subCategoryPrice.hashCode() : 0);
        result = 31 * result + (packagePrice != null ? packagePrice.hashCode() : 0);
        result = 31 * result + (workDuration != null ? workDuration.hashCode() : 0);
        result = 31 * result + (baseFare != null ? baseFare.hashCode() : 0);
        result = 31 * result + (subTotal != null ? subTotal.hashCode() : 0);
        result = 31 * result + (adminCommission != null ? adminCommission.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (chargeId != null ? chargeId.hashCode() : 0);
        result = 31 * result + (transactionId != null ? transactionId.hashCode() : 0);
        return result;
    }


}