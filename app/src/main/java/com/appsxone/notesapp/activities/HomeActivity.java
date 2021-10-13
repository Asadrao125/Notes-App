package com.appsxone.notesapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.appsxone.notesapp.BuildConfig;
import com.appsxone.notesapp.R;
import com.appsxone.notesapp.alarm.SetAlarm;
import com.appsxone.notesapp.database.Database;
import com.appsxone.notesapp.model.Categories;
import com.appsxone.notesapp.model.Notes;
import com.appsxone.notesapp.model.ToDoModel;
import com.appsxone.notesapp.utils.Quotes;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    public static int[] imagesArray;
    Database database;
    ImageView imgSettings;
    TextView tvPosition;
    ViewFlipper viewFlipper;
    CardView cv1, cv2, cv3, cv4, cv5, cv6;
    TextView tvCategories, tvNotes, tvVersionName, tvToDo;

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
        database.createDatabase();
        viewFlipper = findViewById(R.id.viewFlipper);
        tvPosition = findViewById(R.id.tvPosition);
        tvToDo = findViewById(R.id.tvToDo);
        imgSettings = findViewById(R.id.imgSettings);

        imagesArray = new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4};

        database.createDatabase();
        setText();

        tvVersionName.setText("Version: " + BuildConfig.VERSION_NAME);

        for (int image : imagesArray) {
            flipperImages(image);
        }

        SetAlarm.setAlarm(this, 8, 0, 1, "Good Morning");
        SetAlarm.setAlarm(this, 14, 0, 2, "Good Evening");
        SetAlarm.setAlarm(this, 21, 0, 3, "Good Night");

        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImageviewActivity.class);
                intent.putExtra("index", viewFlipper.getDisplayedChild());
                startActivity(intent);
            }
        });

        imgSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });

        tvCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
            }
        });

        tvNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NotesActivity.class));
            }
        });

        tvToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ToDoActivity.class));
            }
        });

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
                /* Notes */
                startActivity(new Intent(getApplicationContext(), NotesActivity.class));
            }
        });

        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Quote */
                startActivity(new Intent(getApplicationContext(), QuoteActivity.class));
            }
        });

        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Trash */
                startActivity(new Intent(getApplicationContext(), TrashActivity.class));
            }
        });

        cv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Make Image */
                Intent intent = new Intent(getApplicationContext(), MakeImageActivity.class);
                intent.putExtra("quote", Quotes.getRandomQuoteFromList());
                startActivity(intent);
            }
        });

        cv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* TODO  */
                startActivity(new Intent(getApplicationContext(), ToDoActivity.class));
            }
        });
    }

    public void flipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    private void setText() {
        ArrayList<Categories> categoriesArrayList = database.getAllCategories(0);
        ArrayList<Notes> notesArrayList = database.getAllNotes();
        ArrayList<ToDoModel> toDoModelArrayList = database.getAllToDo();

        if (categoriesArrayList != null) {
            tvCategories.setText("Category: " + categoriesArrayList.size());
        }

        if (notesArrayList != null) {
            tvNotes.setText("Note: " + notesArrayList.size());
        }

        if (toDoModelArrayList != null) {
            tvToDo.setText("TODO: " + toDoModelArrayList.size());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setText();
    }
}