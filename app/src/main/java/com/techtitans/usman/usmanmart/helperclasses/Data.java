package com.techtitans.usman.usmanmart.helperclasses;

public class Data {
    private String title;
    private String body;
    private String senderId;

    public Data(String title, String body, String senderId) {
        this.title = title;
        this.body = body;
        this.senderId = senderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
