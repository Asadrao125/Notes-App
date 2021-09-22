package com.appsxone.notesapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.appsxone.notesapp.utils.SharedPref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    Database database;
    ImageView imgFilter;
    EditText edtSearch;
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
        SharedPref.init(this);
        edtSearch = findViewById(R.id.edtSearch);

        fabAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategoryDialog();
            }
        });

        setDapter();

        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        rvCategories.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    fabAddCategory.hide();
                } else {
                    fabAddCategory.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                final String query = s.toString().toLowerCase().trim();
                final ArrayList<Categories> filteredList = new ArrayList<>();
                for (int k = 0; k < categoriesArrayList.size(); k++) {
                    final String text = categoriesArrayList.get(k).category_name.toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(categoriesArrayList.get(k));
                    }
                }
                rvCategories.setAdapter(new CategoriesAdapter(getApplicationContext(), filteredList));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showFilterDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.diaog_filter, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

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

        if (SharedPref.read("key", "").equals("A")) {
            rbAllTime.setChecked(true);
        } else if (SharedPref.read("key", "").equals("D")) {
            rbDaily.setChecked(true);
        } else if (SharedPref.read("key", "").equals("W")) {
            rbWeekly.setChecked(true);
        } else if (SharedPref.read("key", "").equals("M")) {
            rbMonthly.setChecked(true);
        } else if (SharedPref.read("key", "").equals("Y")) {
            rbYearly.setChecked(true);
        }

        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbAllTime.isChecked()) {
                    SharedPref.write("key", "A");
                    setDapter();
                } else if (rbDaily.isChecked()) {
                    SharedPref.write("key", "D");
                    setDapter();
                } else if (rbWeekly.isChecked()) {
                    SharedPref.write("key", "W");
                    setDapter();
                } else if (rbMonthly.isChecked()) {
                    SharedPref.write("key", "M");
                    setDapter();
                } else if (rbYearly.isChecked()) {
                    SharedPref.write("key", "Y");
                    setDapter();
                }
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private void setDapter() {
        if (SharedPref.read("key", "").equals("A")) {
            categoriesArrayList = database.getAllCategories();
        } else if (SharedPref.read("key", "").equals("D")) {
            categoriesArrayList = database.getAllDailyCategories(DateFunctions.getCurrentDate());
        } else if (SharedPref.read("key", "").equals("W")) {
            categoriesArrayList = database.getAllWeeklyMonthlyYearlyNotes(DateFunctions.getCurrentDate(),
                    DateFunctions.getCalculatedDate("", -7));
        } else if (SharedPref.read("key", "").equals("M")) {
            database.getAllWeeklyMonthlyYearlyNotes(DateFunctions.getCurrentDate(),
                    DateFunctions.getCalculatedDate("", -30));
        } else if (SharedPref.read("key", "").equals("Y")) {
            database.getAllWeeklyMonthlyYearlyNotes(DateFunctions.getCurrentDate(),
                    DateFunctions.getCalculatedDate("", -365));
        } else {
            categoriesArrayList = database.getAllCategories();
        }

        if (categoriesArrayList != null) {
            rvCategories.setAdapter(new CategoriesAdapter(this, categoriesArrayList));
            rvCategories.setVisibility(View.VISIBLE);
        } else {
            rvCategories.setVisibility(View.GONE);
        }
    }

    public void addCategoryDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_category, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

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

    @Override
    protected void onResume() {
        super.onResume();
        setDapter();
    }
}