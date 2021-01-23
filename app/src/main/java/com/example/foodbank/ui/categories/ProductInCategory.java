package com.example.foodbank.ui.categories;

public class ProductInCategory {
    private String product_name;
    private String code;
    private String nutriscore_grade;
    private String ecoscore_grade;
    private String nova_group;
    private String image_small_url;

    public ProductInCategory(String product_name, String code, String nutriscore_grade, String ecoscore_grade, String nova_group, String image_small_url) {
        this.product_name = product_name;
        this.code = code;
        this.nutriscore_grade = nutriscore_grade;
        this.ecoscore_grade = ecoscore_grade;
        this.nova_group = nova_group;
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

    public String getImage_small_url() {
        return image_small_url;
    }

    @Override
    public String toString() {
        return product_name;
    }

}


