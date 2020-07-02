package com.example.notika.services;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notika.R;
import com.example.notika.services.models.Notes;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private ArrayList<Notes> notesList;
    private Context context;

    public NotesAdapter(ArrayList<Notes> notesList, Context context){
        this.notesList = notesList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false);

        return new NotesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        Notes currentNote = notesList.get(position);
        holder.bind(currentNote);


    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView noteTitle;
        private View noteBanner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.note_title);
            noteBanner = itemView.findViewById(R.id.category_banner);


        }

        public void bind(Notes currentNote) {
            noteTitle.setText(currentNote.getTitle());

            int colorCode;

            switch (currentNote.getCategory()){
                case "Travel":
                    colorCode = R.color.colorTravel;
                    break;

                case "Work":
                    colorCode = R.color.colorWork;
                    break;

                case "Personal":
                    colorCode = R.color.colorPersonal;
                    break;

                default:
                    colorCode = R.color.colorPrimaryDark;
            }

            Log.d("Color Code", currentNote.getCategory());

            noteBanner.setBackgroundColor(context.getResources().getColor(colorCode));
        }
    }
}
