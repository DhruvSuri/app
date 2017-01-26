package com.app.azazte.azazte;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.azazte.azazte.Beans.HomeScreenAdapter;
import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Utils.Api.ApiExecutor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;
import xdroid.toaster.Toaster;

public class HomeScreen extends AppCompatActivity {

    HomeScreenAdapter myadapter;
    ArrayList<NewsCard> allNews;
    NewsCard headCard;
    Picasso picasso;
    public RelativeLayout twilightFilter;
    public ImageView twilight;
    RelativeLayout topBar;
    ImageView notification;
    ImageView imageView;
    TextView nighttxt;
    TextView bellTxt;
    TextView imageTxt;
    int back = 0;
    private SwipeRefreshLayout swipeRefresh;
    RecyclerView recyclerView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

      if (PrefManager.getInstance().isFirstTimeLaunch()) {
         showOverLay();
          PrefManager.getInstance().setFirstTimeLaunch(false);
       }


        context = getApplicationContext();
         FrameLayout refreshfilter = (FrameLayout) findViewById(R.id.refreshfilter);
        topBar = (RelativeLayout) findViewById(R.id.topBar);
        FrameLayout settings = (FrameLayout) findViewById(R.id.settingsClick);
        twilightFilter = (RelativeLayout) findViewById(R.id.nightUI);
        allNews = Connector.getInstance().getAllNews();
        headCard = allNews.get(0);
        allNews.remove(0);



refreshfilter.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        refresh();
    }
});

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();

            }
        });

        initUI();





    }

    private void refresh() {
        Toaster.toast("Hold on! refreshing");
        ApiExecutor.getInstance().getNews(null, swipeRefresh);

    }



    public void openSettings() {
        View view = getLayoutInflater().inflate(R.layout.settings, null);
        final Dialog settingSheet = new Dialog(HomeScreen.this, R.style.leftSheet);
        settingSheet.setContentView(view);
        settingSheet.setCancelable(true);
        settingSheet.show();
        settingSheet.getWindow().setGravity(Gravity.RIGHT);
        notification = (ImageView) view.findViewById(R.id.notification);
        imageView = (ImageView) view.findViewById(R.id.noimage);
        twilight = (ImageView) view.findViewById(R.id.light);
        FrameLayout shareApp = (FrameLayout) view.findViewById(R.id.shareFrame);
        final FrameLayout about = (FrameLayout) view.findViewById(R.id.aboutFrame);
        bellTxt = (TextView) view.findViewById(R.id.bellText);
        imageTxt = (TextView) view.findViewById(R.id.imageText);
        nighttxt = (TextView) view.findViewById(R.id.nightText);
        FrameLayout privacy = (FrameLayout) view.findViewById(R.id.privacyFrame);
        FrameLayout rate = (FrameLayout) view.findViewById(R.id.rateFrame);
        FrameLayout mailUs = (FrameLayout) view.findViewById(R.id.contactFrame);
        FrameLayout write = (FrameLayout) view.findViewById(R.id.writeFrame);
        FrameLayout feedback = (FrameLayout) view.findViewById(R.id.feedbackFrame);
        FrameLayout close = (FrameLayout) view.findViewById(R.id.close);
        FrameLayout homefilter = (FrameLayout) view.findViewById(R.id.homeFilter);

        if (PrefManager.getInstance().getNotificationState().equals(PrefManager.NOTIFICATION_STATE_OFF)) {
            notification.setImageResource(R.drawable.belloff);
            bellTxt.setTextColor(Color.parseColor("#626262"));
        } else {
            notification.setImageResource(R.drawable.bell);

            bellTxt.setTextColor(Color.parseColor("#44b3f9"));
        }

        if (PrefManager.getInstance().getImageState().equals(PrefManager.IMAGE_STATE_OFF)) {
            imageView.setImageResource(R.drawable.image);
            imageTxt.setTextColor(Color.parseColor("#626262"));
        } else {
            imageView.setImageResource(R.drawable.imageon);
            imageTxt.setTextColor(Color.parseColor("#44b3f9"));
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSheet.dismiss();
            }
        });

        homefilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),
                        HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        settingSheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // categoriesText.setVisibility(View.VISIBLE);
                //  topRefreshButton.setVisibility(View.VISIBLE);
                //  categoriesButton.setImageResource(R.drawable.ic_menu);
                //  settings.setImageResource(R.drawable.ic_settings);

            }
        });


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
                imageView.setImageResource(getImageDrawable());
                if (imageView.getDrawable() == getResources().getDrawable(R.drawable.imageon)) {

                    imageTxt.setTextColor(Color.parseColor("#44b3f9"));

                } else {
                    imageTxt.setTextColor(Color.parseColor("#626262"));
                }

            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification.setImageResource(getNotificationDrawable());
                if (notification.getDrawable() == getResources().getDrawable(R.drawable.bell)) {
                    bellTxt.setTextColor(Color.parseColor("#44b3f9"));
                } else {
                    bellTxt.setTextColor(Color.parseColor("#626262"));
                }
            }
        });

        twilight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twilightMode();
            }
        });


    }

    public int getNotificationDrawable() {
        if (PrefManager.getInstance().getNotificationState().equals(PrefManager.NOTIFICATION_STATE_OFF)) {
            PrefManager.getInstance().setNotificationOn();
            return R.drawable.bell;
        } else {
            PrefManager.getInstance().setNotificationOff();
            return R.drawable.belloff;
        }
    }

    public int getImageDrawable() {
        if (PrefManager.getInstance().getImageState().equals(PrefManager.IMAGE_STATE_OFF)) {
            PrefManager.getInstance().setImageOn();

            return R.drawable.imageon;
        } else {
            PrefManager.getInstance().setImageOff();
            return R.drawable.image;
        }
    }


    public void twilightMode() {
        if (twilightFilter.getVisibility() == View.INVISIBLE) {
            twilightFilter.setVisibility(View.VISIBLE);
            twilight.setImageResource(R.drawable.lighton);
            nighttxt.setTextColor(Color.parseColor("#44b3f9"));

        } else {
            twilightFilter.setVisibility(View.INVISIBLE);
            twilight.setImageResource(R.drawable.lightoff);
            nighttxt.setTextColor(Color.parseColor("#626262"));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (back > 1) {
            Toaster.toast("Press again to exit");
            back++;
            moveTaskToBack(true);
        }

    }

    @Override
    protected void onResume() {
        back = 0;
        super.onResume();
    }
    public void showOverLay() {
        final Dialog dialog = new Dialog(this, R.style.bubble);
        dialog.setContentView(R.layout.privacy);
        TextView privacy = (TextView) dialog.findViewById(R.id.privacy);

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="http://www.finup.in/privacy.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        Button accept = (Button) dialog.findViewById(R.id.accept);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        dialog.show();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                PrefManager.getInstance().setFirstTimeLaunch(true);

            }
        });
    }

    private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 8;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view = LayoutInflater.from(
                        getBaseContext()).inflate(R.layout.item_vp_list, null, false);

                final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(
                                getBaseContext(),2)
                );

              //  recyclerView.setAdapter(new RecycleAdapter());

                switch (position) {
                    case 0:
                        myadapter = new HomeScreenAdapter(allNews, 0, "All News", getApplicationContext());
                        break;
                    case 1:
                        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 1), 1, "Business", context);
                        break;
                    case 2:
                        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 2), 2, "Current Affairs", context);
                        break;
                    case 3:
                        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 3), 3, "Politics", context);
                        break;
                    case 4:
                        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 4), 4, "World", context);
                        break;
                    case 5:
                        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 5), 5, "Entertainment", context);
                        break;
                    case 6:
                        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 6), 6, "Sports", context);
                        break;
                    case 7:
                        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 7), 7, "Startup", context);
                        break;
                }

                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(myadapter);
                container.addView(view);
                return view;
            }
        });

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.news),
                        Color.parseColor(colors[0]))
                        .title("All News")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.business),
                        Color.parseColor(colors[1]))
                        .title("Business")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.trending),
                        Color.parseColor(colors[2]))
                        .title("Trends")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.entertainment),
                        Color.parseColor(colors[3]))
                        .title("Entertainment")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.world_icon),
                        Color.parseColor(colors[2]))
                        .title("World")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.politics_icon),
                        Color.parseColor(colors[1]))
                        .title("Politics")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.sports_icon),
                        Color.parseColor(colors[4]))
                        .title("Sports")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.startup),
                        Color.parseColor(colors[3]))
                        .title("Startup")
                        .build()
        );



        navigationTabBar.setModels(models);
        navigationTabBar.setTitleSize(14);
        navigationTabBar.setIconSizeFraction(0.7F);
        navigationTabBar.setViewPager(viewPager, 0);



      navigationTabBar.post(new Runnable() {
          @Override
          public void run() {
              final View viewPager = findViewById(R.id.vp_horizontal_ntb);
              ((ViewGroup.MarginLayoutParams) viewPager.getLayoutParams()).topMargin =
                      (int) -navigationTabBar.getBadgeMargin();
              viewPager.requestLayout();
          }
      });

        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {

            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                model.hideBadge();
            }
        });

    // findViewById(R.id.mask).setOnClickListener(new View.OnClickListener() {
    //     @Override
    //     public void onClick(final View v) {
    //         for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
    //             final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
    //             navigationTabBar.postDelayed(new Runnable() {
    //                 @Override
    //                 public void run() {
    //                     final String title = String.valueOf(new Random().nextInt(15));
    //                     if (!model.isBadgeShowed()) {
    //                         model.setBadgeTitle(title);
    //                         model.showBadge();
    //                     } else model.updateBadgeTitle(title);
    //                 }
    //             }, i * 100);
    //         }
    //     }
    // });


    }




}

