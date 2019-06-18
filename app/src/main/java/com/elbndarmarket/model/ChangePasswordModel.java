package com.elbndarmarket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("messages")
    @Expose
    private String messages;


    public Boolean getStatus() {
        return status;
    }

    public String getMessages() {
        return messages;
    }
}
