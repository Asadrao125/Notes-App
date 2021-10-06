package com.appsxone.notesapp.model;

public class ToDoModel {
    public int id;
    public String title;
    public int isCompleted;
    public int isActive;
    public int isDeleted;
    public String date;
    public String time;
    public String completeDate;

    public ToDoModel(int id, String title, int isCompleted, int isActive, int isDeleted, String date, String time, String completeDate) {
        this.id = id;
        this.title = title;
        this.isCompleted = isCompleted;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.date = date;
        this.time = time;
        this.completeDate = completeDate;
    }
}
