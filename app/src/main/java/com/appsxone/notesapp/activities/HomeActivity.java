package com.appsxone.notesapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appsxone.notesapp.R;
import com.appsxone.notesapp.adapter.CategoriesAdapter;
import com.appsxone.notesapp.database.Database;
import com.appsxone.notesapp.model.Categories;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    Database database;
    RecyclerView rvCategories;
    FloatingActionButton fabAddCategory;
    ArrayList<Categories> categoriesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = new Database(this);
        database.createDatabase();
        fabAddCategory = findViewById(R.id.fabAddCategory);
        rvCategories = findViewById(R.id.rvCategories);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvCategories.setLayoutManager(linearLayoutManager);
        rvCategories.setHasFixedSize(true);
        fabAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategoryDialog();
            }
        });
        setDapter();
    }

    private void setDapter() {
        if (database.getAllCategories() != null) {
            categoriesArrayList.clear();
            categoriesArrayList = database.getAllCategories();
            rvCategories.setAdapter(new CategoriesAdapter(this, categoriesArrayList));
        }
    }

    public void addCategoryDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_category, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        EditText edtCategory = dialogView.findViewById(R.id.edtCategory);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = edtCategory.getText().toString().trim();
                if (!category.isEmpty()) {

                    Date todaysdate = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    String date = format.format(todaysdate);
                    System.out.println(date);

                    Calendar currentTime = Calendar.getInstance();
                    int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = currentTime.get(Calendar.MINUTE);

                    String time = hour + " : " + minute;

                    long status = database.saveCategory(new Categories(category, 0, date, time));
                    if (status != -1) {
                        Toast.makeText(HomeActivity.this, "Category Added", Toast.LENGTH_SHORT).show();
                        setDapter();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(HomeActivity.this, "Failed to add category", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    edtCategory.setError("Required");
                    edtCategory.requestFocus();
                }
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
}