package com.example.notika.services;



import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.notika.LoginActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_APPEND;


public class TokenRenewInterceptor implements Interceptor {

    private final static String myPreference = "myPref";
    private static String savedToken = "";


    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder builder = request.newBuilder();


        String token = savedToken;
        setAuthHeader(builder, token);

        request = builder.build();
        Response response = chain.proceed(request);

//        if(response.code() == 401){
//            synchronized (this){
//
//            }
//        }



        return response;

    }


    public static void  saveToken(String token, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }


    public static String getToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        String gottenToken = sharedPreferences.getString("token", "");

        Log.d("InRenew", gottenToken);
        savedToken = gottenToken;

        return gottenToken;
    }


    private void setAuthHeader(Request.Builder builder, String token) {
        if (token != null) //Add Auth token to each request if authorized
            builder.header("Authorization", String.format("Bearer %s", token));
    }
}
