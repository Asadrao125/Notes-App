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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsxone.notesapp.R;
import com.appsxone.notesapp.adapter.ToDoAdapter;
import com.appsxone.notesapp.database.Database;
import com.appsxone.notesapp.model.Categories;
import com.appsxone.notesapp.model.ToDoModel;
import com.appsxone.notesapp.utils.DateFunctions;

import java.util.ArrayList;

public class ToDoActivity extends AppCompatActivity {
    ImageView imgBack;
    Database database;
    RecyclerView rvToDo;
    CheckBox cbCompleted;
    LinearLayout addLayout;
    public static TextView tvSize;
    ArrayList<ToDoModel> toDoModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        imgBack = findViewById(R.id.imgBack);
        rvToDo = findViewById(R.id.rvToDo);
        rvToDo.setLayoutManager(new LinearLayoutManager(this));
        rvToDo.setHasFixedSize(true);
        addLayout = findViewById(R.id.addLayout);
        database = new Database(this);
        cbCompleted = findViewById(R.id.cbCompleted);
        tvSize = findViewById(R.id.tvSize);

        setDapter();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDoDialog();
            }
        });

        cbCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toDoModelArrayList = database.getCompletedUnCompleteToDo(1);
                    if (toDoModelArrayList != null) {
                        rvToDo.setAdapter(new ToDoAdapter(ToDoActivity.this, toDoModelArrayList));
                        tvSize.setText("TODO's: " + toDoModelArrayList.size());
                    }
                } else {
                    setDapter();
                }
            }
        });
    }

    public void addToDoDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_todo, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        EditText edtToDo = dialogView.findViewById(R.id.edtToDo);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtToDo.getText().toString().trim();
                if (!title.isEmpty()) {
                    long status = database.saveToDO(new ToDoModel(0, title, 0, 0, 0, DateFunctions.getCurrentDate(),
                            DateFunctions.getCurrentTime(), DateFunctions.getCompleteDate()));
                    if (status != -1) {
                        setDapter();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(ToDoActivity.this, "Failed to add todo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    edtToDo.setError("Required");
                    edtToDo.requestFocus();
                }
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private void setDapter() {
        toDoModelArrayList.clear();
        toDoModelArrayList = database.getAllToDo();
        if (toDoModelArrayList != null) {
            rvToDo.setAdapter(new ToDoAdapter(this, toDoModelArrayList));
            tvSize.setText("TODO's " + toDoModelArrayList.size());
        }
    }
}