package com.android.panpin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.panpin.activities.CartActivity;
import com.android.panpin.adapter.HomepageAdapter;
import com.android.panpin.models.Cake;
import com.android.panpin.models.CartItem;
import com.android.panpin.models.ShopViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Home_Fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Home_Fragment.class.getSimpleName();

    RecyclerView homeRecyclerView;
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference cakeRef = rootRef.collection("cake");
    private HomepageAdapter mAdapter;
    private View root;
    private ShopViewModel shopViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        setupRecyclerView();
        dynamicButton();
        setCart();

        return root;
    }

    private void setCart() {
        //cakeList=Paper.book("cart").read("cakeList",new HashSet<Cake>());
        final Button cartBtn = root.findViewById(R.id.category_cart);
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getCart().observe(getViewLifecycleOwner(), new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                cartBtn.setText(String.valueOf(cartItems.size()));
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }
        });
    }

    private void dynamicButton() {
        LinearLayout parentLayout = root.findViewById(R.id.categoryLayout);
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            View x = parentLayout.getChildAt(i);
            x.setOnClickListener(Home_Fragment.this);
        }

    }

    private void setupRecyclerView() {
        //rvLoading.setVisibility(View.VISIBLE);
        Query query = cakeRef.orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Cake> options = new FirestoreRecyclerOptions.Builder<Cake>()
                .setQuery(query, Cake.class)
                .build();

        mAdapter = new HomepageAdapter(options);
        homeRecyclerView = root.findViewById(R.id.homepage_rv);
        homeRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        //rvLoading.setVisibility(View.INVISIBLE);
        homeRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cat_cake:
                changeId("cake");
                break;
            case R.id.cat_bread:
                changeId("bread");
                break;
            case R.id.cat_misc:
                changeId("misc");
                break;
            default:
                break;
        }
    }

    private void changeId(String cake) {
        //TransitionManager.beginDelayedTransition(homeConstrained);
        Query query = rootRef.collection(cake).orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Cake> options = new FirestoreRecyclerOptions.Builder<Cake>()
                .setQuery(query, Cake.class)
                .build();
        mAdapter.updateOptions(options);
    }
}