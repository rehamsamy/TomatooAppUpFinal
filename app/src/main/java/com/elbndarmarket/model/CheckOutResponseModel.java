package com.elbndarmarket.model;

import com.google.gson.annotations.SerializedName;

public class CheckOutResponseModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("order_id")
    private int order_id;


    public Boolean getStatus() {
        return status;
    }

    public int getOrder_id() {
        return order_id;
    }
}
