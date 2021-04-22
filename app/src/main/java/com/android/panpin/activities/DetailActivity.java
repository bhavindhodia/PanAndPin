package com.android.panpin.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.panpin.R;
import com.android.panpin.models.Cake;
import com.android.panpin.models.CartItem;
import com.android.panpin.models.ShopViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static String TAG = DetailActivity.class.getSimpleName();
    Cake cakeData;
    ShopViewModel shopViewModel;
    private int quantity = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        cakeData = getIntent().getParcelableExtra("CakeData");
        //cakeList = Paper.book("cart").read("cakeList",new HashSet<Cake>());
        assert cakeData != null;
        setMainData(cakeData);

        //shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getCart().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                Log.d(TAG, "onChanged: " + cartItems.size() + " = > " + cartItems.toString());
            }
        });

        final ElegantNumberButton quantityBtn = (ElegantNumberButton) findViewById(R.id.quantityBtn);
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
        detailPrice.setText("Rs. " + cakeData.getPrice());
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
                    switchView(PaymentActivity.class);
                } else {
                    //switchView(CartActivity.class);
                    //cakeList.add(cakeData);
                    addItemToCart(cakeData, quantity);
                    //Paper.book("cart").write("cakeList",cakeList);

                }
            }
        };
    }

    private void addItemToCart(Cake c, int q) {

        shopViewModel.addToCart(c, q);
        //Log.d(TAG, "addItemToCart: "+isAdded+ " => "+ );
    }

    private void switchView(Class<? extends Activity> activityName) {
        startActivity(new Intent(getBaseContext(), activityName));
        finish();

    }
}
