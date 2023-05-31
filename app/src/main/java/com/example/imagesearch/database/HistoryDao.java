package com.example.imagesearch.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.imagesearch.Model.ItemHistory;

import java.util.List;

@Dao
public interface HistoryDao {
    @Insert
    void insertHistory(ItemHistory itemHistory);

    @Query("SELECT * FROM history ORDER BY id DESC")
    List<ItemHistory> getListHistory();

    @Delete
    void deleteItemHistory(ItemHistory itemHistory);
    @Query("DELETE FROM history")
    void delete();
}
