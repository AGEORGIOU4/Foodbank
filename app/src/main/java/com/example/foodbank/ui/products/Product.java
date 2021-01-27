package com.example.foodbank.ui.products;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "products")
public class Product {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String barcode;
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNutriScore(String nutriScore) {
        this.nutriScore = nutriScore;
    }

    public void setNovaGroup(String novaGroup) {
        this.novaGroup = novaGroup;
    }

    public void setEcoScore(String ecoScore) {
        this.ecoScore = ecoScore;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    private String nutriScore;
    private String novaGroup;
    private String ecoScore;
    private String ingredients;
    private String nutriments;
    private String vegan;
    private String vegetarian;
    private String categoriesImported;
    private boolean starred;
    private long timestamp;
    private String imageUrl;

    public Product(int id, String barcode, String title, String nutriScore, String novaGroup, String ecoScore, String ingredients, String nutriments,
                   String vegan, String vegetarian, String categoriesImported, boolean starred, long timestamp, String imageUrl) {
        this.id = id;
        this.barcode = barcode;
        this.title = title;
        this.nutriScore = nutriScore;
        this.novaGroup = novaGroup;
        this.ecoScore = ecoScore;
        this.ingredients = ingredients;
        this.nutriments = nutriments;
        this.vegan = vegan;
        this.vegetarian = vegetarian;
        this.categoriesImported = categoriesImported;
        this.starred = starred;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
    }

    @Ignore
    public Product(String barcode, String title, String nutriScore, String novaGroup, String ecoScore, String ingredients, String nutriments,
                   String vegan, String vegetarian, String categoriesImported, boolean starred, long timestamp, String imageUrl) {
        this.title = title;
        this.barcode = barcode;
        this.nutriScore = nutriScore;
        this.novaGroup = novaGroup;
        this.ecoScore = ecoScore;
        this.ingredients = ingredients;
        this.nutriments = nutriments;
        this.vegan = vegan;
        this.vegetarian = vegetarian;
        this.categoriesImported = categoriesImported;
        this.starred = starred;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
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

    public String getNutriments() { return nutriments; }

    public String getVegan() { return vegan; }

    public String getVegetarian() { return vegetarian; }

    public String getCategoriesImported() { return categoriesImported; }

    public boolean isStarred() {
        return starred;
    }

    public long getTimestamp() { return timestamp; }

    public String getImageUrl() { return imageUrl; }
}