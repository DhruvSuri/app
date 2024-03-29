package com.app.azazte.azazte.Beans;

/**
 * Created by Mac on 08/10/16.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.azazte.azazte.NewUI;
import com.app.azazte.azazte.PrefManager;
import com.app.azazte.azazte.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenAdapter.MyViewHolder> {

    private ArrayList<NewsCard> allnews;
    NewsCard newsCard;
    Context context;
    LayoutInflater inflater;
    String categoryName;
    int category;


    public HomeScreenAdapter(ArrayList<NewsCard> allnews, int category, String categoryName, Context context) {
        this.allnews = allnews;
        this.context = context;
        this.categoryName = categoryName;
        this.category = category;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.grid_item, parent, false);

        MyViewHolder holder = new MyViewHolder(itemView);


        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        newsCard = allnews.get(position);
        holder.heading.setText(newsCard.newsHead.trim());
        setImageIntoView(holder.newsImage, newsCard.imageUrl);
    }

    @Override
    public int getItemCount() {
        return allnews.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView heading;
        public ImageView newsImage;
        public FrameLayout clickFrame;

        public MyViewHolder(View itemView) {
            super(itemView);

            //    clickFrame = (FrameLayout) itemView.findViewById(R.id.clickFrame);
            newsImage = (ImageView) itemView.findViewById(R.id.newsimage);
            heading = (TextView) itemView.findViewById(R.id.headline);

            newsImage.setOnClickListener(this);
            heading.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            newsCard = allnews.get(getPosition());
            Intent intent = new Intent(context,
                    NewUI.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            PrefManager.getInstance().saveNewsCardId(newsCard.id);
            intent.putExtra("category", category);
            intent.putExtra("categoryChosenString", categoryName);
            context.startActivity(intent);

        }
    }

    private void setImageIntoView(ImageView imageView, String imageUrl) {

        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .placeholder(R.drawable.placeholder) // can also be a drawable
                .into(imageView);

    }


}

