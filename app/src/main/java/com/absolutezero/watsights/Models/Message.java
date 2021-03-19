package com.absolutezero.watsights.Models;

public class Message {
    int id;
    String message;
    long group_id;
    long person_id;
    String timestamp;

    public Message(int id, String message, long group_id, long person_id, String timestamp) {
        this.id = id;
        this.message = message;
        this.group_id = group_id;
        this.person_id = person_id;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public long getGroup_id() {
        return group_id;
    }

    public long getPerson_id() {
        return person_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public void setPerson_id(long person_id) {
        this.person_id = person_id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
