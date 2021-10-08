package com.appsxone.notesapp.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsxone.notesapp.R;
import com.appsxone.notesapp.activities.MakeImageActivity;
import com.appsxone.notesapp.database.Database;

import java.util.ArrayList;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.MyViewHolder> {
    Context context;
    Database database;
    ArrayList<String> quoteList;

    public QuoteAdapter(Context c, ArrayList<String> message) {
        context = c;
        quoteList = message;
        database = new Database(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_quote, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvQuote.setText(quoteList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup(holder.itemView, position);
            }
        });
    }

    private void popup(View itemView, int pos) {
        PopupMenu popup = new PopupMenu(context, itemView);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.btnCopy:
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label", quoteList.get(pos));
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.btnShare:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, quoteList.get(pos));
                        sendIntent.setType("text/plain");
                        context.startActivity(sendIntent);
                        return true;
                    case R.id.btnMakeImage:
                        Intent intent = new Intent(context, MakeImageActivity.class);
                        intent.putExtra("quote", quoteList.get(pos));
                        context.startActivity(intent);
                }
                return false;
            }
        });
        popup.inflate(R.menu.quote_menu);
        popup.show();
    }

    @Override
    public int getItemCount() {
        return quoteList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuote;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuote = itemView.findViewById(R.id.tvQuote);
        }
    }
}