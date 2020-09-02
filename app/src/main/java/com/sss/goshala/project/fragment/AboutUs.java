package com.sss.goshala.project.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sss.goshala.project.R;
import com.sss.goshala.project.activity.MainActivity;

import java.util.Objects;


public class AboutUs extends Fragment {

    View v;
    TextView aboutTv,fbTv,contacttv,wtsaptv,contacttv1;
    Intent in;
    String fbid,phnno;

    public AboutUs() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_about_us, container, false);
        aboutTv=v.findViewById(R.id.blogtv);
        fbTv=v.findViewById(R.id.facebooktv);
        contacttv=v.findViewById(R.id.contacttv);
        contacttv1=v.findViewById(R.id.contacttv1);
        wtsaptv=v.findViewById(R.id.wtsapptv);
        aboutTv.setOnClickListener(view -> {
            Uri uri = Uri.parse("https://madeinmygoshala.com/");
             in = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(in);
        });


        fbTv.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/html");
            intent.putExtra(Intent.EXTRA_EMAIL, "madeinmygoshala@gmail.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Need Information about Goshala ");
            intent.putExtra(Intent.EXTRA_TEXT, "Hi I would like to know information about Goshala Products");

            startActivity(Intent.createChooser(intent, "Send Email"));
        });

        wtsaptv.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + "Chilakalapudi, Machilipatnam, Andhra Pradesh 521002"));
            AboutUs.this.startActivity(intent);

        });



        contacttv.setOnClickListener(view -> {
            try {
                phnno="7207208385";
                Uri call = Uri.parse("tel:" + phnno);
                in = new Intent(Intent.ACTION_DIAL, call);
                startActivity(in);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Could not Place a call now", Toast.LENGTH_SHORT).show();
            }
        });


        contacttv1.setOnClickListener(view -> {
            try {
                phnno="7207207485";
                Uri call = Uri.parse("tel:" + phnno);
                in = new Intent(Intent.ACTION_DIAL, call);
                startActivity(in);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Could not Place a call now", Toast.LENGTH_SHORT).show();
            }
        });


        Objects.requireNonNull(v).setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener((v12, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    return true;
                }
            }
            return false;
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        Objects.requireNonNull(getActivity()).setTitle("Contact Us");
    }
}
