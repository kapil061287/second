package com.depex.okeyclick.user.model;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.Date;


public class ServiceHistory  {

    @SerializedName("id")
    private String id;
    @SerializedName("task_name")
    private String taskName;
    @SerializedName("task_key")
    private String taskKey;
    @SerializedName("status")
    private String status;
    @SerializedName("created_date")
    private Date createdByDate;

    @SerializedName("is_scheduler")
    private String isScheduler;

    @SerializedName("category")
    private String category;

    @SerializedName("category_icon")
    private String categoryIcon;

    @SerializedName("sp_name")
    private String spName;

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getSpName() {
        return spName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public Date getCreatedByDate() {
        return createdByDate;
    }

    public void setCreatedByDate(Date createdByDate) {
        this.createdByDate = createdByDate;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceHistory that = (ServiceHistory) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (taskName != null ? !taskName.equals(that.taskName) : that.taskName != null)
            return false;
        if (taskKey != null ? !taskKey.equals(that.taskKey) : that.taskKey != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (createdByDate != null ? !createdByDate.equals(that.createdByDate) : that.createdByDate != null)
            return false;
        if (isScheduler != null ? !isScheduler.equals(that.isScheduler) : that.isScheduler != null)
            return false;
        if (category != null ? !category.equals(that.category) : that.category != null)
            return false;
        if (categoryIcon != null ? !categoryIcon.equals(that.categoryIcon) : that.categoryIcon != null)
            return false;
        return spName != null ? spName.equals(that.spName) : that.spName == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (taskName != null ? taskName.hashCode() : 0);
        result = 31 * result + (taskKey != null ? taskKey.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdByDate != null ? createdByDate.hashCode() : 0);
        result = 31 * result + (isScheduler != null ? isScheduler.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (categoryIcon != null ? categoryIcon.hashCode() : 0);
        result = 31 * result + (spName != null ? spName.hashCode() : 0);
        return result;
    }

    public static class ServiceHistoryComparator implements Comparator<ServiceHistory> {
        @Override
        public int compare(ServiceHistory serviceHistory, ServiceHistory t1) {
            return (-1)*serviceHistory.getCreatedByDate().compareTo(t1.createdByDate);
        }
    }
}
