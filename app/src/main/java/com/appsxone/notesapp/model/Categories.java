package com.appsxone.notesapp.model;

public class Categories {
    public String category_name;
    public int category_id;
    public String date;
    public String time;
    public int idDeleted;

    public Categories(String category_name, int category_id, String date, String time, int idDeleted) {
        this.category_name = category_name;
        this.category_id = category_id;
        this.date = date;
        this.time = time;
        this.idDeleted = idDeleted;
    }
}
