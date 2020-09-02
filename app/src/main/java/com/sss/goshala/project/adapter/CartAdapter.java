package com.sss.goshala.project.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sss.goshala.project.R;
import com.sss.goshala.project.activity.CartActivity;
import com.sss.goshala.project.model.Cart;
import com.sss.goshala.project.util.localstorage.LocalStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    List<Cart> cartList;
    List<Cart> arrayadap;
    Context context;
    int pQuantity = 1;
    String _subtotal, _price, _quantity;
    LocalStorage localStorage;
    Gson gson;

    public CartAdapter(List<Cart> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cart, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Cart cart = cartList.get(position);
        localStorage = new LocalStorage(context);
        gson = new Gson();
        holder.title.setText(cart.getTitle());
        holder.attribute.setText(cart.getAttribute());
        _price = cart.getPrice();
        _quantity = cart.getQuantity();

        Picasso.get()
                .load(cart.getImage())
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Error : ", e.getMessage());
                    }
                });




        holder.quantity.setText(_quantity);
        holder.price.setText(_price);
        holder.currency.setText(cart.getCurrency());
        _subtotal = String.valueOf(Double.parseDouble(_price) * Integer.parseInt(_quantity));
        holder.subTotal.setText(_subtotal);
       /* Picasso.get()
                .load(cart.getImage())
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }


                });*/

        holder.plus.setOnClickListener(v -> {

            pQuantity = Integer.parseInt(holder.quantity.getText().toString());
            if (pQuantity >= 1) {
                int total_item = Integer.parseInt(holder.quantity.getText().toString());
                total_item++;
                holder.quantity.setText(total_item + "");
                for (int i = 0; i < cartList.size(); i++) {

                    if (cartList.get(i).getId().equalsIgnoreCase(cart.getId())) {

                        // Log.d("totalItem", total_item + "");

                        _subtotal = String.valueOf(Double.parseDouble(holder.price.getText().toString()) * total_item);
                        cartList.get(i).setQuantity(holder.quantity.getText().toString());
                        cartList.get(i).setSubTotal(_subtotal);
                        holder.subTotal.setText(_subtotal);
                        String cartStr = gson.toJson(cartList);
                        //Log.d("CART", cartStr);
                        localStorage.setCart(cartStr);
                        ((CartActivity) context).updateTotalPrice();
                    }
                }
            }


        });
        holder.minus.setOnClickListener(v -> {

            pQuantity = Integer.parseInt(holder.quantity.getText().toString());
            if (pQuantity != 1) {
                int total_item = Integer.parseInt(holder.quantity.getText().toString());
                total_item--;
                holder.quantity.setText(total_item + "");
                for (int i = 0; i < cartList.size(); i++) {
                    if (cartList.get(i).getId().equalsIgnoreCase(cart.getId())) {

                        //holder.quantity.setText(total_item + "");
                        //Log.d("totalItem", total_item + "");
                        _subtotal = String.valueOf(Double.parseDouble(holder.price.getText().toString()) * total_item);
                        cartList.get(i).setQuantity(holder.quantity.getText().toString());
                        cartList.get(i).setSubTotal(_subtotal);
                        holder.subTotal.setText(_subtotal);
                        String cartStr = gson.toJson(cartList);
                        //Log.d("CART", cartStr);
                        localStorage.setCart(cartStr);
                        ((CartActivity) context).updateTotalPrice();

                    }
                }

            }
        });

        holder.delete.setOnClickListener(v -> {

            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());
            Gson gson = new Gson();
            String cartStr = gson.toJson(cartList);
            Log.d("CART", cartStr);
            localStorage.setCart(cartStr);
            ((CartActivity) context).updateTotalPrice();


        });


    }

    @Override
    public int getItemCount() {

        return cartList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        ProgressBar progressBar;
        CardView cardView;
        TextView offer, currency, price, quantity, attribute, addToCart, subTotal;
        Button plus, minus, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.product_title);
            progressBar = itemView.findViewById(R.id.progressbar);
            quantity = itemView.findViewById(R.id.quantity);
            currency = itemView.findViewById(R.id.product_currency);
            attribute = itemView.findViewById(R.id.product_attribute);
            plus = itemView.findViewById(R.id.quantity_plus);
            minus = itemView.findViewById(R.id.quantity_minus);
            delete = itemView.findViewById(R.id.cart_delete);
            subTotal = itemView.findViewById(R.id.sub_total);
            price = itemView.findViewById(R.id.product_price);
        }
    }
}