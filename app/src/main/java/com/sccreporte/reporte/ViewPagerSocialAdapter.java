package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.sccreporte.reporte.data.SocialMediaData;

public class ViewPagerSocialAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private SocialMediaData[] socialMediaData;
    String facebook_page_id;

    public ViewPagerSocialAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return socialMediaData.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.social_item_page, null);

        TextView oneTV = view.findViewById(R.id.oneTV);
        oneTV.setText(socialMediaData[position].OneSocialName);
        TextView twoTV = view.findViewById(R.id.twoTV);
        twoTV.setText(socialMediaData[position].TwoSocialName);
        TextView threeTV = view.findViewById(R.id.threeTV);
        threeTV.setText(socialMediaData[position].ThreeSocialName);


        ImageButton oneIB = view.findViewById(R.id.oneIB);
        oneIB.setImageResource(socialMediaData[position].OneSocialImage);
        oneIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSCCWebPage(socialMediaData[position].OneSocialLink);
            }
        });
        ImageButton twoIB = view.findViewById(R.id.twoIB);
        twoIB.setImageResource(socialMediaData[position].TwoSocialImage);
        twoIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSCCWebPage(socialMediaData[position].TwoSocialLink);
            }
        });
        ImageButton threeIB = view.findViewById(R.id.threeIB);
        threeIB.setImageResource(socialMediaData[position].ThreeSocialImage);

        if(socialMediaData[position].ThreeSocialName.equals(context.getString(R.string.facebook_label)))
        {
            threeIB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSCCWebPage(getFacebookPageURL(facebook_page_id,
                            socialMediaData[position].ThreeSocialLink));
                }
            });
        }else{
            threeIB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSCCWebPage(socialMediaData[position].ThreeSocialLink);
                }
            });
        }

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

    public void FillData(SocialMediaData[] socialMediaData, String facebook_page_id){
        this.socialMediaData = socialMediaData;
        this.facebook_page_id = facebook_page_id;
    }

    private void openSCCWebPage(String url){
        PackageManager packageManager = context.getPackageManager();
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if(intent.resolveActivity(packageManager) != null){
            context.startActivity(intent);
        }
    }

    /**
     * Devuelve el valor url en dependencia de si facebook app esta
     * instalado o no (para que se abra el browser o la app)
     * @return
     */
    public String getFacebookPageURL(String facebook_page_id, String facebook_url){
        if(appInstalled("com.facebook.katana")){
            return "fb://page/" + facebook_page_id;
        }else {
            return facebook_url;
        }
    }

    /**
     * To know if facebook app is installed
     * @param uri
     * @return
     */
    private boolean appInstalled(String uri)
    {
        PackageManager packageManager = context.getPackageManager();
        try
        {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            boolean activated =  packageManager.getApplicationInfo(uri, 0).enabled;
            return activated;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
