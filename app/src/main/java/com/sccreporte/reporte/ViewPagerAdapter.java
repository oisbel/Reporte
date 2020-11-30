package com.sccreporte.reporte;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images ;
    private String[] titles ;
    private String[] details ;

    public ViewPagerAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_page, null);
        ImageView imageView = view.findViewById(R.id.ivImage);
        imageView.setImageResource(images[position]);
        TextView title = view.findViewById(R.id.tvTitle);
        title.setText(titles[position]);
        TextView detail = view.findViewById(R.id.tvdetails);
        detail.setText(details[position]);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    public void FillData(Integer[] images, String[] titles, String[] details){
        this.images = images;
        this.titles = titles;
        this.details = details;
    }
}
