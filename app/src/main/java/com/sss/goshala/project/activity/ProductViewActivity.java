package com.sss.goshala.project.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sss.goshala.project.R;
import com.sss.goshala.project.helper.Converter;
import com.sss.goshala.project.model.Cart;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductViewActivity extends BaseActivity {
    private static int cart_count = 0;
    public TextView quantity, inc, dec;
    String _id, _title, _image, _description, _price, _currency, _discount, _attribute,_outofstock;
    TextView id, title, description, price, currency, discount, attribute;
    ImageView imageView;
    ProgressBar progressBar;
    LinearLayout addToCart, share;
    RelativeLayout quantityLL;
    List<Cart> cartList = new ArrayList<>();
    int cartId;
    Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);


        Intent intent = getIntent();

        _id = intent.getStringExtra("id");
        _title = intent.getStringExtra("title");
        _image = intent.getStringExtra("image");
        _description = intent.getStringExtra("description");
        _price = intent.getStringExtra("price");
        _currency = intent.getStringExtra("currency");
        _discount = intent.getStringExtra("discount");
        _attribute = intent.getStringExtra("attribute");
        _outofstock = intent.getStringExtra("outofstock");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        changeActionBarTitle(getSupportActionBar());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        //upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        cart_count = cartCount();

        title = findViewById(R.id.apv_title);
        description = findViewById(R.id.apv_description);
        currency = findViewById(R.id.apv_currency);
        price = findViewById(R.id.apv_price);
        attribute = findViewById(R.id.apv_attribute);
        discount = findViewById(R.id.apv_discount);
        imageView = findViewById(R.id.apv_image);
        progressBar = findViewById(R.id.progressbar);
        addToCart = findViewById(R.id.add_to_cart_ll);
        share = findViewById(R.id.apv_share);
        quantityLL = findViewById(R.id.quantity_rl);
        quantity = findViewById(R.id.quantity);
        inc = findViewById(R.id.quantity_plus);
        dec = findViewById(R.id.quantity_minus);


        if(_outofstock.equals("OUT OF STOCK")){
            addToCart.setVisibility(View.GONE);
        }else{
            addToCart.setVisibility(View.VISIBLE);
        }

        cartList = getCartList();
        title.setText(_title);
        description.setText(_description);
        price.setText(_price);
        currency.setText(_currency);
        attribute.setText(_attribute);
        discount.setText(_discount);
        Log.d(TAG, "Discount : " + _discount);
        if (_discount != null || _discount.length() != 0 || _discount != "") {
            discount.setVisibility(View.VISIBLE);
        } else {
            discount.setVisibility(View.GONE);
        }
        if (_image != null) {
            Picasso.get().load(_image).error(R.drawable.no_image).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    progressBar.setVisibility(View.GONE);
                }
            });

            imageView.setOnClickListener(view -> {
                final Dialog dialog=new Dialog(ProductViewActivity.this);
                dialog.setContentView(R.layout.imagepopup);
                ImageView imagev=dialog.findViewById(R.id.imagePopup);
                Picasso.get().load(_image).error(R.drawable.no_image).into(imagev, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                       // Toast.makeText(ProductViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

                dialog.show();

            });
        }

        if (!cartList.isEmpty()) {
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).getId().equalsIgnoreCase(_id)) {
                    addToCart.setVisibility(View.GONE);
                    quantityLL.setVisibility(View.VISIBLE);
                    quantity.setText(cartList.get(i).getQuantity());
                    cartId = i;

                }
            }
        }



        share.setOnClickListener(view -> {
            String userEntry = _image + "\n" + _title + "\n" + _description + "\n" + _attribute + "-" + _currency + _price + "(" + _discount + ")";

            Intent textShareIntent = new Intent(Intent.ACTION_SEND);
            textShareIntent.putExtra(Intent.EXTRA_TEXT, userEntry);
            textShareIntent.setType("text/plain");
            startActivity(textShareIntent);
        });


        addToCart.setOnClickListener(view -> {
            _price = price.getText().toString();

            cart = new Cart(_id, _title, _image, _currency, _price, _attribute, "1", _price);
            cartList.add(cart);
            String cartStr = gson.toJson(cartList);
            //Log.d("CART", cartStr);
            localStorage.setCart(cartStr);
            onAddProduct();
            addToCart.setVisibility(View.GONE);
            quantityLL.setVisibility(View.VISIBLE);
        });


        inc.setOnClickListener(view -> {
            _price = price.getText().toString();


            // int total_item = Integer.parseInt(cartList.get(cartId).getQuantity());
            int total_item = Integer.parseInt(quantity.getText().toString());
            total_item++;
            Log.d("totalItem", total_item + "");
            quantity.setText(total_item + "");
            String subTotal = String.valueOf(Double.parseDouble(_price) * total_item);
            cartList.get(cartId).setQuantity(quantity.getText().toString());
            cartList.get(cartId).setSubTotal(subTotal);
            cartList.get(cartId).setAttribute(attribute.getText().toString());
            cartList.get(cartId).setPrice(_price);
            String cartStr = gson.toJson(cartList);
            //Log.d("CART", cartStr);
            localStorage.setCart(cartStr);
        });

        dec.setOnClickListener(view -> {
            _price = price.getText().toString();

            //int total_item = Integer.parseInt(quantity.getText().toString());
            int total_item = Integer.parseInt(quantity.getText().toString());
            if (total_item != 1) {
                total_item--;
                quantity.setText(total_item + "");
                Log.d("totalItem", total_item + "");
                String subTotal = String.valueOf(Double.parseDouble(_price) * total_item);


                cartList.get(cartId).setQuantity(quantity.getText().toString());
                cartList.get(cartId).setSubTotal(subTotal);
                cartList.get(cartId).setAttribute(attribute.getText().toString());
                cartList.get(cartId).setPrice(_price);
                String cartStr = gson.toJson(cartList);
                //Log.d("CART", cartStr);
                localStorage.setCart(cartStr);
            }
        });


    }

    private void changeActionBarTitle(ActionBar actionBar) {
        // Create a LayoutParams for TextView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
        TextView tv = new TextView(getApplicationContext());
        // Apply the layout parameters to TextView widget
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(null, Typeface.BOLD);
        // Set text to display in TextView
        tv.setText(_title); // ActionBar title text
        tv.setTextSize(20);

        // Set the text color of TextView to red
        // This line change the ActionBar title text color
        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Set the ActionBar display option
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // Finally, set the newly created TextView as ActionBar custom view
        actionBar.setCustomView(tv);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(ProductViewActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            case R.id.cart_action:
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(ProductViewActivity.this, cart_count, R.drawable.cartss));
        return true;
    }


    @Override
    public void onAddProduct() {
        cart_count++;
        invalidateOptionsMenu();

    }

    @Override
    public void onRemoveProduct() {
        cart_count--;
        invalidateOptionsMenu();

    }

}
