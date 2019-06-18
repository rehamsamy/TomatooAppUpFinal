package com.elbndarmarket.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryDataModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("categories")
    private List<Category> categories;
    @SerializedName("message")
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public String getMessage() {
        return message;
    }
}
