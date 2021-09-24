package com.appsxone.notesapp.model;

public class Categories {
    public String category_name;
    public int category_id;
    public String date;
    public String time;
    public int idDeleted;
    public String completeDate;

    public Categories(String category_name, int category_id, String date, String time, int idDeleted, String completeDate) {
        this.category_name = category_name;
        this.category_id = category_id;
        this.date = date;
        this.time = time;
        this.idDeleted = idDeleted;
        this.completeDate = completeDate;
    }
}
