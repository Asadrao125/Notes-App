package com.appsxone.notesapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsxone.notesapp.activities.HomeActivity;
import com.appsxone.notesapp.R;
import com.appsxone.notesapp.activities.NewNoteActivity;
import com.appsxone.notesapp.database.Database;
import com.appsxone.notesapp.model.Categories;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {
    Context context;
    Database database;
    ArrayList<Categories> categoriesArrayList;

    public CategoriesAdapter(Context c, ArrayList<Categories> message) {
        context = c;
        categoriesArrayList = message;
        database = new Database(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_categories, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Categories categories = categoriesArrayList.get(position);
        holder.tvCategoryName.setText(categories.category_name);
        holder.textView.setText("" + categories.category_name.toUpperCase().charAt(0));

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCategoryDialog(categoriesArrayList.get(position).category_id, categoriesArrayList.get(position).category_name,
                        categoriesArrayList.get(position).date, categoriesArrayList.get(position).time);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDiaog(categoriesArrayList.get(position).category_id, position, categoriesArrayList.get(position).category_name);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewNoteActivity.class);
                intent.putExtra("name", categoriesArrayList.get(position).category_name);
                intent.putExtra("id", categoriesArrayList.get(position).category_id);
                context.startActivity(intent);
            }
        });

        if (database.getAllNotes(categoriesArrayList.get(position).category_id) != null) {
            holder.tvNotesSize.setText(database.getAllNotes(categoriesArrayList.get(position).category_id).size() + " Notes");
        }

    }

    @Override
    public int getItemCount() {
        return categoriesArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView, tvCategoryName, tvNotesSize;
        ImageView imgEdit, imgDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            tvNotesSize = itemView.findViewById(R.id.tvNotesSize);
        }
    }

    public void editCategoryDialog(int id, String name, String date, String time) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_category, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        //alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        EditText edtCategory = dialogView.findViewById(R.id.edtCategory);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);

        tvTitle.setText("Edit " + name);
        edtCategory.setText(name);

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
                    database.updateCategory(new Categories(category, id, date, time), id);
                    alertDialog.dismiss();
                    context.startActivity(new Intent(context, HomeActivity.class));
                    ((Activity) context).finish();
                } else {
                    edtCategory.setError("Required");
                    edtCategory.requestFocus();
                }
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    public void confirmationDiaog(int categoryId, int pos, String categoryTitle) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Are you sure you want to delete " + categoryTitle + " ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                database.deleteCategory(categoryId);
                categoriesArrayList.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, categoriesArrayList.size());
            }
        });
        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}