package com.example.notika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.notika.services.NotesAdapter;
import com.example.notika.services.NotesService;
import com.example.notika.services.ServiceBuilder;
import com.example.notika.services.TokenRenewInterceptor;
import com.example.notika.services.models.Notes;
import com.example.notika.services.models.User;
import com.squareup.picasso.Picasso;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.navigation.NavigationView;




public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , SearchView.OnQueryTextListener  {
    private Toolbar toolbar;
    private ArrayList<Notes> notes;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private NotesAdapter notesAdapter;


    private ImageView prof_image;
    private DrawerLayout drawerLayout;
    private TextView userNameView;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        prof_image= headerView.findViewById(R.id.profile_image);
        userNameView =headerView.findViewById(R.id.name);

        //display the image and User Name
        String imageUrl = TokenRenewInterceptor.getImageUrl(getApplicationContext());
        String userName = TokenRenewInterceptor.getUserName(getApplicationContext());
        userNameView.setText(userName);


        //display image
        RequestOptions options = new RequestOptions()
                .centerCrop();

        Glide.with(this)
                .load(imageUrl)
                .apply(options)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(prof_image);




        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab_main);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdd = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intentAdd);
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

                if (t instanceof IOException) {
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                }

                Log.e("NotesError", "Check your internet connection");
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_settings:
                Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_profile:
                Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);
                myIntent.putExtra("EXTRA_ORDER_KEY", notes);
                startActivity(myIntent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
                return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String userInput= newText.toLowerCase();
        List<Notes> notesList = new ArrayList<>();

        for ( Notes note : notes){
           if (note.getTitle().contains(userInput)  || note.getBody().contains(userInput)){
                notesList.add(note);
           }
        }

        notesAdapter.updateList(notesList);

        return true;
    }
}