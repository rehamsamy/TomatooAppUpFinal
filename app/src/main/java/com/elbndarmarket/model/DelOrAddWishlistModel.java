package com.elbndarmarket.model;

import com.google.gson.annotations.SerializedName;

public class DelOrAddWishlistModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("messages")
    private String message;


    // Getter ...
    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
