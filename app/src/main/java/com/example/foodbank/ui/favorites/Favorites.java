package com.example.foodbank.ui.favorites;

import com.example.foodbank.ui.products.Product;

import java.util.Vector;

public class Favorites {
    public Vector<Product> favorites;

    public Vector<Product> getFavorites() {
        return favorites;
    }

    public void setFavorites(Vector<Product> favorites) {
        this.favorites = favorites;
    }
}
