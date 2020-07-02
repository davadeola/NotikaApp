package com.example.notika.services;

import com.example.notika.services.models.Notes;
import com.example.notika.services.models.Token;
import com.example.notika.services.models.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotesService {



    //Call<Response data type>
    @FormUrlEncoded
    @POST("login")
    Call<Token> login(@Field("email") String email, @Field("password") String password);


    @POST("signup")
    Call<Token> signup(@Body User newUser);

    @GET("notes")
    Call<ArrayList<Notes>> getNotes(@Header("Authorization") String authorization);

}
