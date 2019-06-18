package com.elbndarmarket.model;

import com.google.gson.annotations.SerializedName;

public class DeleteNotificatiomModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("message")
    private String message;


    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
