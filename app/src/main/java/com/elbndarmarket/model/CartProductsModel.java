package com.elbndarmarket.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.LinearSmoothScroller;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartProductsModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("products")
    private List<Product> products;
    @SerializedName("messages")
    private String messages;

    //Getter...
    public Boolean getStatus() {
        return status;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getMessages() {
        return messages;
    }

    // Product Class ...
    public static class Product implements Parcelable {
        @SerializedName("Product_id")
        private int Product_id;
        @SerializedName("Categoryid")
        private int Categoryid;
        @SerializedName("SubCategoryid")
        private int SubCategoryid;
        @SerializedName("Brand_id")
        private int Brand_id;
        @SerializedName("product_name_ar")
        private String product_name_ar;
        @SerializedName("product_name_en")
        private String product_name_en;
        @SerializedName("Price")
        private float Price;
        @SerializedName("product_desc_ar")
        private String product_desc_ar;
        @SerializedName("product_desc_en")
        private String product_desc_en;
        @SerializedName("category_name_ar")
        private String category_name_ar;
        @SerializedName("category_name_en")
        private String category_name_en;
        @SerializedName("brand_name_ar")
        private String brand_name_ar;
        @SerializedName("brand_name_en")
        private String brand_name_en;
        @SerializedName("photo")
        private String photo;
        @SerializedName("quentity")
        private int quentity;
        @SerializedName("Cart_id")
        private int Cart_id;


        protected Product(Parcel in) {
            Product_id = in.readInt();
            Categoryid = in.readInt();
            SubCategoryid = in.readInt();
            Brand_id = in.readInt();
            product_name_ar = in.readString();
            product_name_en = in.readString();
            Price = in.readFloat();
            product_desc_ar = in.readString();
            product_desc_en = in.readString();
            category_name_ar = in.readString();
            category_name_en = in.readString();
            brand_name_ar = in.readString();
            brand_name_en = in.readString();
            photo = in.readString();
            quentity = in.readInt();
            Cart_id = in.readInt();
        }

        public static final Creator<Product> CREATOR = new Creator<Product>() {
            @Override
            public Product createFromParcel(Parcel in) {
                return new Product(in);
            }

            @Override
            public Product[] newArray(int size) {
                return new Product[size];
            }
        };

        // Getters ...
        public int getProduct_id() {
            return Product_id;
        }

        public int getCategoryid() {
            return Categoryid;
        }

        public int getSubCategoryid() {
            return SubCategoryid;
        }

        public int getBrand_id() {
            return Brand_id;
        }

        public String getProduct_name_ar() {
            return product_name_ar;
        }

        public String getProduct_name_en() {
            return product_name_en;
        }

        public float getPrice() {
            return Price;
        }

        public String getPhoto() {
            return photo;
        }

        public String getCategory_name_ar() {
            return category_name_ar;
        }

        public String getCategory_name_en() {
            return category_name_en;
        }

        public String getBrand_name_ar() {
            return brand_name_ar;
        }

        public String getBrand_name_en() {
            return brand_name_en;
        }

        public String getProduct_desc_ar() {
            return product_desc_ar;
        }

        public String getProduct_desc_en() {
            return product_desc_en;
        }

        public int getQuentity() {
            return quentity;
        }

        public void setQuentity(int quentity) {
            this.quentity = quentity;
        }

        public int getCart_id() {
            return Cart_id;
        }

        public static Creator<Product> getCREATOR() {
            return CREATOR;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(Product_id);
            dest.writeInt(Categoryid);
            dest.writeInt(SubCategoryid);
            dest.writeInt(Brand_id);
            dest.writeString(product_name_ar);
            dest.writeString(product_name_en);
            dest.writeFloat(Price);
            dest.writeString(product_desc_ar);
            dest.writeString(product_desc_en);
            dest.writeString(category_name_ar);
            dest.writeString(category_name_en);
            dest.writeString(brand_name_ar);
            dest.writeString(brand_name_en);
            dest.writeString(photo);
            dest.writeInt(quentity);
            dest.writeInt(Cart_id);
        }
    }
}
