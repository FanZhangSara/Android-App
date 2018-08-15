package com.example.fanzhang.myapplication3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class resultActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public static detailInfoAdapter[] mAdapter = new detailInfoAdapter[3];
    private RecyclerView.LayoutManager mLayoutManager;
    String []responseDetail = new String[20];
    ArrayList<String> responseDetail1 = new ArrayList<>();
    private int pagenum = 0;
    String pageToken="";
    String res;
    public ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultpage);



        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            res = bundle.getString("res");
            try{
                JSONObject jsonObject = new JSONObject(res);
                JSONArray jsonResult = jsonObject.getJSONArray("results");
                if(jsonResult.length()>0){
                    this.findViewById(R.id.emptyresult).setVisibility(View.GONE);
                }
                parse(res);
            }catch (Exception e){
                e.printStackTrace();
            }


            Log.i("LEN",String.valueOf(responseDetail1.size()));
            Log.i("LEN",res);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Search results");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Log.i("btn",TabFragment1.responseDetail1[0]);
        mAdapter[0] = new detailInfoAdapter(responseDetail1);


        mAdapter[0].setOnItemClickListener(new detailInfoAdapter.OnItemClickListener() {

            @Override
            public void onClick(int position) {
//                Toast.makeText("onClick事件您点击了第"+position+"个Item").show();
                Log.i("click",responseDetail1.get(position).split("&&&&")[0]);
                Log.i("click",responseDetail1.get(position).split("&&&&")[3]);
                String info = responseDetail1.get(position).split("&&&&")[0]+"&&&&"+responseDetail1.get(position).split("&&&&")[3];
//                responseDetail1.get(position);
                Intent intent = new Intent(resultActivity.this,ffourinfoActivity.class);
                intent.putExtra("info", info);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter[0]);


        Button next =(Button)findViewById(R.id.next);
        Button pre =(Button)findViewById(R.id.pre);
        if(pagenum==2){
            next.setEnabled(false);
        }
        if(pagenum==0){
            pre.setEnabled(false);
        }
        if(pageToken==null)
            next.setEnabled(false);
        Log.i("page",Integer.toString(pagenum));
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pagenum==2){
                    Button nextb =(Button)findViewById(R.id.next);
                    nextb.setEnabled(false);
                }

                else if(mAdapter[pagenum+1] != null){
                    mRecyclerView.setAdapter(mAdapter[pagenum+1]);
                    pagenum++;
                    Button pre =(Button)findViewById(R.id.pre);
                    pre.setEnabled(true);
                    Log.i("num","1");
                    if(pagenum==2){
                        Button nextb =(Button)findViewById(R.id.next);
                        nextb.setEnabled(false);
                    }
                }
                else if(pageToken!=null){
                    Log.i("num","2");
                    Button pre =(Button)findViewById(R.id.pre);
                    pre.setEnabled(true);
                    progress = new ProgressDialog(v.getContext());
                    progress.setCancelable(true);
                    progress.setMessage("Fetching next page");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setProgress(0);
                    progress.setMax(90);
                    progress.show();
                    requstnext(pageToken);
                    if(pagenum==2){
                        Button nextb =(Button)findViewById(R.id.next);
                        nextb.setEnabled(false);
                    }

                }
                else{
                    Button buttonnext = (Button) v.findViewById(R.id.next);
                    buttonnext.setEnabled(false);
                }



            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button next =(Button)findViewById(R.id.next);
                next.setEnabled(true);
                if(pagenum==0){
                    Button pre =(Button)findViewById(R.id.pre);
                    pre.setEnabled(false);
                }
                else{
                    pagenum--;
                    mRecyclerView.setAdapter(mAdapter[pagenum]);
                    if(pagenum==0){
                        Button pre =(Button)findViewById(R.id.pre);
                        pre.setEnabled(false);
                    }

                }
                Log.i("page",Integer.toString(pagenum));
            }
        });




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

    public void parse(String res){
        responseDetail1=new ArrayList<>();
        try {

            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonResult = jsonObject.getJSONArray("results");

                if(jsonObject.has("next_page_token")){
                    pageToken = jsonObject.getString("next_page_token");
                }else{
                    pageToken = null;
                }
            Log.v("ttee", "len:" + jsonResult.length());
            for(int i = 0; i < jsonResult.length();i++){
                JSONObject json = (JSONObject) jsonResult.get(i);
                responseDetail1.add(json.getString("name")+"&&&&"+json.getString("icon")+"&&&&"+json.getString("vicinity")+"&&&&"+json.getString("place_id"));
                Log.i("n",json.getString("name"));
            }


        }catch (Exception e){
            e.printStackTrace();
        }


    }
    public void requstnext(String nexttoken){

        String nextnode = nexttoken;
        Log.i("REQU","NEXT");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url="http://fanzhang-env.us-east-2.elasticbeanstalk.com/nextinfo?pagetoken=" +nextnode;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
//                            Log.i("btn",response);
                res=response;
                parse(res);
                Log.i("next",res);
                pagenum++;
                Log.i("page",Integer.toString(pagenum));
                if(pagenum==2){
                    Button nextb =(Button)findViewById(R.id.next);
                    nextb.setEnabled(false);
                }
                progress.dismiss();
                mAdapter[pagenum]= new detailInfoAdapter(responseDetail1);
//                Log.i("num",)

                mAdapter[pagenum].setOnItemClickListener(new detailInfoAdapter.OnItemClickListener() {

                    @Override
                    public void onClick(int position) {
//                Toast.makeText("onClick事件您点击了第"+position+"个Item").show();
                        Log.i("click",responseDetail1.get(position).split("&&&&")[0]);
                        Log.i("click",responseDetail1.get(position).split("&&&&")[3]);
                        String info = responseDetail1.get(position).split("&&&&")[0]+"&&&&"+responseDetail1.get(position).split("&&&&")[3];
//                responseDetail1.get(position);
                        Intent intent = new Intent(resultActivity.this,ffourinfoActivity.class);
                        intent.putExtra("info", info);
                        startActivity(intent);
                    }
                });
                mRecyclerView.setAdapter(mAdapter[pagenum]);

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
