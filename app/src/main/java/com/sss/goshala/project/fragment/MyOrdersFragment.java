package com.sss.goshala.project.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sss.goshala.project.R;
import com.sss.goshala.project.activity.MainActivity;
import com.sss.goshala.project.activity.ViewOrderDetailsAct;
import com.sss.goshala.project.util.localstorage.REST;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class MyOrdersFragment extends Fragment {
    SharedPreferences sharedPreferences;
    String email;
    View view;
    private RecyclerView recyclerView;
    ProgressDialog pd;
    OAdapter oAdapter;
    public MyOrdersFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_my_orders, container, false);
        recyclerView = view.findViewById(R.id.order_rc);
        pd=new ProgressDialog(getActivity());
        sharedPreferences= Objects.requireNonNull(getActivity()).getSharedPreferences("MySharedPref", MODE_PRIVATE);
        email=sharedPreferences.getString("email","");
       // Toast.makeText(getActivity(), mob, Toast.LENGTH_SHORT).show();
        setUpFilter(email);



        Objects.requireNonNull(view).setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v12, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                   startActivity(new Intent(getActivity(), MainActivity.class));
                    return true;
                }
            }
            return false;
        });



        return view;
    }


    private void setUpFilter(String email){
        pd.setMessage("Loading...");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REST.MYORDERS, response -> {
            Log.i("orsesp",response);
            pd.dismiss();

            try {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                JSONArray jarr = new JSONArray(response);

                List<Olist> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {

                    Olist adapter = new Olist();
                    JSONObject json = jarr.getJSONObject(i);

                    adapter.setOrderid(json.getString("order_id"));
                    adapter.setOrderdate(json.getString("created_on"));
                    adapter.setOrderprice(json.getString("total_amount"));
                    adapter.setOrderstatus(json.getString("status"));
                    adapter.setOrderlist(json.getString("items_list"));
                    adapter.setCoupondisc(json.getString("coupondisc"));

                    list.add(adapter);
                }

                //  recyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
                oAdapter = new OAdapter(list,getActivity());
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(oAdapter);

            } catch (JSONException e) {
                recyclerView.setVisibility(View.GONE);

              //  Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"No Orders Found",Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();
               params.put("email",email);

                Log.i("preethika", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);

    }


    public class OAdapter extends RecyclerView.Adapter<OAdapter.MyViewHolder> {

        List<Olist> OlistAdapter;
        Bundle bundle;

        Context context;

        public OAdapter(List<Olist> Ol, Context context) {
            this.OlistAdapter = Ol;
            this.context = context;
        }


        @NonNull
        @Override
        public OAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View itemView;

                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_order, parent, false);
            return new OAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final OAdapter.MyViewHolder holder, final int position) {
            final Olist olist = OlistAdapter.get(position);
            bundle=new Bundle();

            holder.orderid.setText(olist.getOrderid());
            holder.orderdate.setText(olist.getOrderdate());
            holder.orderprice.setText(olist.getOrderprice());
            holder.orderstatus.setText(olist.getOrderstatus());
            holder.orderdetails.setOnClickListener(view -> {
                String itemlist=olist.getOrderlist();
                String oid=olist.getOrderid();
                String oprice=olist.getOrderprice();
                String createdon=olist.getOrderdate();
                String orderstatus=olist.getOrderstatus();
                String coupondisc=olist.getCoupondisc();
                bundle.putString("items",itemlist);
                bundle.putString("oid",oid);
                bundle.putString("oprice",oprice);
                bundle.putString("items",itemlist);
                bundle.putString("createdon",createdon);
                bundle.putString("orderstatus",orderstatus);
                bundle.putString("coupondisc",coupondisc);
                Intent in=new Intent(context, ViewOrderDetailsAct.class);
                in.putExtras(bundle);
                context.startActivity(in);

            });

        }

        @Override
        public int getItemCount() {

            return OlistAdapter.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }





        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView orderid,orderdate,orderstatus,orderprice,orderdetails;
            Button plus, minus;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);


                orderid = itemView.findViewById(R.id.order_id);
                orderdate = itemView.findViewById(R.id.date);
                orderstatus = itemView.findViewById(R.id.total_amount);
                orderprice = itemView.findViewById(R.id.status);
                orderdetails = itemView.findViewById(R.id.viewdtls);


            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        Objects.requireNonNull(getActivity()).setTitle("My Orders");
    }


}