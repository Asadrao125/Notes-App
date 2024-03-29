package com.appsxone.notesapp.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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
import com.appsxone.notesapp.activities.NotesActivity;
import com.appsxone.notesapp.database.Database;
import com.appsxone.notesapp.model.Notes;
import com.appsxone.notesapp.utils.InternetConnection;
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
                editNotesDialog(notesArrayList.get(position).note_id, notesArrayList.get(position).note_title,
                        notesArrayList.get(position).note_description, notesArrayList.get(position).date,
                        notesArrayList.get(position).time, notesArrayList.get(position).category_id, position);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDiaog(notesArrayList.get(position).note_id, position, notesArrayList.get(position).note_title);
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

        holder.tvTime.setText(notesArrayList.get(position).date + " " + notesArrayList.get(position).time);

        if (InternetConnection.isNetworkConnected((Activity) context)) {
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
        } else {
            holder.urlEmbeddedView.setVisibility(View.GONE);
        }

        holder.tvDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descriptionDialog(notesArrayList.get(position).note_description);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvTime;
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
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    public void editNotesDialog(int note_id, String title, String description, String date, String time, int categoryId, int pos) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_note, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        EditText edtNoteTitle = dialogView.findViewById(R.id.edtNoteTitle);
        EditText edtNoteDecription = dialogView.findViewById(R.id.edtNoteDecription);
        Button btnUpdateNote = dialogView.findViewById(R.id.btnUpdateNote);
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

        btnUpdateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtNoteTitle.getText().toString().trim();
                String description = edtNoteDecription.getText().toString().trim();
                if (!title.isEmpty() && !description.isEmpty()) {
                    database.updateNote(new Notes(note_id, categoryId, title, description, date, time), note_id);
                    notesArrayList.get(pos).note_title = title;
                    notesArrayList.get(pos).note_description = description;
                    notifyItemChanged(pos);
                    alertDialog.dismiss();

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

    public void confirmationDiaog(int noteId, int pos, String note_title) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Are you sure you want to delete " + note_title + " ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                database.deleteNote(noteId);
                notesArrayList.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, notesArrayList.size());
                dialog.dismiss();
                if (notesArrayList != null) {
                    NotesActivity.tvNotes.setText("Note: " + notesArrayList.size());
                }
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

    public void descriptionDialog(String descriptio) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(descriptio);
        builder1.setCancelable(true);

        builder1.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}