package com.absolutezero.watsights.Models;

public class Spam {
    int id;
    long messageId;

    public Spam(int id, long messageId) {
        this.id = id;
        this.messageId = messageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
}
