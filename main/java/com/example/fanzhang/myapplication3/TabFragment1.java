package com.example.fanzhang.myapplication3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.fanzhang.myapplication3.PlaceAutocompleteAdapter;

public class TabFragment1  extends Fragment implements GoogleApiClient.OnConnectionFailedListener{
    public static String[] responseDetail;
    public ProgressDialog progress;

    private Button searchBt;
    private Button clearBt;
    double lat;
    double lon;
    String keywordstr;
    String distance;
    int distanceInt;
    String text;
    private GoogleApiClient mGoogleApiClient;
    private GeoDataClient mGeoData;
    private PlaceAutocompleteAdapter placeAutoCompleteAdapter;

    private Context mContext;

    //private autoCompleteAdapter PlaceAutoCompleteAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext()).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).enableAutoManage(getActivity(), this).build();
        placeAutoCompleteAdapter = new PlaceAutocompleteAdapter(getContext(),mGoogleApiClient,LAT_LNG_BOUNDS,null);
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        findview(view);
        return view;
    }


    private void findview(View view) {
        searchBt = (Button) view.findViewById(R.id.search);
        MySearchBtn listener = new MySearchBtn();
        searchBt.setOnClickListener(listener);

        clearBt=(Button)view.findViewById(R.id.clear);
        clearBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText1 =(EditText)getView().findViewById(R.id.keyword);
                editText1.setText("");
                editText1.setHint("Enter keyword");
                EditText editText2 =(EditText)getView().findViewById(R.id.editText3);
                editText2.setText("");
                editText2.setHint("Enter distance(default 10 miles)");
                AutoCompleteTextView editText3 =(AutoCompleteTextView)getView().findViewById(R.id.location);
                editText3.setText("");
                editText3.setHint("Type in the Location");


                resultActivity.mAdapter = new detailInfoAdapter[3];
                TextView show1 = (TextView)getView().findViewById(R.id.validationMessage1);
                show1.setVisibility(v.GONE);
                TextView show2 = (TextView)getView().findViewById(R.id.validationMessage2);
                show2.setVisibility(v.GONE);

            }
        });

    }
    class MySearchBtn implements OnClickListener{
        public void onClick(View v){
            Log.i("btn","clicked");



//            progress = new ProgressDialog(v.getContext());
//            progress.setCancelable(true);
//            progress.setMessage("Fetching results");
//            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progress.setProgress(0);
//            progress.setMax(90);
//            progress.show();

            keywordstr="";
            distance = "";
            Spinner spinner;
            String cate = "";

            EditText editText1 =(EditText)getView().findViewById(R.id.keyword);
            keywordstr=editText1.getText().toString();
            Log.i("btn",keywordstr);

            spinner = (Spinner) getView().findViewById(R.id.spinner);
            text = spinner.getSelectedItem().toString();
            Log.i("btn",text);

            EditText editText2 =(EditText)getView().findViewById(R.id.editText3);
            distance=editText2.getText().toString();
            if(distance.equals("")){
                distance="10";
            }
            distanceInt = Integer.parseInt(distance);
            Log.i("btn",String.valueOf(distanceInt));


            RadioGroup radiogroup = (RadioGroup)getView().findViewById(R.id.from);
            RadioButton radioButton = (RadioButton)getView().findViewById(radiogroup.getCheckedRadioButtonId());
            String location = radioButton.getText().toString();
            Log.i("btn",location);
            lat = MainActivity.lat;
            lon = MainActivity.lon;
            boolean num = false;
            if(location.equals("Current location")){
                num = true;
            }


            AutoCompleteTextView editTextn =(AutoCompleteTextView)getView().findViewById(R.id.location);
            editTextn.setAdapter(placeAutoCompleteAdapter);

            //validation
            if( (!isValidInput(editText1)) || ( (!isValidInput(editTextn))&& !num) ){
                if( (!isValidInput(editText1)) ){
                    Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                    TextView show = (TextView)getView().findViewById(R.id.validationMessage1);
                    show.setVisibility(v.VISIBLE);
                }
                if(( (!isValidInput(editTextn))&& !num)){
                    Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                    TextView show = (TextView)getView().findViewById(R.id.validationMessage2);
                    show.setVisibility(v.VISIBLE);
                }
                return;
            }else {
                TextView show1 = (TextView)getView().findViewById(R.id.validationMessage1);
                show1.setVisibility(v.GONE);
                TextView show2 = (TextView)getView().findViewById(R.id.validationMessage2);
                show2.setVisibility(v.GONE);
            }
            progress = new ProgressDialog(v.getContext());
            progress.setCancelable(true);
            progress.setMessage("Fetching results");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setProgress(0);
            progress.setMax(90);
            progress.show();


            if(location.equals("Current location")){
                Log.i("btn","0");
                 lat = MainActivity.lat;
                 lon = MainActivity.lon;
                Log.i("btn",String.valueOf(lat));
                Log.i("btn",String.valueOf(lon));
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
//            String url ="http://www.google.com";
                String url="http://fanzhang-env.us-east-2.elasticbeanstalk.com/searchinfo?lat=" +lat + "&lon=" + lon+ "&keyword=" + keywordstr+ "&Category=" + text+ "&distance=" + distanceInt*1609.344;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                            Log.i("btn",response);
                        Intent intent = new Intent(getActivity(),resultActivity.class);
                        intent.putExtra("res", response);
                        progress.dismiss();
                        startActivity(intent);
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
            else{
                Log.i("btn","1");
                AutoCompleteTextView editTextaddress =(AutoCompleteTextView)getView().findViewById(R.id.location);
                String address=editTextaddress.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
//            String url ="http://www.google.com";
                //String url="http://fanzhang-env.us-east-2.elasticbeanstalk.com/searchinfo?lat=" +lat + "&lon=" + lon+ "&keyword=" + keywordstr+ "&Category=" + text+ "&distance=" + distanceInt*1609.344;
                String url= "http://fanzhang-env.us-east-2.elasticbeanstalk.com/getlatlon?location=" +address;
                Log.i("LOC",url);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonResult = jsonObject.getJSONArray("results");
                            JSONObject jsonObject1 = (JSONObject) jsonResult.get(0);
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("geometry");
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("location");
                            lat = Double.parseDouble(jsonObject3.getString("lat"));
                            lon = Double.parseDouble(jsonObject3.getString("lng"));
                            Log.i("WOW",Double.toString(lat));
                            Log.i("WOW",Double.toString(lon));


                            RequestQueue queue1 = Volley.newRequestQueue(getActivity().getApplicationContext());
//            String url ="http://www.google.com";
                            String url1="http://fanzhang-env.us-east-2.elasticbeanstalk.com/searchinfo?lat=" +lat + "&lon=" + lon+ "&keyword=" + keywordstr+ "&Category=" + text+ "&distance=" + distanceInt*1609.344;

                            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
//                            Log.i("btn",response);
                                    Intent intent = new Intent(getActivity(),resultActivity.class);
                                    intent.putExtra("res", response);
                                    progress.dismiss();
                                    startActivity(intent);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("btn","??????????");
                                    Log.i("btn","error");
                                }
                            });



                            queue1.add(stringRequest1);


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





//                RequestQueue queue1 = Volley.newRequestQueue(getActivity().getApplicationContext());
////            String url ="http://www.google.com";
//                String url1="http://fanzhang-env.us-east-2.elasticbeanstalk.com/searchinfo?lat=" +lat + "&lon=" + lon+ "&keyword=" + keywordstr+ "&Category=" + text+ "&distance=" + distanceInt*1609.344;
//
//                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
////                            Log.i("btn",response);
//                        Intent intent = new Intent(getActivity(),resultActivity.class);
//                        intent.putExtra("res", response);
//                        startActivity(intent);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.i("btn","??????????");
//                        Log.i("btn","error");
//                    }
//                });
//
//
//
//                queue1.add(stringRequest1);




            }



