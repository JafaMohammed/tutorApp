package com.mojaafar.tutorapp.http;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiEndpoint {
    @POST("users/signup")
    Call<User> createUser(@Body User user);
    @POST("users/login")
    Call <User>loginUser(@Body User user);


}

