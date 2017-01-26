package com.app.azazte.azazte;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

public class NewHomeScreen extends AppCompatActivity {

    private MaterialViewPager mViewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home_screen);

        setTitle("");

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

       toolbar = mViewPager.getToolbar();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 8) {
                    case 0:
                        return RecyclerViewFragment.newInstance();
                    case 1:
                        return RecyclerViewFragment.newInstance();
                    case 2:
                        return RecyclerViewFragment.newInstance();
                    case 3:
                        return RecyclerViewFragment.newInstance();
                    case 4:
                        return RecyclerViewFragment.newInstance();
                    case 5:
                        return RecyclerViewFragment.newInstance();
                    case 6:
                        return RecyclerViewFragment.newInstance();
                    case 7:
                        return RecyclerViewFragment.newInstance();
                    default:
                        return RecyclerViewFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 8;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 8) {
                    case 0:
                        return "All News";
                    case 1:
                        return "Business";
                    case 2:
                        return "Current Affairs";
                    case 3:
                        return "Politics";
                    case 4:
                        return "World";
                    case 5:
                        return "Entertainment";
                    case 6:
                        return "Startup";
                    case 7:
                        return "Sports";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.thehindu.com/todays-paper/tp-national/article17015739.ece/alternates/FREE_660/09-krishnadas-S+GGQ13RIR9.3.jpg.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "https://i0.wp.com/quickreadbuzz.com/wp-content/uploads/2016/08/iStock_460331609.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.thehindu.com/todays-paper/tp-national/article17015739.ece/alternates/FREE_660/09-krishnadas-S+GGQ13RIR9.3.jpg.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
