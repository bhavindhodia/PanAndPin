package com.android.panpin.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.panpin.R;
import com.android.panpin.fragments.CartFragment;
import com.android.panpin.fragments.HomeFragment;
import com.android.panpin.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private static String TAG = HomeActivity.class.getSimpleName();
    private BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.homeFragment:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.categoryFragment:
                    selectedFragment = new CartFragment();
                    break;
                case R.id.profileFragment:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListner);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();


    }


}
