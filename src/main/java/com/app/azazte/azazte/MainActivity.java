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
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Event.MessageEvent;
import com.app.azazte.azazte.Utils.Api.ApiExecutor;
import com.app.azazte.azazte.Utils.MixPanelUtils;
import com.crashlytics.android.Crashlytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    public static String emailAddress = "";
    String id;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
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
        String id = this.getIntent().getStringExtra("id");
        setContentView(R.layout.activity_main);
       // animate();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ApiExecutor.getInstance().getNews(MainActivity.emailAddress, null);

        init();
        this.id = id;
    }



    private void startSplashScreen() {


        Intent intent = new Intent(getApplicationContext(),
                HomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        MainActivity.this.finish();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//          // Intent intent = new Intent(getApplicationContext(),
//          //         NewUI.class);
//          //     intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//          //     intent.putExtra("id", id);
//          //     startActivity(intent);
////                Intent intent = new Intent(getApplicationContext(),
////                     HomeScreen.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////                startActivity(intent);
////                MainActivity.this.finish();
//            }
//        }, 2000);

    }

    private void animate() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        RelativeLayout logo = (RelativeLayout) findViewById(R.id.logo);
        logo.clearAnimation();
        logo.startAnimation(anim);
    }


    private void init() {
        Connector.connector = new Connector(this.getApplicationContext());
        MixPanelUtils.init(this);
        PrefManager.init(this);
        MixPanelUtils.fetchRegistrationId();
        MixPanelUtils.track("Logged into main activity");
        extractEmailAddress();
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

}

