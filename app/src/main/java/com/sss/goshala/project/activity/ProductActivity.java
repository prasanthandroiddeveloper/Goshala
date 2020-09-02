package com.sss.goshala.project.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.sss.goshala.project.R;
import com.sss.goshala.project.helper.Converter;
import com.sss.goshala.project.helper.Data;
import com.sss.goshala.project.interfaces.AddorRemoveCallbacks;
import com.sss.goshala.project.model.Cart;
import com.sss.goshala.project.model.Product;
import com.sss.goshala.project.util.localstorage.LocalStorage;
import com.sss.goshala.project.util.localstorage.REST;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.sss.goshala.project.util.localstorage.REST.ALLCATEGORIES;
import static com.sss.goshala.project.util.localstorage.REST.ITEM_NAMES;


public class ProductActivity extends BaseActivity {
    private static int cart_count = 0;
    Data data;
    ProductAdapter mAdapter;
    String Tag = "Grid",categorynm,categoryId;
    private RecyclerView recyclerView;
    Bundle bun;
    ProgressDialog pd;
    TextView tvlh,tvhl,tvaz,tvza;;
    String sortKey;
    LinearLayout lnrpro;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
       getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43A047")));
        //getSupportActionBar().setBackgroundDrawable(R.drawable.background);
        changeActionBarTitle(getSupportActionBar());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pd=new ProgressDialog(ProductActivity.this);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        tvlh=findViewById(R.id.lhtv);
        tvhl=findViewById(R.id.hltv);
        tvaz=findViewById(R.id.aztv);
        tvza=findViewById(R.id.zatv);
        lnrpro=findViewById(R.id.lnrpro);
        cart_count = cartCount();
        recyclerView = findViewById(R.id.product_rv);
        data = new Data();
        //setUpRecyclerView();
        bun=getIntent().getExtras();
        assert bun != null;
        categorynm=bun.getString("categoryname");
        categoryId=bun.getString("categoryid");
        Log.i("catname",categoryId);
       // Toast.makeText(this, categoryId, Toast.LENGTH_SHORT).show();

        if(categoryId.equals("7")){
            lnrpro.setVisibility(View.GONE);
        }else{
            lnrpro.setVisibility(View.VISIBLE);
        }
       // setUpListFromDb();


        tvlh.setOnClickListener(view12 -> {
            sortKey = "lh";
            setUpFilter(sortKey);
            tvlh.setBackgroundColor(Color.parseColor("#ffffff"));
            tvlh.setTextColor(Color.BLACK);
            tvhl.setBackgroundColor(Color.parseColor("#FFC107"));
            tvhl.setTextColor(Color.WHITE);
            tvaz.setBackgroundColor(Color.parseColor("#FFC107"));
            tvaz.setTextColor(Color.WHITE);
            tvza.setBackgroundColor(Color.parseColor("#FFC107"));
            tvza.setTextColor(Color.WHITE);

        });
        tvhl.setOnClickListener(view12 -> {
            sortKey = "hl";
            setUpFilter(sortKey);
            tvhl.setBackgroundColor(Color.parseColor("#ffffff"));
            tvhl.setTextColor(Color.BLACK);
            tvlh.setBackgroundColor(Color.parseColor("#FFC107"));
            tvlh.setTextColor(Color.WHITE);
            tvaz.setBackgroundColor(Color.parseColor("#FFC107"));
            tvaz.setTextColor(Color.WHITE);
            tvza.setBackgroundColor(Color.parseColor("#FFC107"));
            tvza.setTextColor(Color.WHITE);

        });
        tvaz.setOnClickListener(view12 -> {
            sortKey = "az";
            setUpFilter(sortKey);
            tvaz.setBackgroundColor(Color.parseColor("#ffffff"));
            tvaz.setTextColor(Color.BLACK);
            tvza.setBackgroundColor(Color.parseColor("#FFC107"));
            tvza.setTextColor(Color.WHITE);
            tvlh.setBackgroundColor(Color.parseColor("#FFC107"));
            tvlh.setTextColor(Color.WHITE);
            tvhl.setBackgroundColor(Color.parseColor("#FFC107"));
            tvhl.setTextColor(Color.WHITE);


        });
        tvza.setOnClickListener(view12 -> {
            sortKey = "za";
            setUpFilter(sortKey);
            tvza.setBackgroundColor(Color.parseColor("#ffffff"));
            tvza.setTextColor(Color.BLACK);
            tvlh.setBackgroundColor(Color.parseColor("#FFC107"));
            tvlh.setTextColor(Color.WHITE);
            tvhl.setBackgroundColor(Color.parseColor("#FFC107"));
            tvhl.setTextColor(Color.WHITE);
            tvaz.setBackgroundColor(Color.parseColor("#FFC107"));
            tvaz.setTextColor(Color.WHITE);


        });
        if(categoryId.equals("7")){
            setUpAllcat();
        }
        else {
        setUpGridFromDb();
        }

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
        tv.setText("Products"); // ActionBar title text
        tv.setTextSize(20);

