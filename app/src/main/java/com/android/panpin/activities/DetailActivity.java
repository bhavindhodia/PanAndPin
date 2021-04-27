package com.android.panpin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.panpin.R;
import com.android.panpin.fragments.CartFragment;
import com.android.panpin.models.Cake;
import com.android.panpin.models.CartItem;
import com.android.panpin.models.ShopViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static String TAG = DetailActivity.class.getSimpleName();
    Cake cakeData;
    ShopViewModel shopViewModel;
    private LinearLayout linearLayout;
    private int quantity = 1;
    private ElegantNumberButton quantityBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        linearLayout = findViewById(R.id.detailPage);
        quantityBtn = findViewById(R.id.quantityBtn);

        cakeData = getIntent().getParcelableExtra("CakeData");
        assert cakeData != null;
        setMainData(cakeData);


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getCart().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {

            }
        });


        quantityBtn.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = Integer.parseInt(quantityBtn.getNumber());
            }
        });

    }

    private void setMainData(Cake cakeData) {
        TextView detailName = findViewById(R.id.txt_name);
        TextView detailPrice = findViewById(R.id.txt_price);
        TextView detailCalories = findViewById(R.id.detail_calories);
        TextView detailDescription = findViewById(R.id.detail_description);
        TextView detailStars = findViewById(R.id.detail_stars);
        ImageView thumbnail = findViewById(R.id.detail_thumbnail);
        Button buyButton = findViewById(R.id.buyBtn);
        Button cartbutton = findViewById(R.id.cartBtn);


        buyButton.setOnClickListener(onButtonClick(buyButton));
        cartbutton.setOnClickListener(onButtonClick(cartbutton));

        detailName.setText(cakeData.getName());
        detailPrice.setText(getResources().getString(R.string.detail_currency, cakeData.getPrice()));
        detailCalories.setText(String.valueOf(cakeData.getCalories()));
        detailStars.setText(String.valueOf(cakeData.getRating()));
        detailDescription.setText(cakeData.getDescription());

        RequestOptions glideOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(getApplicationContext()).load(cakeData.getImgUrl()).apply(glideOptions).into(thumbnail);


    }

    View.OnClickListener onButtonClick(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == R.id.buyBtn) {
                    Intent i = new Intent(getApplicationContext(), PaymentActivity.class);
                    Log.d(TAG, "onClick: " + cakeData.getPrice() * quantity);
                    String totalPrice = String.valueOf(cakeData.getPrice() * quantity);
                    i.putExtra("EXPRESS_CHECKOUT", true);
                    i.putExtra("ORDER_TOTAL", totalPrice);
                    i.putExtra("TOTAL_QUANTITY", quantity);
                    startActivity(i);
                } else {
                    addItemToCart(cakeData, quantity);

                }
            }
        };
    }

    private void addItemToCart(Cake c, int q) {
        boolean ifAdded = shopViewModel.addToCart(c, q);
        if (ifAdded) {
//            Snackbar.make(linearLayout,c.getName() + " added to cart.", Snackbar.LENGTH_LONG)
//                    .setActionTextColor(getResources().getColor(R.color.colorPrimary))
//                    .setAction("Checkout", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            getSupportFragmentManager().beginTransaction()
//                                    .replace(android.R.id.content,new CartFragment()).commit();
//                            //startActivity(new Intent(getApplicationContext(),CartFragment.this));
//                        }
//
//                    })
//                    .show();
            Snackbar sb = Snackbar.make(linearLayout, getResources().getString(R.string.successCart, c.getName()), Snackbar.LENGTH_LONG);
            sb.setActionTextColor(getResources().getColor(R.color.colorPrimary));
            sb.setAction(getResources().getString(R.string.cart_pc), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(android.R.id.content, new CartFragment()).commit();
                    //startActivity(new Intent(getApplicationContext(),CartFragment.this));
                }

            });
            View sbView = sb.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.success));
            TextView sbText = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            sbText.setTextColor(getResources().getColor(R.color.colorPrimary));
            sb.show();

        } else {
            Snackbar sb = Snackbar.make(linearLayout, getResources().getString(R.string.failedCart), Snackbar.LENGTH_SHORT);
            View sbView = sb.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.error));
            TextView sbText = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            sbText.setTextColor(getResources().getColor(R.color.colorPrimary));
            sb.show();
        }
    }

}
