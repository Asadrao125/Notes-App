package com.appsxone.notesapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsxone.notesapp.R;
import com.appsxone.notesapp.model.Categories;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MakeImageActivity extends AppCompatActivity {
    String quote;
    int[] imagesArray;
    int[] colorsArray;
    ImageView image, imgBack, imgSave, imgAddQuote;
    TextView tvQuote, tvTitle;
    RelativeLayout imageLayout;
    Button btnBackground, btnTextBackground;
    int imageIndex = 0, colorsIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_image);

        quote = getIntent().getStringExtra("quote");
        image = findViewById(R.id.image);
        imgBack = findViewById(R.id.imgBack);
        tvQuote = findViewById(R.id.tvQuote);
        btnBackground = findViewById(R.id.btnBackground);
        btnTextBackground = findViewById(R.id.btnTextBackground);
        tvTitle = findViewById(R.id.tvTitle);
        imgSave = findViewById(R.id.imgSave);
        imageLayout = findViewById(R.id.imageLayout);
        imgAddQuote = findViewById(R.id.imgAddQuote);

        tvQuote.setText(quote);
        imagesArray = new int[]{R.drawable.pic11, R.drawable.pic22, R.drawable.pic33, R.drawable.pic44, R.drawable.pic5,
                R.drawable.pic6, R.drawable.pic7, R.drawable.pic8, R.drawable.pic9, R.drawable.pic10};

        colorsArray = new int[]{R.color.color11, R.color.color22, R.color.color33, R.color.color44, R.color.color55,
                R.color.color66, R.color.color77, R.color.color88, R.color.color99, R.color.transparent};

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvTitle.setText(quote);

        btnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setImageResource(imagesArray[++imageIndex]);
                if (imageIndex == imagesArray.length - 1) {
                    imageIndex = 0;
                }
            }
        });

        btnTextBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvQuote.setBackgroundResource(colorsArray[++colorsIndex]);
                if (colorsIndex == colorsArray.length - 1) {
                    colorsIndex = 0;
                }
            }
        });

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        imgAddQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuoteDialog();
            }
        });

    }

    public void addQuoteDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_quote, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        EditText edtQuote = dialogView.findViewById(R.id.edtQuote);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd);
        TextView tvClear = dialogView.findViewById(R.id.tvClear);

        edtQuote.setText(quote);
        tvClear.setText("");

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quote = edtQuote.getText().toString().trim();
                if (!quote.isEmpty()) {
                    tvQuote.setText(quote);
                    alertDialog.dismiss();
                } else {
                    edtQuote.setError("Required");
                    edtQuote.requestFocus();
                }
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private void checkPermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Bitmap bitmap = Bitmap.createBitmap(imageLayout.getWidth(), imageLayout.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                imageLayout.draw(canvas);

                OutputStream fos;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                    Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                    Toast.makeText(getApplicationContext(), "Image saved in gallery", Toast.LENGTH_SHORT).show();

                    try {
                        fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        fos.flush();
                        fos.close();


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();

    }
}