package com.elbndarmarket.model;

public class AddOrderModel {

    private int product_id;
    private int category_id;
    private int subCategory_id;
    private int brand_id;
    private String product_name_ar;
    private String product_name_en;
    private String photo;
    private int unit_price;
    private int item_num;


    public AddOrderModel(int product_id, int category_id, int subCategory_id, int brand_id, String product_name_ar, String product_name_en, String photo, int unit_price, int item_num) {
        this.product_id = product_id;
        this.category_id = category_id;
        this.subCategory_id = subCategory_id;
        this.brand_id = brand_id;
        this.product_name_ar = product_name_ar;
        this.product_name_en = product_name_en;
        this.photo = photo;
        this.unit_price = unit_price;
        this.item_num = item_num;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getSubCategory_id() {
        return subCategory_id;
    }

    public void setSubCategory_id(int subCategory_id) {
        this.subCategory_id = subCategory_id;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getProduct_name_ar() {
        return product_name_ar;
    }

    public void setProduct_name_ar(String product_name_ar) {
        this.product_name_ar = product_name_ar;
    }

    public String getProduct_name_en() {
        return product_name_en;
    }

    public void setProduct_name_en(String product_name_en) {
        this.product_name_en = product_name_en;
    }

    public int getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }

    public int getItem_num() {
        return item_num;
    }

    public void setItem_num(int item_num) {
        this.item_num = item_num;
    }
}
