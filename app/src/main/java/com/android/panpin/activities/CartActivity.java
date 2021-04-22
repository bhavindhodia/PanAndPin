package com.android.panpin.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.panpin.R;
import com.android.panpin.adapter.CartAdapter;
import com.android.panpin.models.CartItem;
import com.android.panpin.models.ShopViewModel;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartInterface {

    private static String TAG = CartActivity.class.getSimpleName();
    RecyclerView cartRecyclerView;
    ShopViewModel shopViewModel;
    TextView totalPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartRecyclerView = findViewById(R.id.cart_rv);

        totalPrice = findViewById(R.id.orderTotal);

        initCart();


    }

    private void initCart() {
        final CartAdapter cartAdapter = new CartAdapter(this);
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        cartRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getCart().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                cartAdapter.submitList(cartItems);
            }
        });
        shopViewModel.getTotalPrice().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                //Log.d("TAG", "calculateCartTotal: "+aDouble);
                totalPrice.setText("Total Rs. " + aDouble.toString());
            }
        });
    }

    @Override
    public void deleteItem(CartItem cartItem) {
        shopViewModel.removeItemCart(cartItem);
    }
}
