package com.app.azazte.azazte;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Event.MessageEvent;
import com.app.azazte.azazte.Utils.Api.ApiExecutor;
import com.app.azazte.azazte.Utils.MixPanelUtils;
import com.app.azazte.azazte.Utils.NewscardFragment;
import com.app.azazte.azazte.animation.DepthTransform;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import xdroid.toaster.Toaster;



public class NewUI extends AppCompatActivity implements NewscardFragment.OnFragmentInteractionListener {

    public static Integer categoryChosen;
    public static String categoryChosenString;
    public static NewUI instance;
    public RelativeLayout topBar;
    public RelativeLayout twilightFilter;
    public ImageView twilight;

    Animation fadeIn;
    Animation fadeOut;
    DrawerLayout Drawer;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private BottomSheetBehavior categoriesSheet;
    private BottomSheetBehavior settingSheet;
    private int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault("fonts/HelveticaNeue.tff");
        instance = this;
        String categoryName;
        setContentView(R.layout.activity_new_ui);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadein);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadeout);
        RelativeLayout categoriesLayout = (RelativeLayout) findViewById(R.id.bottom_sheet);
        RelativeLayout settingsLayout = (RelativeLayout) findViewById(R.id.settings_sheet);
        topBar = (RelativeLayout) findViewById(R.id.topBar);
        twilightFilter = (RelativeLayout) findViewById(R.id.nightUI);
        setListeners();
        categoriesSheet = BottomSheetBehavior.from(categoriesLayout);
        settingSheet = BottomSheetBehavior.from(settingsLayout);

        int category = getIntent().getIntExtra("category", 0);
        int newsPostion = getNewsPosition(this.getIntent());
        TextView categoriesText = (TextView) findViewById(R.id.categoriesTextMenu);
        if (category == 0) {
            categoryName = "All News";
        } else {
            categoryName = getIntent().getStringExtra("categoryChosenString");
        }
        categoriesText.setText(categoryName);
        MixPanelUtils.trackCategories(categoryName);
        setupViewPager(category);
        viewPager.setCurrentItem(newsPostion, true);
    }

    private int getNewsPosition(Intent intent) {
        String id = intent.getStringExtra("id");
        ArrayList<NewsCard> allNews = Connector.getInstance().getAllNews();
        int position = 0;
        for (NewsCard news : allNews) {
            if (news.id.equals(id)) {
                return position;
            }
            position++;
        }
        return 0;
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
        ImageView settings = (ImageView) findViewById(R.id.settings);
        ImageView categoriesButton = (ImageView) findViewById(R.id.categories);
        final ImageView topRefreshButton = (ImageView) findViewById(R.id.top_refresh);
        TextView categoriesText = (TextView) findViewById(R.id.categoriesTextMenu);


        //categories listeners


        allNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupViewPager(0);
                categoryChosen = 0;
                categoryChosenString = "All News";
                onRestart();
            }
        });

        economy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupViewPager(1);
                categoryChosen = 1;
                categoryChosenString = "Economy";
                onRestart();
            }
        });

        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupViewPager(2);
                categoryChosen = 2;
                categoryChosenString = "Business";
                onRestart();
            }
        });

        tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupViewPager(3);
                categoryChosen = 3;
                categoryChosenString = "Tax";
                onRestart();
            }
        });

        finace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupViewPager(4);
                categoryChosen = 4;
                categoryChosenString = "Finance";
                onRestart();
            }
        });

        law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupViewPager(5);
                categoryChosen = 5;
                categoryChosenString = "Law";
                onRestart();
            }
        });

        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupViewPager(6);
                categoryChosen = 6;
                categoryChosenString = "Global";
                onRestart();
            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setupViewPager(-1);
                categoryChosen = -1;
                categoryChosenString = "Bookmarks";
                onRestart();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriesSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                settingSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        categoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoriesSheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    settingSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    categoriesSheet.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    categoriesSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });


        //settings items

        ImageView shareApp = (ImageView) findViewById(R.id.privacy);
        final ImageView about = (ImageView) findViewById(R.id.about);
        ImageView notification = (ImageView) findViewById(R.id.notification);
        ImageView noImage = (ImageView) findViewById(R.id.noimage);
        twilight = (ImageView) findViewById(R.id.night);

        ImageView privacy = (ImageView) findViewById(R.id.shareApp);
        ImageView rate = (ImageView) findViewById(R.id.rate);
        ImageView mailUs = (ImageView) findViewById(R.id.mailus);
        ImageView write = (ImageView) findViewById(R.id.write);
        ImageView feedback = (ImageView) findViewById(R.id.feedback);


        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), AzazteWebView.class);
                intent.putExtra("url", getString(R.string.privacy));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AzazteWebView.class);
                intent.putExtra("url", getString(R.string.about_us));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });


        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rate = new Intent(Intent.ACTION_VIEW);
                rate.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(rate);
            }
        });


        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareAppIntent = new Intent();
                shareAppIntent.setAction(Intent.ACTION_SEND);
                shareAppIntent.putExtra(Intent.EXTRA_TEXT, "Need to manage your finances but the technicalities make you stress.?\n Finup : Decoding business, finance and technology in 30 second reads.\n" +
                        "Download app: https://goo.gl/BjupvI");
                shareAppIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareAppIntent, "Spread A Word"));
            }
        });


        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AzazteWebView.class);
                intent.putExtra("url", getString(R.string.write_us));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });


        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AzazteWebView.class);
                intent.putExtra("url", getString(R.string.feedback_url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });


        mailUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("plain/text");
                sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"notifications@azazte.com"});
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(sendIntent);


            }
        });


        noImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        twilight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twilightMode();
            }
        });


        categoriesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                categoriesSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });


        topRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 0) {
                    refresh();
                } else {
                    viewPager.setCurrentItem(0, true);
                }
            }
        });
    }

    public  void twilightMode(){
        if(twilightFilter.getVisibility()==View.INVISIBLE) {
            twilightFilter.setVisibility(View.VISIBLE);
            twilight.setImageResource(R.drawable.lighton);

        }
        else {
            twilightFilter.setVisibility(View.INVISIBLE);
            twilight.setImageResource(R.drawable.lightoff);
        }
    }


    private void refresh() {
        Toaster.toast("Hold on! refreshing");
        ApiExecutor.getInstance().getNews(null, null);
    }


    public void setupViewPager(Integer category) {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setPageTransformer(true, new DepthTransform());
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        setupNewsCards(adapter, category);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setupNewsCards(ViewPagerAdapter adapter, Integer category) {
        if (category == -1) {
            ArrayList<NewsCard> allBookmarks = Connector.getInstance().getAllBookmarks();
            for (NewsCard newsCard : allBookmarks) {
                adapter.addFrag(new NewscardFragment(newsCard, this.getApplicationContext()));
            }
            return;
        }

        ArrayList<NewsCard> allNews = Connector.getInstance().getAllNews();
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
    }


    public void showTopBar() {
        if (topBar.getVisibility() == View.GONE) {
            topBar.setVisibility(View.VISIBLE);
            topBar.startAnimation(fadeIn);
            topBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    topBar.startAnimation(fadeOut);
                    topBar.setVisibility(View.GONE);
                    topBar.clearAnimation();
                }
            }, 5000);

        } else {
            topBar.startAnimation(fadeOut);
            topBar.clearAnimation();
            topBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        Intent intent = new Intent(this, NewUI.class);
        intent.putExtra("category", categoryChosen);
        intent.putExtra("categoryChosenString", categoryChosenString);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        // Unregister
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

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
    }


}
