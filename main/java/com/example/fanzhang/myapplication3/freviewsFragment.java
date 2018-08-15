package com.example.fanzhang.myapplication3;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class freviewsFragment extends Fragment {
    Spinner spinner;
    ArrayAdapter<String> adapter;
    List<String> list;
    Spinner spinner1;
    ArrayAdapter<String> adapter1;
    List<String> list1;
    public static String yelpid="";
    public static boolean isgoogle = true;
    public static reviewAdapter reviewAdapter;
    public static RecyclerView mRecyclerView;
    ArrayList<String> yelpreviewList;
    ArrayList<String> yelpreviewListfill;
    ArrayList<String> googlereviewList;
    ArrayList<String> googlereviewListfill;
    ArrayList<String> googlereviewListtime;
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.reviewsfragment, container, false);
        showreview();
        return view;
    }
    public void showreview(){
//        RecyclerView mRecyclerView;
        googlereviewList = new ArrayList<>();
        googlereviewListfill = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager;
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //get review data

        String str=ffourinfoActivity.eachdetailres;
        try {
//            Log.i("D",str);

            JSONObject jsonObject = new JSONObject(str);

            JSONArray jsonResult= jsonObject.getJSONObject("result").getJSONArray("reviews");
            for(int i = 0; i < jsonResult.length();i++){
                JSONObject json = (JSONObject) jsonResult.get(i);
                long stamp = Long.parseLong(json.getString("time"));
                googlereviewList.add(json.getString("author_name")+"&&&&"+json.getString("author_url")+"&&&&"+json.getString("profile_photo_url")+"&&&&"+json.getString("rating")+"&&&&"+json.getString("text")+"&&&&"+timeconverter(stamp));
                googlereviewListfill.add(json.getString("author_name")+"&&&&"+json.getString("author_url")+"&&&&"+json.getString("profile_photo_url")+"&&&&"+json.getString("rating")+"&&&&"+json.getString("text")+"&&&&"+timeconverter(stamp));


            }
            if(googlereviewList.size()==0){
                view.findViewById(R.id.emptyresult).setVisibility(View.VISIBLE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        reviewAdapter = new reviewAdapter(googlereviewList);
        mRecyclerView.setAdapter(reviewAdapter);


        spinner=(Spinner)view.findViewById(R.id.spinner);
        initDatas();
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int position, long id) {
                Toast.makeText(getActivity(),"CHOOSE："+list.get(position),Toast.LENGTH_SHORT).show();
                if(position==0){
                    isgoogle=true;
                    Log.i("CHOSE","0");
                    if(googlereviewList.size()==0){
                        view.findViewById(R.id.emptyresult).setVisibility(View.VISIBLE);
                    }else{
                        view.findViewById(R.id.emptyresult).setVisibility(View.GONE);
                        reviewAdapter = new reviewAdapter(googlereviewList);
                        mRecyclerView.setAdapter(reviewAdapter);
                    }

                }else{
                    Log.i("CHOSE","1");
                    isgoogle=false;
                    getyelp();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner1=(Spinner)view.findViewById(R.id.order);
        initDatas1();
        adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list1);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"CHOOSE："+list1.get(position),Toast.LENGTH_SHORT).show();
                if(position==0){
                    Log.i("CHOSE","D");
                    if(isgoogle){
                        reviewAdapter = new reviewAdapter(googlereviewList);
                        mRecyclerView.setAdapter(reviewAdapter);
                    }else{
                        reviewAdapter = new reviewAdapter(yelpreviewList);
                        mRecyclerView.setAdapter(reviewAdapter);
                    }
                }else if(position==1){
                    if(isgoogle){
                        Collections.sort(googlereviewListfill, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return Integer.parseInt(o2.split("&&&&")[3])-Integer.parseInt(o1.split("&&&&")[3]);
                            }
                        });
                        reviewAdapter = new reviewAdapter(googlereviewListfill);
                        mRecyclerView.setAdapter(reviewAdapter);
                    }else{
                        Collections.sort(yelpreviewListfill, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return Integer.parseInt(o2.split("&&&&")[3])-Integer.parseInt(o1.split("&&&&")[3]);
                            }
                        });
                        reviewAdapter = new reviewAdapter(yelpreviewListfill);
                        mRecyclerView.setAdapter(reviewAdapter);
                    }
                }
                else if(position==2){
                    if(isgoogle){
                        Collections.sort(googlereviewListfill, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return Integer.parseInt(o1.split("&&&&")[3])-Integer.parseInt(o2.split("&&&&")[3]);
                            }
                        });
                        reviewAdapter = new reviewAdapter(googlereviewListfill);
                        mRecyclerView.setAdapter(reviewAdapter);
                    }else{
                        Collections.sort(yelpreviewListfill, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return Integer.parseInt(o1.split("&&&&")[3])-Integer.parseInt(o2.split("&&&&")[3]);
                            }
                        });
                        reviewAdapter = new reviewAdapter(yelpreviewListfill);
                        mRecyclerView.setAdapter(reviewAdapter);
                    }
                }
                else if(position==3){
                    if (isgoogle){
                        Collections.sort(googlereviewListfill, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return o2.split("&&&&")[5].compareTo(o1.split("&&&&")[5]);
                            }
                        });
                        reviewAdapter = new reviewAdapter(googlereviewListfill);
                        mRecyclerView.setAdapter(reviewAdapter);
                    }else{
                        Collections.sort(yelpreviewListfill, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return o2.split("&&&&")[5].compareTo(o1.split("&&&&")[5]);
                            }
                        });
                        reviewAdapter = new reviewAdapter(yelpreviewListfill);
                        mRecyclerView.setAdapter(reviewAdapter);

                    }
                }
                else if(position==4)
                    if(isgoogle){
                        Collections.sort(googlereviewListfill, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return o1.split("&&&&")[5].compareTo(o2.split("&&&&")[5]);
                            }
                        });
                        reviewAdapter = new reviewAdapter(googlereviewListfill);
                        mRecyclerView.setAdapter(reviewAdapter);
                    }else{
                        Collections.sort(yelpreviewListfill, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return o1.split("&&&&")[5].compareTo(o2.split("&&&&")[5]);
                            }
                        });
                        reviewAdapter = new reviewAdapter(yelpreviewListfill);
                        mRecyclerView.setAdapter(reviewAdapter);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }




    private void initDatas() {
        list=new ArrayList<String>();
        list.add("Google reviews");
        list.add("Yelp reviews");
    }
    private void initDatas1() {
        list1=new ArrayList<String>();
        list1.add("Default Order");
        list1.add("Highest Rating");
        list1.add("Lowest Rating");
        list1.add("Most Recent");
        list1.add("Least Recent");

    }
    public String timeconverter(long stamp){
        long unix_seconds = stamp;
        Date date = new Date(unix_seconds*1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String java_date = jdf.format(date);
        return java_date;
    }
    public Timestamp toTime(String str){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(str);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return  timestamp;
        } catch(Exception e) { //this generic but you can control another types of exception
            return null;
        }

    }




    public void getyelp(){
        String str=ffourinfoActivity.eachdetailres;
        String name="";
        String city="";
        String state="";
        String address="";
        try {
            JSONObject jsonObject = new JSONObject(str);
            name= jsonObject.getJSONObject("result").getString("name");
            int len = jsonObject.getJSONObject("result").getString("formatted_address").split(",").length;
            city= jsonObject.getJSONObject("result").getString("formatted_address").split(",")[len-3].trim();
            state= jsonObject.getJSONObject("result").getString("formatted_address").split(",")[len-2].trim().split(" ")[0];
            address= jsonObject.getJSONObject("result").getString("formatted_address");

        }catch (Exception e){
            e.printStackTrace();
        }






        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        String url="http://fanzhang-env.us-east-2.elasticbeanstalk.com/searchinfo?lat=" +lat + "&lon=" + lon+ "&keyword=" + keywordstr+ "&Category=" + text+ "&distance=" + distanceInt*1609.344;

        String url= "http://fanzhang-env.us-east-2.elasticbeanstalk.com/yelpbest?name=" +name + "&city=" + city+ "&state=" + state+ "&address=" + address;
        Log.i("url",url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String res = response;
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonResult = jsonObject.getJSONArray("businesses");
                    if(jsonResult.length()==0){
                        Log.i("1","NO");
                        view.findViewById(R.id.emptyresult).setVisibility(View.VISIBLE);

                        return;
                    }else {
                        JSONObject json = (JSONObject) jsonResult.get(0);
                        yelpid = json.getString("id");
                        Log.i("get",yelpid);
                        getyelpreview();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

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





    public void getyelpreview(){


        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //String url= "http://fanzhang-env.us-east-2.elasticbeanstalk.com/yelpbest?name=" +name + "&city=" + city+ "&state=" + state+ "&address=" + address;
        String url = "http://fanzhang-env.us-east-2.elasticbeanstalk.com/yelpGetReview?id="+yelpid;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String res = response;
                try {
                    Log.i("GET","NEXT");
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonResult = jsonObject.getJSONArray("reviews");
                    if(jsonResult.length()==0){
                        yelpreviewList = new ArrayList<>();
                        reviewAdapter = new reviewAdapter(yelpreviewList);
                        mRecyclerView.setAdapter(reviewAdapter);
                        Log.i("NOOOOOOOO","NOOOOOO");
                        view.findViewById(R.id.emptyresult).setVisibility(View.VISIBLE);
                        return;
                    }else {
                        view.findViewById(R.id.emptyresult).setVisibility(View.GONE);
                        yelpreviewList = new ArrayList<>();
                        yelpreviewListfill = new ArrayList<>();
                        for(int i = 0; i < jsonResult.length();i++){
                            JSONObject json = (JSONObject) jsonResult.get(i);
//                            ArrayList<String> yelpreviewList = new ArrayList<>();
                            Log.i("NAME",json.getJSONObject("user").getString("name"));
                            yelpreviewList.add(json.getJSONObject("user").getString("name")+"&&&&"+json.getString("url")+"&&&&"+json.getJSONObject("user").getString("image_url")+"&&&&"+json.getString("rating")+"&&&&"+json.getString("text")+"&&&&"+json.getString("time_created"));





                            yelpreviewListfill.add(json.getJSONObject("user").getString("name")+"&&&&"+json.getString("url")+"&&&&"+json.getJSONObject("user").getString("image_url")+"&&&&"+json.getString("rating")+"&&&&"+json.getString("text")+"&&&&"+json.getString("time_created"));

                        }
                        Log.i("NAME",Integer.toString(yelpreviewList.size()));
                        reviewAdapter = new reviewAdapter(yelpreviewList);
                        mRecyclerView.setAdapter(reviewAdapter);


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

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
}
