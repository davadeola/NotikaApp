package com.example.notika.services;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {


    private static final String URL ="https://us-central1-notika-8a4e3.cloudfunctions.net/api/";

    //create logger to assist in debugging
    private static HttpLoggingInterceptor logger = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);



    //create OKHttp client and merge the logger
    public static OkHttpClient.Builder okHttp = new OkHttpClient.Builder()
            .addInterceptor(logger);
//            .addInterceptor(new TokenRenewInterceptor());

    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build());

    private static Retrofit retrofit = builder.build();

    public static <s> s buildService(Class<s> serviceType){
        return retrofit.create(serviceType);
    }

}
