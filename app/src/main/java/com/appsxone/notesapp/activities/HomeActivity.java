package com.appsxone.notesapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appsxone.notesapp.R;
import com.appsxone.notesapp.adapter.CategoriesAdapter;
import com.appsxone.notesapp.alarm.SetAlarm;
import com.appsxone.notesapp.database.Database;
import com.appsxone.notesapp.model.Categories;
import com.appsxone.notesapp.ui_design.DesignActivity;
import com.appsxone.notesapp.utils.DateFunctions;
import com.appsxone.notesapp.utils.SharedPref;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    Database database;
    EditText edtSearch;
    CheckBox cbReverse;
    LinearLayout addLayout;
    RecyclerView rvCategories;
    LinearLayout noDataLayout;
    ImageView imgFilter, imgMore;
    public static TextView tvCategories;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Categories> categoriesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = new Database(this);
        database.createDatabase();
        SharedPref.init(getApplicationContext());
        cbReverse = findViewById(R.id.cbReverse);
        rvCategories = findViewById(R.id.rvCategories);
        linearLayoutManager = new LinearLayoutManager(this);
        imgMore = findViewById(R.id.imgMore);
        addLayout = findViewById(R.id.addLayout);
        tvCategories = findViewById(R.id.tvCategories);

        if (SharedPref.read("reverse", "").equals("true")) {
            cbReverse.setChecked(true);
            setLayoutManager(true);
        } else {
            cbReverse.setChecked(false);
            setLayoutManager(false);
        }

        cbReverse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPref.write("reverse", "true");
                    setLayoutManager(true);
                    setDapter();
                } else {
                    SharedPref.write("reverse", "false");
                    setLayoutManager(false);
                    setDapter();
                }
            }
        });

        SetAlarm.setAlarm(this, 8, 0, 1, "Good Morning");
        SetAlarm.setAlarm(this, 14, 0, 2, "Good Evening");
        SetAlarm.setAlarm(this, 21, 0, 3, "Good Night");

        rvCategories.setLayoutManager(linearLayoutManager);
        rvCategories.setHasFixedSize(true);
        noDataLayout = findViewById(R.id.noDataLayout);
        imgFilter = findViewById(R.id.imgFilter);
        SharedPref.init(this);
        edtSearch = findViewById(R.id.edtSearch);

        addLayout.setOnClickListener(new View.OnClickListener() {
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

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
            }
        });
    }

    private void popup() {
        PopupMenu popup = new PopupMenu(getApplicationContext(), imgMore);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.btnTrash:
                        startActivity(new Intent(getApplicationContext(), TrashActivity.class));
                        return true;
                    case R.id.btnSettings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        return true;
                    case R.id.btnCustomFilter:
                        createDialogWithoutDateField().show();
                        return true;
                    case R.id.btnNewDesign:
                        startActivity(new Intent(getApplicationContext(), DesignActivity.class));
                        return true;
                }
                return false;
            }
        });
        popup.inflate(R.menu.home_menu);
        popup.show();
    }

    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(this, null, 2014, 1, 24);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return dpd;
    }

    private void setLayoutManager(boolean check) {
        linearLayoutManager.setReverseLayout(check);
        linearLayoutManager.setStackFromEnd(check);
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
        RadioButton rbYesterday = dialogView.findViewById(R.id.rbYesterday);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (SharedPref.read("key", "").equals("A")) {
            rbAllTime.setChecked(true);
        } else if (SharedPref.read("key", "").equals("L")) {
            rbYesterday.setChecked(true);
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
                } else if (rbYesterday.isChecked()) {
                    SharedPref.write("key", "L");
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
            categoriesArrayList = database.getAllCategories(0);
        } else if (SharedPref.read("key", "").equals("L")) {
            categoriesArrayList = database.getAllWeeklyMonthlyYearlyCategories(DateFunctions.getCurrentDate(), DateFunctions.getCalculatedDate("", -1), 0);
        } else if (SharedPref.read("key", "").equals("D")) {
            categoriesArrayList = database.getAllDailyCategories(DateFunctions.getCurrentDate(), 0);
        } else if (SharedPref.read("key", "").equals("W")) {
            categoriesArrayList = database.getAllWeeklyMonthlyYearlyCategories(DateFunctions.getCurrentDate(),
                    DateFunctions.getCalculatedDate("", -7), 0);
        } else if (SharedPref.read("key", "").equals("M")) {
            categoriesArrayList = database.getAllWeeklyMonthlyYearlyCategories(DateFunctions.getCurrentDate(),
                    DateFunctions.getCalculatedDate("", -30), 0);
        } else if (SharedPref.read("key", "").equals("Y")) {
            categoriesArrayList = database.getAllWeeklyMonthlyYearlyCategories(DateFunctions.getCurrentDate(),
                    DateFunctions.getCalculatedDate("", -365), 0);
        } else {
            categoriesArrayList = database.getAllCategories(0);
        }

        if (categoriesArrayList != null) {
            rvCategories.setAdapter(new CategoriesAdapter(this, categoriesArrayList));
            rvCategories.setVisibility(View.VISIBLE);
            tvCategories.setText("Categories: " + categoriesArrayList.size());
        } else {
            rvCategories.setVisibility(View.GONE);
            tvCategories.setText("Categories: 0");
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
                    long status = database.saveCategory(new Categories(category, 0, DateFunctions.getCurrentDate(),
                            DateFunctions.getCurrentTime(), 0, DateFunctions.getCompleteDate()));
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