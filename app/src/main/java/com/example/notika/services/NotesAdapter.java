package com.example.notika.services;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notika.R;
import com.example.notika.ViewNote;
import com.example.notika.services.models.Notes;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private ArrayList<Notes> notesList;
    private Context context;
    public static final SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM dd yyyy, hh:mm:ss a");


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
        String token = TokenRenewInterceptor.getToken(context);
        Log.d("Before state", String.valueOf(currentNote.isFavorite()));

        holder.favoriteToggle.setOnClickListener(v -> {
            NotesService notesService = ServiceBuilder.buildService(NotesService.class);
            Call<Void> favoriteCall = notesService.favoriteNotes(currentNote.getNoteId(), String.format("Bearer %s", token));
            holder.isFavorite = !holder.isFavorite;

            holder.favoriteToggle.setImageResource(
                    holder.isFavorite ? R.drawable.ic_baseline_star_24 : R.drawable.ic_baseline_star_border_24);


            favoriteCall.enqueue(new Callback<Void>() {

                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 401){
                        Toast.makeText(context, "Please login and try again", Toast.LENGTH_SHORT).show();
                    }else if(response.code() == 500){
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    if(t instanceof IOException){
                        Toast.makeText(context, "Internet connection lost", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }


    public  void updateList(List<Notes> newList){
        notesList = new ArrayList<>();
        notesList.addAll(newList);
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView noteTitle,date;
        private View noteBanner;
        private ImageView favoriteToggle;
        private boolean isFavorite;
        private String noteId;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteBanner = itemView.findViewById(R.id.category_banner);
            favoriteToggle = itemView.findViewById(R.id.favorite_toggle_icon);
            date = itemView.findViewById(R.id.date);
        }





        public void bind(Notes currentNote) {
            noteTitle.setText(currentNote.getTitle());
            isFavorite = currentNote.isFavorite();

            //format the date string

            PrettyTime prettyTime = new PrettyTime();

            try {

                date.setText(prettyTime.format(inputFormat.parse(currentNote.getLastEdited())));
                Log.d("DateFormat", inputFormat.parse(currentNote.getLastEdited()).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            noteId = currentNote.getNoteId();
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

            noteBanner.setBackgroundColor(context.getResources().getColor(colorCode));

            favoriteToggle.setImageResource(
                    isFavorite ? R.drawable.ic_baseline_star_24 : R.drawable.ic_baseline_star_border_24);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Notes currNote = notesList.get(position);
            Log.d("CLICKING", "clicked"+position);
            Intent intent = new Intent(context, ViewNote.class);
            intent.putExtra("Note", currNote);

            v.getContext().startActivity(intent);
        }
    }
}
