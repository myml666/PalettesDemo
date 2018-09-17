package com.itfitness.palettesdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tablayout;
    private ViewPager vp;
    private int imgs[]={R.mipmap.bg1,R.mipmap.bg2,R.mipmap.bg3,R.mipmap.bg4};
    private ArrayList<Bitmap> bitmaps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDatas();
    }

    private void initDatas() {
        bitmaps=new ArrayList<>();
        for(int x=0;x<imgs.length;x++){
            bitmaps.add(BitmapFactory.decodeResource(getResources(),imgs[x]));
        }
        vp.setAdapter(new MyVpAdapter());
        tablayout.setupWithViewPager(vp);
        setPaletteColor(0);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        vp = (ViewPager) findViewById(R.id.vp);
        setSupportActionBar(toolbar);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setPaletteColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void setPaletteColor(int position) {
        Bitmap bitmap = bitmaps.get(position);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
//                int darkMutedColor = palette.getDarkMutedColor(Color.BLUE);
//                int darkVibrantColor = palette.getDarkVibrantColor(Color.BLUE);
//                int lightVibrantColor = palette.getLightVibrantColor(Color.BLUE);
//                int lightMutedColor = palette.getLightMutedColor(Color.BLUE);
//                int vibrantColor = palette.getVibrantColor(Color.BLUE);
//                int mutedColor = palette.getMutedColor(Color.BLUE);
                Palette.Swatch vibrant = palette.getVibrantSwatch();//获取颜色样本。在这里做了非空判断，如果获取的颜色样本为空就从所有的样本中获取一个样本。
                if (vibrant == null) {
                    for (Palette.Swatch swatch : palette.getSwatches()) {
                        vibrant = swatch;
                        break;
                    }
                }
                // 这样获取的颜色可以进行改变。
                int rgb = vibrant.getRgb();//从样本中获取颜色的RGB值。获取到RGB值之后可以直接给其他控件使用这个值，或者稍微调整这个值的颜色再使用。
                tablayout.setBackgroundColor(rgb);
                toolbar.setBackgroundColor(rgb);
                if (Build.VERSION.SDK_INT > 21) {
                    Window window = getWindow();
                    //状态栏改变颜色。
                    int color = changeColor(rgb);
                    window.setStatusBarColor(color);
                }

            }
        });
    }
    /**
     * @method  changeColor
     * @description 修改状态栏颜色
     * @date: 2018/9/17 11:33
     * @author: LML
     * @param rgb 颜色值参数
     * @return int
     */
    private int changeColor(int rgb) {
        int red = rgb >> 16 & 0xFF;
        int green = rgb >> 8 & 0xFF;
        int blue = rgb & 0xFF;
        red = (int) Math.floor(red * (1 - 0.2));
        green = (int) Math.floor(green * (1 - 0.2));
        blue = (int) Math.floor(blue * (1 - 0.2));
        return Color.rgb(red, green, blue);
    }
    private class MyVpAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return bitmaps.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View inflate = View.inflate(MainActivity.this, R.layout.vp_item_image, null);
            ImageView imageView = inflate.findViewById(R.id.vp_item_image_img);
            imageView.setImageBitmap(bitmaps.get(position));
            container.addView(inflate);
            return inflate;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "第"+position+"个";
        }
    }
}
