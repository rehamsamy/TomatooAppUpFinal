package com.elbndarmarket.model;

import com.google.gson.annotations.SerializedName;

public class DeleteCartModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("messages")
    private String messages;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
