package com.depex.okeyclick.user.model;

import com.google.gson.annotations.SerializedName;



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
    private String createdByDate;


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

    public String getCreatedByDate() {
        return createdByDate;
    }

    public void setCreatedByDate(String createdByDate) {
        this.createdByDate = createdByDate;
    }
}
