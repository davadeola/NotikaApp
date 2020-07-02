package com.example.notika;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.notika.services.NotesAdapter;
import com.example.notika.services.NotesService;
import com.example.notika.services.ServiceBuilder;
import com.example.notika.services.TokenRenewInterceptor;
import com.example.notika.services.models.Notes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private List<Notes> notes;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));




    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        //String token = sharedPreferences.getString("token", "");
        String token = TokenRenewInterceptor.getToken(getApplicationContext());

        NotesService notesService = ServiceBuilder.buildService(NotesService.class);
        Call<List<Notes>> notesCall =  notesService.getNotes(String.format("Bearer %s", token));

        notesCall.enqueue(new Callback<List<Notes>>() {
            @Override
            public void onResponse(Call<List<Notes>> call, Response<List<Notes>> response) {
                if (response.isSuccessful()){
                    notes = response.body();
                    recyclerView.setAdapter(new NotesAdapter(notes, getApplicationContext()));
                    //Log.d("GottenN", response.body().get(0).getTitle());
                }else if (response.code() == 403){
                    Log.e("Error","Unauthorized");
                }
            }

            @Override
            public void onFailure(Call<List<Notes>> call, Throwable t) {
                Log.e("NotesError", "Something is wrong");
            }
        });


    }
}