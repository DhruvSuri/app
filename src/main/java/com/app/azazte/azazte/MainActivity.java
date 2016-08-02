package com.app.azazte.azazte;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Fetcher.BookmarksFetcher;
import com.app.azazte.azazte.Fetcher.ImageFetcher;
import com.app.azazte.azazte.Utils.Api.ApiExecutor;
import com.app.azazte.azazte.Utils.MixPanelUtils;
import com.app.azazte.azazte.Utils.azUtils;
import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static String emailAddress;
    public BottomSheetBehavior categorySheet = null;
    private static Menu optionsMenu;
    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle;
    private ViewPagerAdapter adapter;
    private Toolbar mToolbar;

    Thread splashTread;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        init();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);



        StartSplashScreen();



    //    setupToolbar();

    //    View BottomSheet = findViewById(R.id.bottom_sheet);
    //    categorySheet = BottomSheetBehavior.from(BottomSheet);
    //    CategorySheet();
        //   categorySheet.setState(BottomSheetBehavior.STATE_EXPANDED);

    //    setupViewPager();
     // Categories.filterNewsByCategories();

        ApiExecutor.getInstance().getNews(MainActivity.emailAddress, null);

        //   SharedPreferences shaPreferences = getSharedPreferences("ShaPreferences", Context.MODE_PRIVATE);
    //   SharedPreferencesUtils.sharedPreferences = shaPreferences;
    //   if (shaPreferences.getBoolean("second", true)) {
    //       SharedPreferences sharedPreferences = shaPreferences;
    //       final SharedPreferences.Editor editor = sharedPreferences.edit();
    //       editor.putBoolean("second", false);
    //       //For commit the changes, Use either editor.commit(); or  editor.apply();.
    //       editor.commit();

    //       //showOptionsOverLay();
    //   }

                 // Finally we set the drawer toggle sync State


    }

    private void StartSplashScreen() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);



        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        RelativeLayout iv = (RelativeLayout) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(MainActivity.this,
                            NewUI.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    MainActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    MainActivity.this.finish();
                }

            }
        };
        splashTread.start();

    }




    private void init() {

        //ParseUtils.registerParse(this);
        //ParseAnalytics.trackAppOpenedInBackground(this.getIntent());
        Connector.connector = new Connector(this);
        azUtils.setPicassoInstance(this);
        BookmarksFetcher.indexBookmarks();
        new ImageFetcher(this);
        MixPanelUtils.init(this);
        MixPanelUtils.setGCM();
        emailAddress = "";
        //setContentView(R.layout.activity_splash_screen);
        try {
            Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
            for (Account account : accounts) {
                if (account.name.contains(".com")) {
                    emailAddress = emailAddress + account.name.replaceAll("gmail.com", "g");
                }
            }
            MixPanelUtils.setIdentity(emailAddress);
            Crashlytics.setUserIdentifier(emailAddress);
            if (emailAddress.length() >= 40) {
                emailAddress = emailAddress.substring(0, 39);
            }
            //throw new RuntimeException();
        } catch (Exception e) {
            //  Crashlytics.log("error");
            emailAddress = "SoSorryNotFound ";
        }

        MixPanelUtils.track("Logged into main activity");
        MixPanelUtils.fetchRegistrationId();

    }


    public void showOverLay() {

        final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);

        dialog.setContentView(R.layout.help_overlay);
        //   final ImageView help = (ImageView) dialog.findViewById(R.id.help);
        final LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.overlayLayout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_about_, menu);
        return super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        switch (id) {

            case R.id.refresh:

                setRefreshActionButtonState(true);
                ApiExecutor.getInstance().getNews(MainActivity.emailAddress, null);
                Toast.makeText(this, "Fetching News", Toast.LENGTH_SHORT).show();

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public static void setRefreshActionButtonState(final boolean refresh) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.refresh);
            if (refreshItem != null) {
                if (refresh) {
                    refreshItem.setActionView(R.layout.actionbar_refresh);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    //action bar refreshing animation

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}

