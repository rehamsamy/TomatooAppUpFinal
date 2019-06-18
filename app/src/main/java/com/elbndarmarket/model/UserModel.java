package com.elbndarmarket.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable{

    private int id;
    private String name;
    private String email;
    private String phone;
    private String image;
    private String token;
    private float BalanceAmount;
    private String Country;
    private String City;
    private String Address;
    private String building_no;
    private String floor_no;
    private String apartment_no;
    private String villa_no;
    private String other;


    public UserModel() {
    }

    protected UserModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        image = in.readString();
        token = in.readString();
        BalanceAmount = in.readInt();
        Country = in.readString();
        City = in.readString();
        Address = in.readString();
        building_no = in.readString();
        floor_no = in.readString();
        apartment_no = in.readString();
        villa_no = in.readString();
        other = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public float getBalanceAmount() {
        return BalanceAmount;
    }

    public void setBalanceAmount(float balanceAmount) {
        BalanceAmount = balanceAmount;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getBuilding_no() {
        return building_no;
    }

    public void setBuilding_no(String building_no) {
        this.building_no = building_no;
    }

    public String getFloor_no() {
        return floor_no;
    }

    public void setFloor_no(String floor_no) {
        this.floor_no = floor_no;
    }

    public String getApartment_no() {
        return apartment_no;
    }

    public void setApartment_no(String apartment_no) {
        this.apartment_no = apartment_no;
    }

    public String getVilla_no() {
        return villa_no;
    }

    public void setVilla_no(String villa_no) {
        this.villa_no = villa_no;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(image);
        dest.writeString(token);
        dest.writeFloat(BalanceAmount);
        dest.writeString(Country);
        dest.writeString(City);
        dest.writeString(Address);
        dest.writeString(building_no);
        dest.writeString(floor_no);
        dest.writeString(apartment_no);
        dest.writeString(villa_no);
        dest.writeString(other);
    }
}
