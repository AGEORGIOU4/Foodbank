package com.example.foodbank.ui.categories;

public class ProductInCategory {
    private String code;
    private String product_name;
    private String nutriscore_grade;
    private String ecoscore_grade;
    private String nova_group;
    private String ingredients_text = "";
    private String vegan;
    private String vegetarian;
    private String categories;
    private boolean starred;
    private String image_small_url = "";

    public ProductInCategory(String code, String product_name, String nutriscore_grade, String ecoscore_grade,
                             String nova_group, String ingredients_text, String vegan, String vegetarian,
                             String categories, String image_small_url) {
        this.code = code;
        this.product_name = product_name;
        this.nutriscore_grade = nutriscore_grade;
        this.ecoscore_grade = ecoscore_grade;
        this.nova_group = nova_group;
        this.ingredients_text = ingredients_text;
        this.vegan = vegan;
        this.vegetarian = vegetarian;
        this.categories = categories;
        this.image_small_url = image_small_url;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getCode() {
        return code;
    }

    public String getNutriscore_grade() {
        return nutriscore_grade;
    }

    public String getEcoscore_grade() {
        return ecoscore_grade;
    }

    public String getNova_group() {
        return nova_group;
    }

    public String getIngredients_text() { return ingredients_text; }

    public String getVegan() { return vegan; }

    public String getVegetarian() { return vegetarian; }

    public String getCategories() { return categories; }

    public String getImage_small_url() {
        return image_small_url;
    }

    @Override
    public String toString() {
        return product_name;
    }

}


