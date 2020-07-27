package com.example.notika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.notika.services.NotesAdapter;
import com.example.notika.services.NotesService;
import com.example.notika.services.ServiceBuilder;
import com.example.notika.services.TokenRenewInterceptor;
import com.example.notika.services.models.Notes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    ArrayList<Notes> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }


    @Override
    protected void onResume() {
        super.onResume();

        String token = TokenRenewInterceptor.getToken(getApplicationContext());
        NotesService notesService = ServiceBuilder.buildService(NotesService.class);

        Call<Void> verifyCall =  notesService.verifyToken(String.format("Bearer %s", token));

        verifyCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.code() == 200){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }else if(response.code() == 403){
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.e("OffStep", "onResponse: Note what I expected" );
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                if (t instanceof IOException){
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                }

                Log.e("NotesError", "Check your internet connection");
            }
        });
    }
}