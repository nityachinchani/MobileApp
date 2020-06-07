package com.example.coen268project.Model;

public class MessagesDao {
    private String uid;
    private String messageContent;
    private String name;

    public MessagesDao(String uid, String name, String messageContent)
    {
        this.uid = uid;
        this.messageContent = messageContent;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
