package com.techtitans.usman.usmanmart.models;

import java.io.Serializable;

public class Product implements Serializable {
    String productId,sellerId,productTitle,productDes,category;

    String  images;
    String bulletPoints;
    String tags;
    double price;
    int quantity;
    String mainImage;
    float rating=5;
    double sale=0;

    public Product(){

    }
    public Product(String sellerId, String productTitle, String productDes, String category, double price) {
        this.sellerId = sellerId;
        this.productTitle = productTitle;
        this.productDes = productDes;
        this.category = category;
        this.price = price;
    }

    public Product(String sellerId, String productTitle, String productDes, String category, String images, String bulletPoints, String tags, double price,int quantity) {
        this.sellerId = sellerId;
        this.productTitle = productTitle;
        this.productDes = productDes;
        this.category = category;
        this.images = images;
        this.bulletPoints = bulletPoints;
        this.tags = tags;
        this.price = price;
        this.quantity=quantity;
    }

    public Product(String productId, String productTitle, double price, int quantity,String images) {
        this.productId = productId;
        this.productTitle = productTitle;
        this.price = price;
        this.images = images;
        this.quantity=quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductDes() {
        return productDes;
    }

    public void setProductDes(String productDes) {
        this.productDes = productDes;
    }

    public String getImages() {
        return images;
    }


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getBulletPoints() {
        return bulletPoints;
    }

    public void setBulletPoints(String bulletPoints) {
        this.bulletPoints = bulletPoints;
    }

    public String getMainImage(){
        if(images.isEmpty())
            return "";
        else if (images.contains("&&")) {
            String[] arr=images.split("&&");
            return arr[0];
        } else
            return images;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }
}
