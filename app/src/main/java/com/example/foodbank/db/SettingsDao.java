package com.example.foodbank.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodbank.classes.Settings;

import java.util.List;

@Dao
public interface SettingsDao {


    @Insert
    void insert(Settings... settings);

    @Update
    void update(Settings settings);

    @Query("SELECT * FROM settings")
    List<Settings> getSettings();

    @Delete
    void delete(Settings settings);
}