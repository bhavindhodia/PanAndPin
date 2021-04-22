package com.android.panpin.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.panpin.R;
import com.android.panpin.activities.DetailActivity;
import com.android.panpin.models.Cake;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HomepageAdapter extends FirestoreRecyclerAdapter<Cake, HomepageAdapter.HomeViewHolder> {


    public HomepageAdapter(@NonNull FirestoreRecyclerOptions<Cake> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HomeViewHolder holder, int position, @NonNull Cake model) {
        holder.setProductName(model.getName());
        holder.setProductThumbnail(model.getImgUrl());
        holder.onThumbClickListener(model);

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card, parent, false);
        // onThumbClick(v);
        return new HomeViewHolder(v);
    }


    static class HomeViewHolder extends RecyclerView.ViewHolder {

        private View view;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        void onThumbClickListener(final Cake cakeData) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(), DetailActivity.class);
                    i.putExtra("CakeData", cakeData);
                    v.getContext().startActivity(i);
                }
            });
        }


        void setProductName(String productName) {
            TextView textView = view.findViewById(R.id.categoryTitle);
            textView.setText(productName);
        }

        public void setProductThumbnail(String productThumbnail) {
            RequestOptions glideOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);

            ImageView imageView = view.findViewById(R.id.categoryThumbnail);
            Glide.with(getApplicationContext()).load(productThumbnail).apply(glideOptions).into(imageView);
        }


    }


}