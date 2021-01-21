package com.example.foodbank.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodbank.ui.categories.Category;

import java.util.List;

@Dao
public interface CategoriesDao {
    @Insert
    void insert(Category... categories);

    @Update
    void update(Category category);

    @Query("SELECT * FROM categories ORDER BY name ASC")
    List<Category> getCategoriesSortedByTitle();

    @Query("SELECT * FROM categories ORDER BY products DESC")
    List<Category> getCategoriesSortedByProducts();

    @Delete
    void delete(Category category);
}