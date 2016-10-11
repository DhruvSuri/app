package com.app.azazte.azazte.Beans;

/**
 * Created by Mac on 08/10/16.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Build;
import android.support.v7.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.azazte.azazte.NewUI;
import com.app.azazte.azazte.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenAdapter.MyViewHolder> {

    private ArrayList<NewsCard> allnews;
    NewsCard newsCard;
    Context context;
    LayoutInflater inflater;
    Picasso picasso;





    public HomeScreenAdapter(ArrayList<NewsCard>  allnews, Context context) {
        this.allnews = allnews;
        this.context = context;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.grid_item2, parent, false);

        MyViewHolder holder = new MyViewHolder(itemView);


        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        newsCard = allnews.get(position+1);
        holder.heading.setText(newsCard.newsHead);
        setImageIntoView(picasso, holder.newsImage, newsCard.imageUrl);
        holder.heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                                NewUI.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("id", newsCard.id);
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return allnews.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView heading;
        public ImageView newsImage;
        public FrameLayout clickFrame;

        public MyViewHolder(View itemView) {
            super(itemView);

           // clickFrame = (FrameLayout) itemView.findViewById(R.id.click);
            newsImage = (ImageView) itemView.findViewById(R.id.newsimage);
            heading = (TextView) itemView.findViewById(R.id.headline);

        }
    }
    private void setImageIntoView(Picasso picasso, ImageView imageView, String imageUrl) {

        Glide.with(context).load(imageUrl).into(imageView);

    }


}

