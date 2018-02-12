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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPackage that = (UserPackage) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getPackageName() != null ? !getPackageName().equals(that.getPackageName()) : that.getPackageName() != null)
            return false;
        if (getPackageImage() != null ? !getPackageImage().equals(that.getPackageImage()) : that.getPackageImage() != null)
            return false;
        if (getPackagePricePerHr() != null ? !getPackagePricePerHr().equals(that.getPackagePricePerHr()) : that.getPackagePricePerHr() != null)
            return false;
        return getPackageDescription() != null ? getPackageDescription().equals(that.getPackageDescription()) : that.getPackageDescription() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getPackageName() != null ? getPackageName().hashCode() : 0);
        result = 31 * result + (getPackageImage() != null ? getPackageImage().hashCode() : 0);
        result = 31 * result + (getPackagePricePerHr() != null ? getPackagePricePerHr().hashCode() : 0);
        result = 31 * result + (getPackageDescription() != null ? getPackageDescription().hashCode() : 0);
        return result;
    }
}
