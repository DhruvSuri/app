package com.app.azazte.azazte;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.azazte.azazte.Beans.HomeScreenAdapter;
import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Utils.Api.ApiExecutor;
import com.app.azazte.azazte.Utils.AzazteUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        FrameLayout refreshfilter = (FrameLayout) findViewById(R.id.refreshfilter);
        topBar = (RelativeLayout) findViewById(R.id.topBar);
        FrameLayout settings = (FrameLayout) findViewById(R.id.settingsClick);
        twilightFilter = (RelativeLayout) findViewById(R.id.nightUI);
        ImageView headerImage = (ImageView) findViewById(R.id.headerImage);
        TextView heading = (TextView) findViewById(R.id.headline);
        FrameLayout headClick = (FrameLayout) findViewById(R.id.clickFrame);
        allNews = Connector.getInstance().getAllNews();
        headCard = allNews.get(0);
        allNews.remove(0);
        setUpHead(headerImage, heading, headClick);
        setupRV();
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();

            }
        });
        refreshfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();

            }
        });




    }

    private void refresh() {
        Toaster.toast("Hold on! refreshing");
        ApiExecutor.getInstance().getNews(null, swipeRefresh);

    }

    private void setupRV() {


    recyclerView = (RecyclerView) findViewById(R.id.all_rv);
        RecyclerView businessRv = (RecyclerView) findViewById(R.id.business_rv);
        RecyclerView financeRv = (RecyclerView) findViewById(R.id.fin_rv);
        RecyclerView worldRv = (RecyclerView) findViewById(R.id.world_rv);
        RecyclerView techRv = (RecyclerView) findViewById(R.id.tech_rv);
        RecyclerView ecoRv = (RecyclerView) findViewById(R.id.eco_rv);
        RecyclerView lawRv = (RecyclerView) findViewById(R.id.law_rv);
        RecyclerView moneyRv = (RecyclerView) findViewById(R.id.money_rv);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager4 = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager5 = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager6 = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager7 = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);


        myadapter = new HomeScreenAdapter(allNews, 0, "All News", this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 2), 2, "Business", this);
        businessRv.setLayoutManager(gridLayoutManager1);
        businessRv.setItemAnimator(new DefaultItemAnimator());
        businessRv.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 3), 3, "Politics", this);
        financeRv.setLayoutManager(gridLayoutManager2);
        financeRv.setItemAnimator(new DefaultItemAnimator());
        financeRv.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 1), 1, "Current Affairs", this);
        ecoRv.setLayoutManager(gridLayoutManager3);
        ecoRv.setItemAnimator(new DefaultItemAnimator());
        ecoRv.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 5), 5, "World", this);
        worldRv.setLayoutManager(gridLayoutManager4);
        worldRv.setItemAnimator(new DefaultItemAnimator());
        worldRv.setAdapter(myadapter);


        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 7), 7, "Startup", this);
        techRv.setLayoutManager(gridLayoutManager5);
        techRv.setItemAnimator(new DefaultItemAnimator());
        techRv.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 6), 6, "Sports", this);
        lawRv.setLayoutManager(gridLayoutManager6);
        lawRv.setItemAnimator(new DefaultItemAnimator());
        lawRv.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 4), 4, "Money", this);
        moneyRv.setLayoutManager(gridLayoutManager7);
        moneyRv.setItemAnimator(new DefaultItemAnimator());
        moneyRv.setAdapter(myadapter);
    }


    private void setUpHead(final ImageView headerImage, TextView heading, FrameLayout headClick) {
        if (allNews.size() == 0) {
            return;
        }
        AzazteUtils.getInstance().setImageIntoView(this.getApplicationContext(), headerImage, headCard.imageUrl, R.drawable.placeholder);
        heading.setText(headCard.newsHead);
        headerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        NewUI.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                PrefManager.getInstance().saveNewsCardId(headCard.id);
                startActivity(intent);
            }
        });


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
}
