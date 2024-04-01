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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {
    private Context context;
    private ArrayList<Child> children;

    private OnItemClickListener onItemClickListener;
    private boolean isChatEnabled;

    public ChildAdapter(Context context, ArrayList<Child> children, boolean isChatEnabled, OnItemClickListener listener) {
        this.context = context;
        this.children = children;
        this.isChatEnabled = isChatEnabled;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.childentry,parent,false);
        return new ChildViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        Child child = children.get(position);
        holder.textViewName.setText(child.getChildFname());
        if(child.getChildDob()!=null){
            holder.textViewDob.setText(child.getChildDob());
        }
        if(child.getChildImage()!=null){
            Picasso.get().load(new File(child.getChildImage())).into(holder.profileImageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null) {
                    onItemClickListener.onItemClick(child);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName,textViewDob;
        ImageView profileImageView;
        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textName);
            textViewDob = itemView.findViewById(R.id.textdob);
            profileImageView  = itemView.findViewById(R.id.picProfile);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Child child);
    }

}
