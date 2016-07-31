package com.app.azazte.azazte;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Fetcher.BookmarksFetcher;
import com.app.azazte.azazte.Fetcher.ImageFetcher;
import com.app.azazte.azazte.Utils.Api.ApiExecutor;
import com.app.azazte.azazte.Utils.Categories;
import com.app.azazte.azazte.Utils.MixPanelUtils;
import com.app.azazte.azazte.Utils.ParseUtils;
import com.app.azazte.azazte.Utils.SharedPreferencesUtils;
import com.app.azazte.azazte.Utils.azUtils;
import com.crashlytics.android.Crashlytics;
import com.parse.ParseAnalytics;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        setupToolbar();

        View BottomSheet = findViewById(R.id.bottom_sheet);
        categorySheet = BottomSheetBehavior.from(BottomSheet);
        CategorySheet();
        //   categorySheet.setState(BottomSheetBehavior.STATE_EXPANDED);

        setupViewPager();
        Categories.filterNewsByCategories();
        ApiExecutor.getInstance().getNews(MainActivity.emailAddress, null);


        SharedPreferences shaPreferences = getSharedPreferences("ShaPreferences", Context.MODE_PRIVATE);
        SharedPreferencesUtils.sharedPreferences = shaPreferences;
        if (shaPreferences.getBoolean("second", true)) {
            SharedPreferences sharedPreferences = shaPreferences;
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("second", false);
            //For commit the changes, Use either editor.commit(); or  editor.apply();.
            editor.commit();

            //showOverLay();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, mToolbar, R.string.skip, R.string.start) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State


    }


    void CategorySheet() {


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


    private void setupViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupToolbar() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("azazte");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TabFragment(0), "ALL NEWS");
        adapter.addFrag(new TabFragment(1), "Economy");
        adapter.addFrag(new TabFragment(2), "Business & Finance");
        adapter.addFrag(new TabFragment(3), "Tax");
        adapter.addFrag(new TabFragment(4), "My Finance");
        adapter.addFrag(new TabFragment(5), "Law & Regulatory");
        adapter.addFrag(new TabFragment(6), "Rulings");
        adapter.addFrag(new BookmarkFragment(), "Bookmarks");


        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
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
                                showOverLay();
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
                                viewpager.setCurrentItem(8, true);
                                return true;

                            default:
                                return this.onNavigationItemSelected(menuItem);
                        }
                    }
                });
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

