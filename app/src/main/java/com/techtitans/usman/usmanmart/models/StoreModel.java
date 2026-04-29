package com.techtitans.usman.usmanmart.models;

import java.io.Serializable;
import java.util.Date;

public class StoreModel implements Serializable {
    private String storeId,sellerId,storeName,storeLogo,storeBanner,description;
    private String category,phone,email,address;
    private boolean isApproved;
    private long createdAt;
    private double rating;
    private int totalProducts;
    private int totalOrders;
    public StoreModel(){

    }

    public StoreModel(String sellerId, String storeName) {
        this.sellerId = sellerId;
        this.storeName = storeName;
    }
    public StoreModel(String storeId, String sellerId, String storeName, String description, String category, String phone, String address, long createdAt, int totalProducts, int totalOrders) {
        this.storeId = storeId;
        this.sellerId = sellerId;
        this.storeName = storeName;
        this.description = description;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
        this.totalProducts = totalProducts;
        this.totalOrders = totalOrders;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getStoreBanner() {
        return storeBanner;
    }

    public void setStoreBanner(String storeBanner) {
        this.storeBanner = storeBanner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

}
