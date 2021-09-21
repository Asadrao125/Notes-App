package com.appsxone.notesapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.appsxone.notesapp.R;
import com.appsxone.notesapp.adapter.CategoriesAdapter;
import com.appsxone.notesapp.database.Database;
import com.appsxone.notesapp.model.Categories;
import com.appsxone.notesapp.utils.DateFunctions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    Database database;
    ImageView imgFilter;
    LinearLayout noDataLayout;
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
        noDataLayout = findViewById(R.id.noDataLayout);
        imgFilter = findViewById(R.id.imgFilter);

        fabAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategoryDialog();
            }
        });
        setDapter(database.getAllCategories());

        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
    }

    private void showFilterDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.diaog_filter, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        //alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        Button btnApplyFilter = dialogView.findViewById(R.id.btnApplyFilter);
        RadioButton rbAllTime = dialogView.findViewById(R.id.rbAllTime);
        RadioButton rbDaily = dialogView.findViewById(R.id.rbDaily);
        RadioButton rbWeekly = dialogView.findViewById(R.id.rbWeekly);
        RadioButton rbMonthly = dialogView.findViewById(R.id.rbMonthly);
        RadioButton rbYearly = dialogView.findViewById(R.id.rbYearly);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbAllTime.isChecked()) {
                    setDapter(database.getAllCategories());
                } else if (rbDaily.isChecked()) {
                    setDapter(database.getAllDailyCategories(DateFunctions.getCurrentDate()));
                } else if (rbWeekly.isChecked()) {

                    setDapter(database.getAllWeeklyMonthlyYearlyNotes(DateFunctions.getCurrentDate(),
                            DateFunctions.getCalculatedDate("", -7)));

                } else if (rbMonthly.isChecked()) {

                    setDapter(database.getAllWeeklyMonthlyYearlyNotes(DateFunctions.getCurrentDate(),
                            DateFunctions.getCalculatedDate("", -30)));

                } else if (rbYearly.isChecked()) {

                    setDapter(database.getAllWeeklyMonthlyYearlyNotes(DateFunctions.getCurrentDate(),
                            DateFunctions.getCalculatedDate("", -365)));

                }
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private void setDapter(ArrayList<Categories> categoriesArrayList) {
        if (categoriesArrayList != null) {
            rvCategories.setAdapter(new CategoriesAdapter(this, categoriesArrayList));
            rvCategories.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        } else {
            noDataLayout.setVisibility(View.VISIBLE);
            rvCategories.setVisibility(View.GONE);
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
                    long status = database.saveCategory(new Categories(category, 0, DateFunctions.getCurrentDate(), DateFunctions.getCurrentTime()));
                    if (status != -1) {
                        setDapter(database.getAllCategories());
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

    @Override
    protected void onResume() {
        super.onResume();
        setDapter(database.getAllCategories());
    }
}