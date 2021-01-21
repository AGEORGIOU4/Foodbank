package com.example.foodbank.ui.categories;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    private final String url;
    private final String id;
    private final String name;
    private final int products;
    private final int known;
    @PrimaryKey(autoGenerate = true)
    private int pkId;


    public Category(int pkId, String url, String id, String name, int products, int known) {
        this.pkId = pkId;
        this.url = url;
        this.id = id;
        this.name = name;
        this.products = products;
        this.known = known;
    }

    @Ignore
    public Category(String url, String id, String name, int products, int known) {
        this.url = url;
        this.id = id;
        this.name = name;
        this.products = products;
        this.known = known;
    }

    public int getPkId() {
        return pkId;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getProducts() {
        return products;
    }

    public int getKnown() {
        return known;
    }

    @Override
    public String toString() {
        return name + " - " + products;
    }
}
