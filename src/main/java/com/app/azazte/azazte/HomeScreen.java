package com.app.azazte.azazte;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.azazte.azazte.Beans.HomeScreenAdapter;
import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Utils.Api.ApiExecutor;
import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    HomeScreenAdapter myadapter;
    ArrayList<NewsCard> allNews;
    NewsCard newsCard;
    Picasso picasso;
    RelativeLayout topBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        topBar = (RelativeLayout) findViewById(R.id.topBar);

        ImageView headerImage = (ImageView) findViewById(R.id.headerImage);
        TextView heading = (TextView) findViewById(R.id.headline);
        FrameLayout headClick = (FrameLayout)findViewById(R.id.clickFrame);
        allNews = Connector.getInstance().getAllNews();
        setUpHead(headerImage,heading,headClick);
       // RecyclerViewHeader header = (RecyclerViewHeader) findViewById(R.id.header);


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


        myadapter = new HomeScreenAdapter(allNews, this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(allNews, this);
        businessRv.setLayoutManager(gridLayoutManager1);
        businessRv.setItemAnimator(new DefaultItemAnimator());
        businessRv.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(allNews, this);
        financeRv.setLayoutManager(gridLayoutManager2);
        financeRv.setItemAnimator(new DefaultItemAnimator());
        financeRv.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(allNews, this);
        ecoRv.setLayoutManager(gridLayoutManager3);
        ecoRv.setItemAnimator(new DefaultItemAnimator());
        ecoRv.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(allNews, this);
        worldRv.setLayoutManager(gridLayoutManager4);
        worldRv.setItemAnimator(new DefaultItemAnimator());
        worldRv.setAdapter(myadapter);


        myadapter = new HomeScreenAdapter(allNews, this);
        techRv.setLayoutManager(gridLayoutManager5);
        techRv.setItemAnimator(new DefaultItemAnimator());
        techRv.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(allNews, this);
        lawRv.setLayoutManager(gridLayoutManager6);
        lawRv.setItemAnimator(new DefaultItemAnimator());
        lawRv.setAdapter(myadapter);

        myadapter = new HomeScreenAdapter(allNews, this);
        moneyRv.setLayoutManager(gridLayoutManager7);
        moneyRv.setItemAnimator(new DefaultItemAnimator());
        moneyRv.setAdapter(myadapter);



    }


    private void setUpHead(ImageView headerImage, TextView heading, FrameLayout headClick) {
        if (allNews.size() == 0){
            return;
        }
        newsCard = allNews.get(0);

        setImageIntoView(picasso,headerImage,newsCard.imageUrl);
        heading.setText(newsCard.newsHead);
        headClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),
                        NewUI.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("id", newsCard.id);
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
