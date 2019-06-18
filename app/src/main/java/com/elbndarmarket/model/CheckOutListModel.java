package com.elbndarmarket.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckOutListModel implements Parcelable {

    private int Product_id;
    private int quntity;
    private float Price;

    public CheckOutListModel(int product_id, int product_Qty, float product_price) {
        this.Product_id = product_id;
        this.quntity = product_Qty;
        this.Price = product_price;
    }


    protected CheckOutListModel(Parcel in) {
        Product_id = in.readInt();
        quntity = in.readInt();
        Price = in.readFloat();
    }

    public static final Creator<CheckOutListModel> CREATOR = new Creator<CheckOutListModel>() {
        @Override
        public CheckOutListModel createFromParcel(Parcel in) {
            return new CheckOutListModel(in);
        }

        @Override
        public CheckOutListModel[] newArray(int size) {
            return new CheckOutListModel[size];
        }
    };

    public int getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(int product_id) {
        this.Product_id = product_id;
    }

    public int getProduct_Qty() {
        return quntity;
    }

    public void setProduct_Qty(int product_Qty) {
        this.quntity = product_Qty;
    }

    public float getProduct_price() {
        return Price;
    }

    public void setProduct_price(float product_price) {
        this.Price = product_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Product_id);
        dest.writeInt(quntity);
        dest.writeFloat(Price);
    }
}
