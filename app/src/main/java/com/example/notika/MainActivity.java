package com.example.notika;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.notika.services.NotesAdapter;
import com.example.notika.services.NotesService;
import com.example.notika.services.ServiceBuilder;
import com.example.notika.services.TokenRenewInterceptor;
import com.example.notika.services.models.Notes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ArrayList<Notes> notes;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        notes = getIntent().getParcelableArrayListExtra("notes");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        recyclerView.setAdapter(new NotesAdapter(notes, getApplicationContext()));

    }

    @Override
    protected void onResume() {
        super.onResume();

        //SharedPreferences sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        //String token = sharedPreferences.getString("token", "");



    }
}