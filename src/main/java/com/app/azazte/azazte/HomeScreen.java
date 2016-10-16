package com.app.azazte.azazte;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.azazte.azazte.Beans.HomeScreenAdapter;
import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    HomeScreenAdapter myadapter;
    ArrayList<NewsCard> allNews;
    NewsCard newsCard;
    NewsCard headCard;
    Picasso picasso;
    RelativeLayout topBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        topBar = (RelativeLayout) findViewById(R.id.topBar);

        ImageView headerImage = (ImageView) findViewById(R.id.headerImage);
        TextView heading = (TextView) findViewById(R.id.headline);
        FrameLayout headClick = (FrameLayout) findViewById(R.id.clickFrame);
        allNews = Connector.getInstance().getAllNews();
        headCard = allNews.get(0);
        allNews.remove(0);
        setUpHead(headerImage, heading, headClick);
        setupRV();
    }

    private void setupRV() {


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.all_rv);
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

        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 3), 3, "Finance", this);
        financeRv.setLayoutManager(gridLayoutManager2);
        financeRv.setItemAnimator(new DefaultItemAnimator());
        financeRv.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 1), 1, "Economy", this);
        ecoRv.setLayoutManager(gridLayoutManager3);
        ecoRv.setItemAnimator(new DefaultItemAnimator());
        ecoRv.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 5), 5, "World", this);
        worldRv.setLayoutManager(gridLayoutManager4);
        worldRv.setItemAnimator(new DefaultItemAnimator());
        worldRv.setAdapter(myadapter);


        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 7), 7, "Technology", this);
        techRv.setLayoutManager(gridLayoutManager5);
        techRv.setItemAnimator(new DefaultItemAnimator());
        techRv.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 6), 6, "Law", this);
        lawRv.setLayoutManager(gridLayoutManager6);
        lawRv.setItemAnimator(new DefaultItemAnimator());
        lawRv.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(Connector.getInstance().getAllNewsByCategory(allNews, 4), 4, "Money", this);
        moneyRv.setLayoutManager(gridLayoutManager7);
        moneyRv.setItemAnimator(new DefaultItemAnimator());
        moneyRv.setAdapter(myadapter);
    }


    private void setUpHead(ImageView headerImage, TextView heading, FrameLayout headClick) {
        if (allNews.size() == 0) {
            return;
        }
        setImageIntoView(picasso, headerImage, headCard.imageUrl);
        heading.setText(headCard.newsHead);
        headClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        NewUI.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("id", headCard.id);
                startActivity(intent);
            }
        });


    }

    private void setImageIntoView(Picasso picasso, ImageView imageView, String imageUrl) {

        Glide.with(this).load(imageUrl).into(imageView);

    }

    public void showTopBar() {
        if (topBar.getVisibility() == View.GONE) {
            topBar.setVisibility(View.VISIBLE);


        } else {

            topBar.setVisibility(View.GONE);
        }
    }


}
