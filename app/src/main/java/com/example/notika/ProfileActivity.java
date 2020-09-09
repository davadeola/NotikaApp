package com.example.notika;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notika.services.NotesAdapter;
import com.example.notika.services.models.Notes;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private TextView total_notes;
    private TextView favourites_total;
    private TextView popularCategory;
    //private ArrayList<Notes> notes;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        ArrayList<Notes> note = (ArrayList<Notes>) getIntent().getSerializableExtra("EXTRA_ORDER_KEY");
        total_notes = findViewById(R.id.no_notes);
        total_notes.setText(String.valueOf(totalNotes(note)));

        favourites_total = findViewById(R.id.no_favourite_notes);
        favourites_total.setText(String.valueOf(favourites(note)));

        popularCategory = findViewById(R.id.no_popular_category);
        popularCategory.setText(popularCategory(note));
    }


    public int totalNotes(ArrayList arrayList){
        if (arrayList != null) {
            return arrayList.size();
        } else {
            return 0;
        }
    }

    public int favourites(ArrayList<Notes> arrayList){
        int count_favourites = 0;
        for(int i = 0; i < arrayList.size(); i++){
            if(arrayList.get(i).isFavorite()){
                count_favourites++;
            }
        }
        return count_favourites;
    }

    public String popularCategory(ArrayList<Notes> arrayList) {
        int travel = 0, work = 0, personal = 0, def = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            switch (arrayList.get(i).getCategory()) {
                case "Travel":
                    travel++;
                    break;

                case "Work":
                    work++;
                    break;

                case "Personal":
                    personal++;
                    break;

                default:
                    def++;
                    break;
            }
        }
        if ((travel == 0) && (work == 0) && (personal == 0)) {
            return "None";
        } else if ((travel > work) && (travel > personal)) {
            return "Travel";
        } else if ((work > travel) && (work > personal)) {
            return "Work";
        } else if ((travel == work) && (work > personal)) {
            return "Travel & Work";
        } else if ((work == personal) && (work > travel)) {
            return "Work & Personal";
        } else if ((travel == personal) && personal > work) {
            return "Travel & Personal";
        } else if ((travel == work) && (work == personal)) {
            return "Travel & Work & Personal";
        } else {
            return "Personal";
        }
    }

}
