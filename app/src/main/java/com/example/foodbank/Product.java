package com.example.foodbank;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String grade;
    private int novaGroup;
    private boolean starred;

    public Product(int id, String title, String grade, int novaGroup, boolean starred) {
        this.id = id;
        this.title = title;
        this.grade = grade;
        this.novaGroup = novaGroup;
        this.starred = starred;
    }

    @Ignore
    public Product(String title, String grade, int novaGroup, boolean starred) {
        this.title = title;
        this.grade = grade;
        this.novaGroup = novaGroup;
        this.starred = starred;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getGrade() {
        return grade;
    }

    public int getNovaGroup() {
        return novaGroup;
    }

    public boolean isStarred() {
        return starred;
    }

}