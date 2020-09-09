package com.example.notika.services.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

public class Notes implements Parcelable {
    private String title;
    private String body;
    private String category;
    private String author;
    private String noteId;
    private String createdAt;
    private String lastEdited;
    private boolean favorite;

    protected Notes(Parcel in) {
        title = in.readString();
        body = in.readString();
        category = in.readString();
        author = in.readString();
        noteId = in.readString();
        createdAt = in.readString();
        lastEdited = in.readString();
        favorite = in.readInt() != 0;

    }

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public String getCategory() {
        return category;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getLastEdited() {
        return lastEdited;
    }

    public String getNoteId() {
        return noteId;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(category);
        dest.writeString(author);
        dest.writeString(noteId);
        dest.writeString(createdAt);
        dest.writeString(lastEdited);
        dest.writeInt(favorite ? 1 : 0);
    }

    public boolean isFavorite() {
        return favorite;
    }


}
