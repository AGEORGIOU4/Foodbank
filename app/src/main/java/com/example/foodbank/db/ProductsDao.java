package com.example.foodbank.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodbank.classes.CustomList;
import com.example.foodbank.classes.Product;
import com.example.foodbank.classes.ProductToList;

import java.util.List;

@Dao
public interface ProductsDao {


    /*------------------PRODUCTS------------------*/
    @Insert
    void insert(Product... products);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM products ORDER BY title ASC")
    List<Product> getProductsSortedByTitle();

    @Query("SELECT * FROM products ORDER BY nutriScore")
    List<Product> getProductsSortedByNutriscore();

    @Query("SELECT * FROM products ORDER BY ecoScore ASC")
    List<Product> getProductsSortedByEcoscore();

    @Query("SELECT * FROM products ORDER BY novaGroup ASC")
    List<Product> getProductsSortedByNovaGroup();

    @Query("SELECT * FROM products ORDER BY timestamp DESC")
    List<Product> getProductsSortedByTimestamp();

    /*------------------FAVORITES------------------*/
    @Query("SELECT * FROM products WHERE starred IS 1")
    List<Product> getFavoriteProducts();

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

    /*------------------LISTS------------------*/
    @Insert
    void insert(CustomList... customList);

    @Update
    void update(CustomList customList);

    @Delete
    void delete(CustomList customList);

    @Query("SELECT * FROM customLists ORDER BY name")
    List<CustomList> getCustomLists();

    /*-------------LIST TO PRODUCT-------------*/
    @Insert
    void insert(ProductToList... productToLists);

    @Delete
    void delete(ProductToList productToList);

    @Query("SELECT * FROM products_to_lists")
    List<ProductToList> getProductsToLists();
}