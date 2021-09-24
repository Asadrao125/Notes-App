package com.appsxone.notesapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.appsxone.notesapp.R;
import com.appsxone.notesapp.adapter.TrashCategoriesAdapter;
import com.appsxone.notesapp.database.Database;
import com.appsxone.notesapp.model.Categories;

import java.util.ArrayList;

public class TrashActivity extends AppCompatActivity {
    ImageView imgBack;
    Database database;
    RecyclerView rvTrashCategories;
    ArrayList<Categories> categoriesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        imgBack = findViewById(R.id.imgBack);
        database = new Database(this);
        rvTrashCategories = findViewById(R.id.rvTrashCategories);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvTrashCategories.setLayoutManager(linearLayoutManager);
        rvTrashCategories.setHasFixedSize(true);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        categoriesArrayList = database.getAllCategories(1);
        if (categoriesArrayList != null) {
            rvTrashCategories.setAdapter(new TrashCategoriesAdapter(this, categoriesArrayList));
        }
    }
}