//            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
////            String url ="http://www.google.com";
//            String url="http://fanzhang-env.us-east-2.elasticbeanstalk.com/searchinfo?lat=" +lat + "&lon=" + lon+ "&keyword=" + keywordstr+ "&Category=" + text+ "&distance=" + distanceInt*1609.344;
//
//                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            // Display the first 500 characters of the response string.
////                            Log.i("btn",response);
//                            Intent intent = new Intent(getActivity(),resultActivity.class);
//                            intent.putExtra("res", response);
//                            startActivity(intent);
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.i("btn","??????????");
//                    Log.i("btn","error");
//                }
//            });
//
//
//
//            queue.add(stringRequest);



//            Intent intent = new Intent(getActivity(),resultActivity.class);
//            startActivity(intent);
        }
//        public void parse(String res){
//            try {
//                JSONObject jsonObject = new JSONObject(res);
//                JSONArray jsonResult = jsonObject.getJSONArray("results");
////                if(jsonObject.has("next_page_token")){
////                    pageToken = jsonObject.getString("next_page_token");
////                }else{
////                    pageToken = null;
////                }
//                for(int i = 0; i < jsonResult.length();i++){
//                    JSONObject json = (JSONObject) jsonResult.get(i);
//                    responseDetail[i]+=json.getString("name");
//                    responseDetail[i]+="/";
//                    responseDetail[i]+=json.getString("icon");
//                    responseDetail[i]+="/";
//                    responseDetail[i]+=json.getString("vicinity");
//                    responseDetail[i]+="/";
//                    responseDetail[i]+=json.getString("place_id");
//                }
//
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private boolean isValidInput(EditText etText) {
        if(etText.getText().toString().trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }




    }






