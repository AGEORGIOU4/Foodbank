package com.example.foodbank.classes;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "products_to_lists")
public class ProductToList {
    @PrimaryKey(autoGenerate = true)
    private int product_to_list_id;
    private String product_code;
    private String list_id;

    public ProductToList(int product_to_list_id, String product_code, String list_id) {
        this.product_to_list_id = product_to_list_id;
        this.product_code = product_code;
        this.list_id = list_id;
    }


    @Ignore
    public ProductToList(String product_code, String list_id) {
        this.product_code = product_code;
        this.list_id = list_id;
    }

    public int getProduct_to_list_id() {
        return product_to_list_id;
    }

    public void setProduct_to_list_id(int product_to_list_id) {
        this.product_to_list_id = product_to_list_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }
}