package com.example.w24_3175_g11_peekaboo.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.model.Child;
import com.example.w24_3175_g11_peekaboo.model.Entry;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        String imageUrl = entry.getEntryImage();
        holder.textViewTitle.setText(entry.getEntryTitle());
        if(entry.getEntryDesc()!=null){
            holder.textViewDesc.setText(entry.getEntryDesc());
        }
        if(entry.getEntryImage()!=null){
            Glide.with(context).load(entry.getEntryImage()).into(holder.entryImageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null) {
                    onItemClickListener.onItemClick(entry);
                }
            }
        });

        holder.downloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage(imageUrl);
            }
        });



    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle,textViewDesc;
        ImageView entryImageView,downloadIcon;
        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textTitle);
            textViewDesc = itemView.findViewById(R.id.textdesc);
            entryImageView  = itemView.findViewById(R.id.entryImage);
            downloadIcon = itemView.findViewById(R.id.downloadIcon);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Entry entry);
    }

    public void downloadImage(String imageUrl){
        // Create a reference to the Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReferenceFromUrl(imageUrl);

        // Get the external storage directory and create a unique file name
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, fileName);

        imageRef.getFile(imageFile).addOnSuccessListener(taskSnapshot -> {
            // Image downloaded successfully, now let's save it to the gallery
            addPictureToGallery(imageFile.getAbsolutePath());
            Toast.makeText(context, "Image Downloaded and saved to Gallery", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show();
        });
    }

    private void addPictureToGallery(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
