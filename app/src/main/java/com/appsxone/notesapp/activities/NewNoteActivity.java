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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsxone.notesapp.R;
import com.appsxone.notesapp.adapter.NotesAdapter;
import com.appsxone.notesapp.database.Database;
import com.appsxone.notesapp.model.Notes;
import com.appsxone.notesapp.utils.DateFunctions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NewNoteActivity extends AppCompatActivity {
    String name;
    int categoryId;
    TextView tvTitle;
    Database database;
    ImageView imgBack;
    RecyclerView rvNotes;
    LinearLayout addLayout;
    LinearLayout noDataLayout;
    public static TextView tvNotes;
    ArrayList<Notes> notesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        name = getIntent().getStringExtra("name");
        categoryId = getIntent().getIntExtra("id", 1000);
        database = new Database(this);
        rvNotes = findViewById(R.id.rvNotes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvNotes.setLayoutManager(linearLayoutManager);
        imgBack = findViewById(R.id.imgBack);
        tvTitle = findViewById(R.id.tvTitle);
        noDataLayout = findViewById(R.id.noDataLayout);
        addLayout = findViewById(R.id.addLayout);
        tvNotes = findViewById(R.id.tvNotes);

        tvTitle.setText(name);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setAdapter();

        addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoteDialog();
            }
        });

    }

    public void setAdapter() {
        notesArrayList = database.getAllNotes(categoryId);
        if (notesArrayList != null) {
            rvNotes.setAdapter(new NotesAdapter(this, notesArrayList));
            tvNotes.setText("Notes: " + notesArrayList.size());
        }
    }

    public void addNoteDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        EditText edtNoteTitle = dialogView.findViewById(R.id.edtNoteTitle);
        EditText edtNoteDecription = dialogView.findViewById(R.id.edtNoteDecription);
        Button btnAddNote = dialogView.findViewById(R.id.btnAddNote);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtNoteTitle.getText().toString().trim();
                String description = edtNoteDecription.getText().toString().trim();
                if (!title.isEmpty() && !description.isEmpty()) {
                    long status = database.saveNote(new Notes(0, categoryId, title, description,
                            DateFunctions.getCurrentDate(), DateFunctions.getCurrentTime()));
                    if (status != -1) {
                        alertDialog.dismiss();
                        setAdapter();
                    } else {
                        Toast.makeText(NewNoteActivity.this, "Failed to add note", Toast.LENGTH_SHORT).show();
                    }
                } else if (title.isEmpty()) {
                    edtNoteTitle.setError("Required");
                    edtNoteTitle.requestFocus();
                } else if (description.isEmpty()) {
                    edtNoteDecription.setError("Required");
                    edtNoteDecription.requestFocus();
                }
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
}