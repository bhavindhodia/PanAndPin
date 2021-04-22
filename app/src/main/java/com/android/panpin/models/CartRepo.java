package com.android.panpin.models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class CartRepo {
    private MutableLiveData<List<CartItem>> mutableCart = new MutableLiveData<>();
    private MutableLiveData<Double> mutableTotalPrice = new MutableLiveData<>();

    public LiveData<List<CartItem>> getCart() {


        if (ifNull()) {
            initCart();
        } else {
            List<CartItem> isCartItemEmpty = Paper.book("cart").read("cartItemList", new ArrayList<CartItem>());

            mutableCart.setValue(isCartItemEmpty);
        }
        calculateCartTotal();
        return mutableCart;
    }

    public void initCart() {
        mutableCart.setValue(new ArrayList<CartItem>());

    }

    public boolean addItemToCart(Cake cake, int quantity) {
        if (mutableCart.getValue() == null) {
            initCart();
        }
        List<CartItem> cartItemList = new ArrayList<>(mutableCart.getValue());

        for (CartItem cartItem : cartItemList) {
            if (cartItem.getCakeData().getName().equals(cake.getName())) {
                int index = cartItemList.indexOf(cartItem);
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItemList.set(index, cartItem);
                Paper.book("cart").write("cartItemList", cartItemList);
                mutableCart.setValue(cartItemList);
                Log.d("TAG", "addItemToCart: " + cartItem.getQuantity());
                calculateCartTotal();
                return true;
            }
        }

        CartItem cartItem = new CartItem(cake, quantity);
        cartItemList.add(cartItem);
        Log.d("TAG", "addItemToCart: " + cartItemList.size());
        Paper.book("cart").write("cartItemList", cartItemList);
        mutableCart.setValue(cartItemList);
        calculateCartTotal();
        return true;
    }

    private void calculateCartTotal() {
        if (mutableCart.getValue() == null) return;
        double total = 0.0;
        List<CartItem> cartItemList = mutableCart.getValue();
        for (CartItem cartItem : cartItemList) {
            //total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
            total += cartItem.getCakeData().getPrice() * cartItem.getQuantity();
        }
        Log.d("TAG", "calculateCartTotal: " + total);
        //Paper.book("cart").write("cartTotalPrice",total);
        mutableTotalPrice.setValue(total);

    }


    public LiveData<Double> getTotalPrice() {
        if (mutableTotalPrice.getValue() == null) {
            mutableTotalPrice.setValue(0.0);
        }

        return mutableTotalPrice;
    }

    public boolean removeItemCart(CartItem cartItem) {
        if (mutableCart.getValue() == null) {
            return false;
        }
        List<CartItem> cartItemList = new ArrayList<>(mutableCart.getValue());
        cartItemList.remove(cartItem);
        Paper.book("cart").write("cartItemList", cartItemList);
        mutableCart.setValue(cartItemList);
        calculateCartTotal();
        return true;
    }

    private boolean ifNull() {
        List<CartItem> isCartItemEmpty = Paper.book("cart").read("cartItemList", new ArrayList<CartItem>());
        if (mutableCart.getValue() == null && isCartItemEmpty.size() == 0) return true;
        final boolean b = false;
        return b;
    }
}
