package com.elbndarmarket.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationsModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("notfication")
    private List<NotificationData> notfication;


    public Boolean getStatus() {
        return status;
    }

    public List<NotificationData> getNotfication() {
        return notfication;
    }

    // Class Notification Data
    public static class NotificationData {

        @SerializedName("id")
        private String id;
        @SerializedName("type")
        private String type;
        @SerializedName("data")
        private String data;
        @SerializedName("notifiable_id")
        private String notifiable_id;
        @SerializedName("notifiable_type")
        private String notifiable_type;
        @SerializedName("created_at")
        private String created_at;


        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getData() {
            return data;
        }

        public String getNotifiable_id() {
            return notifiable_id;
        }

        public String getNotifiable_type() {
            return notifiable_type;
        }

        public String getCreated_at() {
            return created_at;
        }
    }
}
