package com.elbndarmarket.model;

import com.google.gson.annotations.SerializedName;

public class AddToCartResponseModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("cart_id")
    private int cart_id;
    @SerializedName("messages")
    private String messages;


    // Getters ....
    public Boolean getStatus() {
        return status;
    }

    public int getCart_id() {
        return cart_id;
    }

    public String getMessages() {
        return messages;
    }
}
