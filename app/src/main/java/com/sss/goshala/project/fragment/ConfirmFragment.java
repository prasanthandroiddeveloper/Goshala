package com.sss.goshala.project.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sss.goshala.project.R;
import com.sss.goshala.project.activity.BaseActivity;
import com.sss.goshala.project.activity.CartActivity;
import com.sss.goshala.project.activity.PayuConfirmAct;
import com.sss.goshala.project.activity.UpiPaymentAct;
import com.sss.goshala.project.adapter.CheckoutCartAdapter;
import com.sss.goshala.project.model.Cart;
import com.sss.goshala.project.model.Order;
import com.sss.goshala.project.util.localstorage.LocalStorage;
import com.sss.goshala.project.util.localstorage.REST;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class ConfirmFragment extends Fragment {
    LocalStorage localStorage;
    List<Cart> cartList = new ArrayList<>();
    Gson gson;
    RecyclerView recyclerView;
    CheckoutCartAdapter adapter;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    TextView back, placeOrder,tvupipaymentclick,tvpayumoneyclick;
    TextView total, shipping, totalAmount;
    Double _total, _shipping, _totalAmount,amount;
    ProgressDialog progressDialog;
    List<Order> orderList = new ArrayList<>();
    String orderNo,item_name;
    String id,nm,em,mob,addrs,city,state,zip,landmark,areaname;
    StringBuilder stringBuilder=new StringBuilder(),
            stringBuilder1=new StringBuilder(),
            stringBuilder2=new StringBuilder();
    SharedPreferences sh;

    RadioButton card, cash;
    Bundle bundle;
    int qntsum,attrsum;
    int totals=0;



    public ConfirmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm, container, false);
        localStorage = new LocalStorage(getContext());
        recyclerView = view.findViewById(R.id.cart_rv);
        totalAmount = view.findViewById(R.id.total_amount);
        total = view.findViewById(R.id.total);
        shipping = view.findViewById(R.id.shipping_amount);
        back = view.findViewById(R.id.back);
        placeOrder = view.findViewById(R.id.place_order);
        progressDialog = new ProgressDialog(getContext());
        tvupipaymentclick = view.findViewById(R.id.upipayment);
        tvpayumoneyclick = view.findViewById(R.id.payu);
        bundle=new Bundle();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());




        card = view.findViewById(R.id.card_payment);
        cash = view.findViewById(R.id.cash_on_delivery);
        gson = new Gson();
        orderList = ((BaseActivity) getActivity()).getOrderList();
        Random rnd = new Random();
        orderNo = String.valueOf(100000 + rnd.nextInt(900000));
        setUpCartRecyclerview();
        if (orderList.isEmpty()) {
            id = "1";
        } else {
            id = String.valueOf(orderList.size() + 1);
        }
        sh= getActivity().getSharedPreferences("MySharedPref",
                MODE_PRIVATE);



        _total = ((BaseActivity) getActivity()).getTotalPrice();
     //   Toast.makeText(getActivity(), String.valueOf(_total), Toast.LENGTH_SHORT).show();
        _shipping = 0.0;
        _totalAmount = _total + _shipping;
        total.setText("₹"+" "+_total + "");
        shipping.setText("₹"+" "+_total + "");
        totalAmount.setText("₹"+" "+_totalAmount + "");

        back.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CartActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });

        placeOrder.setOnClickListener(v -> {

            showDeleteDialog();

        });
       useritemdetails();



        tvupipaymentclick.setOnClickListener(view1 -> {


            List<Cart> cartlist = ((BaseActivity) getActivity()).getCartList();
            nm=sh.getString("name","");
            em=sh.getString("email","");
            mob=sh.getString("mobile","");
            addrs=sh.getString("address","");
            city=sh.getString("city","");
            state=sh.getString("state","");
            zip=sh.getString("pin","");
            landmark=sh.getString("landmark",""); //newly added
            areaname=sh.getString("areaname",""); //newly added
            Log.i("z2",zip);


            for (int i = 0; i < cartlist.size(); i++) {
                final Cart cart = cartList.get(i);
                stringBuilder.append(cart.getTitle()).append("=").append(cart.getQuantity()+"\n");

                qntsum= Integer.parseInt(cart.getQuantity());
                item_name= cart.getAttribute();
                if(item_name.equals("1 kg") || item_name.equals("1 ltr")){
                    item_name= String.valueOf(1000);
                }
                else {
                    item_name= cart.getAttribute();
                }

                int values=Integer.parseInt(item_name.replaceAll("[^0-9]", ""));
                Log.i("attrs", String.valueOf(values));
                totals+=qntsum*values;


            }
            Log.i("item_name",stringBuilder.toString());
            Log.i("quan",stringBuilder1.toString());
            Log.i("attr", item_name);
            Log.i("qtsm", String.valueOf(totals));

            Intent in=new Intent(getActivity(), UpiPaymentAct.class);
            bundle.putString("amount", String.valueOf(_total));//total amount
            bundle.putString("name", nm);//customer name
            bundle.putString("email", em);//customer email
            bundle.putString("mobileno", mob);//customer mobile
            bundle.putString("address", addrs);//customer addrss
            bundle.putString("items_list", String.valueOf(stringBuilder));//items_list
            bundle.putString("orderno", orderNo);//customer addrss
            bundle.putString("status", "orderplaced");//order status
            bundle.putString("city", city);//customer addrss
            bundle.putString("paymenttype", "UPI Payment");//customer addrss
            bundle.putString("createdon", currentDateandTime);//customer addrss
            bundle.putInt("qtsm", totals);//price
            bundle.putString("myorderid", id);//customer addrss
            bundle.putString("state", state);//state
            bundle.putString("zip", zip);//zip
            bundle.putString("areaname", areaname);//newly added
            bundle.putString("landmark", landmark);//newly added

           /* Log.i("stringbuildersss", String.valueOf(stringBuilder));*/
            in.putExtras(bundle);

           // Toast.makeText(getActivity(), stringBuilder, Toast.LENGTH_SHORT).show();
           startActivity(in);

        });

        tvpayumoneyclick.setOnClickListener(view12 -> {


            List<Cart> cartlist = ((BaseActivity) getActivity()).getCartList();
            nm=sh.getString("name","");
            em=sh.getString("email","");
            mob=sh.getString("mobile","");
            addrs=sh.getString("address","");
            city=sh.getString("city","");
            state=sh.getString("state",""); //newly added
            zip=sh.getString("pin","");
            landmark=sh.getString("landmark",""); //newly added
            areaname=sh.getString("areaname","");//newly added
            Log.i("z1",zip);


            for (int i = 0; i < cartlist.size(); i++) {
                final Cart cart = cartList.get(i);
                stringBuilder.append(cart.getTitle()).append("=").append(cart.getQuantity()+"\n");

                qntsum= Integer.parseInt(cart.getQuantity());
                item_name= cart.getAttribute();
                if(item_name.equals("1 kg") || item_name.equals("1 ltr")){
                    item_name= String.valueOf(1000);
                }
                else {
                    item_name= cart.getAttribute();
                }

                int values=Integer.parseInt(item_name.replaceAll("[^0-9]", ""));
                Log.i("attrs", String.valueOf(values));
                totals+=qntsum*values;


            }
            Intent in=new Intent(getActivity(), PayuConfirmAct.class);
            bundle.putString("amount", String.valueOf(_total));//total amount
            bundle.putString("name", nm);//customer name
            bundle.putString("email", em);//customer email
            bundle.putString("mobileno", mob);//customer mobile
            bundle.putString("address", addrs);//customer addrss
            bundle.putString("items_list", String.valueOf(stringBuilder));//items_list
            bundle.putString("orderno", orderNo);//customer addrss
            bundle.putString("status", "orderplaced");//order status
            bundle.putString("city", city);//customer addrss
            bundle.putString("paymenttype", "PAYU");//customer addrss
            bundle.putString("createdon", currentDateandTime);//customer addrss
            bundle.putInt("qtsm", totals);//price
            bundle.putString("myorderid", id);//customer addrss
            bundle.putString("state", state);//state
            bundle.putString("zip", zip);
            bundle.putString("landmark", landmark);//to do newly added
            bundle.putString("areaname", areaname);//to do newly added

             Log.i("stringbus", String.valueOf(stringBuilder));
            in.putExtras(bundle);
          //  Toast.makeText(getActivity(), stringBuilder, Toast.LENGTH_SHORT).show();
           startActivity(in);
        });




        return view;
    }

    public void showDeleteDialog() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getActivity())

                //set message, title, and icon
                .setTitle("Oder Confirm")
                .setMessage("Do you want to Place Order")
                .setIcon(R.drawable.close)

                .setPositiveButton("Yes", (dialog, whichButton) -> {
                    amount = ((BaseActivity) getActivity()).getTotalPrice();
                    List<Cart> cartlist = ((BaseActivity) getActivity()).getCartList();
                    nm=sh.getString("name","");
                    em=sh.getString("email","");
                    mob=sh.getString("mobile","");
                    addrs=sh.getString("address","");
                    Log.i("nms",nm);
                    for (int i = 0; i < cartlist.size(); i++) {
                        final Cart cart = cartList.get(i);
                        stringBuilder.append(cart.getTitle()).append("=").append(cart.getQuantity()+" ");
                    }
                    Log.i("item_name",stringBuilder.toString());

                   // insertdata();
                    closeProgress();
                })

                .setNegativeButton("No", (dialog, which) -> {
                 //   processinsertdata();
                    dialog.dismiss();
                })
                .create();
        myQuittingDialogBox.show();

    }

    private void closeProgress() {
        Handler handler = new Handler();
        handler.postDelayed(() -> progressDialog.dismiss(), 3000); // 5000 milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        Order order = new Order(id, orderNo, currentDateandTime, "Rs. " + _totalAmount, "CONFIRMED");
        orderList.add(order);
        String orderString = gson.toJson(orderList);
        localStorage.setOrder(orderString);
        localStorage.deleteCart();

    }






    private void setUpCartRecyclerview() {

        cartList = new ArrayList<>();
        cartList = ((BaseActivity) getContext()).getCartList();


        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        adapter = new CheckoutCartAdapter(cartList, getContext());
        recyclerView.setAdapter(adapter);
    }





    private void useritemdetails(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        List<Cart> cartlist = ((BaseActivity) getActivity()).getCartList();
        nm=sh.getString("name","");
        em=sh.getString("email","");
        mob=sh.getString("mobile","");
        addrs=sh.getString("address","");
        city=sh.getString("city","");
        state=sh.getString("state",""); //newly added
        zip=sh.getString("pin","");
        landmark=sh.getString("landmark",""); //newly added
        areaname=sh.getString("areaname","");//newly added
        Log.i("z1",zip);


        for (int i = 0; i < cartlist.size(); i++) {
            final Cart cart = cartList.get(i);
            stringBuilder2.append(cart.getTitle()).append("=").append(cart.getQuantity() + "\n");
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REST.USERITEMDETAILSINSERT , response -> {
            //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();

        },
                error -> Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                params.put("name",nm);
                params.put("emailid",em);
                params.put("mobile",mob);
                params.put("addrs",addrs);
                params.put("city",city);
                params.put("state",state);
                params.put("zip",zip);
                params.put("areaname",areaname);
                params.put("landmark",landmark);
                params.put("items_list", String.valueOf(stringBuilder2));
                params.put("amount", String.valueOf(_total));
                params.put("created_on", currentDateandTime);
                Log.i("dbparams", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Confirm");
    }

}
