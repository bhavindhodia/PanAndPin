package com.android.panpin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.panpin.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = PaymentActivity.class.getSimpleName();

    RadioGroup paymentGroup;
    RadioButton paymentGroupBtn;
    FirebaseUser user;
    String userThumbnail;
    Button checkoutBtn;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        linearLayout = findViewById(R.id.payment_parent);
        user = Paper.book("user").read("FirebaseUser");
        userThumbnail = Paper.book("user").read("FirebaseUserProfile");
        initVariables();


    }

    private void initVariables() {
        paymentGroup = findViewById(R.id.payment_group);
        checkoutBtn = findViewById(R.id.payment_checkout);
        ImageView thumbnail = findViewById(R.id.payment_avatar);
        TextView username = findViewById(R.id.payment_username);
        TextView price = findViewById(R.id.payment_total);
        TextView quantity = findViewById(R.id.payment_quantity);
        //Log.d(TAG, "initVariables: "+shopViewModel.getAllQuantity());


        username.setText(user.getDisplayName());
        price.setText(getResources().getString(R.string.paymentCurrency, getIntent().getStringExtra("ORDER_TOTAL")));
        quantity.setText(String.valueOf(getIntent().getIntExtra("TOTAL_QUANTITY", 0)));

        RequestOptions glideOptions = new RequestOptions()
                .circleCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(getApplicationContext())
                .load(userThumbnail)
                .apply(glideOptions)
                .placeholder(R.drawable.fakeuser)
                .error(R.drawable.fakeuser)
                .into(thumbnail);

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (paymentGroup.getCheckedRadioButtonId() != -1) {
                    int selectedId = paymentGroup.getCheckedRadioButtonId();
                    paymentGroupBtn = findViewById(selectedId);
                    Intent i = new Intent(getApplicationContext(), Successful.class);
                    Boolean expressCheckout = getIntent().getBooleanExtra("EXPRESS_CHECKOUT", false);
                    i.putExtra("EXPRESS_CHECKOUT", expressCheckout);
                    startActivity(i);
                    //startActivity(new Intent(getApplicationContext(),Successful.class));
                } else {
                    Snackbar sb = Snackbar.make(linearLayout, "Please select a payment method", Snackbar.LENGTH_SHORT);
                    View sbView = sb.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.error));
                    TextView sbText = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
                    sbText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    sb.show();
                }

            }
        });

    }
}
