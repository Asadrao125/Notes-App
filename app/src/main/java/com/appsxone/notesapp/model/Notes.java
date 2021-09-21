package com.appsxone.notesapp.model;

public class Notes {
    public int note_id;
    public int category_id;
    public String note_title;
    public String note_description;
    public String date;
    public String time;

    public Notes(int note_id, int category_id, String note_title, String note_description, String date, String time) {
        this.note_id = note_id;
        this.category_id = category_id;
        this.note_title = note_title;
        this.note_description = note_description;
        this.date = date;
        this.time = time;
    }
}
