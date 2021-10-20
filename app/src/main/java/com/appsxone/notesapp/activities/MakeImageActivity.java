package com.appsxone.notesapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
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

import com.appsxone.notesapp.BuildConfig;
import com.appsxone.notesapp.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MakeImageActivity extends AppCompatActivity {
    String quote;
    String[] fontFamilyArray;
    TextView tvQuote, tvTitle;
    RelativeLayout imageLayout;
    int[] imagesArray, colorsArray;
    RelativeLayout shareLayout, saveLayout;
    int imageIndex = 0, colorsIndex, fontIndex = 0;
    ImageView image, imgBack, imgChangeImage, imgColorFilter, imgAddText, imgAddImage, imgFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_image);

        quote = getIntent().getStringExtra("quote");
        image = findViewById(R.id.image);
        imgBack = findViewById(R.id.imgBack);
        tvQuote = findViewById(R.id.tvQuote);
        tvTitle = findViewById(R.id.tvTitle);
        saveLayout = findViewById(R.id.saveLayout);
        imageLayout = findViewById(R.id.imageLayout);
        imgChangeImage = findViewById(R.id.imgChangeImage);
        imgColorFilter = findViewById(R.id.imgColorFilter);
        imgAddText = findViewById(R.id.imgAddText);
        imgAddImage = findViewById(R.id.imgAddImage);
        imgFont = findViewById(R.id.imgFont);
        shareLayout = findViewById(R.id.shareLayout);

        tvQuote.setText(quote);
        imagesArray = new int[]{
                R.drawable.pic11, R.drawable.pic33, R.drawable.pic12, R.drawable.pic13,
                R.drawable.pic5, R.drawable.pic6, R.drawable.pic7, R.drawable.pic8,
                R.drawable.pic10, R.drawable.pic11};

        colorsArray = new int[]{
                R.color.color11, R.color.color22, R.color.color33, R.color.color44,
                R.color.color55, R.color.color66, R.color.color77, R.color.color88,
                R.color.color99, R.color.transparent};

        fontFamilyArray = new String[]{
                "AltoneTrial-Regular.ttf", "Geliat-ExtraLight.otf",
                "Sarahfadhilla-Free.ttf", "Head-Kick.ttf",
                "Time-Stop.ttf", "Slant.otf"};

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvTitle.setText(quote);

        imgChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.setImageResource(imagesArray[++imageIndex]);
                if (imageIndex == imagesArray.length - 1) {
                    imageIndex = 0;
                }
            }
        });

        imgFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Typeface face = Typeface.createFromAsset(getAssets(), "fonts/" + fontFamilyArray[++fontIndex]);
                tvQuote.setTypeface(face);
                if (fontIndex == fontFamilyArray.length - 1) {
                    fontIndex = 0;
                }
            }
        });

        imgColorFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvQuote.setBackgroundResource(colorsArray[++colorsIndex]);
                if (colorsIndex == colorsArray.length - 1) {
                    colorsIndex = 0;
                }
            }
        });

        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvQuote.setBackgroundResource(colorsArray[++colorsIndex]);
                if (colorsIndex == colorsArray.length - 1) {
                    colorsIndex = 0;
                }
            }
        });

        saveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        imgAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuoteDialog();
            }
        });

        imgAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 123);
            }
        });

        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, tvQuote.getText().toString().trim());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && reqCode == 123) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                image.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
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

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("*/*");
                        intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
                        startActivity(Intent.createChooser(intent, "Notes App"));

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

    private Uri getLocalBitmapUri(Bitmap bitmap) {
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "Premium Quotes" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(this, "com.appsxone.notesapp" + ".provider", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}