package com.elbndarmarket.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegisterationModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("messages")
    @Expose
    private Errors messages;


    // Getters and Setters
    public Boolean getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public Errors getMessages() {
        return messages;
    }


    // ------------ USER Class -------------
    public static class User implements Parcelable {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("Mobile")
        @Expose
        private String Mobile;
        @SerializedName("user_image")
        @Expose
        private String user_image;


        protected User(Parcel in) {
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readInt();
            }
            username = in.readString();
            email = in.readString();
            Mobile = in.readString();
            user_image = in.readString();
        }

        public static final Creator<User> CREATOR = new Creator<User>() {
            @Override
            public User createFromParcel(Parcel in) {
                return new User(in);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };

        // Getters
        public Integer getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getMobile() {
            return Mobile;
        }

        public String getUser_image() {
            return user_image;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(id);
            }
            dest.writeString(username);
            dest.writeString(email);
            dest.writeString(Mobile);
            dest.writeString(user_image);
        }
    }


    // ------------ Error Class -------------
    public static class Errors {
        @SerializedName("username")
        @Expose
        private List<String> username = null;
        @SerializedName("email")
        @Expose
        private List<String> email = null;
        @SerializedName("Mobile")
        @Expose
        private List<String> Mobile = null;

        // Getters
        public List<String> getUsername() {
            return username;
        }

        public List<String> getEmail() {
            return email;
        }

        public List<String> getMobile() {
            return Mobile;
        }
    }
}
