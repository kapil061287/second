package com.depex.okeyclick.user.model;
import com.google.gson.annotations.SerializedName;
public class SubService {
    @SerializedName("service_name")
    private String subServiceName;
    @SerializedName("id")
    private String id;
    @SerializedName("s_id")
    private String serviceId;
    @SerializedName("subcategory_image")
    private String subServiceUrl;
    @SerializedName("description")
    private String description;

    public String getSubServiceName() {
        return subServiceName;
    }

    public void setSubServiceName(String subServiceName) {
        this.subServiceName = subServiceName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSubServiceUrl() {
        return subServiceUrl;
    }

    public void setSubServiceUrl(String subServiceUrl) {
        this.subServiceUrl = subServiceUrl;
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

        SubService service = (SubService) o;

        if (getSubServiceName() != null ? !getSubServiceName().equals(service.getSubServiceName()) : service.getSubServiceName() != null)
            return false;
        if (getId() != null ? !getId().equals(service.getId()) : service.getId() != null)
            return false;
        if (getServiceId() != null ? !getServiceId().equals(service.getServiceId()) : service.getServiceId() != null)
            return false;
        if (getSubServiceUrl() != null ? !getSubServiceUrl().equals(service.getSubServiceUrl()) : service.getSubServiceUrl() != null)
            return false;
        return getDescription() != null ? getDescription().equals(service.getDescription()) : service.getDescription() == null;
    }

    @Override
    public int hashCode() {
        int result = getSubServiceName() != null ? getSubServiceName().hashCode() : 0;
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (getServiceId() != null ? getServiceId().hashCode() : 0);
        result = 31 * result + (getSubServiceUrl() != null ? getSubServiceUrl().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        return result;
    }
}