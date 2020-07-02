package com.example.notika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.notika.services.NotesAdapter;
import com.example.notika.services.NotesService;
import com.example.notika.services.ServiceBuilder;
import com.example.notika.services.TokenRenewInterceptor;
import com.example.notika.services.models.Notes;

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
        Call<ArrayList<Notes>> notesCall =  notesService.getNotes(String.format("Bearer %s", token));

        notesCall.enqueue(new Callback<ArrayList<Notes>>() {
            @Override
            public void onResponse(Call<ArrayList<Notes>> call, Response<ArrayList<Notes>> response) {
                if (response.isSuccessful()){
                    notes = response.body();

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putParcelableArrayListExtra("notes", notes);
                    startActivity(intent);

                }else{
                    Log.e("Error","Unauthorized");
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Notes>> call, Throwable t) {
                Log.e("NotesError", "Something is wrong");
            }
        });
    }
}