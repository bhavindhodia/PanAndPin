package com.android.panpin.models;

public class Cake {
    private String name;
    private String imgUrl;
    private String description;
    private int price;
    private int rating;
    private int calories;

    public Cake() {
    }

    public Cake(String name, String imgUrl, String description, int price, int rating, int calories) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getRating() {
        return rating;
    }

    public int getCalories() {
        return calories;
    }
}
