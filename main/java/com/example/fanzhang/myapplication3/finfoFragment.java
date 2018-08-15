package com.example.fanzhang.myapplication3;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class finfoFragment extends Fragment{
    public static String pagestr="";
    public static String webstr="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String str=ffourinfoActivity.eachdetailres;
        String addstr="";
        String phonestr="";
        String pricestr="";
        String ratestr="";


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


//        Log.i("wow??",str);
        View view= inflater.inflate(R.layout.infofragment, container, false);
        TextView address = (TextView) view.findViewById(R.id.addressinput);
        address.setText(addstr);

        TextView phone = (TextView) view.findViewById(R.id.phoneinput);
        Linkify.addLinks(phone, Linkify.PHONE_NUMBERS);
        phone.setText(phonestr);

        TextView price = (TextView) view.findViewById(R.id.priceinput);
//        price.setText(pricestr);
        if(pricestr.length()>0){
        int j =Integer.parseInt(pricestr);
        for(int i = 0; i<j;i++){
            price.setText("$");
        }}

        RatingBar rate = (RatingBar) view.findViewById(R.id.ratinginput);
//        rate.setText(ratestr);
        if(ratestr!=null&&!"".equals(ratestr)){
            rate.setRating(Float.valueOf(ratestr));
        }


        TextView page = (TextView) view.findViewById(R.id.pageinput);
        page.setText(pagestr);
        page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(pagestr);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

        TextView web = (TextView) view.findViewById(R.id.websiteinput);
        web.setText(webstr);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(webstr);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });



        return view;
    }


}
