package com.example.fanzhang.myapplication3;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    public static double lat;
    public static double lon;
    private Button searchBt;
    private FusedLocationProviderClient mFusedLocationClient;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        FontIconTypefaceHolder.init(getAssets(), "fontawesome-webfont.ttf");

//        Typeface font = Typeface.createFromAsset(getAssets(),"font/fontawesome_webfont.ttf");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Place Search");
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText("SEARCH"));
//        tabLayout.addTab(tabLayout.newTab().setText("FAVORITES"));
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(0).setCustomView(getTabView("SEARCH",R.drawable.search));
        tabLayout.getTabAt(1).setCustomView(getTabView("FAVORITES",R.drawable.whiteheart));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.i("btn","locOK");
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            Toast.makeText(MainActivity.this,lat+" "+lon,Toast.LENGTH_SHORT).show();
                        }
                    }
                });







    }



    public View getTabView(String str, int image_id) {
        //change tab xml to view
        View view = LayoutInflater.from(this).inflate(R.layout.tab, null);
        TextView txt_title = (TextView) view.findViewById(R.id.text);
        txt_title.setText(str);
        ImageView img_title = (ImageView) view.findViewById(R.id.imageView);
        img_title.setImageResource(image_id);
        return view;
    }

}
