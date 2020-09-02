package com.sss.goshala.project.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.sss.goshala.project.util.localstorage.LocalStorage;
import com.sss.goshala.project.util.localstorage.REST;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.ceil;

public class UpiPaymentAct extends AppCompatActivity {

    EditText amount, note, name, upivirtualid,tgms,ethandling,etdelivery,etfinalprc,etappcoupon,etdiscprice;
    Button send,btnapplycoupon;
    String TAG ="main";
    final int UPI_PAYMENT = 0;
    String  getamnt,getname,getemail,getmobileno,getaddrs,getitemlist,getorderno,getstatus,getcity,getpaytype,getcreatedon,getmyorderid
            ,getstate,getpin,getareaname,getlandmark;
    Bundle bundle;
    int gettotalsumweigths;
    double d1,d2,d3,d4,d5,d6,amountverify,d10;
    LocalStorage localStorage;
    TextView T1,T2,plusone,plustwo;
    String uniqueid,COUPON_CODE,GETCOUPON_CODE,discountamount,comments;
    ProgressDialog pd;
    LinearLayout disclinearlyt;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upi_payment);
        localStorage=new LocalStorage(UpiPaymentAct.this);
        pd=new ProgressDialog(UpiPaymentAct.this);

        send = findViewById(R.id.send);
        amount = findViewById(R.id.amount_et);
        note = findViewById(R.id.note);
        name = findViewById(R.id.name);
        upivirtualid = findViewById(R.id.upi_id);
        tgms = findViewById(R.id.totalweights);
        ethandling = findViewById(R.id.handling_et);
        etdelivery = findViewById(R.id.delivery_et);
        etfinalprc = findViewById(R.id.final_et);
        etdiscprice = findViewById(R.id.afterDisc_et);
        disclinearlyt = findViewById(R.id.discLayout);
        etappcoupon = findViewById(R.id.app_coupon);
        btnapplycoupon = findViewById(R.id.applyBtn);
        T1=findViewById(R.id.t1);
        T2=findViewById(R.id.t2);
        plusone=findViewById(R.id.plusOne);
        plustwo=findViewById(R.id.plusTwo);
        bundle=getIntent().getExtras();
        assert bundle != null;
        uniqueid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

     //   Toast.makeText(this, uniqueid, Toast.LENGTH_SHORT).show();
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
        getareaname=bundle.getString("areaname");
        getlandmark=bundle.getString("landmark");

        tgms.setText(String.valueOf(gettotalsumweigths));
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
        Log.i("getpinno", getpin);
        Log.i("getareaname", getareaname);
        Log.i("getlandmark", getlandmark);

       //Toast.makeText(UpiPaymentAct.this, getitemlist, Toast.LENGTH_SHORT).show();

        applyCoupon();




        amountverify=Double.parseDouble(getamnt);

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

        //todo changed here
        d6=d2+d5;


        if(amountverify>=1000){
            amount.setText(String.valueOf(getamnt));
            etfinalprc.setText(String.valueOf(getamnt));
            amount.setVisibility(View.GONE);
            ethandling.setVisibility(View.GONE);
            etdelivery.setVisibility(View.GONE);
            plusone.setVisibility(View.GONE);
            plustwo.setVisibility(View.GONE);
            T1.setVisibility(View.GONE);
            T2.setVisibility(View.GONE);
            Toast.makeText(this, "Hi!! You have Got Shipping Free", Toast.LENGTH_SHORT).show();

        }else{
            amount.setText(String.valueOf(getamnt));
            ethandling.setText(String.valueOf(d2));
            etfinalprc.setText(String.valueOf(d4));
            etdelivery.setText(String.valueOf(d6));


        }
        btnapplycoupon.setOnClickListener(view -> {
            GETCOUPON_CODE=etappcoupon.getText().toString();

            if(GETCOUPON_CODE.equals(COUPON_CODE)){
                discountamount= String.valueOf(Math.round(Double.parseDouble(getamnt)*((Float.parseFloat("20"))/Float.parseFloat("100"))));
                Log.i("cop", (getamnt));

                if(amountverify>=1000){
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



      //  amount.setText(String.valueOf(d3));


        send.setOnClickListener(view -> {

            if (TextUtils.isEmpty(name.getText().toString().trim())){
                Toast.makeText(UpiPaymentAct.this," Name is invalid", Toast.LENGTH_SHORT).show();

            }else if (TextUtils.isEmpty(upivirtualid.getText().toString().trim())){
                Toast.makeText(UpiPaymentAct.this," UPI ID is invalid", Toast.LENGTH_SHORT).show();

            }else if (TextUtils.isEmpty(amount.getText().toString().trim())){
                Toast.makeText(UpiPaymentAct.this," Amount is invalid", Toast.LENGTH_SHORT).show();
            }else{


                if(TextUtils.isEmpty(note.getText().toString())){
                    comments="Payment";
                } else {
                    comments=note.getText().toString().trim();
                }

                payUsingUpi(name.getText().toString(), upivirtualid.getText().toString(),
                       comments, etfinalprc.getText().toString());

            }

        });
    }

    void payUsingUpi(  String name,String upiId, String note, String amount) {
        Log.e("main ", "name "+name +"--up--"+upiId+"--"+ note+"--"+amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        upiPayIntent.setPackage("com.google.android.apps.nbu.paisa.user");

        try{

            startActivityForResult(upiPayIntent, UPI_PAYMENT);

        } catch (ActivityNotFoundException e) {

            Toast.makeText(this, "Please Install Google Pay and Try Again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );

        if (requestCode == UPI_PAYMENT) {
            if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                if (data != null) {
                    String trxt = data.getStringExtra("response");
                    Log.e("UPI", "onActivityResult: " + trxt);
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(trxt);
                    upiPaymentDataOperation(dataList);
                } else {
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
            } else {
                //when user simply back without payment
                Log.e("UPI", "onActivityResult: " + "Return data is null");
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }
        }
    }



    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(UpiPaymentAct.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String[] response = str.split("&");
            for (String s : response) {
                String[] equalStr = s.split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }


            if (status.equals("success")) {
                //Code to handle successful transaction here.
               Toast.makeText(UpiPaymentAct.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, REST.ITEMSORDERED, response1 -> {
                    Log.i("dataresponse",response1);
                     localStorage.deleteCart();
                     insertUserDeviceId();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Calendar c = Calendar.getInstance();
                    try {
                        c.setTime(sdf.parse(getcreatedon));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    c.add(Calendar.DATE, 10);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    String output = sdf1.format(c.getTime());




                       Intent in=new Intent(UpiPaymentAct.this,FinalOrderAct.class);
                       bundle.putString("odid",getorderno);
                       bundle.putString("tamnt", etfinalprc.getText().toString());
                       bundle.putString("excpdt", output);
                       bundle.putString("paytpe","UPI");
                       bundle.putString("discprice",(discountamount==null) ? "no":discountamount);
                       in.putExtras(bundle);
                       startActivity(in);

                }, error -> {
                    // Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
                    Toast.makeText(UpiPaymentAct.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params =new HashMap<>();

                        params.put("order_id",getorderno);
                        params.put("created_on",getcreatedon);
                        params.put("items_list",getitemlist);
                        params.put("total_amount", etfinalprc.getText().toString());
                        params.put("customer_name", getname);
                        params.put("contact_num", getmobileno);
                        params.put("address", getaddrs);
                        params.put("city", getcity);
                        params.put("status", getstatus);
                        params.put("payment_type", getpaytype);
                        params.put("email", getemail);
                        params.put("state", getstate);
                        params.put("pincode", getpin);
                        params.put("areaname", getareaname);//todo newly added
                        params.put("landmark", getlandmark);//todo newly added
                        params.put("coupondisc",(discountamount==null) ? "no":discountamount );//todo newly added
                        Log.i("pameters", String.valueOf(params));

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(UpiPaymentAct.this);
                requestQueue.add(stringRequest);

            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(UpiPaymentAct.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);
                Intent intent=new Intent(UpiPaymentAct.this,CheckoutActivity.class);
                startActivity(intent);

            }
            else {
                Intent intent=new Intent(UpiPaymentAct.this,CheckoutActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Payment Cancelled by user", Toast.LENGTH_SHORT).show();

            }
        } else {
            Log.e("UPI", "Internet issue: ");

            Toast.makeText(UpiPaymentAct.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected() && netInfo.isConnectedOrConnecting() && netInfo.isAvailable();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent in=new Intent(UpiPaymentAct.this,CheckoutActivity.class);
        startActivity(in);
        super.onBackPressed();
    }


    private void applyCoupon(){
        pd.setMessage("Loading");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REST.USERDISCOUNTCHECK, response -> {
           // Toast.makeText(UpiPaymentAct.this, response, Toast.LENGTH_SHORT).show();

            if(response.equals("NEWUSER")){

                 COUPON_CODE="GOSHALA20";

            }else{
                COUPON_CODE="NONO20NONO";
               // Toast.makeText(this, "COUPON IS ALLOWED TO USE ONLY ONCE ", Toast.LENGTH_SHORT).show();
            }

            pd.dismiss();

        }, error -> Toast.makeText(UpiPaymentAct.this, "Something went wrong.", Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                params.put("user_device_id",uniqueid);
                Log.i("badari76", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UpiPaymentAct.this);
        requestQueue.add(stringRequest);
    }

    private void insertUserDeviceId(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REST.USERIDINSERTCOUPON, response -> {
          //  Toast.makeText(UpiPaymentAct.this, response, Toast.LENGTH_SHORT).show();


        }, error -> Toast.makeText(UpiPaymentAct.this, "Something went wrong.", Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                params.put("user_device_id",uniqueid);
                Log.i("badari77", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UpiPaymentAct.this);
        requestQueue.add(stringRequest);

    }

}