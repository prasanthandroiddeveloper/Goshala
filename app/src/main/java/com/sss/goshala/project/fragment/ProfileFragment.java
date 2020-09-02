package com.sss.goshala.project.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sss.goshala.project.R;
import com.sss.goshala.project.activity.MainActivity;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    SharedPreferences sharedPreferences;
    TextView name,email,mobile,address,area,cty;
    String nm,em,mob,addrs,ar,ct;
    Button addbtn;
    LinearLayout lnrname,lnrmobile,lnraddrs,lnrcity,lnrarea,lnremial;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sharedPreferences= Objects.requireNonNull(getActivity()).getSharedPreferences("MySharedPref", MODE_PRIVATE);


        name = view.findViewById(R.id.nameTv);
        email = view.findViewById(R.id.emailTv);
        mobile = view.findViewById(R.id.mobTv);
        address = view.findViewById(R.id.addrsTv);
        area = view.findViewById(R.id.areaTv);
        cty = view.findViewById(R.id.cityTv);
        addbtn=view.findViewById(R.id.addbtn);


        lnrname=view.findViewById(R.id.labelname);
        lnremial=view.findViewById(R.id.labelemail);
        lnrcity=view.findViewById(R.id.labelcity);
        lnraddrs=view.findViewById(R.id.labeldoorno);
        lnrmobile=view.findViewById(R.id.labelmobile);
        lnrarea=view.findViewById(R.id.labelarea);




        nm=sharedPreferences.getString("name","");
        em=sharedPreferences.getString("email","");
        mob=sharedPreferences.getString("mobile","");
        addrs=sharedPreferences.getString("address","");
        ar=sharedPreferences.getString("areaname","");
        ct=sharedPreferences.getString("city","");

        name.setText(nm);
        email.setText(em);
        mobile.setText(mob);
        address.setText(addrs);
        area.setText(ar);
        cty.setText(ct);


      if(nm.isEmpty()){
          Toast.makeText(getActivity(), "Please Fill your Details After Selection of Products in Address Page", Toast.LENGTH_SHORT).show();
          lnrname.setVisibility(View.GONE);
          lnremial.setVisibility(View.GONE);
          lnrmobile.setVisibility(View.GONE);
          lnraddrs.setVisibility(View.GONE);
          lnrarea.setVisibility(View.GONE);
          lnrcity.setVisibility(View.GONE);
      }
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("My Profile");
    }
}
