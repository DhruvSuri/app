package com.app.azazte.azazte;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.animation.DepthTransform;
import com.app.azazte.azazte.animation.ZoomTransformer;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Utils.NewscardFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewUI extends AppCompatActivity implements NewscardFragment.OnFragmentInteractionListener {

    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private BottomSheetBehavior categoriesSheet;
    public RelativeLayout topBar;
    Animation fadeIn;
    Animation fadeOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_new_ui);

        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadein);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadeout);

        RelativeLayout bottomSheet = (RelativeLayout) findViewById(R.id.bottom_sheet);
        topBar = (RelativeLayout) findViewById(R.id.topBar);

        setListeners();
        categoriesSheet = BottomSheetBehavior.from(bottomSheet);

        ImageView categoriesButton = (ImageView) findViewById(R.id.categoriesButton);

        categoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriesSheet.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });


        setupViewPager(0);
    }

    private void setListeners() {
        ImageView allNews = (ImageView) findViewById(R.id.allnews);
        ImageView business = (ImageView) findViewById(R.id.business);
        ImageView tax = (ImageView) findViewById(R.id.tax);
        ImageView law = (ImageView) findViewById(R.id.law);
        ImageView finace = (ImageView) findViewById(R.id.finance);
        ImageView economy = (ImageView) findViewById(R.id.economy);
        ImageView global = (ImageView) findViewById(R.id.global);
        ImageView bookmark = (ImageView) findViewById(R.id.bookmark);

        allNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupViewPager(0);
            }
        });

        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupViewPager(1);
            }
        });

        tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupViewPager(2);
            }
        });

        law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupViewPager(3);
            }
        });
    }


    private void setupViewPager(int category) {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setPageTransformer(true, new DepthTransform());
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        setupNewsCards(adapter, category);
        viewPager.setAdapter(adapter);
    }

    private void setupNewsCards(ViewPagerAdapter adapter, int category) {
        ArrayList<NewsCard> allNews = Connector.getInstance().getAllNews();
        //Assuming we got the right category
        Collections.reverse(allNews);
        for (NewsCard newsCard : allNews) {
            int cardCategory;
            try {
                cardCategory = Integer.valueOf(newsCard.category);
            } catch (Exception e) {
                cardCategory = 0;
            }
            if (category == cardCategory || category == 0) {
                adapter.addFrag(new NewscardFragment(newsCard, this.getApplicationContext()));
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void showTopBar() {
        if (topBar.getVisibility() == View.INVISIBLE) {
            topBar.setVisibility(View.VISIBLE);
            topBar.startAnimation(fadeIn);
            topBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    topBar.startAnimation(fadeOut);
                    topBar.setVisibility(View.INVISIBLE);
                }
            },10000);

        } else {
         topBar.startAnimation(fadeOut);
            topBar.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<NewsCard> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
            //mFragmentTitleList.add(newsCard);
        }
  //     @Override
        // public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
    }


}
