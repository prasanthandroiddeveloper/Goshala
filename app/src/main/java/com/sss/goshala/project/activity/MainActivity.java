package com.sss.goshala.project.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sss.goshala.project.R;
import com.sss.goshala.project.fragment.AboutUs;
import com.sss.goshala.project.fragment.CategoryFragment;
import com.sss.goshala.project.fragment.HomeFragment;
import com.sss.goshala.project.fragment.MyOrdersFragment;
import com.sss.goshala.project.fragment.ProfileFragment;
import com.sss.goshala.project.helper.Converter;
import com.sss.goshala.project.model.User;
import com.sss.goshala.project.util.localstorage.LocalStorage;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static int cart_count = 0;
    User user;
    SharedPreferences sharedPreferences;
    String name,unique_id;


    @SuppressLint("ResourceAsColor")
    static void centerToolbarTitle(@NonNull final Toolbar toolbar) {
        final CharSequence title = toolbar.getTitle();
        final ArrayList<View> outViews = new ArrayList<>(1);
        toolbar.setBackgroundColor(Color.parseColor("#43A047"));
        toolbar.findViewsWithText(outViews, title, View.FIND_VIEWS_WITH_TEXT);
        if (!outViews.isEmpty()) {
            final TextView titleView = (TextView) outViews.get(0);
            titleView.setGravity(Gravity.CENTER);
            titleView.setTextColor(Color.parseColor("#ffffff"));
            final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) titleView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            toolbar.requestLayout();
            //also you can use titleView for changing font: titleView.setTypeface(Typeface);
        }
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(() -> exit = false, 3 * 1000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        MenuItem menuItem1 = menu.findItem(R.id.search);
        menuItem1.setVisible(false);
        menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this, cart_count, R.drawable.cartwhite));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cart_action) {
            startActivity(new Intent(getApplicationContext(), CartActivity.class));
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    @SuppressLint({"RestrictedApi", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        centerToolbarTitle(toolbar);
        cart_count = cartCount();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(view -> {
            String phoneNumberWithCountryCode = "+917207208385";
            String message = "Hi I would like to know About Panchgavya Products";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://api.whatsapp.com/send?phone=%s&text=%s", phoneNumberWithCountryCode, message))));
                }
        );


        localStorage = new LocalStorage(getApplicationContext());
        String userString = localStorage.getUserLogin();
        Gson gson = new Gson();
        userString = localStorage.getUserLogin();
        user = gson.fromJson(userString, User.class);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().getItem(0).setChecked(true);

        TextView nav_user = hView.findViewById(R.id.nav_header_name);
        LinearLayout nav_footer = findViewById(R.id.footer_text);
        sharedPreferences = MainActivity.this.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        name = sharedPreferences.getString("name", "");

        if (name != null) {
            nav_user.setText(name);
        }
        nav_footer.setOnClickListener(view -> {
            localStorage.logoutUser();

        });

        displaySelectedScreen(R.id.nav_home);




    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.nav_category:
                fragment = new CategoryFragment();
                break;
            case R.id.nav_popular_products:
              /*  fragment = new PopularProductFragment();*/
                break;
            case R.id.nav_new_product:
             /*   fragment = new NewProductFragment();*/
                break;

            case R.id.nav_offers:
           /*     fragment = new OffrersFragment();*/
                break;
            case R.id.nav_search:
                //fragment = new CategoryFragment();
                break;
            case R.id.nav_my_order:
                fragment = new MyOrdersFragment();
                break;
            case R.id.nav_my_cart:
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;

            case R.id.nav_about_us:
                fragment=new AboutUs();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
            ft.replace(R.id.content_frame, fragment);
            ft.commit();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    @Override
    public void onAddProduct() {
        super.onAddProduct();
        cart_count++;
        invalidateOptionsMenu();

    }

    @Override
    public void onRemoveProduct() {
        super.onRemoveProduct();
    }





}
