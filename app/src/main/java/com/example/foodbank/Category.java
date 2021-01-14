package com.example.foodbank;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private long timestamp;

    public Category(int id, String title, long timestamp) {
        this.id = id;
        this.title = title;
        this.timestamp = timestamp;
    }

    @Ignore
    public Category(String title, long timestamp) {
        this.title = title;

        this.timestamp = timestamp;
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

    public long getTimestamp() { return timestamp; }

}