package com.android.panpin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.panpin.R;
import com.android.panpin.activities.PaymentActivity;
import com.android.panpin.adapter.CartAdapter;
import com.android.panpin.models.CartItem;
import com.android.panpin.models.ShopViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import io.paperdb.Paper;

public class CartFragment extends Fragment implements CartAdapter.CartInterface {

    private View root;
    private ScrollView scrollView;
    RecyclerView cartRecyclerView;
    ShopViewModel shopViewModel;
    TextView totalPrice;
    private Integer quantity = 0;
    private String totalPrices;

    public CartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cart, container, false);
        Paper.init(requireContext());
        initVariables();
        return root;
    }

    private void initVariables() {

        cartRecyclerView = root.findViewById(R.id.cart_rv);
        totalPrice = root.findViewById(R.id.orderTotal);
        Button placeOrder = root.findViewById(R.id.placeOrderButton);
        scrollView = root.findViewById(R.id.cart_layout);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PaymentActivity.class);
                i.putExtra("EXPRESS_CHECKOUT", false);
                i.putExtra("ORDER_TOTAL", totalPrices);
                i.putExtra("TOTAL_QUANTITY", quantity);
                startActivity(i);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final CartAdapter cartAdapter = new CartAdapter(this);
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        cartRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        shopViewModel.getCart().observe(getViewLifecycleOwner(), new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                cartAdapter.submitList(cartItems);
                for (CartItem cartItem : cartItems) {
                    quantity += cartItem.getQuantity();
                }
            }
        });
        shopViewModel.getTotalPrice().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                totalPrice.setText(getString(R.string.cart_total_price, aDouble));
                totalPrices = String.valueOf(aDouble);
            }
        });
    }


    @Override
    public void deleteItem(CartItem cartItem) {
        boolean ifSuccess = shopViewModel.removeItemCart(cartItem);
        if (ifSuccess) {
            Snackbar sb = Snackbar.make(scrollView, getString(R.string.snackbar_cart_delete_s, cartItem.getCakeData().getName()), Snackbar.LENGTH_LONG);
            View sbView = sb.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.warning));
            TextView sbText = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            sbText.setTextColor(getResources().getColor(R.color.darkGrey));
            sb.show();
        } else {
            Snackbar sb = Snackbar.make(scrollView, getString(R.string.snackbar_cart_delete_f, cartItem.getCakeData().getName()), Snackbar.LENGTH_SHORT);
            View sbView = sb.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.error));
            TextView sbText = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            sbText.setTextColor(getResources().getColor(R.color.colorPrimary));
            sb.show();
        }
    }
}