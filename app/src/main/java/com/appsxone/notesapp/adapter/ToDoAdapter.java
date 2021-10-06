package com.appsxone.notesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsxone.notesapp.R;
import com.appsxone.notesapp.activities.ToDoActivity;
import com.appsxone.notesapp.database.Database;
import com.appsxone.notesapp.model.ToDoModel;

import java.util.ArrayList;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
    Context context;
    Database database;
    ArrayList<ToDoModel> toDoModelArrayList;

    public ToDoAdapter(Context c, ArrayList<ToDoModel> message) {
        context = c;
        toDoModelArrayList = message;
        database = new Database(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_todo, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoModel toDoModel = toDoModelArrayList.get(position);
        holder.cbTitle.setText(toDoModel.title);

        if (toDoModelArrayList.get(position).isCompleted == 1) {
            holder.cbTitle.setChecked(true);
        } else {
            holder.cbTitle.setChecked(false);
        }

        holder.cbTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    database.updateTODO(new ToDoModel(toDoModelArrayList.get(position).id,
                            toDoModelArrayList.get(position).title, 1,
                            toDoModelArrayList.get(position).isActive, toDoModelArrayList.get(position).isDeleted,
                            toDoModelArrayList.get(position).date, toDoModelArrayList.get(position).time,
                            toDoModelArrayList.get(position).completeDate), toDoModelArrayList.get(position).id);
                } else {
                    database.updateTODO(new ToDoModel(toDoModelArrayList.get(position).id,
                            toDoModelArrayList.get(position).title, 0,
                            toDoModelArrayList.get(position).isActive, toDoModelArrayList.get(position).isDeleted,
                            toDoModelArrayList.get(position).date, toDoModelArrayList.get(position).time,
                            toDoModelArrayList.get(position).completeDate), toDoModelArrayList.get(position).id);
                }
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.deleteToDo(toDoModelArrayList.get(position).id);
                toDoModelArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, toDoModelArrayList.size());
                if (toDoModelArrayList != null) {
                    ToDoActivity.tvSize.setText("TODO: " + toDoModelArrayList.size());
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return toDoModelArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbTitle;
        ImageView imgDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cbTitle = itemView.findViewById(R.id.cbTitle);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}