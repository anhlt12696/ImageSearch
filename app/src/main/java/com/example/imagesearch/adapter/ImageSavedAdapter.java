package com.example.imagesearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imagesearch.Interface.AdapterClick;
import com.example.imagesearch.Model.Hits;
import com.example.imagesearch.Model.ItemHistory;
import com.example.imagesearch.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageSavedAdapter extends RecyclerView.Adapter {

    private final Context context;
    private List<String> imgList ;
    private final AdapterClick adapterClick;


    public ImageSavedAdapter(Context context, List<String> imgList, AdapterClick adapterClick) {
        this.context = context;
        this.imgList = imgList;
        this.adapterClick = adapterClick;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.image_saved_layout,parent,false);
        return new ImageSavedAdapter.ImageThumbnailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ImageSavedAdapter.ImageThumbnailViewHolder) holder).bidData(imgList.get(position));
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }
    public void setData(List<String> list){
        this.imgList = list;
        notifyDataSetChanged();
    }

    public class ImageThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivThumbnail;
        ImageView ivDelete;
        public ImageThumbnailViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            ivDelete = itemView.findViewById(R.id.ic_delete);
            itemView.setOnClickListener(this);
        }
        public void bidData(String url) {
            Glide.with(context).load(url).into(ivThumbnail);
            ivDelete.setOnClickListener(view -> {
                File fdelete = new File(url);
                if (fdelete.exists()) {
                    fdelete.delete();
                    imgList.remove(url);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            adapterClick.itemImageClick(imgList.get(position),position);
        }
    }
}