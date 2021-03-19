package com.absolutezero.watsights.Models;

public class Group {
    long id;
    String name;
    String last_viewed;
    int icon;
    boolean store_messages;
    int priority;

    public Group(int id, String name, String last_viewed, int icon, boolean store_messages, int priority) {
        this.id = id;
        this.name = name;
        this.last_viewed = last_viewed;
        this.icon = icon;
        this.store_messages = store_messages;
        this.priority = priority;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLast_viewed(String last_viewed) {
        this.last_viewed = last_viewed;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setStore_messages(boolean store_messages) {
        this.store_messages = store_messages;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLast_viewed() {
        return last_viewed;
    }

    public int getIcon() {
        return icon;
    }

    public boolean isStore_messages() {
        return store_messages;
    }

    public int getPriority() {
        return priority;
    }
}
