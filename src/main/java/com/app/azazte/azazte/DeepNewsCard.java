package com.app.azazte.azazte;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.app.azazte.azazte.Beans.NewsCard;

import com.app.azazte.azazte.Database.Connector;

public class DeepNewsCard extends AppCompatActivity {

    Animation slide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_news_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        slide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadein);

        Intent myIntent = getIntent();
        String deepNewsid = myIntent.getStringExtra("id");

        NewsCard newsCard = Connector.getInstance().getNewsById(deepNewsid);


        TextView newshead = (TextView) findViewById(R.id.headtxt);
        TextView newstxt = (TextView) findViewById(R.id.newstxt);
        TextView newsSource = (TextView) findViewById(R.id.newsSource);
        TextView date = (TextView) findViewById(R.id.date);
        TextView impact = (TextView) findViewById(R.id.impactTxt);
        TextView author = (TextView) findViewById(R.id.author);

        newshead.setText(newsCard.newsHead);
        newstxt.setText(newsCard.newsBody);
        newsSource.setText(newsCard.newsSourceName);
        date.setText(newsCard.date);
        impact.setText(newsCard.impact);
        author.setText(newsCard.author);
    }


}





