package com.example.notika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.notika.services.NotesAdapter;
import com.example.notika.services.NotesService;
import com.example.notika.services.ServiceBuilder;
import com.example.notika.services.TokenRenewInterceptor;
import com.example.notika.services.models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ArrayList<Notes> notes;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private NotesAdapter notesAdapter;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab_main);
        button = findViewById(R.id.button_profile);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdd = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intentAdd);
            }
        });
        //button for switching to profile activity
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(this, count(), Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);
                myIntent.putExtra("EXTRA_ORDER_KEY", notes);
                //myIntent.putExtra()
                //myIntent.putExtra("EXTRA_ORDER_KEY_II", recyclerView);
                startActivity(myIntent);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);



    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            String token = TokenRenewInterceptor.getToken(getApplicationContext());

            Notes deletedNote = notes.get(position);

            Log.d("NoteId", "onSwiped: "+ deletedNote.getNoteId());

            switch (direction){
                case ItemTouchHelper.RIGHT:
                    Log.d("Swiped left", "onSwiped: Swiped left");
                    NotesService notesService = ServiceBuilder.buildService(NotesService.class);
                    Call<Void> deleteCall = notesService.deleteNotes(deletedNote.getNoteId(), String.format("Bearer %s", token));

                    deleteCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                           if (response.isSuccessful()){
                               Toast.makeText(getApplicationContext(), "Note has been deleted", Toast.LENGTH_SHORT).show();
                               Log.e("Deleted", "onResponse: Deleted from firebase");

                           }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            if(t instanceof IOException){
                                Toast.makeText(getApplicationContext(), "Internet connection lost", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    notes.remove(position);
                    notesAdapter.notifyItemRemoved(position);

                    break;

            }

        }
    };

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
                    notesAdapter = new NotesAdapter(notes, getApplicationContext());
                    recyclerView.setAdapter(notesAdapter);
                }else{
                    Log.e("Error","Unauthorized");
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Notes>> call, Throwable t) {

                if (t instanceof IOException){
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                }

                Log.e("NotesError", "Check your internet connection");
            }
        });



    }
    public MainActivity(){

    }


}