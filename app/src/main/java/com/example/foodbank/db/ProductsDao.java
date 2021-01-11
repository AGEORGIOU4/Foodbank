package com.example.foodbank.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodbank.Product;

import java.util.List;

@Dao
public interface ProductsDao {


    @Insert
    void insert(Product... products);

    @Update
    void update(Product product);

    @Query("SELECT * FROM products ORDER BY title ASC")
    List<Product> getProductsSortedByTitle();

    @Query("SELECT * FROM products ORDER BY grade DESC")
    List<Product> getProductsSortedByGrade();

    @Delete
    void delete(Product product);
}