package com.example.fanzhang.myapplication3;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.places.GeoDataClient;

public class fphotoFragment extends Fragment {
    public GeoDataClient mGeoDataClient;
//    private ImageView imageView;
//    private ImageView imageView1;
    private ViewGroup group;
    private View nview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.photofragment, container, false);
        nview=view;
        mGeoDataClient = Places.getGeoDataClient(getContext());
        group = view.findViewById(R.id.viewGroup);
//        imageView=view.findViewById(R.id.photto);
//        imageView1=view.findViewById(R.id.photto2);
        getPhotos(view);
        return view;
    }
    private void getPhotos(View view){
        final String placeId = ffourinfoActivity.info.split("&&&&")[1];
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
//                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
//                PlacePhotoMetadata photoMetadata1 = photoMetadataBuffer.get(1);
                if(photoMetadataBuffer.getCount()==0){
                    nview.findViewById(R.id.emptyresult).setVisibility(View.VISIBLE);
                }

                int i = 0;
                while (photoMetadataBuffer.getCount() > i){
                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(i);
                    if(photoMetadata == null){
                        Log.i("SDSD","ENTER");
                        //nview.findViewById(R.id.emptyresult).setVisibility(View.VISIBLE);
                        return;
                    }
                    Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                    Log.i("photo","t");
                    photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                            ImageView imageView = new ImageView(getActivity());
                            //imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                           // new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)

                            int padding = 10;
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( 1200,800);
                            //设置左右边距

                            params.leftMargin = (int) UIUtil.dp2px(getContext(), padding);
                            params.rightMargin = (int) UIUtil.dp2px(getContext(), padding);
                            params.bottomMargin = (int) UIUtil.dp2px(getContext(), padding);
                            params.topMargin = (int) UIUtil.dp2px(getContext(), padding);
                            imageView.setLayoutParams(params);


                            PlacePhotoResponse photo = task.getResult();
                            Bitmap bitmap = photo.getBitmap();
                            imageView.setImageBitmap(bitmap);
                            group.addView(imageView);
                        }
                    });
                    i++;
                }



//                Task<PlacePhotoResponse> photoResponse1 = mGeoDataClient.getPhoto(photoMetadata1);
//                Log.i("photo","t1");
//                photoResponse1.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
//                    @Override
//                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
//                        PlacePhotoResponse photo = task.getResult();
//                        Bitmap bitmap = photo.getBitmap();
//                        imageView1.setImageBitmap(bitmap);
//                    }
//                });
            }
        });

    }
}
