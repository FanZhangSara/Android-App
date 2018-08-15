package com.example.fanzhang.myapplication3;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class fmapFragment extends Fragment implements OnMapReadyCallback {
    Spinner spinner;
    ArrayAdapter<String> adapter1;
    List<String> list1;
    private GoogleMap mMap;
    String lat;
    String lng;
    String latinput;
    String lnginput;
    String str;
    String location;
    EditText editText1;
    String mode;
    PolylineOptions polylineOptions;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mapfragment, container, false);

        spinner=(Spinner)view.findViewById(R.id.spinner);
        initDatas1();
        adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list1);
        spinner.setAdapter(adapter1);
        showmap(view);
        drawmap(view);

//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        MapView mapFragment = (MapView) view.findViewById(R.id.map);
        mapFragment.onCreate(savedInstanceState);
        mapFragment.onResume();
        mapFragment.getMapAsync(this);





        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"CHOOSE："+list1.get(position),Toast.LENGTH_SHORT).show();
                if(position==0){
                    mode=TransportMode.DRIVING;
//                    drawlinebetween();
                }else if(position==1){
                    mode=TransportMode.BICYCLING;
                    drawlinebetween();
                }
                else if(position==2){
                    mode = TransportMode.TRANSIT;
                    drawlinebetween();
                }
                else if(position==3){
                    mode = TransportMode.WALKING;
                    drawlinebetween();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
    }


    public void showmap(View view){

        String str=ffourinfoActivity.eachdetailres;
        try {
//            Log.i("D",str);

            JSONObject jsonObject = new JSONObject(str);

            JSONObject jsonResult= jsonObject.getJSONObject("result");
            JSONObject jsonResult1= jsonResult.getJSONObject("geometry");
            JSONObject jsonResult2= jsonResult1.getJSONObject("location");
            lat = jsonResult2.getString("lat");
            lng = jsonResult2.getString("lng");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void drawmap(View view){
        editText1 =(EditText)view.findViewById(R.id.editText);
        location=editText1.getText().toString();
        Log.i("LOC","111111111111");
        ((EditText)editText1).setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus){
                            Log.i("fish","finish");
                            location=editText1.getText().toString();
                            Log.i("LOC",location);

                            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
//            String url ="http://www.google.com";
                            //String url="http://fanzhang-env.us-east-2.elasticbeanstalk.com/searchinfo?lat=" +lat + "&lon=" + lon+ "&keyword=" + keywordstr+ "&Category=" + text+ "&distance=" + distanceInt*1609.344;
                            String url= "http://fanzhang-env.us-east-2.elasticbeanstalk.com/getlatlon?location=" +location;
                            Log.i("LOC",url);

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    str = response;

                                    try {

                                        JSONObject jsonObject = new JSONObject(str);
                                        JSONArray jsonResult = jsonObject.getJSONArray("results");
                                        JSONObject jsonObject1 = (JSONObject) jsonResult.get(0);
                                        JSONObject jsonObject2 = jsonObject1.getJSONObject("geometry");
                                        JSONObject jsonObject3 = jsonObject2.getJSONObject("location");
                                        latinput = jsonObject3.getString("lat");
                                        lnginput = jsonObject3.getString("lng");
                                        Log.i("WOW",latinput);
                                        Log.i("WOW",lnginput);
                                        drawlinebetween();

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }}, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("btn","lo");
                                    Log.i("btn","error");
                                }
                            });

                            queue.add(stringRequest);


                        }

                    }
                }
        );





    }
    public void drawlinebetween(){
        mMap.clear();
        Log.i("DEAW","MAP");
        String serverKey = "AIzaSyCUI0X4M70Bx0T7oBalSlc4jWudqBQCN_c";
         final LatLng origin = new LatLng(Double.parseDouble(latinput), Double.parseDouble(lnginput));
        final LatLng destination = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

        LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        mMap.addMarker(new MarkerOptions().position(sydney));
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));

        LatLng sydney1 = new LatLng(Double.parseDouble(latinput), Double.parseDouble(lnginput));
        mMap.addMarker(new MarkerOptions().position(sydney1));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney1,15));

        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(mode)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        Route route = direction.getRouteList().get(0);
                        Leg leg = route.getLegList().get(0);
                        ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                        polylineOptions = DirectionConverter.createPolyline(getContext(), directionPositionList, 5, Color.BLUE);
                        LatLngBounds bounds=LatLngBounds.builder().include(origin).include(destination).build();
                        mMap.addPolyline(polylineOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,10));
                        Log.i("SU","SU");
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                    }
                });
    }
    private void initDatas1() {
        list1=new ArrayList<String>();
        list1.add("Driving");
        list1.add("Bicycling");
        list1.add("Transit");
        list1.add("Walking");

    }
}
