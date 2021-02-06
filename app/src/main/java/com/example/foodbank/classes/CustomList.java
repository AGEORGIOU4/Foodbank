package com.example.foodbank.classes;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "customLists")
public class CustomList {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private int numOfProducts;
    private long timestamp;
    private String color;

    public CustomList(int id, String name, String description, int numOfProducts, long timestamp, String color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numOfProducts = numOfProducts;
        this.timestamp = timestamp;
        this.color = color;
    }

    @Ignore
    public CustomList(String name, String description, int numOfProducts, long timestamp, String color) {
        this.name = name;
        this.description = description;
        this.numOfProducts = numOfProducts;
        this.timestamp = timestamp;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getNumOfProducts() {
        return numOfProducts;
    }

    public void setNumOfProducts(int numOfProducts) {
        this.numOfProducts = numOfProducts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}