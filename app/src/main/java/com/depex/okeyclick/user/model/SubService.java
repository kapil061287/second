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
    @SerializedName("min_hr_price")
    private String min_hr_price;
    @SerializedName("discount_price")
    private String discount_price;
    @SerializedName("max_limit")
    private int maxLimit;
    @SerializedName("service_type")
    private String serviceType;



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

    public String getMin_hr_price() {
        return min_hr_price;
    }

    public void setMin_hr_price(String min_hr_price) {
        this.min_hr_price = min_hr_price;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubService that = (SubService) o;

        if (maxLimit != that.maxLimit) return false;
        if (subServiceName != null ? !subServiceName.equals(that.subServiceName) : that.subServiceName != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (serviceId != null ? !serviceId.equals(that.serviceId) : that.serviceId != null)
            return false;
        if (subServiceUrl != null ? !subServiceUrl.equals(that.subServiceUrl) : that.subServiceUrl != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (min_hr_price != null ? !min_hr_price.equals(that.min_hr_price) : that.min_hr_price != null)
            return false;
        if (discount_price != null ? !discount_price.equals(that.discount_price) : that.discount_price != null)
            return false;
        return serviceType != null ? serviceType.equals(that.serviceType) : that.serviceType == null;
    }

    @Override
    public int hashCode() {
        int result = subServiceName != null ? subServiceName.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (serviceId != null ? serviceId.hashCode() : 0);
        result = 31 * result + (subServiceUrl != null ? subServiceUrl.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (min_hr_price != null ? min_hr_price.hashCode() : 0);
        result = 31 * result + (discount_price != null ? discount_price.hashCode() : 0);
        result = 31 * result + maxLimit;
        result = 31 * result + (serviceType != null ? serviceType.hashCode() : 0);


        return result;
    }
}