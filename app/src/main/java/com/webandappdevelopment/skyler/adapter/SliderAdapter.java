package com.webandappdevelopment.skyler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.webandappdevelopment.skyler.R;
import com.webandappdevelopment.skyler.modelclass.SliderList;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    Context context;
    List<SliderList> listItem;
    LayoutInflater layoutInflater;
    public SliderAdapter(Context context, List<SliderList> listItem) {
        this.context = context;
        this.listItem = listItem;
//        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //arrays to store img
//    public int[] slider_images = {
//            R.drawable.slider1,
//            R.drawable.slider2,
//            R.drawable.slider3
//    };

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
        SliderList item = listItem.get(position);
        ImageView slide_imageView = view.findViewById(R.id.l_sliderRowLayout);
//        slide_imageView.setImageResource(item.getImg_url());
        Picasso.get().load(item.getImg_url())
                .placeholder(R.mipmap.ic_launcher)
                .into(slide_imageView);
        container.addView(view);
            return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
