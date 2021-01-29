package com.example.foodbank.classes;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "settings")
public class Settings {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private boolean darkMode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public Settings(int id, boolean darkMode) {
        this.id = id;
        this.darkMode = darkMode;
    }

    @Ignore
    public Settings(boolean darkMode) {
        this.darkMode = darkMode;
    }


}