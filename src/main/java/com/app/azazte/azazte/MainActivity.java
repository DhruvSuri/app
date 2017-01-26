package com.app.azazte.azazte;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Event.MessageEvent;
import com.app.azazte.azazte.Utils.Api.ApiExecutor;
import com.app.azazte.azazte.Utils.AzazteUtils;
import com.app.azazte.azazte.Utils.MixPanelUtils;
import com.crashlytics.android.Crashlytics;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements OnProgressBarListener {
    private NumberProgressBar progressBar;

    private Timer timer;
    public static String emailAddress = "";
    public static String id;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGB_565);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        startSplashScreen();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        //id = this.getIntent().getStringExtra("notificationId");
        setContentView(R.layout.activity_main);
        // animate();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ApiExecutor.getInstance().getNews(MainActivity.emailAddress, null);
        ApiExecutor.getInstance().fetchBubbles();

        progressBar = (NumberProgressBar)findViewById(R.id.number_progress_bar);
        progressBar.setOnProgressBarListener(this);
        progressBar.setMax(100);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.incrementProgressBy(5);

                    }
                });
            }
        }, 1000, 100);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(MainActivity.this,HomeScreen.class);
                startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, 3000);

        init();
    }


    private void startSplashScreen() {

        Intent intent = new Intent(getApplicationContext(),
                HomeScreen.class);
        if (PrefManager.getInstance().getNewsCardId() != null) {
            if (!PrefManager.getInstance().getNewsCardId().equals("null")) {
                intent = new Intent(getApplicationContext(),
                    HomeScreen.class);
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        MainActivity.this.finish();
    }




    private void init() {
        Connector.connector = new Connector(this.getApplicationContext());
        MixPanelUtils.init(this);
        PrefManager.init(this);
        MixPanelUtils.fetchRegistrationId();
        MixPanelUtils.track("Logged into main activity");
        extractEmailAddress();
        AzazteUtils.getInstance().intialize(this);
    }

    private void extractEmailAddress() {
        try {
            emailAddress = "";
            Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
            for (Account account : accounts) {
                if (account.name.contains(".com")) {
                    emailAddress = emailAddress + account.name.replaceAll("gmail.com", "g");
                }
            }
            MixPanelUtils.setEmail(emailAddress);
            Crashlytics.setUserIdentifier(emailAddress);
            if (emailAddress.length() >= 40) {
                emailAddress = emailAddress.substring(0, 39);
            }
        } catch (Exception e) {
            emailAddress = "SoSorryNotFound ";
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onProgressChange(int current, int max) {
        if(current == max) {
            progressBar.setProgress(0);
        }
    }
}

