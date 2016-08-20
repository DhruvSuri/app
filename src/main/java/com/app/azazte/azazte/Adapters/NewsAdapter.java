package com.app.azazte.azazte.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.azazte.azazte.AzazteWebView;
import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Fetcher.BookmarksFetcher;
import com.app.azazte.azazte.Fetcher.ImageFetcher;
import com.app.azazte.azazte.R;

import com.app.azazte.azazte.Utils.MixPanelUtils;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.util.List;

import static xdroid.core.Global.getResources;

public class NewsAdapter extends ArrayAdapter<NewsCard> {
    Context context;
    int layoutResourceId;
    List<NewsCard> data = null;
    Picasso picasso;

    public NewsAdapter(Context context, int resource, List<NewsCard> objects, Connector connector) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourceId = resource;
        this.data = objects;
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        picasso = new Picasso.Builder(context).build();
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewGroup _parent = parent;
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View row = inflater.inflate(R.layout.newscard, parent, false);
        final TextView headView = (TextView) row.findViewById(R.id.headtxt);
        headView.setTextColor(Color.parseColor("#1f1d1d"));
        headView.setTextSize(17);
        final TextView bodyView = (TextView) row.findViewById(R.id.newstxt);
        bodyView.setTextColor(Color.parseColor("#0e0c0c"));
        bodyView.setTextSize(14);
        final ImageButton share = (ImageButton) row.findViewById(R.id.share);
        final ImageView imageView = (ImageView) row.findViewById(R.id.imageView2);
        final ImageView dummyImageView = (ImageView) row.findViewById(R.id.dummyImageForCache);
        final ImageView bookmark = (ImageView) row.findViewById(R.id.write);
        TextView sourceView = (TextView) row.findViewById(R.id.newsSource);
        TextView date = (TextView) row.findViewById(R.id.date);

        final NewsCard newsCard = data.get(position);
        if (position % 25 == 0) {
            MixPanelUtils.track("on news : " + newsCard.newsHead);
        }

        headView.setText(newsCard.newsHead);
        bodyView.setText(newsCard.newsBody);
        bodyView.setTextColor(Color.parseColor("#4c3e3e"));
        sourceView.setText(newsCard.newsSourceName);
        date.setText(newsCard.date);
        sourceView.setTextColor(Color.parseColor("#c0392b"));



        setImageIntoView(picasso, imageView, newsCard);

        if (BookmarksFetcher.bookmarkSet.contains(newsCard.id)) {
            bookmark.setImageDrawable(getResources().getDrawable(R.drawable.bookmark));
        } else {
            bookmark.setImageDrawable(getResources().getDrawable(R.drawable.notbookmark));
        }

        row.findViewById(R.id.moreAt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixPanelUtils.track(MixPanelUtils.ON_MORE_AT + newsCard.newsHead);
                String url = data.get(position).newsSourceUrl;
Intent intent = new Intent(getContext(), AzazteWebView.class);
            intent.putExtra("url", url);
              getContext().startActivity(intent);

            }
        });


        sourceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixPanelUtils.track(MixPanelUtils.ON_MORE_AT + newsCard.newsHead);
                String url = data.get(position).newsSourceUrl;
