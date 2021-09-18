package com.example.tsquared.Activities;

import android.os.Bundle;

import com.example.tsquared.Adapters.DiscoverViewPagerAdapter;
import com.example.tsquared.Adapters.SliderImagesViewPagerAdapter;
import com.example.tsquared.Models.SliderImage;
import com.example.tsquared.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class AnswerImagesActivity extends AppCompatActivity {
    private ArrayList<SliderImage> images;
    private ViewPager2 sliderImages;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_slider);
        setUpViews();
        setUpViewPager();
    }

    private void setUpViews(){
        sliderImages = (ViewPager2) findViewById(R.id.imageSlider);
    }

    private void setUpViewPager(){
        dummySliderImages();

        SliderImagesViewPagerAdapter sliderImagesViewPagerAdapter = new SliderImagesViewPagerAdapter(images, getApplicationContext());
        sliderImages.setAdapter(sliderImagesViewPagerAdapter);
    }

    private void dummySliderImages(){
        images = new ArrayList<>();
        SliderImage image;
        image = new SliderImage("https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg");
        images.add(image);
        image = new SliderImage("https://insights.som.yale.edu/sites/default/files/styles/rectangle_xs/public/insights/background/What%20the%20Plunge%20in%20the%20Stock%20Market%20Means%20for%20Individual%20Investors.jpg?h=d0d46503&itok=vl15_CSn");
        images.add(image);
        image = new SliderImage("https://www.reuters.com/resizer/vsRFGKTi9vWULwxZlnZkhbTmaLs=/1200x628/smart/filters:quality(80)/cloudfront-us-east-2.images.arcpublishing.com/reuters/HKYMFSORGZJRHMWZ6YYOZO4MTY.jpg");
        images.add(image);
        image = new SliderImage("https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg");
        images.add(image);
        image = new SliderImage("https://insights.som.yale.edu/sites/default/files/styles/rectangle_xs/public/insights/background/What%20the%20Plunge%20in%20the%20Stock%20Market%20Means%20for%20Individual%20Investors.jpg?h=d0d46503&itok=vl15_CSn");
        images.add(image);
        image = new SliderImage("https://www.reuters.com/resizer/vsRFGKTi9vWULwxZlnZkhbTmaLs=/1200x628/smart/filters:quality(80)/cloudfront-us-east-2.images.arcpublishing.com/reuters/HKYMFSORGZJRHMWZ6YYOZO4MTY.jpg");
    }
}
