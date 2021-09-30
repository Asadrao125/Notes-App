package com.appsxone.notesapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.appsxone.notesapp.R;
import com.appsxone.notesapp.adapter.QuoteAdapter;
import com.appsxone.notesapp.utils.Quotes;

import java.util.ArrayList;

public class QuoteActivity extends AppCompatActivity {
    ImageView imgBack;
    RecyclerView rvQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        imgBack = findViewById(R.id.imgBack);
        rvQuote = findViewById(R.id.rvQuotes);
        rvQuote.setLayoutManager(new LinearLayoutManager(this));
        rvQuote.setHasFixedSize(true);
        rvQuote.setAdapter(new QuoteAdapter(this, Quotes.quoteList()));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}