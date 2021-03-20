package com.absolutezero.watsights.ChatBot;

import com.google.gson.annotations.SerializedName;

public class Request {
    @SerializedName("command")
    String command;

    public Request(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
