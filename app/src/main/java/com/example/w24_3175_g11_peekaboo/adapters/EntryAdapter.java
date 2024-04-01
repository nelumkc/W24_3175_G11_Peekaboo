package com.example.w24_3175_g11_peekaboo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.model.Child;
import com.example.w24_3175_g11_peekaboo.model.Entry;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder>{
    private Context context;
    private ArrayList<Entry> entries;

    private EntryAdapter.OnItemClickListener onItemClickListener;

    public EntryAdapter(Context context, ArrayList<Entry> entries, EntryAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.entries = entries;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activityentry,parent,false);
        return new EntryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryAdapter.EntryViewHolder holder, int position) {
        Entry entry = entries.get(position);
        holder.textViewTitle.setText(entry.getEntryTitle());
        if(entry.getEntryDesc()!=null){
            holder.textViewDesc.setText(entry.getEntryDesc());
        }
        if(entry.getEntryImage()!=null){
            Picasso.get().load(new File(entry.getEntryImage())).into(holder.entryImageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null) {
                    onItemClickListener.onItemClick(entry);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle,textViewDesc;
        ImageView entryImageView;
        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textTitle);
            textViewDesc = itemView.findViewById(R.id.textdesc);
            entryImageView  = itemView.findViewById(R.id.entryImage);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Entry entry);
    }
}
