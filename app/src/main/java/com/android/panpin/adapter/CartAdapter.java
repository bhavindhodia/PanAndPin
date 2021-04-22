package com.android.panpin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.panpin.R;
import com.android.panpin.models.Cake;
import com.android.panpin.models.CartItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CartAdapter extends ListAdapter<CartItem, CartAdapter.CartVH> {


    private CartInterface cartInterface;

    public CartAdapter(CartInterface cartInterface) {

        super(CartItem.itemCallback);
        this.cartInterface = cartInterface;
    }

    @NonNull
    @Override
    public CartVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row, parent, false);
        return new CartVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartVH holder, int position) {

        holder.setCartRow(getItem(position));
        holder.setProductThumbnail(getItem(position).getCakeData().getImgUrl());
        holder.deleteProduct(getItem(position).getCakeData());
    }

    public interface CartInterface {
        void deleteItem(CartItem cartItem);
    }

    class CartVH extends RecyclerView.ViewHolder {

        private View view;

        public CartVH(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        void setCartRow(CartItem cartItem) {
            TextView productName = view.findViewById(R.id.cart_title);
            TextView quantity = view.findViewById(R.id.cart_quantity);
            TextView price = view.findViewById(R.id.cart_price);

            productName.setText(cartItem.getCakeData().getName());
            quantity.setText("Qty " + cartItem.getQuantity());
            price.setText("Rs. " + cartItem.getCakeData().getPrice());

        }

        public void setProductThumbnail(String productThumbnail) {
            RequestOptions glideOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);

            ImageView imageView = view.findViewById(R.id.cart_thumbnail);
            Glide.with(getApplicationContext()).load(productThumbnail).apply(glideOptions).into(imageView);
        }

        public void deleteProduct(final Cake cakeData) {
            Button deleteBtn = view.findViewById(R.id.cart_delete);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartInterface.deleteItem(getItem(getBindingAdapterPosition()));
                }
            });
        }
    }
}
