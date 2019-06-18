package com.elbndarmarket.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserOrderModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("0")
    private List<OrderData> order;
    @SerializedName("message")
    private String message;


    public Boolean getStatus() {
        return status;
    }

    public List<OrderData> getOrder() {
        return order;
    }

    public String getMessage() {
        return message;
    }


    // Class OrderData
    public static class OrderData implements Parcelable {
        @SerializedName("Order_id")
        private int Order_id;
        @SerializedName("Order_User_id")
        private int Order_User_id;
        @SerializedName("ItemsNo")
        private int ItemsNo;
        @SerializedName("Order_Status")
        private String Order_Status;
        @SerializedName("TotalCost")
        private int TotalCost;
        @SerializedName("TotalShippingCost")
        private int TotalShippingCost;
        @SerializedName("PayMethod")
        private String PayMethod;
        @SerializedName("Order_created_at")
        private String Order_created_at;
        @SerializedName("Order_updated_at")
        private String Order_updated_at;


        protected OrderData(Parcel in) {
            Order_id = in.readInt();
            Order_User_id = in.readInt();
            ItemsNo = in.readInt();
            Order_Status = in.readString();
            TotalCost = in.readInt();
            TotalShippingCost = in.readInt();
            PayMethod = in.readString();
            Order_created_at = in.readString();
            Order_updated_at = in.readString();
        }

        public static final Creator<OrderData> CREATOR = new Creator<OrderData>() {
            @Override
            public OrderData createFromParcel(Parcel in) {
                return new OrderData(in);
            }

            @Override
            public OrderData[] newArray(int size) {
                return new OrderData[size];
            }
        };

        // Getter ...
        public int getOrder_id() {
            return Order_id;
        }

        public int getOrder_User_id() {
            return Order_User_id;
        }

        public int getItemsNo() {
            return ItemsNo;
        }

        public String getOrder_Status() {
            return Order_Status;
        }

        public int getTotalCost() {
            return TotalCost;
        }

        public int getTotalShippingCost() {
            return TotalShippingCost;
        }

        public String getPayMethod() {
            return PayMethod;
        }

        public String getOrder_created_at() {
            return Order_created_at;
        }

        public String getOrder_updated_at() {
            return Order_updated_at;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(Order_id);
            dest.writeInt(Order_User_id);
            dest.writeInt(ItemsNo);
            dest.writeString(Order_Status);
            dest.writeInt(TotalCost);
            dest.writeInt(TotalShippingCost);
            dest.writeString(PayMethod);
            dest.writeString(Order_created_at);
            dest.writeString(Order_updated_at);
        }
    }
}
