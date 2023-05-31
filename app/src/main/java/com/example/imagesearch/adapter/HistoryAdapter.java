package com.example.imagesearch.adapter;

import android.content.Context;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagesearch.Interface.AdapterClick;
import com.example.imagesearch.Model.ItemHistory;
import com.example.imagesearch.R;
import com.example.imagesearch.database.HistoryDao;
import com.example.imagesearch.database.HistoryDatabase;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<ItemHistory> mlist = new ArrayList<>();
    Context context;
    private final ClickItem adapterClick;

    public HistoryAdapter(Context context,List<ItemHistory> mlist, ClickItem adapterClick) {
        this.context = context;
        this.mlist = mlist;
        this.adapterClick = adapterClick;
    }

    public void setData(List<ItemHistory> list){
        this.mlist = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        ItemHistory itemHistory = mlist.get(position);
        holder.historyItem.setText(itemHistory.getHistoryItem());
        holder.historyItem.setOnClickListener(view -> {
               adapterClick.onClick(itemHistory.getHistoryItem());

        });
        holder.deleteItem.setOnClickListener(view -> {
            HistoryDatabase.getInstance(context).historyDao().deleteItemHistory(itemHistory);
            adapterClick.upDateData();
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView historyItem;
        private ImageView deleteItem;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historyItem = itemView.findViewById(R.id.item_history);
            deleteItem = itemView.findViewById(R.id.img_delete);
        }

    }
    public interface ClickItem{
        void onClick(String history);
        void upDateData();
    }

}
