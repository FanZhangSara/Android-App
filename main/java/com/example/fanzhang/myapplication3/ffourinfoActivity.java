package com.example.fanzhang.myapplication3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import org.json.JSONObject;

public class ffourinfoActivity extends AppCompatActivity{
    public static String info = "";
    public static String eachdetailres ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info="";
        GoogleApiClient mGoogleApiClient;
        String placeId = "ChIJ7aVxnOTHwoARxKIntFtakKo";
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            info = bundle.getString("info");
        }





        RequestQueue queue = Volley.newRequestQueue(this);
//        String url="http://fanzhang-env.us-east-2.elasticbeanstalk.com/searchinfo?lat=" +lat + "&lon=" + lon+ "&keyword=" + keywordstr+ "&Category=" + text+ "&distance=" + distanceInt*1609.344;
        String url = "http://cs-server.usc.edu:39464/hw9.php?varplace="+info.split("&&&&")[1];
        Log.i("URL",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
//                            Log.i("btn",response);
//                Log.i("RES",response);

                eachdetailres=response;


                setContentView(R.layout.ffourinfo);

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                String info1 = info.split("&&&&")[0];
                Log.i("id",info.split("&&&&")[1]);

                toolbar.setTitle(info1);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);



                ImageView Bt=findViewById(R.id.share);
                Bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("tiwter","ok");
                        shareTwitter();
                    }
                });




                TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//                tabLayout.addTab(tabLayout.newTab().setText("INFO"));
//                tabLayout.addTab(tabLayout.newTab().setText("PHOTOS"));
//                tabLayout.addTab(tabLayout.newTab().setText("MAP"));
//                tabLayout.addTab(tabLayout.newTab().setText("REVIEWS"));

                tabLayout.addTab(tabLayout.newTab());
                tabLayout.addTab(tabLayout.newTab());
                tabLayout.addTab(tabLayout.newTab());
                tabLayout.addTab(tabLayout.newTab());
                tabLayout.getTabAt(0).setCustomView(getTabView("INFO",R.drawable.info));
                tabLayout.getTabAt(1).setCustomView(getTabView("PHOTOS",R.drawable.photo));
                tabLayout.getTabAt(2).setCustomView(getTabView("MAP",R.drawable.map));
                tabLayout.getTabAt(3).setCustomView(getTabView("REVIEWS",R.drawable.review));


                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


                final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                final fPagerAdapter adapter = new fPagerAdapter
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




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("btn","??????????");
                Log.i("btn","error");
            }
        });

        queue.add(stringRequest);










    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public View getTabView(String str, int image_id) {
        //change tab xml to view
        View view = LayoutInflater.from(this).inflate(R.layout.tab1, null);
        TextView txt_title = (TextView) view.findViewById(R.id.text);
        txt_title.setText(str);
        ImageView img_title = (ImageView) view.findViewById(R.id.imageView);
        img_title.setImageResource(image_id);
        return view;
    }

    public void shareTwitter() {



        String str=ffourinfoActivity.eachdetailres;
        String addstr="";
        String phonestr="";
        String pricestr="";
        String ratestr="";
        String pagestr="";
        String webstr="";


        try {
//            Log.i("D",str);

            JSONObject jsonObject = new JSONObject(str);

            addstr= jsonObject.getJSONObject("result").getString("formatted_address");
            phonestr =jsonObject.getJSONObject("result").getString("formatted_phone_number");
            pricestr =jsonObject.getJSONObject("result").getString("price_level");
            ratestr =jsonObject.getJSONObject("result").getString("rating");
            pagestr =jsonObject.getJSONObject("result").getString("url");
            webstr =jsonObject.getJSONObject("result").getString("website");


        }catch (Exception e){
            e.printStackTrace();
        }


        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://twitter.com/intent/tweet?text=Check out "+info.split("&&&&")[0]+" located at "+ addstr +". Website: "+webstr+"\n#TravelAndEntertainmentSearch"));
        startActivity(browserIntent);
    }
}