        // Set the text color of TextView to red
        // This line change the ActionBar title text color
        tv.setTextColor(Color.WHITE);

        // Set the ActionBar display option
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // Finally, set the newly created TextView as ActionBar custom view
        actionBar.setCustomView(tv);
    }

    private void setUpGridFromDb(){
        pd.setMessage("Loading...");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ITEM_NAMES, response -> {
            Log.i("Leaveresponse",response);
        pd.dismiss();
            try {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                JSONArray jarr = new JSONArray(response);

                List<Product> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {

                    Product adapter = new Product();
                    JSONObject json = jarr.getJSONObject(i);

                    adapter.setId(json.getString("id"));
                    adapter.setCategoryId(json.getString("categoryId"));
                    adapter.setTitle(json.getString("title"));
                    adapter.setDescription(json.getString("description"));
                    adapter.setAttribute(json.getString("attribute"));
                    adapter.setPrice(json.getString("price"));
                    adapter.setDiscount(json.getString("discount_value"));
                   // adapter.setCurrency(json.getString("currency"));
                    adapter.setImage(json.getString("image"));

                    list.add(adapter);
                }

              //  recyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
                mAdapter = new ProductAdapter(list,ProductActivity.this,Tag);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                recyclerView.setVisibility(View.GONE);

                  Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
               // Toast.makeText(ProductActivity.this,"No Data",Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(ProductActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();
               params.put("categoryId",categoryId);
               Log.i("prra", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(ProductActivity.this));
        requestQueue.add(stringRequest);

    }

    private void setUpAllcat(){
        pd.setMessage("Loading...");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ALLCATEGORIES, response -> {
            Log.i("all",response);
            pd.dismiss();
            try {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                JSONArray jarr = new JSONArray(response);

                List<Product> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {

                    Product adapter = new Product();
                    JSONObject json = jarr.getJSONObject(i);

                    adapter.setId(json.getString("id"));
                    adapter.setCategoryId(json.getString("categoryId"));
                    adapter.setTitle(json.getString("title"));
                    adapter.setDescription(json.getString("description"));
                    adapter.setAttribute(json.getString("attribute"));
                    adapter.setPrice(json.getString("price"));
                    adapter.setDiscount(json.getString("discount_value"));
                    // adapter.setCurrency(json.getString("currency"));
                    adapter.setImage(json.getString("image"));

                    list.add(adapter);
                }

                //  recyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
                mAdapter = new ProductAdapter(list,ProductActivity.this,Tag);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                recyclerView.setVisibility(View.GONE);

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(ProductActivity.this,"No Data",Toast.LENGTH_SHORT).show();
            }

        }, error -> Toast.makeText(ProductActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show());

        RequestQueue requestQueue = Volley.newRequestQueue(ProductActivity.this);
        requestQueue.add(stringRequest);

    }

    private void setUpFilter(String key){
        pd.setMessage("Loading...");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REST.FILETRSSELECT, response -> {
            Log.i("Leaveresponse",response);
            pd.dismiss();

            try {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                JSONArray jarr = new JSONArray(response);

                List<Product> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {

                    Product adapter = new Product();
                    JSONObject json = jarr.getJSONObject(i);

                    adapter.setId(json.getString("id"));
                    adapter.setCategoryId(json.getString("categoryId"));
                    adapter.setTitle(json.getString("title"));
                    adapter.setDescription(json.getString("description"));
                    adapter.setAttribute(json.getString("attribute"));
                    adapter.setPrice(json.getString("price"));
                    adapter.setDiscount(json.getString("discount_value"));
                    // adapter.setCurrency(json.getString("currency"));
                    adapter.setImage(json.getString("image"));

                    list.add(adapter);
                }

                //  recyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
                mAdapter = new ProductAdapter(list,ProductActivity.this,Tag);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                recyclerView.setVisibility(View.GONE);

              //  Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(ProductActivity.this,"No Data",Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(ProductActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();
                params.put("categoryId",categoryId);
                params.put("filterKey",key);
                Log.i("vishwa", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(ProductActivity.this));
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(ProductActivity.this, MainActivity.class);
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
        menu.clear();
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(ProductActivity.this, cart_count, R.drawable.cartwhite));

        MenuItem items = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) items.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });

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


    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

        List<Product> productList;
        List<Product> arrayadap;
        Context context;
        String Tag;
        int pQuantity = 1;
        LocalStorage localStorage;
        Gson gson;
        List<Cart> cartList = new ArrayList<>();
        String _quantity, _price, _attribute, _subtotal;

        public ProductAdapter(List<Product> productList, Context context) {
            this.productList = productList;
            this.context = context;
        }

        public ProductAdapter(List<Product> productList, Context context, String tag) {
            this.productList = productList;
            this.context = context;
            this.arrayadap=productList;
            Tag = tag;
        }

        @NonNull
        @Override
        public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View itemView;
            if (Tag.equalsIgnoreCase("List")) {
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_products, parent, false);
            } else {
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_grid_products, parent, false);
            }


            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ProductAdapter.MyViewHolder holder, final int position) {

            final Product product = productList.get(position);
            localStorage = new LocalStorage(context);
            gson = new Gson();
            cartList = ((BaseActivity) context).getCartList();
            holder.title.setText(product.getTitle());
            holder.offer.setText(product.getDiscount());
            holder.attribute.setText((product.getAttribute()));
            //holder.currency.setText(product.getCurrency());
            holder.price.setText((product.getPrice()));
            Picasso.get()
                    .load(product.getImage())
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


            if (product.getDiscount() == null || product.getDiscount().length() == 0) {
                holder.offer.setVisibility(View.GONE);
            }

            if(product.getDiscount().equals("OUT OF STOCK")){
                holder.addToCart.setEnabled(false);
            }else{
                holder.addToCart.setEnabled(true);
            }



            if (!cartList.isEmpty()) {
                for (int i = 0; i < cartList.size(); i++) {
                    if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {
                        holder.addToCart.setVisibility(View.GONE);
                        holder.subTotal.setVisibility(View.VISIBLE);
                        holder.quantity.setText(cartList.get(i).getQuantity());
                        _quantity = cartList.get(i).getQuantity();
                        _price = product.getPrice();
                        _subtotal = String.valueOf(Double.parseDouble(_price) * Integer.parseInt(_quantity));
                        holder.subTotal.setText(_quantity + "X" + _price + "= Rs." + _subtotal);
                        Log.d("Tag : ", cartList.get(i).getId() + "-->" + product.getId());
                    }
                }
            } else {

                holder.quantity.setText("1");
            }

            holder.plus.setOnClickListener(v -> {
                pQuantity = Integer.parseInt(holder.quantity.getText().toString());
                if (pQuantity >= 1) {
                    int total_item = Integer.parseInt(holder.quantity.getText().toString());
                    total_item++;
                    holder.quantity.setText(total_item + "");
                    for (int i = 0; i < cartList.size(); i++) {

                        if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {

                            // Log.d("totalItem", total_item + "");

                            _subtotal = String.valueOf(Double.parseDouble(holder.price.getText().toString()) * total_item);
                            cartList.get(i).setQuantity(holder.quantity.getText().toString());
                            cartList.get(i).setSubTotal(_subtotal);
                            holder.subTotal.setText(total_item + "X" + holder.price.getText().toString() + "= Rs." + _subtotal);
                            String cartStr = gson.toJson(cartList);
                            //Log.d("CART", cartStr);
                            localStorage.setCart(cartStr);
                            notifyItemChanged(position);
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
                        if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {

                            //holder.quantity.setText(total_item + "");
                            //Log.d("totalItem", total_item + "");
                            _subtotal = String.valueOf(Double.parseDouble(holder.price.getText().toString()) * total_item);
                            cartList.get(i).setQuantity(holder.quantity.getText().toString());
                            cartList.get(i).setSubTotal(_subtotal);
                            holder.subTotal.setText(total_item + "X" + holder.price.getText().toString() + "= Rs." + _subtotal);
                            String cartStr = gson.toJson(cartList);
                            //Log.d("CART", cartStr);
                            localStorage.setCart(cartStr);
                            notifyItemChanged(position);
                        }
                    }

                }

            });


            holder.cardView.setOnClickListener(v -> {
                Bundle bundle=new Bundle();

            Intent intent = new Intent(context, ProductViewActivity.class);
            bundle.putString("id",productList.get(position).getId());
            bundle.putString("title",productList.get(position).getTitle());
            bundle.putString("image",productList.get(position).getImage());
            bundle.putString("price",productList.get(position).getPrice());
            bundle.putString("currency","");
            bundle.putString("discount","");
            bundle.putString("description",productList.get(position).getDescription());
            bundle.putString("attribute",productList.get(position).getAttribute());
            bundle.putString("outofstock",productList.get(position).getDiscount());
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);

            });

            holder.addToCart.setOnClickListener(v -> {

                holder.addToCart.setVisibility(View.GONE);
                holder.subTotal.setVisibility(View.VISIBLE);


                _price = product.getPrice();
                _quantity = holder.quantity.getText().toString();
                _attribute = product.getAttribute();

                if (Integer.parseInt(_quantity) != 0) {
                    _subtotal = String.valueOf(Double.parseDouble(_price) * Integer.parseInt(_quantity));
                    holder.subTotal.setText(_quantity + "X" + _price + "= Rs." + _subtotal+"\n"+"Added to Cart");
                    if (context instanceof ProductActivity) {
                        Cart cart = new Cart(product.getId(), product.getTitle(), product.getImage(), product.getCurrency(), _price, _attribute, _quantity, _subtotal);
                        cartList = ((BaseActivity) context).getCartList();
                        cartList.add(cart);
                        String cartStr = gson.toJson(cartList);
                        //Log.d("CART", cartStr);
                        localStorage.setCart(cartStr);
                        ((AddorRemoveCallbacks) context).onAddProduct();
                        notifyItemChanged(position);
                    }
                } else {
                    Toast.makeText(context, "Please Add Quantity", Toast.LENGTH_SHORT).show();
                }


            });

        }

        @Override
        public int getItemCount() {

            return productList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }



        public Filter getFilter() {


            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSeq) {
                    String charString = charSeq.toString();
                    if (charString.isEmpty()) {
                        productList = arrayadap;
                    } else {
                        ArrayList<Product> filteredList = new ArrayList<>();
                        for (Product filterdata : arrayadap) {
                            if (filterdata.getTitle().toLowerCase().contains(charString)) {
                                filteredList.add(filterdata);
                            }
                        }
                        productList = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = productList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    productList = (ArrayList<Product>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView title;
            ProgressBar progressBar;
            CardView cardView;
            TextView offer, currency, price, quantity, attribute, addToCart, subTotal;
            Button plus, minus;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.product_image);
                title = itemView.findViewById(R.id.product_title);
                progressBar = itemView.findViewById(R.id.progressbar);
                cardView = itemView.findViewById(R.id.card_view);
                offer = itemView.findViewById(R.id.product_discount);
                currency = itemView.findViewById(R.id.product_currency);
                price = itemView.findViewById(R.id.product_price);
                quantity = itemView.findViewById(R.id.quantity);
                addToCart = itemView.findViewById(R.id.add_to_cart);
                attribute = itemView.findViewById(R.id.product_attribute);
                plus = itemView.findViewById(R.id.quantity_plus);
                minus = itemView.findViewById(R.id.quantity_minus);
                subTotal = itemView.findViewById(R.id.sub_total);


            }
        }
    }

}
