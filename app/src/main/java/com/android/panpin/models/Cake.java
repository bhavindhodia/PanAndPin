package com.android.panpin.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Cake implements Parcelable {
    private String name;
    private String imgUrl;
    private String description;
    private int price;
    private int rating;
    private int calories;

    public static final Parcelable.Creator<Cake> CREATOR = new Parcelable.Creator<Cake>() {
        @Override
        public Cake createFromParcel(Parcel source) {
            return new Cake(source);
        }

        @Override
        public Cake[] newArray(int size) {
            return new Cake[size];
        }
    };


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

    public Cake() {
    }

    protected Cake(Parcel source) {
        name = source.readString();
        imgUrl = source.readString();
        description = source.readString();
        price = source.readInt();
        rating = source.readInt();
        calories = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imgUrl);
        dest.writeString(description);
        dest.writeInt(price);
        dest.writeInt(rating);
        dest.writeInt(calories);

    }


}
