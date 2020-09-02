package com.sss.goshala.project.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sss.goshala.project.R;
import com.sss.goshala.project.util.localstorage.REST;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.ceil;

public class PayuConfirmAct extends AppCompatActivity {


    EditText amount,ethandling,etdelivery,etfinalprc,etappcoupon,etdiscprice;
    String  getamnt,getname,getemail,getmobileno,getaddrs,getitemlist,getorderno,getstatus,getcity,getpaytype,
            getcreatedon,getmyorderid,getstate,getpin,getlandmark,getareaname;
    Bundle bundle;
    int gettotalsumweigths;
    double d1,d2,d3,d4,d5,d6,amtnvrfy,d10;
    Button send,btnapplycoupon;
    TextView T11,T22,plusone,plustwo;
    String uniqueid,COUPON_CODE,GETCOUPON_CODE,discountamount;
    ProgressDialog pd;
    LinearLayout disclinearlyt;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payu_confirm);
        pd=new ProgressDialog(PayuConfirmAct.this);

        amount = findViewById(R.id.amount_et);
        ethandling = findViewById(R.id.handling_et);
        etdelivery = findViewById(R.id.delivery_et);
        etfinalprc = findViewById(R.id.final_et);
        etdiscprice = findViewById(R.id.afterDisc_et);
        etappcoupon = findViewById(R.id.app_coupon);
        disclinearlyt = findViewById(R.id.discLayout);
        plusone=findViewById(R.id.plusOne);
        plustwo=findViewById(R.id.plusTwo);

        send = findViewById(R.id.send);
        T11=findViewById(R.id.t11);
        T22=findViewById(R.id.t22);
        btnapplycoupon = findViewById(R.id.applyBtn);

        bundle=getIntent().getExtras();
        assert bundle != null;
        uniqueid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        //amount.setEnabled(false);

        getamnt=bundle.getString("amount");
        getname=bundle.getString("name");
        getemail=bundle.getString("email");
        getmobileno=bundle.getString("mobileno");
        getaddrs=bundle.getString("address");
        getitemlist=bundle.getString("items_list");
        getorderno=bundle.getString("orderno");
        getstatus=bundle.getString("status");
        getcity=bundle.getString("city");
        getpaytype=bundle.getString("paymenttype");
        getcreatedon=bundle.getString("createdon");
        gettotalsumweigths=bundle.getInt("qtsm");
        getmyorderid=bundle.getString("myorderid");
        getstate=bundle.getString("state");
        getpin=bundle.getString("zip");
        getlandmark=bundle.getString("landmark");
        getareaname=bundle.getString("areaname");


        Log.i("test", String.valueOf(gettotalsumweigths));
        Log.i("getname", getname);
        Log.i("getemail", getemail);
        Log.i("getmobileno", getmobileno);
        Log.i("getaddrs", getaddrs);
        Log.i("getitemlist", getitemlist);
        Log.i("getorderno", getorderno);
        Log.i("getstatus", getstatus);
        Log.i("getcity", getcity);
        Log.i("getpaytype", getpaytype);
        Log.i("getcreatedon", getcreatedon);
        Log.i("getstate", getstate);
        Log.i("getpinpayu", getpin);

        amtnvrfy=Double.parseDouble(getamnt);


        applyCoupon();

        if(gettotalsumweigths>=500){
            d1=ceil(gettotalsumweigths)/ (500);
            Log.i("d1", String.valueOf(d1));
            d2=ceil(d1*20);
            Log.i("d2", String.valueOf(d2));
        }

        else{
            d2=20;
        }
        d3= Double.parseDouble(getamnt)+d2;
        d5=20;
        d4= ceil(d3+d5);
        d6=d2+d5;

        if(amtnvrfy>=1000){
            amount.setText(String.valueOf(getamnt));
            etfinalprc.setText(String.valueOf(getamnt));
            amount.setVisibility(View.GONE);
            ethandling.setVisibility(View.GONE);
            etdelivery.setVisibility(View.GONE);
            plusone.setVisibility(View.GONE);
            plustwo.setVisibility(View.GONE);
            T11.setVisibility(View.GONE);
            T22.setVisibility(View.GONE);
            Toast.makeText(this, "Hi!! You have Got Shipping Free", Toast.LENGTH_SHORT).show();

        }else {
            amount.setText(String.valueOf(getamnt));
            ethandling.setText(String.valueOf(d2));
            etfinalprc.setText(String.valueOf(d4));
            etdelivery.setText(String.valueOf(d6));
        }

        btnapplycoupon.setOnClickListener(view -> {
            GETCOUPON_CODE=etappcoupon.getText().toString();

            if(GETCOUPON_CODE.equals(COUPON_CODE)){
                discountamount= String.valueOf(Math.round(Double.parseDouble(getamnt)*((Float.parseFloat("20"))/Float.parseFloat("100"))));
                if(amtnvrfy>=1000){
                    d10=ceil(Double.parseDouble(getamnt)-Double.parseDouble(discountamount));
                }else{
                    d10=ceil((d4)-Double.parseDouble(discountamount));
                }
                Log.i("d10", String.valueOf(d10));
                etfinalprc.setText(String.valueOf(d10));
                disclinearlyt.setVisibility(View.VISIBLE);
                etdiscprice.setText(discountamount);


            }
            else{
                Toast.makeText(this, "Coupon Already Used", Toast.LENGTH_SHORT).show();
            }

        });


        amount.setEnabled(false);
        ethandling.setEnabled(false);
        etfinalprc.setEnabled(false);
        etdelivery.setEnabled(false);

      //  Toast.makeText(this, getitemlist, Toast.LENGTH_SHORT).show();


        send.setOnClickListener(view -> {
            Intent in=new Intent(PayuConfirmAct.this,PayumoneyAct.class);
            bundle.putString("amount", etfinalprc.getText().toString());//total amount
            bundle.putString("name", getname);//customer name
            bundle.putString("email", getemail);//customer email
            bundle.putString("mobileno", getmobileno);//customer mobile
            bundle.putString("address", getaddrs);//customer addrss
            bundle.putString("items_list", getitemlist);//items_list
            bundle.putString("orderno", getorderno);//customer addrss
            bundle.putString("status", "orderplaced");//order status
            bundle.putString("city", getcity);//customer addrss
            bundle.putString("paymenttype", "PAYU");//customer addrss
            bundle.putString("createdon", getcreatedon);//customer addrss
            bundle.putInt("qtsm", gettotalsumweigths);//price
            bundle.putString("myorderid", getmyorderid);//customer addrss
            bundle.putString("state", getstate);//state
            bundle.putString("pin", getpin);//pincode
            bundle.putString("areaname", getareaname);//pincode
            bundle.putString("landmark", getlandmark);//pincode
            bundle.putString("coupondisc", (discountamount==null) ? "no":discountamount);//pincode
            in.putExtras(bundle);
            startActivity(in);
        });



    }

    @Override
    public void onBackPressed() {
        Intent in=new Intent(PayuConfirmAct.this,CheckoutActivity.class);
        startActivity(in);
        super.onBackPressed();
    }

    private void applyCoupon(){
        pd.setMessage("Loading");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REST.USERDISCOUNTCHECK, response -> {
          //  Toast.makeText(PayuConfirmAct.this, response, Toast.LENGTH_SHORT).show();

            if(response.equals("NEWUSER")){

                COUPON_CODE="GOSHALA20";
                //insertUserDeviceId();
            }else{
                COUPON_CODE="NONO20NONO";
                // Toast.makeText(this, "COUPON IS ALLOWED TO USE ONLY ONCE ", Toast.LENGTH_SHORT).show();
            }

            pd.dismiss();

        }, error -> Toast.makeText(PayuConfirmAct.this, "Something went wrong.", Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                params.put("user_device_id",uniqueid);
                Log.i("badari76", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PayuConfirmAct.this);
        requestQueue.add(stringRequest);
    }


}