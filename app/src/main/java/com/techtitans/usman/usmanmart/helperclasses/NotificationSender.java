package com.techtitans.usman.usmanmart.helperclasses;

public class NotificationSender {

    private Message message;

    public NotificationSender(String token, Data data) {
        this.message = new Message(token, data);
    }

    public static class Message {
        private String token;
        private Data data;

        public Message(String token, Data data) {
            this.token = token;
            this.data = data;
        }
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
