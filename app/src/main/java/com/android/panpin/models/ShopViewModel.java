package com.android.panpin.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ShopViewModel extends ViewModel {
    CartRepo cartRepo = new CartRepo();

    public LiveData<List<CartItem>> getCart() {
        return cartRepo.getCart();
    }

    public boolean addToCart(Cake cake, int quantity) {
        return cartRepo.addItemToCart(cake, quantity);
    }

    public boolean removeItemCart(CartItem cartItem) {
        return cartRepo.removeItemCart(cartItem);
    }

    public boolean removeAllItemCart() {
        return cartRepo.removeAllItemCart();
    }

    public LiveData<Double> getTotalPrice() {
        return cartRepo.getTotalPrice();
    }


    public int getQuantity(CartItem cartItem) {
        return cartRepo.getQuantity(cartItem);
    }

    public LiveData<Integer> getAllQuantity() {
        return cartRepo.getAllQuantity();
    }
}
