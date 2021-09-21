package com.appsxone.notesapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appsxone.notesapp.R;

public class NewNoteActivity extends AppCompatActivity {
    String name;
    int categoryId;
    Button btnAddNote;
    EditText edtNoteTitle, edtNoteDecription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        name = getIntent().getStringExtra("name");
        categoryId = getIntent().getIntExtra("id", 1000);

        btnAddNote = findViewById(R.id.btnAddNote);
        edtNoteTitle = findViewById(R.id.edtNoteTitle);
        edtNoteDecription = findViewById(R.id.edtNoteDecription);

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtNoteTitle.getText().toString().trim();
                String description = edtNoteDecription.getText().toString().trim();
                if (!title.isEmpty() && !description.isEmpty()) {

                } else {
                    Toast.makeText(NewNoteActivity.this, "Please enter title and description", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}