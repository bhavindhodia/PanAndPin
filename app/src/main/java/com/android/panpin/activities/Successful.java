package com.android.panpin.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.panpin.R;
import com.android.panpin.models.CartItem;
import com.android.panpin.models.ShopViewModel;
import com.github.jinatonic.confetti.CommonConfetti;

import java.util.List;

public class Successful extends AppCompatActivity {
    private static final String TAG = Successful.class.getSimpleName();
    private ViewGroup container;
    private Button homePageBtn;
    private ShopViewModel shopViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.successful);

        container = findViewById(R.id.successParent);
        homePageBtn = findViewById(R.id.successBack);


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
        Boolean isExpressCheckout = getIntent().getBooleanExtra("EXPRESS_CHECKOUT", false);
        if (!isExpressCheckout) {
            shopViewModel.removeAllItemCart();
        }
        onPaymentDone();

        homePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Successful.this, HomeActivity.class));
            }
        });

    }

    private void onPaymentDone() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonConfetti.rainingConfetti(container, new int[]{Color.RED, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.GREEN})
                        .stream(3000);
            }
        }, 1000);

    }


}
