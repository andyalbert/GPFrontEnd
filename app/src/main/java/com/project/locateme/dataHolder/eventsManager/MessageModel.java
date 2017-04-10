package com.project.locateme.dataHolder.eventsManager;

import java.util.Date;

public class MessageModel {

    private String messageText;
    private String messageUser;
    private String userId;
    private long messageTime;



    public MessageModel(String messageText, String messageUser, String userId) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.userId = userId;
        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public MessageModel(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}