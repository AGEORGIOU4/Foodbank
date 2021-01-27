package com.example.foodbank.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodbank.ui.products.Product;

import java.util.List;

@Dao
public interface ProductsDao {


    @Insert
    void insert(Product... products);

    @Update
    void update(Product product);

    @Query("SELECT * FROM products ORDER BY title ASC")
    List<Product> getProductsSortedByTitle();

    @Query("SELECT * FROM products ORDER BY nutriScore ASC")
    List<Product> getProductsSortedByNutriscore();

    @Query("SELECT * FROM products ORDER BY ecoScore ASC")
    List<Product> getProductsSortedByEcoscore();

    @Query("SELECT * FROM products ORDER BY novaGroup ASC")
    List<Product> getProductsSortedByNovaGroup();

    @Query("SELECT * FROM products ORDER BY timestamp DESC")
    List<Product> getProductsSortedByTimestamp();

    @Query("SELECT * FROM products WHERE starred IS 1")
    List<Product> getProductsFavorites();

    @Query("SELECT * FROM products WHERE starred IS 1 ORDER BY title ASC")
    List<Product> getFavoriteProductsSortedByTitle();

    @Query("SELECT * FROM products WHERE starred IS 1 ORDER BY nutriScore ASC")
    List<Product> getFavoriteProductsSortedByNutriscore();

    @Query("SELECT * FROM products WHERE starred IS 1 ORDER BY ecoScore ASC")
    List<Product> getFavoriteProductsSortedByEcoscore();

    @Query("SELECT * FROM products WHERE starred IS 1 ORDER BY novaGroup ASC")
    List<Product> getFavoriteProductsSortedByNovaGroup();

    @Query("SELECT * FROM products WHERE starred IS 1 ORDER BY timestamp DESC")
    List<Product> getFavoriteProductsSortedByTimestamp();


    @Delete
    void delete(Product product);
}