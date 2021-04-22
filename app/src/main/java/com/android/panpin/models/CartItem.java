package com.android.panpin.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class CartItem {
    public static DiffUtil.ItemCallback<CartItem> itemCallback = new DiffUtil.ItemCallback<CartItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getCakeData().equals(newItem.getCakeData());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.equals(newItem);
        }
    };
    private Cake cakeData;
    private int quantity;

    public CartItem() {
    }

    public CartItem(Cake cakeData, int quantity) {
        this.cakeData = cakeData;
        this.quantity = quantity;
    }

    public Cake getCakeData() {
        return cakeData;
    }

    public void setCakeData(Cake cakeData) {
        this.cakeData = cakeData;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @NonNull
    @Override
    public String toString() {
        return "Cart Items{cakeData => " + cakeData + "},{Quantity" + quantity + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return getQuantity() == cartItem.getQuantity() &&
                getCakeData().equals(cartItem.getCakeData());
    }

}
