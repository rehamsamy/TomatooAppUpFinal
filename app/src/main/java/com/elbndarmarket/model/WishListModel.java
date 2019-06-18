package com.elbndarmarket.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WishListModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("products")
    private List<WishlistItem> products;
    @SerializedName("messages")
    private String messages;

    // Getter ...
    public Boolean getStatus() {
        return status;
    }

    public List<WishlistItem> getProducts() {
        return products;
    }

    public String getMessages() {
        return messages;
    }


    // WishList Item .....
    public static class WishlistItem {
        @SerializedName("wish_id")
        private int wish_id;
        @SerializedName("Product_id")
        private int Product_id;
        @SerializedName("product_name_ar")
        private String product_name_ar;
        @SerializedName("product_name_en")
        private String product_name_en;
        @SerializedName("Price")
        private double Price;
        @SerializedName("photo")
        private String photo;
        @SerializedName("cart")
        private int cart;
        @SerializedName("wishlists")
        private int wishlists;
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


        // Gettter
        public int getWish_id() {
            return wish_id;
        }

        public int getProduct_id() {
            return Product_id;
        }

        public String getProduct_name_ar() {
            return product_name_ar;
        }

        public String getProduct_name_en() {
            return product_name_en;
        }

        public double getPrice() {
            return Price;
        }

        public String getPhoto() {
            return photo;
        }

        public int getCart() {
            return cart;
        }

        public int getWishlists() {
            return wishlists;
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

        public void setCart(int cart) {
            this.cart = cart;
        }
    }
}
