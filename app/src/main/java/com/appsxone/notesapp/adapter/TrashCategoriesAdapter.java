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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.appsxone.notesapp.R;
import com.appsxone.notesapp.activities.HomeActivity;
import com.appsxone.notesapp.activities.NewNoteActivity;
import com.appsxone.notesapp.database.Database;
import com.appsxone.notesapp.model.Categories;
import com.appsxone.notesapp.model.Notes;

import java.util.ArrayList;

public class TrashCategoriesAdapter extends RecyclerView.Adapter<TrashCategoriesAdapter.MyViewHolder> {
    Context context;
    Database database;
    ArrayList<Categories> categoriesArrayList;

    public TrashCategoriesAdapter(Context c, ArrayList<Categories> message) {
        context = c;
        categoriesArrayList = message;
        database = new Database(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_trash_categories, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Categories categories = categoriesArrayList.get(position);
        holder.tvCategoryName.setText(categories.category_name);
        holder.textView.setText("" + categories.category_name.toUpperCase().charAt(0));

        ArrayList<Notes> notesArrayList = database.getAllNotes(categoriesArrayList.get(position).category_id);
        if (notesArrayList != null) {
            int size = notesArrayList.size();
            if (size > 1) {
                holder.tvNotesSize.setText(size + " Notes");
            } else if (size == 1) {
                holder.tvNotesSize.setText(size + " Note");
            }
        }

        holder.tvRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverCategory(categoriesArrayList.get(position).category_id, position, categoriesArrayList.get(position).category_name,
                        categoriesArrayList.get(position).date, categoriesArrayList.get(position).time,
                        categoriesArrayList.get(position).completeDate);
            }
        });

        holder.tvPermanentDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.deleteCategory(categoriesArrayList.get(position).category_id);
                categoriesArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, categoriesArrayList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView, tvCategoryName, tvNotesSize, tvPermanentDelete, tvRecover;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvNotesSize = itemView.findViewById(R.id.tvNotesSize);
            tvPermanentDelete = itemView.findViewById(R.id.tvPermanentDelete);
            tvRecover = itemView.findViewById(R.id.tvRecover);
        }
    }

    public void recoverCategory(int categoryId, int pos, String categoryTitle, String date, String time, String completeDate) {
        database.updateCategory(new Categories(categoryTitle, categoryId, date, time, 0, completeDate), categoryId);
        categoriesArrayList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, categoriesArrayList.size());
    }
}