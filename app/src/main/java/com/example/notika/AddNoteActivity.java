package com.example.notika;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.notika.services.NotesService;
import com.example.notika.services.ServiceBuilder;
import com.example.notika.services.TokenRenewInterceptor;
import com.example.notika.services.models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNoteActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Spinner spinner_cat;
    private String category, title, body;
    private EditText addTitle, addBody;
    private FloatingActionButton addNoteFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        toolbar = findViewById(R.id.include);
        setSupportActionBar(toolbar);
        addBody = findViewById(R.id.addBodyEditText);
        addTitle = findViewById(R.id.addTitleEditText);
        addNoteFab = findViewById(R.id.addNote_fab);



        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.add_note_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner_cat = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.categories, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cat.setAdapter(spinnerAdapter);



        spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       addNoteFab.setOnClickListener(v -> {
           String token = TokenRenewInterceptor.getToken(getApplicationContext());
           title = addTitle.getText().toString();
           body = addBody.getText().toString();
           NotesService notesService = ServiceBuilder.buildService(NotesService.class);
           Call<Notes> addNoteCall = notesService.addNote(String.format("Bearer %s", token), title, body, category);

           addNoteCall.enqueue(new Callback<Notes>() {
               @Override
               public void onResponse(@NotNull Call<Notes> call, @NotNull Response<Notes> response) {
                  if (response.isSuccessful()){
                      startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
                     Toast.makeText(getApplicationContext(), "Your note has been added", Toast.LENGTH_SHORT).show();
                    
                  }
               }

               @Override
               public void onFailure(Call<Notes> call, Throwable t) {
                    if (t instanceof IOException){
                        Toast.makeText(getApplicationContext(), "Check your internet connectivity", Toast.LENGTH_SHORT).show();
                    }
               }
           });
       });

    }
}