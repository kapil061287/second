package com.depex.okeyclick.user.model;

import com.google.gson.annotations.SerializedName;

public class UserPackage  {
    @SerializedName("id")
    private String id;
    @SerializedName("pac_name")
    private String packageName;
    @SerializedName("pac_image")
    private String packageImage;
    @SerializedName("pac_price_per_hr")
    private String packagePricePerHr;

    @SerializedName("pac_description")
    private String packageDescription;

    @SerializedName("subcategory")
    private String subCategoryPrice;

    @SerializedName("city_tax")
    private String cityTax;

    @SerializedName("base_fare")
    private String baseFare;

    @SerializedName("total")
    private String total;



    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageImage() {
        return packageImage;
    }

    public void setPackageImage(String packageImage) {
        this.packageImage = packageImage;
    }

    public String getPackagePricePerHr() {
        return packagePricePerHr;
    }

    public void setPackagePricePerHr(String packagePricePerHr) {
        this.packagePricePerHr = packagePricePerHr;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPackage that = (UserPackage) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null)
            return false;
        if (packageImage != null ? !packageImage.equals(that.packageImage) : that.packageImage != null)
            return false;
        if (packagePricePerHr != null ? !packagePricePerHr.equals(that.packagePricePerHr) : that.packagePricePerHr != null)
            return false;
        if (packageDescription != null ? !packageDescription.equals(that.packageDescription) : that.packageDescription != null)
            return false;
        if (subCategoryPrice != null ? !subCategoryPrice.equals(that.subCategoryPrice) : that.subCategoryPrice != null)
            return false;
        if (cityTax != null ? !cityTax.equals(that.cityTax) : that.cityTax != null) return false;
        if (baseFare != null ? !baseFare.equals(that.baseFare) : that.baseFare != null)
            return false;
        return total != null ? total.equals(that.total) : that.total == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (packageImage != null ? packageImage.hashCode() : 0);
        result = 31 * result + (packagePricePerHr != null ? packagePricePerHr.hashCode() : 0);
        result = 31 * result + (packageDescription != null ? packageDescription.hashCode() : 0);
        result = 31 * result + (subCategoryPrice != null ? subCategoryPrice.hashCode() : 0);
        result = 31 * result + (cityTax != null ? cityTax.hashCode() : 0);
        result = 31 * result + (baseFare != null ? baseFare.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        return result;
    }
}
