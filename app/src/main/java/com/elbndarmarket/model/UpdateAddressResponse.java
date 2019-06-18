package com.elbndarmarket.model;

import com.google.gson.annotations.SerializedName;

public class UpdateAddressResponse {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("messages")
    private String messages;


    public Boolean getStatus() {
        return status;
    }

    public String getMessages() {
        return messages;
    }
}
