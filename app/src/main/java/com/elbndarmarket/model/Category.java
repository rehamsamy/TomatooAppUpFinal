package com.elbndarmarket.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Category implements Parcelable {
    @SerializedName("Categoryid")
    private int category_id;
    @SerializedName("category_name_en")
    private String category_name_en;
    @SerializedName("category_name_ar")
    private String category_name_ar;
    @SerializedName("CategoryTagName")
    private String CategoryTagName;
    @SerializedName("catImage")
    private String CategoryImage;

    public Category(int category_id, String category_name_en, String category_name_ar, String categoryTagName, String categoryImage) {
        this.category_id = category_id;
        this.category_name_en = category_name_en;
        this.category_name_ar = category_name_ar;
        CategoryTagName = categoryTagName;
        CategoryImage = categoryImage;
    }

    protected Category(Parcel in) {
        category_id = in.readInt();
        category_name_en = in.readString();
        category_name_ar = in.readString();
        CategoryTagName = in.readString();
        CategoryImage = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name_en() {
        return category_name_en;
    }

    public void setCategory_name_en(String category_name_en) {
        this.category_name_en = category_name_en;
    }

    public String getCategory_name_ar() {
        return category_name_ar;
    }

    public void setCategory_name_ar(String category_name_ar) {
        this.category_name_ar = category_name_ar;
    }

    public String getCategoryTagName() {
        return CategoryTagName;
    }

    public void setCategoryTagName(String categoryTagName) {
        CategoryTagName = categoryTagName;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(category_id);
        dest.writeString(category_name_en);
        dest.writeString(category_name_ar);
        dest.writeString(CategoryTagName);
        dest.writeString(CategoryImage);
    }
}
