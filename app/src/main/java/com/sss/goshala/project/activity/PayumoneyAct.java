package com.sss.goshala.project.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.sss.goshala.project.R;
import com.sss.goshala.project.payumoney.ServiceWrapper;
import com.sss.goshala.project.util.localstorage.LocalStorage;
import com.sss.goshala.project.util.localstorage.REST;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayumoneyAct extends AppCompatActivity {

    String getname,getorderno,getamount,getphone,getitemslist,getemail,getadrs,getcity,getstate
            ,getstatus,getcreatedon,getpin,getareaname,getlandmark,uniqueid,getcoupondisc;
    Bundle bun;
    LocalStorage localStorage;

    PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
    PayUmoneySdkInitializer.PaymentParam paymentParam;
    {
        try {
            paymentParam = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String TAG ="Payuact", txnid ="txt12346", amount ="1", phone ="7659914896",
            prodname ="test", firstname ="prasanth", email ="githubprasanth@gmail.com",
            merchantId ="6437519", merchantkey="Irpxi2JK";  //   first test key only

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payumoney);
        localStorage=new LocalStorage(PayumoneyAct.this);
        uniqueid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        bun=getIntent().getExtras();
        assert bun != null;
        getname=bun.getString("name");
        getorderno=bun.getString("orderno");
        getamount=bun.getString("amount");
        getphone=bun.getString("mobileno");
        getitemslist=bun.getString("items_list");
        getemail=bun.getString("email");
        getadrs=bun.getString("address");
        getstate=bun.getString("state");
        getstatus=bun.getString("status");
        getcity=bun.getString("city");
        getcreatedon=bun.getString("createdon");
        getpin=bun.getString("pin");
        getareaname=bun.getString("areaname");
        getlandmark=bun.getString("landmark");
        getcoupondisc=bun.getString("coupondisc");


       //Log.i("pyu",getname+"\n"+getorderno+"\n"+getamount+"\n"+getphone+"\n"+getitemslist+"\n"+getstate);
        Log.i("getname", getname);
        Log.i("getemail", getemail);
        Log.i("getmobileno", getphone);
        Log.i("getaddrs", getadrs);
        Log.i("getitemlist", getitemslist);
        Log.i("getorderno", getorderno);
        Log.i("getstatus", getstatus);
        Log.i("getcity", getcity);
        Log.i("getcreatedon", getcreatedon);
        Log.i("getstate", getstate);
        Log.i("getpinno", getpin);
        Log.i("getareaname", getareaname);
        Log.i("getlandmark", getlandmark);

      //Toast.makeText(this, getitemslist, Toast.LENGTH_SHORT).show();

      startpay();
    }

    public void startpay(){

        builder.setAmount(getamount)                          // Payment amount
                .setTxnId(getorderno)                     // Transaction ID
                .setPhone(getphone)                   // User Phone number
                .setProductName(prodname)                   // Product Name or description
                .setFirstName(getname)                              // User First name
                .setEmail(getemail)              // User Email ID
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")     // Success URL (surl)
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")     //Failure URL (furl)
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(false)                              // Integration environment - true (Debug)/ false(Production)
                .setKey(merchantkey)                        // Merchant key
                .setMerchantId(merchantId);


        try {
            paymentParam = builder.build();
            // generateHashFromServer(paymentParam );
            getHashkey();

        } catch (Exception e) {
            Log.e(TAG, " error s "+e.toString());
        }

    }


    public void getHashkey(){
        ServiceWrapper service = new ServiceWrapper(null);
        Call<String> call = service.newHashCall(merchantkey, getorderno, getamount, prodname,
                getname, getemail);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e(TAG, "hash res "+response.body());
                String merchantHash= response.body();
                if (merchantHash.isEmpty() || merchantHash.equals("")) {
                    Toast.makeText(PayumoneyAct.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "hash empty");
                } else {
                    // mPaymentParams.setMerchantHash(merchantHash);
                    paymentParam.setMerchantHash(merchantHash);
                    // Invoke the following function to open the checkout page.
                    // PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, StartPaymentActivity.this,-1, true);
                    PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, PayumoneyAct.this, R.style.AppTheme_default, true);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "hash error "+ t.toString());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra( PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE );

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if(transactionResponse.getTransactionStatus().equals( TransactionResponse.TransactionStatus.SUCCESSFUL )){
                    //Success Transaction
                    Log.i("helo","successgood");
                    insertdata();
                    insertUserDeviceId();


                } else{
                    //Failure Transaction
                   Intent intent=new Intent(PayumoneyAct.this,CheckoutActivity.class);
                   startActivity(intent);
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
                Log.e(TAG, "tran "+payuResponse+"---"+ merchantResponse);
            }
        }
    }


    private void insertdata(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REST.ITEMSORDERED, response1 -> {
            Log.i("payu",response1);
            localStorage.deleteCart();

            //todo newly added

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



            Intent in=new Intent(PayumoneyAct.this,FinalOrderAct.class);
            bun.putString("odid",getorderno);
            bun.putString("tamnt", getamount);
            bun.putString("paytpe","PAYU");
            bun.putString("excpdt", output);
            bun.putString("discprice", getcoupondisc);
            in.putExtras(bun);
            startActivity(in);

        }, error -> {
            // Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
            Toast.makeText(PayumoneyAct.this, error.getMessage(), Toast.LENGTH_LONG).show();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("order_id",getorderno);
                params.put("created_on",getcreatedon);
                params.put("items_list",getitemslist);
                params.put("total_amount", getamount);
                params.put("customer_name", getname);
                params.put("contact_num", getphone);
                params.put("address", getadrs);
                params.put("city", getcity);
                params.put("status", getstatus);
                params.put("payment_type", "PAYU");
                params.put("email", getemail);
                params.put("state", getstate);// newly added
                params.put("pincode", getpin);//todo newly added
                params.put("areaname", getareaname);//todo newly added
                params.put("landmark", getlandmark);//todo newly added
                params.put("coupondisc", getcoupondisc);//todo newly added
                Log.i("pameters", String.valueOf(params));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PayumoneyAct.this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        Intent in=new Intent(PayumoneyAct.this,CheckoutActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        super.onBackPressed();
    }

    private void insertUserDeviceId(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REST.USERIDINSERTCOUPON, response -> {
           // Toast.makeText(PayumoneyAct.this, response, Toast.LENGTH_SHORT).show();


        }, error -> Toast.makeText(PayumoneyAct.this, "Something went wrong.", Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                params.put("user_device_id",uniqueid);
                Log.i("badari77", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PayumoneyAct.this);
        requestQueue.add(stringRequest);

    }
}