package com.example.notika;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.notika.services.models.Notes;

public class ViewNote extends AppCompatActivity {
    private TextView view_title;
    private TextView view_body;
    private Toolbar toolbar;
    private Notes note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        toolbar = findViewById(R.id.include);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view_body = findViewById(R.id.view_body);
        view_title = findViewById(R.id.view_title);

note = getIntent().getParcelableExtra("Note");
view_body.setText(note.getBody());
view_title.setText(note.getTitle());

        Log.d("Note title", "onCreate: "+note.getNoteId());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        view_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editNote = new Intent(ViewNote.this, AddNoteActivity.class);
                editNote.putExtra("Note", note);
                startActivity(editNote);
                finish();
            }
        });
    }
}