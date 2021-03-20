package com.absolutezero.watsights.ChatBot;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("response")
    String response;

    public Response(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
