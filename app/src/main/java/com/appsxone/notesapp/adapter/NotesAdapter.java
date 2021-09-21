package com.appsxone.notesapp.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.appsxone.notesapp.R;
import com.appsxone.notesapp.activities.HomeActivity;
import com.appsxone.notesapp.activities.NewNoteActivity;
import com.appsxone.notesapp.database.Database;
import com.appsxone.notesapp.model.Categories;
import com.appsxone.notesapp.model.Notes;
import com.nguyencse.URLEmbeddedData;
import com.nguyencse.URLEmbeddedView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    Context context;
    Database database;
    ArrayList<Notes> notesArrayList;

    public NotesAdapter(Context c, ArrayList<Notes> message) {
        context = c;
        notesArrayList = message;
        database = new Database(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_notes, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Notes notes = notesArrayList.get(position);
        holder.tvTitle.setText(notes.note_title);
        holder.tvDescription.setText(notes.note_description);
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCategoryDialog(notesArrayList.get(position).note_id, notesArrayList.get(position).note_title,
                        notesArrayList.get(position).note_description, notesArrayList.get(position).date,
                        notesArrayList.get(position).time, notesArrayList.get(position).category_id);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.deleteNote(notesArrayList.get(position).note_id);
                notesArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, notesArrayList.size());
            }
        });

        holder.imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", notesArrayList.get(position).note_description);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
            }
        });

        if (notesArrayList.get(position).note_description.contains("http")) {
            holder.urlEmbeddedView.setVisibility(View.VISIBLE);
            holder.urlEmbeddedView.setURL(notesArrayList.get(position).note_description, new URLEmbeddedView.OnLoadURLListener() {
                @Override
                public void onLoadURLCompleted(URLEmbeddedData data) {
                    holder.urlEmbeddedView.title(data.getTitle());
                    holder.urlEmbeddedView.description(data.getDescription());
                    holder.urlEmbeddedView.host(data.getHost());
                    holder.urlEmbeddedView.thumbnail(data.getThumbnailURL());
                    holder.urlEmbeddedView.favor(data.getFavorURL());
                }
            });
        } else {
            holder.urlEmbeddedView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;
        URLEmbeddedView urlEmbeddedView;
        ImageView imgDelete, imgEdit, imgCopy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            imgCopy = itemView.findViewById(R.id.imgCopy);
            urlEmbeddedView = itemView.findViewById(R.id.urlEmbedView);
        }
    }

    public void editCategoryDialog(int note_id, String title, String description, String date, String time, int categoryId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_note, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        EditText edtNoteTitle = dialogView.findViewById(R.id.edtNoteTitle);
        EditText edtNoteDecription = dialogView.findViewById(R.id.edtNoteDecription);
        Button btnAddNote = dialogView.findViewById(R.id.btnAddNote);
        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);

        tvTitle.setText("Edit " + title);
        edtNoteTitle.setText(title);
        edtNoteDecription.setText(description);

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
                    database.updateNote(new Notes(note_id, categoryId, edtNoteTitle.getText().toString().trim(),
                            edtNoteDecription.getText().toString().trim(), date, time), note_id);
                    alertDialog.dismiss();
                    context.startActivity(new Intent(context, NewNoteActivity.class));
                    ((Activity) context).finish();
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