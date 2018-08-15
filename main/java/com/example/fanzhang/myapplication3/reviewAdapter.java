package com.example.fanzhang.myapplication3;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.ViewHolder>{
    private ArrayList<String> mDataset;


    private reviewAdapter.OnItemClickListener mOnItemClickListener;
    ImageView imgheart;



    public interface OnItemClickListener{
        void onClick( int position);
    }
    public void setOnItemClickListener(reviewAdapter.OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

        }
        public void set(final String str){

            ImageView photo = (ImageView)itemView.findViewById(R.id.imageView);
            TextView name=(TextView)itemView.findViewById(R.id.name);
            //TextView star=(TextView)itemView.findViewById(R.id.star);
            TextView time = (TextView)itemView.findViewById(R.id.time);
            TextView text = (TextView)itemView.findViewById(R.id.text);


            RatingBar rate = (RatingBar)itemView.findViewById(R.id.ratinginput);

//            long stamp = Long.parseLong(str.split("&&&&")[5]);
//            timeconverter(stamp);
            Picasso.get().load(str.split("&&&&")[2]).into(photo);
            name.setText(str.split("&&&&")[0]);
            //star.setText(str.split("&&&&")[3]);
            rate.setRating(Float.valueOf(str.split("&&&&")[3]));
            time.setText(str.split("&&&&")[5]);
            text.setText(str.split("&&&&")[4]);


        }

        public String timeconverter(long stamp){
            long unix_seconds = stamp;
            Date date = new Date(unix_seconds*1000L);
            SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
//            jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
            String java_date = jdf.format(date);
            return java_date;
        }



    }
    public reviewAdapter(ArrayList<String> myDataset) {

        mDataset = myDataset;
    }
    @Override
    public reviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        return new reviewAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.eachreview,null));
    }
    @Override
    public void onBindViewHolder(reviewAdapter.ViewHolder holder, final int position) {
        holder.set(mDataset.get(position));

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });

        }

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
