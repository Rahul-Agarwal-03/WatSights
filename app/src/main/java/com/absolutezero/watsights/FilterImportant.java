package com.absolutezero.watsights;

import android.webkit.URLUtil;

public class FilterImportant {
    String message;
    String[] impWords = {
            "deadline",
            "death",
            "important",
            "internship",
            "internships",
            "meet",
            "meets",
            "meeting",
            "meetings",
            "project",
            "salary",
            "submission",
            "submit",
            "911",
            "emergency",
            "task"};
    public FilterImportant(String message) {
        this.message = message;
    }

    public boolean isImportant() {
        message = message.toLowerCase().trim();
        if (containsWord() || containsNumber() || containsLink() || containsNumber() || isLengthy() || containsRedDots()) {
            return true;
        }
        return false;
    }

    boolean containsWord() {
        for (String word : impWords) {
            if (message.contains(word)) {
                return true;
            }
            else
                continue;
        }
        return false;
    }

    boolean containsLink(){
        return message.matches("(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})");
    }
    boolean containsNumber(){
        if(message.matches("^.*([0-9]{10})+.*$") || message.matches("^.*\\+([0-9]{12})+$") || message.matches("^.*\\+([0-9]{12})+.*[a-zA-Z]+$"))
            return true;
        return false;
    }
    boolean isLengthy(){
        return message.length() >= 500;
    }
    boolean containsRedDots(){
        return (message.contains("\u200B")||message.contains("\uD83D\uDED1"));
    }
}
