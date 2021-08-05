package com.example.xermart.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.xermart.FragAdapter.PagerAdapter;
import com.example.xermart.Fragments.FragmentCategory;
import com.example.xermart.Fragments.FragmentMainMenu;
import com.example.xermart.Fragments.FragmentOrder;
import com.example.xermart.Logn;
import com.example.xermart.R;
import com.example.xermart.User.AllCategories;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static final float END_SCALE = 0.7f;

    DrawerLayout drawerLayout;
    NavigationView nav_view;
    ImageView menuIcon;
    RelativeLayout content;

    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLanguage();
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.xercv));

        drawerLayout = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        content = findViewById(R.id.content);

        navigationDrawer();

        changeLanguage();

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);

        setupViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentMainMenu(), "Main Fragment");
        adapter.addFragment(new FragmentCategory(), "Category Fragment");
        adapter.addFragment(new FragmentOrder(), "Order Fragment");

        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        mViewPager.setCurrentItem(fragmentNumber);
    }

    private void navigationDrawer() {
        nav_view.bringToFront();
        nav_view.setNavigationItemSelectedListener(this);
        nav_view.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else

            super.onBackPressed();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_categories:
                startActivity(new Intent(getApplicationContext(), AllCategories.class));
                break;
            case R.id.changelanguage:
                showChangeLanguageDialog();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Logn.class));
                break;
        }

        return true;
    }

    private void showChangeLanguageDialog() {
        final String[] langs = {"Swahili", "French", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Main.this);
        mBuilder.setTitle("Select Language");
        mBuilder.setSingleChoiceItems(langs, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int xer) {
                if (xer == 0) {
                    setLocale("sw");
                    recreate();
                } else if (xer == 1) {
                    setLocale("fr");
                    recreate();
                } else if (xer == 2) {
                    setLocale("en");
                    recreate();
                }
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mBuilder.show();
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("my_text", MODE_PRIVATE).edit();
        editor.putString("xer", language);
        editor.apply();
    }

    public void changeLanguage() {
        SharedPreferences preferences = getSharedPreferences("my_text", Activity.MODE_PRIVATE);
        String lang = preferences.getString("xer", "");
        setLocale(lang);
    }


}