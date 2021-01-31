package com.example.foodbank.classes;

import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;


@Entity(tableName = "products_to_lists", primaryKeys = {"product_code", "list_id"})
public class ProductToList {

    @NotNull
    private String product_code;
    private int list_id;


    public ProductToList(String product_code, int list_id) {
        this.product_code = product_code;
        this.list_id = list_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getList_id() {
        return list_id;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }
}