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

        if (product_name == null || product_name.equals("")) {
            this.product_name = "unknown";
        } else {
            this.product_name = product_name;
        }

        // Set constraints
        switch (nutriscore_grade) {
            case "1":
            case "a":
                this.nutriscore_grade = "A";
                break;
            case "2":
            case "b":
                this.nutriscore_grade = "B";
                break;
            case "3":
            case "c":
                this.nutriscore_grade = "C";
            case "4":
            case "d":
                this.nutriscore_grade = "D";
                break;
            case "5":
            case "e":
                this.nutriscore_grade = "E";
            case "A":
            case "B":
            case "C":
            case "D":
            case "E":
                this.nutriscore_grade = nutriscore_grade;
                break;
            default:
                this.nutriscore_grade = "Unknown";
        }

        switch (ecoscore_grade) {
            case "1":
            case "a":
                this.ecoscore_grade = "A";
                break;
            case "2":
            case "b":
                this.ecoscore_grade = "B";
                break;
            case "3":
            case "c":
                this.ecoscore_grade = "C";
            case "4":
            case "d":
                this.ecoscore_grade = "D";
                break;
            case "5":
            case "e":
                this.ecoscore_grade = "E";
            case "A":
            case "B":
            case "C":
            case "D":
            case "E":
                this.ecoscore_grade = ecoscore_grade;
                break;
            default:
                this.ecoscore_grade = "Unknown";
        }

        switch (nova_group) {
            case "1":
            case "a":
                this.nova_group = "A";
                break;
            case "2":
            case "b":
                this.nova_group = "B";
                break;
            case "3":
            case "c":
                this.nova_group = "C";
            case "4":
            case "d":
                this.nova_group = "D";
                break;
            case "5":
            case "e":
                this.nova_group = "E";
            case "A":
            case "B":
            case "C":
            case "D":
            case "E":
                this.nova_group = nova_group;
                break;
            default:
                this.nova_group = "Unknown";
        }

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


