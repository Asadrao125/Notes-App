package com.appsxone.notesapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.appsxone.notesapp.BuildConfig;
import com.appsxone.notesapp.R;
import com.appsxone.notesapp.activities.CategoriesActivity;
import com.appsxone.notesapp.activities.QuoteActivity;
import com.appsxone.notesapp.activities.SettingsActivity;
import com.appsxone.notesapp.activities.TrashActivity;
import com.appsxone.notesapp.database.Database;
import com.appsxone.notesapp.model.Categories;
import com.appsxone.notesapp.model.Notes;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    Database database;
    CardView cv1, cv2, cv3, cv4, cv5, cv6;
    TextView tvCategories, tvNotes, tvVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cv1 = findViewById(R.id.cv1);
        cv2 = findViewById(R.id.cv2);
        cv3 = findViewById(R.id.cv3);
        cv4 = findViewById(R.id.cv4);
        cv5 = findViewById(R.id.cv5);
        cv6 = findViewById(R.id.cv6);
        tvCategories = findViewById(R.id.tvCategories);
        tvNotes = findViewById(R.id.tvNotes);
        tvVersionName = findViewById(R.id.tvVersionName);
        database = new Database(this);

        setText();

        tvVersionName.setText("Version: " + BuildConfig.VERSION_NAME);

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Categories */
                startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
            }
        });

        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Quote */
                startActivity(new Intent(getApplicationContext(), QuoteActivity.class));
            }
        });

        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Trash */
                startActivity(new Intent(getApplicationContext(), TrashActivity.class));
            }
        });

        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Settings */
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });

        cv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Share App
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Let me recommed you " + getString(R.string.app_name) + " application" +
                        "\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        cv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Rate App
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            }
        });
    }

    private void setText() {
        ArrayList<Categories> categoriesArrayList = database.getAllCategories(0);
        ArrayList<Notes> notesArrayList = database.getAllNotes();

        if (categoriesArrayList != null) {
            tvCategories.setText("Categories: " + categoriesArrayList.size());
        }

        if (notesArrayList != null) {
            tvNotes.setText("Notes: " + notesArrayList.size());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setText();
    }
}