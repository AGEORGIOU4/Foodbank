package com.example.foodbank;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String barcode;
    private String title;
    private String nutriScore;
    private String novaGroup;
    private String ecoScore;
    private String ingredients;
    private String nutrients;
    private boolean starred;
    private long timestamp;

    public Product(int id, String barcode, String title, String nutriScore, String novaGroup, String ecoScore, String ingredients, String nutrients, boolean starred, long timestamp) {
        this.id = id;
        this.barcode = barcode;
        this.title = title;
        this.nutriScore = nutriScore;
        this.novaGroup = novaGroup;
        this.ecoScore = ecoScore;
        this.ingredients = ingredients;
        this.nutrients = nutrients;
        this.starred = starred;
        this.timestamp = timestamp;
    }


    @Ignore
    public Product(String barcode, String title, String nutriScore, String novaGroup, String ecoScore, String ingredients, String nutrients, boolean starred, long timestamp) {
        this.title = title;
        this.barcode = barcode;
        this.nutriScore = nutriScore;
        this.novaGroup = novaGroup;
        this.ecoScore = ecoScore;
        this.ingredients = ingredients;
        this.nutrients = nutrients;
        this.starred = starred;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() { return barcode; }

    public String getTitle() {
        return title;
    }

    public String getNutriScore() {
        return nutriScore;
    }

    public String getNovaGroup() {
        return novaGroup;
    }

    public String getEcoScore() { return ecoScore; }

    public String getIngredients() { return ingredients; }

    public String getNutrients() { return nutrients; }

    public boolean isStarred() {
        return starred;
    }

    public long getTimestamp() { return timestamp; }
}