package com.techtitans.usman.usmanmart.models;

import java.io.Serializable;

public class SellerModel implements Serializable {
        String sellerId,userName,Email,Password,storeId,phone,fcmToken;
        public SellerModel(){

        }
        public SellerModel(String sellerId, String userName, String email, String password) {
            this.sellerId = sellerId;
            this.userName = userName;
            Email = email;
            Password = password;
        }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
