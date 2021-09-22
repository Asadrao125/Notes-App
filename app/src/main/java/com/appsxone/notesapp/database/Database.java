package com.appsxone.notesapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.appsxone.notesapp.model.Categories;
import com.appsxone.notesapp.model.Notes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Database {
    String DB_PATH = "data/data/com.appsxone.notesapp/databases/";
    String DB_NAME = "notes_db.sqlite";
    Context activity;
    SQLiteDatabase sqLiteDatabase;

    public Database(Context activity) {
        this.activity = activity;
    }

    public void createDatabase() {
        boolean dBExist = false;

        try {
            dBExist = checkDatabase();
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (dBExist) {

        } else {
            try {

                sqLiteDatabase = activity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
                sqLiteDatabase.close();
                copyDatabaseTable();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

    private void copyDatabaseTable() throws IOException {

        //open your local database as input stream
        InputStream myInput = activity.getAssets().open(DB_NAME);

        //path to the created empty database
        String outFileName = DB_PATH + DB_NAME;

        //open the empty database as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private boolean checkDatabase() {

        SQLiteDatabase checkDB = null;
        String myPath = DB_PATH + DB_NAME;
        try {
            try {
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            //no database exists...
        }


        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    public void open() {
        sqLiteDatabase = activity.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
    }

    public void close() {
        sqLiteDatabase.close();
    }

    //============================ START CUSTOM METHODS FOR CATEGORIES TABLE ====================================

    /*
    CREATE TABLE "categories" (
	"category_name"	TEXT,
	"category_id"	INTEGER,
	"date"	TEXT,
	"time"	TEXT,
	PRIMARY KEY("category_id" AUTOINCREMENT));
    */

    public long saveCategory(Categories categories) {
        long rowId = -1;
        try {
            open();
            ContentValues cv = new ContentValues();
            cv.put("category_name", categories.category_name);
            cv.put("date", categories.date);
            cv.put("time", categories.time);
            rowId = sqLiteDatabase.insert("categories", null, cv);
            close();
        } catch (SQLiteException e) {
            Toast.makeText(activity, "Database exception", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        System.out.println("-- Record inserted rowId : " + rowId);
        return rowId;
    }

    public ArrayList<Categories> getAllCategories() {
        open();
        ArrayList<Categories> categoryBeans = new ArrayList<>();
        Categories temp;
        String query = "select * from categories";

        System.out.println("--query in getAllCategories : " + query);
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));
                String category_name = cursor.getString(cursor.getColumnIndex("category_name"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                temp = new Categories(category_name, category_id, date, time);
                categoryBeans.add(temp);
                temp = null;
            }
            while (cursor.moveToNext());
            close();
            return categoryBeans;
        }
        close();
        return null;
    }

    public ArrayList<Categories> getAllDailyCategories(String date) {
        open();
        ArrayList<Categories> categoryBeans = new ArrayList<>();
        Categories temp;
        String query = "select * from categories WHERE date = '" + date + "'";

        System.out.println("--query in getAllDailyCategories : " + query);
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));
                String category_name = cursor.getString(cursor.getColumnIndex("category_name"));
                String date1 = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                temp = new Categories(category_name, category_id, date1, time);
                categoryBeans.add(temp);
                temp = null;
            }
            while (cursor.moveToNext());
            close();
            return categoryBeans;
        }
        close();
        return null;
    }

    public ArrayList<Categories> getAllWeeklyMonthlyYearlyNotes(String startDate, String endDate) {
        open();
        ArrayList<Categories> categoryBeans = new ArrayList<>();
        Categories temp;

        //String query = "SELECT * FROM categories WHERE date BETWEEN '" + endDate + "' AND '" + startDate + "'";
        String query = "SELECT * FROM categories WHERE date >= '" + endDate + "' AND date <= '" + startDate + "'";

        System.out.println("--query in getAllWeeklyMonthlyYearlyNotes : " + query);
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));
                String category_name = cursor.getString(cursor.getColumnIndex("category_name"));
                String date1 = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                temp = new Categories(category_name, category_id, date1, time);
                categoryBeans.add(temp);
                temp = null;
            }
            while (cursor.moveToNext());
            close();
            return categoryBeans;
        }
        close();
        return null;
    }

    public void deleteCategory(int id) {
        open();
        String query = "delete from categories WHERE category_id = '" + id + "'";
        sqLiteDatabase.execSQL(query);
        close();
    }

    public void updateCategory(Categories categories, int id) {
        open();
        ContentValues dataToUpdate = new ContentValues();
        dataToUpdate.put("category_name", categories.category_name);
        dataToUpdate.put("date", categories.date);
        dataToUpdate.put("time", categories.time);
        String where = "category_id" + "=" + "'" + id + "'";
        try {
            int rows = sqLiteDatabase.update("categories", dataToUpdate, where, null);
            System.out.println("-- rows updated: " + rows);
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
    }

    //============================ START CUSTOM METHODS FOR NOTES TABLE ====================================

    /*
    * CREATE TABLE "notes" (
	"note_id"	INTEGER,
	"category_id"	INTEGER,
	"note_title"	TEXT,
	"note_description"	TEXT,
	"date"	TEXT,
	"time"	TEXT,
	PRIMARY KEY("note_id" AUTOINCREMENT));
    */

    public long saveNote(Notes notes) {
        long rowId = -1;
        try {
            open();
            ContentValues cv = new ContentValues();
            cv.put("category_id", notes.category_id);
            cv.put("note_title", notes.note_title);
            cv.put("note_description", notes.note_description);
            cv.put("date", notes.date);
            cv.put("time", notes.time);
            rowId = sqLiteDatabase.insert("notes", null, cv);
            close();
        } catch (SQLiteException e) {
            Toast.makeText(activity, "Database exception", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        System.out.println("-- Record inserted rowId : " + rowId);
        return rowId;
    }

    public ArrayList<Notes> getAllNotes(int categoryId) {
        open();
        ArrayList<Notes> categoryBeans = new ArrayList<>();
        Notes temp;
        String query = "select * from notes WHERE category_id = '" + categoryId + "'";

        System.out.println("--query in getAllAttendance : " + query);
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int note_id = cursor.getInt(cursor.getColumnIndex("note_id"));
                int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));
                String note_title = cursor.getString(cursor.getColumnIndex("note_title"));
                String note_description = cursor.getString(cursor.getColumnIndex("note_description"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                temp = new Notes(note_id, category_id, note_title, note_description, date, time);
                categoryBeans.add(temp);
                temp = null;
            }
            while (cursor.moveToNext());
            close();
            return categoryBeans;
        }
        close();
        return null;
    }

    public void deleteNote(int id) {
        open();
        String query = "delete from notes WHERE note_id = '" + id + "'";
        sqLiteDatabase.execSQL(query);
        close();
    }

    public void updateNote(Notes notes, int id) {
        open();
        ContentValues dataToUpdate = new ContentValues();
        dataToUpdate.put("note_title", notes.note_title);
        dataToUpdate.put("note_description", notes.note_description);
        dataToUpdate.put("category_id", notes.category_id);
        dataToUpdate.put("date", notes.date);
        dataToUpdate.put("time", notes.time);
        String where = "note_id" + "=" + "'" + id + "'";
        try {
            int rows = sqLiteDatabase.update("notes", dataToUpdate, where, null);
            System.out.println("-- rows updated: " + rows);
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
    }
}