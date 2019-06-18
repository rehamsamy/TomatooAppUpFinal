package com.elbndarmarket.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeaturedProductsModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("products")
    private List<ProductData> products;
    @SerializedName("message")
    private String message;


    public Boolean getStatus() {
        return status;
    }

    public List<ProductData> getProducts() {
        return products;
    }

    public String getMessage() {
        return message;
    }



    // Class Poduct
    public static class ProductData  implements Parcelable{
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
        private String Price;
        @SerializedName("photo")
        private String photo;
        @SerializedName("category_name_ar")
        private String category_name_ar;
        @SerializedName("category_name_en")
        private String category_name_en;
        @SerializedName("brand_name_ar")
        private String brand_name_ar;
        @SerializedName("brand_name_en")
        private String brand_name_en;
        @SerializedName("sub_category_name_en")
        private String sub_category_name_en;
        @SerializedName("sub_category_name_ar")
        private String sub_category_name_ar;
        @SerializedName("wishlists")
        private int wishlists;
        @SerializedName("cart")
        private int cart;


        protected ProductData(Parcel in) {
            Product_id = in.readInt();
            Categoryid = in.readInt();
            SubCategoryid = in.readInt();
            Brand_id = in.readInt();
            product_name_ar = in.readString();
            product_name_en = in.readString();
            Price = in.readString();
            photo = in.readString();
            category_name_ar = in.readString();
            category_name_en = in.readString();
            brand_name_ar = in.readString();
            brand_name_en = in.readString();
            sub_category_name_en = in.readString();
            sub_category_name_ar = in.readString();
            wishlists = in.readInt();
            cart = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(Product_id);
            dest.writeInt(Categoryid);
            dest.writeInt(SubCategoryid);
            dest.writeInt(Brand_id);
            dest.writeString(product_name_ar);
            dest.writeString(product_name_en);
            dest.writeString(Price);
            dest.writeString(photo);
            dest.writeString(category_name_ar);
            dest.writeString(category_name_en);
            dest.writeString(brand_name_ar);
            dest.writeString(brand_name_en);
            dest.writeString(sub_category_name_en);
            dest.writeString(sub_category_name_ar);
            dest.writeInt(wishlists);
            dest.writeInt(cart);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ProductData> CREATOR = new Creator<ProductData>() {
            @Override
            public ProductData createFromParcel(Parcel in) {
                return new ProductData(in);
            }

            @Override
            public ProductData[] newArray(int size) {
                return new ProductData[size];
            }
        };

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

        public String getPrice() {
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

        public String getSub_category_name_en() {
            return sub_category_name_en;
        }

        public String getSub_category_name_ar() {
            return sub_category_name_ar;
        }

        public int getWishlists() {
            return wishlists;
        }

        public void setWishlists(int wishlists) {
            this.wishlists = wishlists;
        }

        public int getCart() {
            return cart;
        }

        public void setCart(int cart) {
            this.cart = cart;
        }
    }
}
