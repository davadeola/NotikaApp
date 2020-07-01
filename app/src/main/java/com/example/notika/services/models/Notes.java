package com.example.notika.services.models;

public class Notes {
    private String title;
    private String body;
    private String category;
    private String author;
    private String noteId;
    private String createdAt;
    private String lastEdited;

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
}