//                Intent intent = new Intent(getContext(), AzazteWebView.class);
//                intent.putExtra("url", url);
//                getContext().startActivity(intent);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MixPanelUtils.track(MixPanelUtils.SHARE + newsCard.newsHead);
                enableShare(row);
                row.buildDrawingCache(true);
                Bitmap drawingCache = row.getDrawingCache(true);
                if (drawingCache == null) {
                    NewsCard newsCard = data.get(position);
                    Intent intent = new Intent();
                    intent.setType("text/plain");
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, newsCard.newsHead + "\n" + newsCard.newsBody);
                    getContext().startActivity(Intent.createChooser(intent, "azazte"));
                    row.destroyDrawingCache();
                    disableShare(row);
                    return;
                }
                final Bitmap bitmap = drawingCache.copy(Bitmap.Config.RGB_565, false);
                row.destroyDrawingCache();
                disableShare(row);
                new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        String imageInMemory = ImageFetcher.storeImageInMemory(newsCard.id, bitmap);
                        return null;
                    }
                }.execute();
                NewsCard newsCard = data.get(position);
                String url = newsCard.id + "_azazte_image.png";
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Daily updates on Tax, Finance, Law & Economy in 50 words!\n" +
                        "Download Android app from : https://goo.gl/BjupvI");
                final File file = context.getFileStreamPath(url);
                Uri uri = Uri.fromFile(file);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //intent.setType("text/plain");
                getContext().startActivity(Intent.createChooser(intent, "azazte"));
            }
        });

        //Onclick listener for setting bookmarks
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixPanelUtils.track(MixPanelUtils.BOOKMARK + newsCard.newsHead);
                if (!BookmarksFetcher.bookmarkSet.contains(newsCard.id)) {

                    bookmark.setImageDrawable(getResources().getDrawable(R.drawable.bookmark));
                    //headView.setTextColor(getContext().getColor(R.color.bookmarked));
                    Snackbar.make(row, "News Bookmarked ", Snackbar.LENGTH_LONG)
                            .show();
                    // cardView.setCardBackgroundColor(000);
                    BookmarksFetcher.bookmarkSet.add(newsCard.id);
                    Connector.connector.setBookmarked(newsCard.id);
                } else {
                    //  cardView.setCardBackgroundColor(255);

                    Snackbar.make(row, "Bookmark removed", Snackbar.LENGTH_LONG)
                            .show();
                    bookmark.setImageDrawable(getResources().getDrawable(R.drawable.notbookmark));
                    Connector.connector.unsetBookmarked(newsCard.id);
                    //headView.setTextColor(getContext().getColor(R.color.colorPrimary));
                    BookmarksFetcher.bookmarkSet.remove(newsCard.id);
                    newsCard.isBookmarked = 0;
                }
                BookmarksFetcher.refresh();
            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onClick(View v) {
                MixPanelUtils.track(MixPanelUtils.BOOKMARK + newsCard.newsHead);
                if (!BookmarksFetcher.bookmarkSet.contains(newsCard.id)) {

                    bookmark.setImageDrawable(getResources().getDrawable(R.drawable.bookmark));
                    //headView.setTextColor(getContext().getColor(R.color.bookmarked));
                    Snackbar.make(row, "News Bookmarked ", Snackbar.LENGTH_LONG)
                            .show();
                    // cardView.setCardBackgroundColor(000);
                    BookmarksFetcher.bookmarkSet.add(newsCard.id);
                    Connector.connector.setBookmarked(newsCard.id);
                } else {
                    //  cardView.setCardBackgroundColor(255);

                    Snackbar.make(row, "Bookmark removed", Snackbar.LENGTH_LONG)
                            .show();
                    bookmark.setImageDrawable(getResources().getDrawable(R.drawable.notbookmark));
                    Connector.connector.unsetBookmarked(newsCard.id);
                    //headView.setTextColor(getContext().getColor(R.color.colorPrimary));
                    BookmarksFetcher.bookmarkSet.remove(newsCard.id);
                    newsCard.isBookmarked = 0;
                }
                BookmarksFetcher.refresh();
            }
        });
        return row;
    }

    private void setImageIntoView(Picasso picasso, ImageView imageView, NewsCard newsCard) {
        if (newsCard.imageUrl != null && !newsCard.imageUrl.isEmpty()) {
            picasso.load(newsCard.imageUrl).noFade().placeholder(R.drawable.placeholder).resize(300, 300).into(imageView);
            //picasso.getSnapshot().dump();
        }
    }

    public void enableShare(View row) {

        //row.findViewById(R.id.header).setVisibility(View.VISIBLE);
        row.findViewById(R.id.playstore).setVisibility(View.VISIBLE);
      //  row.findViewById(R.id.moreAt).setVisibility(View.INVISIBLE);
      //  row.findViewById(R.id.newsSource).setVisibility(View.INVISIBLE);
        row.findViewById(R.id.share).setVisibility(View.INVISIBLE);
        row.findViewById(R.id.write).setVisibility(View.INVISIBLE);
    }

    public void disableShare(View row) {

        //row.findViewById(R.id.header).setVisibility(View.INVISIBLE);
        row.findViewById(R.id.playstore).setVisibility(View.INVISIBLE);
      //  row.findViewById(R.id.moreAt).setVisibility(View.VISIBLE);
      //  row.findViewById(R.id.newsSource).setVisibility(View.VISIBLE);
        row.findViewById(R.id.share).setVisibility(View.VISIBLE);
        row.findViewById(R.id.write).setVisibility(View.VISIBLE);
    }
}