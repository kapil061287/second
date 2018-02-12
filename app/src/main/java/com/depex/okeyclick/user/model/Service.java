package com.depex.okeyclick.user.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by we on 1/17/2018.
 */

public class Service implements Serializable{

    @SerializedName("id")
    String id;
    @SerializedName("service_name")
    String serviceName;
    @SerializedName("images")
    String imageUrl;
    @SerializedName("update_date")
    String updateDate;
    @SerializedName("description")
    String description;

    @SerializedName("subcategory")
    List<SubService> subServices;

    public List<SubService> getSubServices() {
        return subServices;
    }

    public void setSubServices(List<SubService> subServices) {
        this.subServices = subServices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Service service = (Service) o;

        if (getId() != null ? !getId().equals(service.getId()) : service.getId() != null)
            return false;
        if (getServiceName() != null ? !getServiceName().equals(service.getServiceName()) : service.getServiceName() != null)
            return false;
        if (getImageUrl() != null ? !getImageUrl().equals(service.getImageUrl()) : service.getImageUrl() != null)
            return false;
        if (getUpdateDate() != null ? !getUpdateDate().equals(service.getUpdateDate()) : service.getUpdateDate() != null)
            return false;
        return getDescription() != null ? getDescription().equals(service.getDescription()) : service.getDescription() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getServiceName() != null ? getServiceName().hashCode() : 0);
        result = 31 * result + (getImageUrl() != null ? getImageUrl().hashCode() : 0);
        result = 31 * result + (getUpdateDate() != null ? getUpdateDate().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        return result;
    }
}
