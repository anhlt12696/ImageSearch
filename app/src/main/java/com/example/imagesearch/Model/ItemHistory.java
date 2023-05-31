package com.example.imagesearch.Model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class ItemHistory {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String historyItem;

    public ItemHistory(String historyItem) {
        this.historyItem = historyItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHistoryItem() {
        return historyItem;
    }

    public void setHistoryItem(String historyItem) {
        this.historyItem = historyItem;
    }

    @Override
    public int hashCode() {
        return historyItem.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(!(obj instanceof ItemHistory))
            return false;
        ItemHistory itemHistory = (ItemHistory) obj;
        return itemHistory.historyItem.equals(historyItem);
    }
}
