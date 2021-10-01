package com.appsxone.notesapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.appsxone.notesapp.R;

public class ImageviewActivity extends AppCompatActivity {
    int index;
    ImageView image, imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);

        index = getIntent().getIntExtra("index", 1000);
        image = findViewById(R.id.image);
        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        image.setImageResource(HomeActivity.imagesArray[index]);
    }
}