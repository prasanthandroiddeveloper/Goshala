package com.sss.goshala.project.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sss.goshala.project.R;
import com.sss.goshala.project.activity.ProductActivity;
import com.sss.goshala.project.helper.Data;
import com.sss.goshala.project.model.Category;
import com.sss.goshala.project.util.localstorage.REST;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CategoryFragment extends Fragment {
    Data data;
    private List<Category> categoryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CategoryAdapter mAdapter;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.category_rv);
        data = new Data();

       // setUpListFromDb();
       mAdapter = new CategoryAdapter(data.getCategoryList(), getContext(), "Category");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        return view;
    }

    private void setUpListFromDb(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REST.CATEGORYITMES, response -> {
            Log.i("Leaveresponse",response);

            try {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                JSONArray jarr = new JSONArray(response);

                List<Category> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {

                    Category adapter = new Category();
                    JSONObject json = jarr.getJSONObject(i);

                    adapter.setId(json.getString("category_id"));
                    adapter.setTitle(json.getString("category"));
                    adapter.setImage(json.getString("image_path"));
                    list.add(adapter);
                }

                //  recyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
                mAdapter = new CategoryAdapter(list,getContext(),"Category");
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                recyclerView.setVisibility(View.GONE);

                //  Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"No Data",Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Category");
    }


    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

        List<Category> categoryList;
        Context context;
        String Tag;

        public CategoryAdapter(List<Category> categoryList, Context context) {
            this.categoryList = categoryList;
            this.context = context;
        }

        public CategoryAdapter(List<Category> categoryList, Context context, String tag) {
            this.categoryList = categoryList;
            this.context = context;
            Tag = tag;
        }

        @NonNull
        @Override
        public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View itemView;
            if (Tag.equalsIgnoreCase("Home")) {
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_home_category, parent, false);
            } else {
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_category, parent, false);
            }


            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

            Category category = categoryList.get(position);
            holder.title.setText(category.getTitle());
            if (Tag.equalsIgnoreCase("Category")) {
                Picasso.get()
                        .load(category.getImage())
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
            }

            holder.cardView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            });

            holder.title.setOnClickListener(view -> {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            if (Tag.equalsIgnoreCase("Home") && categoryList.size() < 6 && categoryList.size() > 3) {
                return 3;
            } else if (Tag.equalsIgnoreCase("Home") && categoryList.size() >= 6) {
                return 6;
            } else {
                return categoryList.size();
            }

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView title;
            ProgressBar progressBar;
            CardView cardView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.category_image);
                title = itemView.findViewById(R.id.category_title);
                progressBar = itemView.findViewById(R.id.progressbar);
                cardView = itemView.findViewById(R.id.card_view);
            }
        }
    }

}
