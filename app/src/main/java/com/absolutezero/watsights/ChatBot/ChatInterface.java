package com.absolutezero.watsights.ChatBot;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChatInterface {
    @POST("api/watsights?code=nhu3EYfq3U09YPS2hEeL5cjBTTLAhJZUMCzQ1oGxyHHgXJwJar/0UQ==")
    @Headers({"Content-Type: application/json"})
    Call<Response> getReply(@Body Request request);

}
