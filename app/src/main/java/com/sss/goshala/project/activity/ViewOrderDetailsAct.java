package com.sss.goshala.project.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sss.goshala.project.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewOrderDetailsAct extends AppCompatActivity {

    Bundle bundle;
    String itemlst,orid,odprc,odt,odrstatus,odrcoupondisc;
    TextView tvitem,tvodrid,tvamnt,tvdt,tvodrdisc,tvodrlabel;
    ImageView i1,i2,i3,i4;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_details);
        i1=findViewById(R.id.img1);
        i2=findViewById(R.id.img2);
        i3=findViewById(R.id.img3);
        bundle=getIntent().getExtras();
        assert bundle!=null;



        itemlst=bundle.getString("items");
        orid=bundle.getString("oid");
        odprc=bundle.getString("oprice");
        odt=bundle.getString("createdon");
        odrstatus=bundle.getString("orderstatus");
        odrcoupondisc=bundle.getString("coupondisc");

       // assert odrcoupondisc != null;



        Log.i("odrstatus",odrstatus);



        if (odrstatus != null && odrstatus.equals("Processing")) {

            i1.setBackgroundResource(R.drawable.ic_tracker_green);
            i2.setBackgroundResource(R.drawable.ic_tracker_btn);
            i3.setBackgroundResource(R.drawable.ic_tracker_btn);
        } else if (odrstatus != null && odrstatus.equals("Shipping")) {
            i1.setBackgroundResource(R.drawable.ic_tracker_green);
            i2.setBackgroundResource(R.drawable.ic_tracker_green);
            i3.setBackgroundResource(R.drawable.ic_tracker_btn);
        } else if(odrstatus != null && odrstatus.equals("orderplaced")){
            i1.setBackgroundResource(R.drawable.ic_tracker_btn);
            i2.setBackgroundResource(R.drawable.ic_tracker_btn);
            i3.setBackgroundResource(R.drawable.ic_tracker_btn);
        }else{
            i1.setBackgroundResource(R.drawable.ic_tracker_green);
            i2.setBackgroundResource(R.drawable.ic_tracker_green);
            i3.setBackgroundResource(R.drawable.ic_tracker_green);
        }


        Log.i("odrstatus",odrstatus);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(odt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 10);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String output = sdf1.format(c.getTime());


        tvitem=findViewById(R.id.items);
        tvodrid=findViewById(R.id.orderTv);
        tvamnt=findViewById(R.id.amount_id);
        tvdt=findViewById(R.id.orderdt);
        tvodrdisc=findViewById(R.id.odercoupondisc);
        tvodrlabel=findViewById(R.id.couponlblTV);
        tvitem.setText(itemlst);
        tvodrid.setText(orid);
        tvamnt.setText(odprc);
        tvdt.setText(output);




        if(odrcoupondisc.equals("") || odrcoupondisc.equals("no")){
            tvodrdisc.setVisibility(View.GONE);
            tvodrlabel.setVisibility(View.GONE);
        }else {
            tvodrdisc.setVisibility(View.VISIBLE);
            tvodrlabel.setVisibility(View.VISIBLE);
            tvodrdisc.setText(odrcoupondisc);
        }


    }
}