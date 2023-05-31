package com.example.imagesearch.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagesearch.Interface.AdapterClick;
import com.example.imagesearch.Model.Color;
import com.example.imagesearch.R;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder>  {
    private List<Color> mlist = new ArrayList<>();
    private Integer selectedPosition = -1;
    private Context context;
    private final AdapterClick adapterClick;


    public ColorAdapter(List<Color> mlist, Context context, AdapterClick adapterClick) {
        this.mlist = mlist;
        this.context = context;
        this.adapterClick = adapterClick;
    }

    public void setData(List<Color> list){
        this.mlist = list;
        notifyDataSetChanged();
    }
    public void updateData(Integer position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ColorAdapter.ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
        return new ColorAdapter.ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorAdapter.ColorViewHolder holder, int position) {
       Color itemColor = mlist.get(position);
       holder.itemColor.setBackgroundTintList(ColorStateList.valueOf(context.getColor(itemColor.getColorInt())));
        holder.itemColorChecked.setVisibility(View.GONE);
        if (selectedPosition == position) {
            holder.itemColorChecked.setVisibility(View.VISIBLE);
        } else {
            holder.itemColorChecked.setVisibility(View.GONE);
        }
       holder.itemColor.setOnClickListener(view -> {
           adapterClick.itemColorClick(itemColor,position);
           updateData(position);

       });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CardView itemColor;
        private ImageView itemColorChecked;
        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            itemColor = itemView.findViewById(R.id.color);
            itemColorChecked = itemView.findViewById(R.id.item_color_checked);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            adapterClick.itemColorClick(mlist.get(position),position);
        }
    }

}