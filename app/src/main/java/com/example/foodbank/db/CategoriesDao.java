package com.example.foodbank.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodbank.Category;
import com.example.foodbank.Product;

import java.util.List;

@Dao
public interface CategoriesDao {

    @Insert
    void insert(Category... categories);

    @Update
    void update(Category categories);

    @Query("SELECT * FROM categories ORDER BY title ASC")
    List<Category> getCategoriesSortedByTitle();

    @Query("SELECT * FROM categories ORDER BY timestamp DESC")
    List<Category> getCategoriesSortedByTimestamp();

    @Delete
    void delete(Category categories);
}