package com.sss.goshala.project.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sss.goshala.project.R;
import com.sss.goshala.project.activity.MainActivity;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;


public class FinalOrder extends Fragment {

    View view;
    String oderid,resp,amnt;
    TextView tv,amnttv;
    SharedPreferences sh;
    ImageView imageView;
    WebView webView;


    public FinalOrder() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_final_order, container, false);
        sh= Objects.requireNonNull(getActivity()).getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        tv=view.findViewById(R.id.orderTv);
        amnttv=view.findViewById(R.id.amount_id);
        imageView=view.findViewById(R.id.successImg);
        webView=view.findViewById(R.id.webviewr);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        oderid=sh.getString("odid","");
        amnt=sh.getString("tamnt","");
        tv.setText(oderid);
        amnttv.setText(amnt);
        Animation ani= AnimationUtils.loadAnimation(getActivity(),R.anim.blink);
        imageView.startAnimation(ani);



        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            }
        };
        Timer t = new Timer();
        t.schedule(task, 10000);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Order Details");
    }
}
