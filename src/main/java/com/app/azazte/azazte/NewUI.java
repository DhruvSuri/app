package com.app.azazte.azazte;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Event.MessageEvent;
import com.app.azazte.azazte.GCM.GCMIntentService;
import com.app.azazte.azazte.Utils.MixPanelUtils;
import com.app.azazte.azazte.Utils.NewscardFragment;
import com.app.azazte.azazte.animation.DepthTransform;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class NewUI extends AppCompatActivity implements NewscardFragment.OnFragmentInteractionListener {

    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private BottomSheetBehavior categoriesSheet;
    private BottomSheetBehavior settingSheet;

    public RelativeLayout topBar;
    Animation fadeIn;
    Animation fadeOut;
    public static Integer categoryChosen;
    public static String categoryChosenString;
    DrawerLayout Drawer;
    public static NewUI instance;

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
        //Toaster.toast("got the following event" + event.message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        String categoryName;
        setContentView(R.layout.activity_new_ui);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadein);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadeout);
        RelativeLayout categoriesLayout = (RelativeLayout) findViewById(R.id.bottom_sheet);
        RelativeLayout settingsLayout = (RelativeLayout) findViewById(R.id.settings_sheet);
        topBar = (RelativeLayout) findViewById(R.id.topBar);
        setListeners();
        setListeners();
        categoriesSheet = BottomSheetBehavior.from(categoriesLayout);
        settingSheet = BottomSheetBehavior.from(settingsLayout);

        int category = getIntent().getIntExtra("category", 0);
        int newsPostion = getIntent().getIntExtra(GCMIntentService.ID, 0);
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


        //settings items

        //   ImageView privacy = (ImageView) findViewById(R.id.privacy);
        //   ImageView about = (ImageView) findViewById(R.id.about);
        //   ImageView invite = (ImageView) findViewById(R.id.invite);
        //   ImageView help = (ImageView) findViewById(R.id.help);
        //   ImageView call = (ImageView) findViewById(R.id.call);
        //   ImageView write = (ImageView) findViewById(R.id.write);
        //   ImageView mail = (ImageView) findViewById(R.id.mail);


        //categories listeners


        allNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupViewPager(0);
                categoryChosen = 0;
                categoryChosenString = "All News";
                onRestart();
            }
        });

        economy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupViewPager(1);
                categoryChosen = 1;
                categoryChosenString = "Economy";
                onRestart();
            }
        });

        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupViewPager(2);
                categoryChosen = 2;
                categoryChosenString = "Business";
                onRestart();
            }
        });

        tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupViewPager(3);
                categoryChosen = 3;
                categoryChosenString = "Tax";
                onRestart();
            }
        });

        finace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupViewPager(4);
                categoryChosen = 4;
                categoryChosenString = "Finance";
                onRestart();
            }
        });

        law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupViewPager(5);
                categoryChosen = 5;
                categoryChosenString = "Law";
                onRestart();
            }
        });

        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupViewPager(6);
                categoryChosen = 6;
                categoryChosenString = "Global";
                onRestart();
            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupViewPager(-1);
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
                settingSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                categoriesSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
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
                }
            }, 5000);

        } else {
            topBar.startAnimation(fadeOut);
            topBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

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


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
                        switch (menuItem.getItemId()) {
                            case R.id.aboutUs:
                                Intent i = new Intent(getBaseContext(), About_Activity.class);
                                startActivity(i);
                                Drawer.closeDrawers();
                                return true;
                            case R.id.newUi:
                                Intent intent = new Intent(getBaseContext(), NewUI.class);
                                startActivity(intent);
                                Drawer.closeDrawers();
                                return true;
                            case R.id.taf:
                                Intent taf = new Intent();
                                taf.setAction(Intent.ACTION_SEND);
                                taf.putExtra(Intent.EXTRA_TEXT, "Daily updates on Economy, Finance, Business, Tax & Law in 70 words!\n" +
                                        "Download app: https://goo.gl/BjupvI");
                                taf.setType("text/plain");
                                startActivity(Intent.createChooser(taf, "Invite A Friend"));
                                Drawer.closeDrawers();
                                return true;
                            case R.id.rta:
                                Intent rate = new Intent(Intent.ACTION_VIEW);
                                rate.setData(Uri.parse("market://details?id=" + getPackageName()));
                                startActivity(rate);
                                Drawer.closeDrawers();
                                return true;
                            case R.id.help:
                                Drawer.closeDrawers();
                                //showOverLay();
                                return true;
                            case R.id.callus:
                                Intent out = new Intent(Intent.ACTION_DIAL);
                                out.setData(Uri.parse("tel:" + Uri.encode("9810076493")));
                                startActivity(out);
                                Drawer.closeDrawers();
                                return true;
                            case R.id.feedback:
                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.setType("plain/text");
                                sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"notifications@azazte.com"});
                                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Azazte app");
                                sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                                startActivity(sendIntent);
                                Drawer.closeDrawers();
                                return true;
                            case R.id.notification:


                                return true;

                            case R.id.book:
                                Drawer.closeDrawers();
                                //viewpager.setCurrentItem(8, true);
                                return true;

                            default:
                                return this.onNavigationItemSelected(menuItem);
                        }
                    }
                });
    }


}
