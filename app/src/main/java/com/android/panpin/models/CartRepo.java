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
    private MutableLiveData<Integer> mutableQuantities = new MutableLiveData<>();

    public LiveData<List<CartItem>> getCart() {
        List<CartItem> isCartItemEmpty = Paper.book("cart").read("cartItemList", new ArrayList<CartItem>());
        if (mutableCart.getValue() == null && isCartItemEmpty.size() == 0) {
            initCart();
        }
        mutableCart.setValue(isCartItemEmpty);
        calculateCartTotal();
        return mutableCart;
    }

    public void initCart() {
        Paper.book("cart").write("cartItemList", new ArrayList<CartItem>());
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
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
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
            total += cartItem.getCakeData().getPrice() * cartItem.getQuantity();
        }
        Log.d("TAG", "calculateCartTotal: " + total);
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

    public boolean removeAllItemCart() {
        if (mutableCart.getValue() == null) {
            return false;
        }
        initCart();
        return true;
    }

    public int getQuantity(CartItem cartItem) {
        if (mutableCart.getValue() == null) {
            return 1;
        }
        List<CartItem> cartItemList = new ArrayList<>(mutableCart.getValue());
        int index = cartItemList.indexOf(cartItem);
        return cartItemList.get(index).getQuantity();
    }

    public LiveData<Integer> getAllQuantity() {
        if (mutableCart.getValue() == null) {
            mutableQuantities.setValue(0);
            //return mutableQuantities;
        }
        //List<CartItem> cartItemList = new ArrayList<>(mutableCart.getValue());
        List<CartItem> cartItemList = Paper.book("cart").read("cartItemList", new ArrayList<CartItem>());
        int q = 0;
        for (CartItem cartItem : cartItemList) {
            q += cartItem.getQuantity();
        }
        Log.d("TAG", "getAllQuantity: " + q);
        mutableQuantities.setValue(q);
        return mutableQuantities;
    }

}
