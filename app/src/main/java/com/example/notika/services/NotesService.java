package com.example.notika.services;

import com.example.notika.services.models.Notes;
import com.example.notika.services.models.Token;
import com.example.notika.services.models.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @POST("notes/{noteId}/favorite")
    Call<Void> favoriteNotes(@Path("noteId") String noteId, @Header("Authorization") String authorization);

    @DELETE("notes/{noteId}")
    Call<Void> deleteNotes(@Path("noteId") String noteId, @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("note")
    Call<Notes> addNote(@Header("Authorization") String authorization, @Field("title") String title, @Field("body") String body, @Field("category") String category);

    @POST("verify")
    Call<Void> verifyToken(@Header("Authorization") String authorization);


    @Multipart
    @POST("uploadProfile")
    Call<ResponseBody> upload(
            @Header("Authorization") String authorization,
            @Part MultipartBody.Part file
    );






}
