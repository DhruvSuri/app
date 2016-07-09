package com.app.azazte.azazte;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.azazte.azazte.Beans.NewsCard;

import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Utils.azUtils;

import java.util.List;

public class DeepNewsCardActivity extends AppCompatActivity {

    final Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_news_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent myIntent = getIntent();
        String deepNewsid = myIntent.getStringExtra("id");

        NewsCard newsCard = Connector.getInstance().getNewsById(deepNewsid);


        TextView newshead = (TextView) findViewById(R.id.headtxt);
        TextView newstxt = (TextView) findViewById(R.id.newstxt);
        TextView newsSource = (TextView) findViewById(R.id.newsSource);
        TextView date = (TextView) findViewById(R.id.date);
        TextView impact = (TextView) findViewById(R.id.impactTxt);
        TextView author = (TextView) findViewById(R.id.author);
        ImageView imageView = (ImageView) findViewById(R.id.imageHead);

        newshead.setText(newsCard.newsHead);
        newstxt.setText(newsCard.newsBody);
        newsSource.setText(newsCard.newsSourceName);
        date.setText(newsCard.date);
        impact.setText(newsCard.impact);
        author.setText(newsCard.author);
        azUtils.setImageIntoView(imageView, newsCard.imageUrl);

    }
}

