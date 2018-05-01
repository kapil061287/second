package com.depex.okeyclick.user.model;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.Date;

public class PaymentHistoryModal {

    @SerializedName("id")
    private String taskId;

    @SerializedName("task_name")
    private String taskName;

    @SerializedName("sp_name")
    private String spName;

    @SerializedName("city")
    private String city;

    @SerializedName("task_key")
    private String taskKey;

    @SerializedName("status")
    private String status;

    @SerializedName("created_date")
    private Date taskDate;

    @SerializedName("is_Scheduler")
    private String isScheduler;

    @SerializedName("category")
    private String category;

    @SerializedName("category_icon")
    private String categoryIcon;

    @SerializedName("work_duration")
    private String workDuration;

    @SerializedName("payment_method")
    private String paymentMethod;

    @SerializedName("payment_status")
    private String paymentStatus;

    @SerializedName("subcategory_price")
    private String subCategoryPrice;

    @SerializedName("city_tax")
    private String cityTax;

    @SerializedName("base_fare")
    private String baseFare;

    @SerializedName("subtotal")
    private String subTotal;

    @SerializedName("total")
    private String total;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public String getIsScheduler() {
        return isScheduler;
    }

    public void setIsScheduler(String isScheduler) {
        this.isScheduler = isScheduler;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(String workDuration) {
        this.workDuration = workDuration;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getSubCategoryPrice() {
        return subCategoryPrice;
    }

    public void setSubCategoryPrice(String subCategoryPrice) {
        this.subCategoryPrice = subCategoryPrice;
    }

    public String getCityTax() {
        return cityTax;
    }

    public void setCityTax(String cityTax) {
        this.cityTax = cityTax;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public static class PaymentHistoryModalComparator implements Comparator<PaymentHistoryModal>{
        @Override
        public int compare(PaymentHistoryModal paymentHistoryModal, PaymentHistoryModal t1) {
            return (t1.getTaskDate().compareTo(paymentHistoryModal.getTaskDate()));
        }

    }
}
