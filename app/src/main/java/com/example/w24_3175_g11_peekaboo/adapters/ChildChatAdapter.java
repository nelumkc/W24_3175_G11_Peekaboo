package com.example.w24_3175_g11_peekaboo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.model.Child;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ChildChatAdapter extends RecyclerView.Adapter<ChildChatAdapter.ChildChatViewHolder> {
    private Context context;
    private ArrayList<Child> children;

    private Consumer<Child> onChildClicked;



    public ChildChatAdapter(Context context, ArrayList<Child> children,Consumer<Child> onChildClicked) {
        this.context = context;
        this.children = children;
        this.onChildClicked = onChildClicked;
    }

    @NonNull
    @Override
    public ChildChatAdapter.ChildChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.childentry,parent,false);
        return new ChildChatAdapter.ChildChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildChatAdapter.ChildChatViewHolder holder, int position) {
        Child child = children.get(position);
        holder.textViewName.setText(child.getChildFname());
        if(child.getChildDob()!=null){
            holder.textViewDob.setText(child.getChildDob());
        }
        if(child.getChildImage()!=null){
            Picasso.get().load(new File(child.getChildImage())).into(holder.profileImageView);
        }

        holder.itemView.setOnClickListener(v -> onChildClicked.accept(child));

    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    public class ChildChatViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName,textViewDob;
        ImageView profileImageView;
        public ChildChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textName);
            textViewDob = itemView.findViewById(R.id.textdob);
            profileImageView  = itemView.findViewById(R.id.picProfile);
        }
    }



}
