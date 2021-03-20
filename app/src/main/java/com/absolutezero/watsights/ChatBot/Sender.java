package com.absolutezero.watsights.ChatBot;

public class Sender {
    boolean bot;
    String message;

    public Sender(boolean bot, String message) {
        this.bot = bot;
        this.message = message;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
