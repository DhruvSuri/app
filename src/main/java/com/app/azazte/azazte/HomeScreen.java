package com.app.azazte.azazte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ImageView headerImage = (ImageView) findViewById(R.id.headerImage);
        TextView heading = (TextView) findViewById(R.id.headline);
        FrameLayout headClick = (FrameLayout)findViewById(R.id.clickFrame);
        allNews = Connector.getInstance().getAllNews();
        setUpHead(headerImage,heading,headClick);
        RecyclerViewHeader header = (RecyclerViewHeader) findViewById(R.id.header);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        myadapter = new HomeScreenAdapter(allNews, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(mLayoutManager);
        header.attachTo(recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myadapter);


    }

    private void setUpHead(ImageView headerImage, TextView heading, FrameLayout headClick) {

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

}
