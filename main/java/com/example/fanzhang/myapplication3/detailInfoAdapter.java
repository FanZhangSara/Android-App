package com.example.fanzhang.myapplication3;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.sip.SipSession;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;

public class detailInfoAdapter extends RecyclerView.Adapter<detailInfoAdapter.ViewHolder> {
//    private String[] mDataset;
    private ArrayList<String> mDataset;
    private OnItemClickListener mOnItemClickListener;
    ImageView imgheart;



    public interface OnItemClickListener{
        void onClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       private TextView name;
       private TextView address;
       private ImageView image;
       private ImageView heart;

        //       private Context context;
       public ViewHolder(View itemView){
           super(itemView);
//           name=(TextView)itemView.findViewById(R.id.name);
//           address=(TextView)itemView.findViewById(R.id.address);
//           image = (ImageView)itemView.findViewById(R.id.imageView);
//           heart = (ImageView)itemView.findViewById(R.id.heart);
//           itemView.setOnClickListener(this);
       }
        @Override
        public void onClick(View view) {
//            Log.d("a", "onClick " + getPosition() + " " + view);
//            Intent intent = new Intent(this,onedetailinfolayout.class);
////            intent.putExtra("res", response);
//            startActivity(intent);

        }
        public void set(final String str){

            name=(TextView)itemView.findViewById(R.id.name);
            address=(TextView)itemView.findViewById(R.id.address);
            image = (ImageView)itemView.findViewById(R.id.imageView);
            heart = (ImageView)itemView.findViewById(R.id.heart);
            name.setText(str.split("&&&&")[0]);
            address.setText(str.split("&&&&")[2]);
            Picasso.get().load(str.split("&&&&")[1]).into(image);
            boolean exist = false;
            for(int i =0; i<TabFragment2.favoArrayList.size(); i++){
                if(TabFragment2.favoArrayList.get(i).split("&&&&")[3].equals(str.split("&&&&")[3])){
                    exist=true;
                    break;
                }
            }
            if(exist){
                heart.setTag(R.drawable.hear_fill_red);
                heart.setImageResource(R.drawable.hear_fill_red);
            }else{
                heart.setTag(R.drawable.heart_outline_black);
                heart.setImageResource(R.drawable.heart_outline_black);
            }

            //

        heart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("H","HEART");
                if((Integer)heart.getTag()==(R.drawable.heart_outline_black)){
                    Log.i("H","BLACK");
                    TabFragment2.favoArrayList.add(str);
//                    Log.i("H",str);
                    TabFragment2.mAdapter.notifyDataSetChanged();
                    Log.i("LEN",Integer.toString(TabFragment2.favoArrayList.size()));
                    heart.setImageResource(R.drawable.hear_fill_red);
                    heart.setTag(R.drawable.hear_fill_red);
                    //notifyItemChanged(position);
                }
                else{
                    Log.i("H","RED");
                    Log.i("len",Integer.toString(TabFragment2.favoArrayList.size()));
                    //TabFragment2.favoArrayList.remove(str);
                    for(int i =0; i<TabFragment2.favoArrayList.size(); i++){
                        if(TabFragment2.favoArrayList.get(i).split("&&&&")[3].equals(str.split("&&&&")[3]))
                            TabFragment2.favoArrayList.remove(i);
                            TabFragment2.mAdapter.notifyDataSetChanged();

                    }
                    heart.setTag(R.drawable.heart_outline_black);
                    heart.setImageResource(R.drawable.heart_outline_black);
                    Log.i("len",Integer.toString(TabFragment2.favoArrayList.size()));
                }
            }
        });


        }
       public TextView getName(){
           return name;
       }
       public TextView getAddress(){
           return address;
       }
       public ImageView getImage(){
            return image;
        }
        public ImageView getHeart(){
           return heart;
        }
    }
    public detailInfoAdapter(ArrayList<String> myDataset) {
//        Log.i("btn",myDataset.get(0));
        mDataset = myDataset;
    }
    @Override
    public detailInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
//        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
//
//        ViewHolder vh = new ViewHolder(v);
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.eachinfoline,null));
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.set(mDataset.get(position));
        ImageView imageView;
        ViewHolder myHolder = (ViewHolder) holder;


        String url = mDataset.get(position).split("&&&&")[1];

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });

        }
//
//        imgheart = (ImageView)myHolder.getHeart();
//        imgheart.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("H","HEART");
//                if((Integer)imgheart.getTag()==(R.drawable.heart_outline_black)){
//                    Log.i("H","BLACK");
//                    TabFragment2.favoArrayList.add(mDataset.get(position));
////                    imgheart.setTag(R.drawable.hear_fill_red);
//
//
////                    Resources resources = this.getResources();
////                    image.setImageDrawable(resources.getDrawable(R.drawable.myfirstimage));
////                    imgheart.setImageDrawable(ContextCompat.getDrawable(null, R.drawable.hear_fill_red));
//
//
//                    imgheart.setImageResource(R.drawable.hear_fill_red);
//                    notifyItemChanged(position);
//                }
//                else{
//                    Log.i("H","RED");
//                    Log.i("len",Integer.toString(TabFragment2.favoArrayList.size()));
//                    TabFragment2.favoArrayList.remove(mDataset.get(position));
//                    imgheart.setTag(R.drawable.heart_outline_black);
//                    imgheart.setImageResource(R.drawable.heart_outline_black);
//                    Log.i("len",Integer.toString(TabFragment2.favoArrayList.size()));
//                }
//            }
//        });


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
