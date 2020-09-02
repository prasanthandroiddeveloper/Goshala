package com.sss.goshala.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sss.goshala.project.R;

import java.util.Timer;
import java.util.TimerTask;

public class FinalOrderAct extends AppCompatActivity {

    String oderid,amnt,expectddt,paytpe,coupondiscount;
    TextView tv,amnttv,expecteddtv,payTypetv,coupondisctv,couponlabel;
    ImageView imageView;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_order);

        bundle=getIntent().getExtras();

        tv=findViewById(R.id.orderTv);
        amnttv=findViewById(R.id.amount_id);
        expecteddtv=findViewById(R.id.expecteddt);
        payTypetv=findViewById(R.id.payTypetv);
        imageView=findViewById(R.id.successImg);
        coupondisctv=findViewById(R.id.couponDiscTV);
        couponlabel=findViewById(R.id.couponlabelTV);

        oderid=bundle.getString("odid");
        amnt=bundle.getString("tamnt");
        expectddt=bundle.getString("excpdt");
        paytpe=bundle.getString("paytpe");
        coupondiscount=bundle.getString("discprice");

        assert coupondiscount != null;
        if(coupondiscount.equals("no")){
            coupondisctv.setVisibility(View.GONE);
            couponlabel.setVisibility(View.GONE);
        }else{
           coupondisctv.setVisibility(View.VISIBLE);
           couponlabel.setVisibility(View.VISIBLE);
           coupondisctv.setText(coupondiscount);
        }

        tv.setText(oderid);
        amnttv.setText(amnt);
        expecteddtv.setText(expectddt);
        payTypetv.setText(paytpe);
        Animation ani= AnimationUtils.loadAnimation(FinalOrderAct.this,R.anim.blink);
        imageView.startAnimation(ani);

        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                Intent intent = new Intent(FinalOrderAct.this, MainActivity.class);
                startActivity(intent);
                FinalOrderAct.this.finish();
            }
        };
        Timer t = new Timer();
        t.schedule(task, 8000);

    }
}