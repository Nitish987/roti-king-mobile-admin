package com.rotiking.admin.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rotiking.admin.R;
import com.rotiking.admin.ToppingActivity;
import com.rotiking.admin.models.Topping;

import java.util.List;

public class ToppingItemRecyclerAdapter extends RecyclerView.Adapter<ToppingItemRecyclerAdapter.ToppingItemHolder> {
    private final List<Topping> toppings;

    public ToppingItemRecyclerAdapter(List<Topping> toppings) {
        this.toppings = toppings;
    }

    @NonNull
    @Override
    public ToppingItemRecyclerAdapter.ToppingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ToppingItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topping, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingItemRecyclerAdapter.ToppingItemHolder holder, int position) {
        Topping topping = toppings.get(position);
        holder.setPhoto(topping.getPhoto());
        holder.setName(topping.getName());
        holder.setPrice(topping.getPrice());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ToppingActivity.class);
            intent.putExtra("TOPPING", topping);
            intent.putExtra("NEW", false);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return toppings.size();
    }

    public static class ToppingItemHolder extends RecyclerView.ViewHolder {
        private final ImageView photo;
        private final TextView name, price;

        public ToppingItemHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
        }

        public void setPhoto(String photo) {
            if (!photo.equals("")) Glide.with(this.photo.getContext()).load(photo).into(this.photo);
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setPrice(int price) {
            String price_ = "\u20B9 " + price;
            this.price.setText(price_);
        }
    }
}
