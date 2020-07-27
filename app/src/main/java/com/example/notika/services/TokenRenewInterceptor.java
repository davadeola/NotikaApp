package com.example.notika.services;



import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.notika.LoginActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import static android.content.Context.MODE_APPEND;


public class TokenRenewInterceptor implements Authenticator {

    private final static String myPreference = "myPref";
    private static String savedToken = "";


    public static void  saveToken(String token, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);

        editor.apply();
    }


    public static String getToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        String gottenToken = sharedPreferences.getString("token", "");

        return gottenToken;
    }

    private void setAuthHeader(Request.Builder builder, String token) {
        if (token != null) //Add Auth token to each request if authorized
            builder.header("Authorization", String.format("Bearer %s", token));
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {

        String newAccessToken = "";

        return response.request().newBuilder().header("Authorization", String.format("Bearer %s", newAccessToken)).build();
    }
}
