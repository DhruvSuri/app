package com.app.azazte.azazte;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Event.MessageEvent;
import com.app.azazte.azazte.Utils.Api.ApiExecutor;
import com.app.azazte.azazte.Utils.MixPanelUtils;
import com.app.azazte.azazte.Utils.NewscardFragment;
import com.app.azazte.azazte.Utils.TemplateFragment;
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
    private CharSequence revert;

    boolean categoryOpen;
    boolean settingOpen;

    Animation fadeIn;
    Animation fadeOut;
    DrawerLayout Drawer;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private BottomSheetBehavior categoriesSheet;
    private BottomSheetBehavior settingSheet;
    private static final String SETTINGS = "Settings";
    private static final String CATEGORIES = "Categories";


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
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/HelveticaNeue.tff")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
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

        categoriesSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    categoriesSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                bottomSheet.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        categoriesSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        int action = MotionEventCompat.getActionMasked(event);
//                        switch (action) {
//                            case MotionEvent.ACTION_DOWN:
//                                return false;
//                            default:
//                                return true;
//                        }
//                    }
//                });
            }
        });
        settingSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    settingSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                bottomSheet.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        settingSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        int action = MotionEventCompat.getActionMasked(event);
//                        switch (action) {
//                            case MotionEvent.ACTION_DOWN:
//                                return false;
//                            default:
//                                return true;
//                        }
//                    }
//                });
            }
        });
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

        if (PrefManager.getInstance().isFirstTimeLaunch()) {
            showOverLay();
            PrefManager.getInstance().setFirstTimeLaunch(false);
        }
    }

    public void showOverLay() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.help_overlay);
        final LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.overlayLayout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
        ImageView money = (ImageView) findViewById(R.id.money);
        ImageView economy = (ImageView) findViewById(R.id.economy);
        ImageView global = (ImageView) findViewById(R.id.global);
        ImageView myLibrary = (ImageView) findViewById(R.id.bookmark);
        final ImageView settings = (ImageView) findViewById(R.id.settings);
        final ImageView categoriesButton = (ImageView) findViewById(R.id.categories);
        final ImageView topRefreshButton = (ImageView) findViewById(R.id.top_refresh);
        final TextView categoriesText = (TextView) findViewById(R.id.categoriesTextMenu);


        FrameLayout topfilter = (FrameLayout) findViewById(R.id.topfilter);
        FrameLayout refreshfilter = (FrameLayout) findViewById(R.id.refreshfilter);
        FrameLayout settingsfilter = (FrameLayout) findViewById(R.id.settingfilter);


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
                categoryChosenString = "Deals";
                onRestart();
            }
        });

        finace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupViewPager(4);
                categoryChosen = 3;
                categoryChosenString = "Finance";
                onRestart();
            }
        });

        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupViewPager(3);
                categoryChosen = 4;
                categoryChosenString = "Money";
                onRestart();
            }
        });

        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupViewPager(3);
                categoryChosen = 5;
                categoryChosenString = "Global";
                onRestart();
            }
        });

        tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupViewPager(3);
                categoryChosen = 6;
                categoryChosenString = "Tax";
                onRestart();
            }
        });

        law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupViewPager(5);
                categoryChosen = 7;
                categoryChosenString = "Startup";
                onRestart();
            }
        });


        myLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setupViewPager(-1);
                categoryChosen = -1;
                categoryChosenString = "My Library";
                onRestart();
            }
        });

        settingsfilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (categoriesSheet.getState() == BottomSheetBehavior.STATE_EXPANDED || categoriesSheet.getState() == BottomSheetBehavior.STATE_SETTLING) { // This is for closing the categories sheet
                    categoriesSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    categoriesButton.setImageResource(R.drawable.ic_menu);
                    categoriesText.setText(revert);
                    topRefreshButton.setVisibility(View.VISIBLE);
                    settings.setImageResource(R.drawable.ic_settings);
                    return;
                }

                if (settingSheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    settingSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                    settings.setImageResource(R.drawable.close);
                    categoriesButton.setImageResource(R.drawable.ic_settings);
                    topRefreshButton.setVisibility(View.INVISIBLE);
                    revert = categoriesText.getText();
                    categoriesText.setText(SETTINGS);
                } else {
                    settingSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    categoriesText.setText(revert);
                    topRefreshButton.setVisibility(View.VISIBLE);
                    settings.setImageResource(R.drawable.ic_settings);
                    categoriesButton.setImageResource(R.drawable.ic_menu);
                }

            }
        });

        topfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingSheet.getState() == BottomSheetBehavior.STATE_EXPANDED || settingSheet.getState() == BottomSheetBehavior.STATE_SETTLING) {
                    settingSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    categoriesText.setText(revert);
                    topRefreshButton.setVisibility(View.VISIBLE);
                    settings.setImageResource(R.drawable.ic_settings);
                    categoriesButton.setImageResource(R.drawable.ic_menu);
                    return;
                }

                if (categoriesSheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    settingSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    if (!categoriesText.getText().equals(CATEGORIES) && !categoriesText.getText().equals(SETTINGS)) {
                        revert = categoriesText.getText();
                    }
                    categoriesText.setText(CATEGORIES);
                    topRefreshButton.setVisibility(View.INVISIBLE);
                    settings.setImageResource(R.drawable.close);
                    categoriesSheet.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    categoriesSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    categoriesButton.setImageResource(R.drawable.ic_menu);
                    categoriesText.setText(revert);
                    topRefreshButton.setVisibility(View.VISIBLE);
                    settings.setImageResource(R.drawable.ic_settings);
                }
            }
        });


        //settings items

        ImageView shareApp = (ImageView) findViewById(R.id.privacy);
        final ImageView about = (ImageView) findViewById(R.id.about);
        final ImageView notification = (ImageView) findViewById(R.id.notification);
        final ImageView imageView = (ImageView) findViewById(R.id.noimage);
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
                shareAppIntent.putExtra(Intent.EXTRA_TEXT, "Need to manage your finances but the technicalities make you sweat ?\n Finup : Decoding business, finance and technology in 30 second reads.\n" +
                        "Download finup: https://goo.gl/BjupvI");
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


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int drawable;
                if (PrefManager.getInstance().getImageState().equals(PrefManager.IMAGE_STATE_OFF)) {
                    PrefManager.getInstance().setImageOn();
                    drawable = R.drawable.imageon;
                } else {
                    PrefManager.getInstance().setImageOff();
                    drawable = R.drawable.image;
                }
                imageView.setImageResource(drawable);
            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int drawable;
                if (PrefManager.getInstance().getNotificationState().equals(PrefManager.NOTIFICATION_STATE_OFF)) {
                    PrefManager.getInstance().setNotificationOn();
                    drawable = R.drawable.bellon;
                } else {
                    PrefManager.getInstance().setNotificationOff();
                    drawable = R.drawable.bell;
                }
                notification.setImageResource(drawable);
            }
        });

        twilight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twilightMode();
            }
        });


        refreshfilter.setOnClickListener(new View.OnClickListener() {
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

    public void twilightMode() {
        if (twilightFilter.getVisibility() == View.INVISIBLE) {
            twilightFilter.setVisibility(View.VISIBLE);
            twilight.setImageResource(R.drawable.lighton);

        } else {
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
            if (allBookmarks.size() == 0) {
                addEndLayoutToFragmentList(adapter, R.layout.emptylibrary);
            }
            for (NewsCard newsCard : allBookmarks) {
                adapter.addFrag(new NewscardFragment(newsCard, this.getApplicationContext()));
            }
            return;
        }

        ArrayList<NewsCard> allNews = Connector.getInstance().getAllNews();
        for (NewsCard newsCard : allNews) {
            int cardCategory = 0;
            String[] split = newsCard.category.split(",");

            for (String categoryString : split) {
                try {
                    cardCategory = Integer.valueOf(categoryString);
                } catch (Exception e) {
                    cardCategory = 0;
                }
                if (category == cardCategory || category == 0) {
                    adapter.addFrag(new NewscardFragment(newsCard, this.getApplicationContext()));
                    break;
                }
            }
        }
        addEndLayoutToFragmentList(adapter, R.layout.endofstory);
    }

    private void addEndLayoutToFragmentList(ViewPagerAdapter adapter, int layout) {
        Bundle bundle = new Bundle();
        bundle.putInt(TemplateFragment.LAYOUT, layout);
        TemplateFragment templateFragment = new TemplateFragment();
        templateFragment.setArguments(bundle);
        adapter.addFrag(templateFragment);
    }


    public void showTopBar() {
        if (topBar.getVisibility() == View.GONE) {
            topBar.setVisibility(View.VISIBLE);
            topBar.startAnimation(fadeIn);
            //      topBar.postDelayed(new Runnable() {
            //          @Override
            //          public void run() {
            //              topBar.startAnimation(fadeOut);
            //              topBar.setVisibility(View.GONE);
            //              topBar.clearAnimation();
            //          }
            //      }, 5000);

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
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
