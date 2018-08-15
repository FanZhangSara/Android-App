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

import java.util.ArrayList;

public class TabFragment2 extends Fragment{
    public static ArrayList<String> favoArrayList = new ArrayList<>();
    public static detailInfoAdapter mAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab_fragment_2, container, false);
        setdata(view);
        return view;
    }
    private void setdata(View view){

        RecyclerView mRecyclerView;

        RecyclerView.LayoutManager mLayoutManager;


        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Log.i("btn",TabFragment1.responseDetail1[0]);
        mAdapter = new detailInfoAdapter(favoArrayList);



        mAdapter.setOnItemClickListener(new detailInfoAdapter.OnItemClickListener() {

            @Override
            public void onClick(int position) {
//                Toast.makeText("onClick事件您点击了第"+position+"个Item").show();
                Log.i("click",favoArrayList.get(position).split("&&&&")[0]);
                Log.i("click",favoArrayList.get(position).split("&&&&")[3]);
                String info = favoArrayList.get(position).split("&&&&")[0]+"&&&&"+favoArrayList.get(position).split("&&&&")[3];
//                responseDetail1.get(position);
                Intent intent = new Intent(getActivity(),ffourinfoActivity.class);
                intent.putExtra("info", info);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }
}
