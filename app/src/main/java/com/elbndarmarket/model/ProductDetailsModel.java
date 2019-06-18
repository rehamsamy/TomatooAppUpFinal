package com.elbndarmarket.model;

import com.elbndarmarket.R;
import com.google.gson.annotations.SerializedName;

import butterknife.BindView;

public class ProductDetailsModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("products")
    private DetailProduct products;


    public Boolean getStatus() {
        return status;
    }

    public DetailProduct getProducts() {
        return products;
    }

    // Class DetailProduct
    public class DetailProduct{
        @SerializedName("details")
        private ItemDetail details;
        @SerializedName("images")
        private ItemImages images;

        public ItemDetail getDetails() {
            return details;
        }

        public ItemImages getImages() {
            return images;
        }
    }

    // Class itemDetail
    public class ItemDetail {

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
        @SerializedName("wishlists")
        private int wishlists;


        // Getter ....
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

        public String getProduct_desc_ar() {
            return product_desc_ar;
        }

        public String getProduct_desc_en() {
            return product_desc_en;
        }

        public int getWishlists() {
            return wishlists;
        }

        public void setWishlists(int wishlists) {
            this.wishlists = wishlists;
        }
    }

    // Class itemImages
    public class ItemImages {

        @SerializedName("media_id")
        private int media_id;
        @SerializedName("Product_id")
        private int Product_id;
        @SerializedName("product_image")
        private String product_image;


        // Getter
        public int getMedia_id() {
            return media_id;
        }

        public int getProduct_id() {
            return Product_id;
        }

        public String getProduct_image() {
            return product_image;
        }
    }
}
