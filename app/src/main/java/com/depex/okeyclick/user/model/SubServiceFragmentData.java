package com.depex.okeyclick.user.model;


import java.util.List;

public class SubServiceFragmentData {

    private String serviceName;
    private String serviceImgUrl;
    private List<SubService> subCategories;

    public String getServiceImgUrl() {
        return serviceImgUrl;
    }

    public void setServiceImgUrl(String serviceImgUrl) {
        this.serviceImgUrl = serviceImgUrl;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public List<SubService> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubService> subCategories) {
        this.subCategories = subCategories;
    }
}