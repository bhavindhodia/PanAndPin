package com.android.panpin;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.panpin.adapter.HomepageAdapter;
import com.android.panpin.models.Cake;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Home_Fragment extends Fragment {

    private static String TAG = Home_Fragment.class.getSimpleName();

    RecyclerView homeRecyclerView;
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference cakeRef = rootRef.collection("cake");
    private HomepageAdapter mAdapter;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        setupRecyclerView();
        return root;
    }

    private void setupRecyclerView() {
        Query query = cakeRef.orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Cake> options = new FirestoreRecyclerOptions.Builder<Cake>()
                .setQuery(query, Cake.class)
                .build();
        mAdapter = new HomepageAdapter(options);
        homeRecyclerView = root.findViewById(R.id.homepage_rv);
        homeRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
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
}