
package com.app.azazte.azazte;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Fetcher.BookmarksFetcher;
import com.app.azazte.azazte.Fetcher.ImageFetcher;
import com.app.azazte.azazte.Utils.MixPanelUtils;
import com.app.azazte.azazte.Utils.azUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xdroid.toaster.Toaster;

import static xdroid.core.Global.getContext;
import static xdroid.core.Global.getResources;



public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private final Fragment tabFragment;
    List<NewsCard> newsCardList;

    public ListAdapter(Fragment tabFragment) {
        newsCardList = new ArrayList<>();
        this.tabFragment = tabFragment;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,
                viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final NewsCard newsCard = newsCardList.get(i);
        adapt(myViewHolder, newsCard);
    }

    private void adapt(final MyViewHolder myViewHolder, final NewsCard newsCard) {
        myViewHolder.newsSourceView.setText(newsCard.newsSourceName);
        myViewHolder.newsHeadingView.setText(newsCard.newsHead);
        myViewHolder.newsHeadingView.setTextColor(Color.parseColor("#1f1d1d"));
        myViewHolder.newsHeadingView.setTextSize(17);
        myViewHolder.newsBodyView.setText(newsCard.newsBody);
        myViewHolder.newsBodyView.setTextColor(Color.parseColor("#4c3e3e"));
        myViewHolder.newsBodyView.setTextSize(14);
        myViewHolder.dateView.setText(newsCard.date);
        myViewHolder.dateView.setTextColor(Color.parseColor("white"));

        if (!newsCard.author.isEmpty()) {
            myViewHolder.author.setText("By " + newsCard.author);
        } else {
            myViewHolder.author.setText("By admin");
        }
        try {
            if (!newsCard.impact.isEmpty() && !newsCard.sentiment.isEmpty()) {
                myViewHolder.impactText.setText(newsCard.impact);
                final Integer sentiment = Integer.valueOf(newsCard.sentiment);
                myViewHolder.itemView.findViewById(R.id.impact_box).setVisibility(View.VISIBLE);
                if (sentiment == -1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        myViewHolder.impactText.setBackground(ContextCompat.getDrawable(this.tabFragment.getContext(), R.drawable.greenbar));
                    } else {
                        myViewHolder.impactText.setBackgroundDrawable(ContextCompat.getDrawable(this.tabFragment.getContext(), R.drawable.greenbar));
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        myViewHolder.impactText.setBackground(ContextCompat.getDrawable(this.tabFragment.getContext(), R.drawable.redbar));
                    } else {
                        myViewHolder.impactText.setBackgroundDrawable(ContextCompat.getDrawable(this.tabFragment.getContext(), R.drawable.redbar));
                    }
                }
            } else {
                myViewHolder.itemView.findViewById(R.id.impact_box).setVisibility(View.GONE);
            }
        } catch (Exception ignored) {
            Log.d("Excep", "");
        }


        azUtils.setImageIntoView(myViewHolder.newsImage, newsCard.imageUrl);




        final ImageView bookmark = myViewHolder.bookmark;

        final View itemView = myViewHolder.itemView;

        if (BookmarksFetcher.bookmarkSet.contains(newsCard.id)) {
            bookmark.setImageDrawable(getResources().getDrawable(R.drawable.bookmark));
        } else {
            bookmark.setImageDrawable(getResources().getDrawable(R.drawable.notbookmark));
        }


        myViewHolder.moreAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MixPanelUtils.track(MixPanelUtils.ON_MORE_AT + newsCard.newsHead);
                String url = newsCard.newsSourceUrl;
                Intent intent = new Intent(getContext(), AzazteWebView.class);
                intent.putExtra("url", url);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);


            }
        });

        myViewHolder.newsSourceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixPanelUtils.track(MixPanelUtils.ON_MORE_AT + newsCard.newsHead);
                String url = newsCard.newsSourceUrl;
                Intent intent = new Intent(getContext(), AzazteWebView.class);
                intent.putExtra("url", url);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);

            }
        });


        bookmark.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onClick(View v) {
                MixPanelUtils.track(MixPanelUtils.BOOKMARK + newsCard.newsHead);


                if (!BookmarksFetcher.bookmarkSet.contains(newsCard.id)) {

                    bookmark.setImageDrawable(getResources().getDrawable(R.drawable.bookmark));
                    Snackbar.make(itemView, "News Bookmarked ", Snackbar.LENGTH_LONG)
                            .show();


                    BookmarksFetcher.bookmarkSet.add(newsCard.id);
                    Connector.connector.setBookmarked(newsCard.id);
                } else {
                    //  cardView.setCardBackgroundColor(255);

                    Snackbar.make(itemView, "Bookmark removed", Snackbar.LENGTH_LONG)
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


        myViewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixPanelUtils.track(MixPanelUtils.COMMENT);
                String deepNewsid = String.valueOf(newsCard.id);
             //   Intent i = new Intent(tabFragment.getContext(), DeepNewsCardActivity.class);
             //   i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
               // i.putExtra("id", deepNewsid);
                //tabFragment.startActivity(i);
            }
        });

        myViewHolder.discuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixPanelUtils.track(MixPanelUtils.COMMENT);
                String deepNewsid = String.valueOf(newsCard.id);
              //  Intent i = new Intent(tabFragment.getContext(), DeepNewsCardActivity.class);
               // i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
               // i.putExtra("id", deepNewsid);
               // tabFragment.startActivity(i);
            }
        });

        myViewHolder.heart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MixPanelUtils.track(MixPanelUtils.HEART);
                Toaster.toast("Coming Soon");

            }
        });


       myViewHolder.share.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               MixPanelUtils.track(MixPanelUtils.SHARE + newsCard.newsHead);
                enableShare(itemView);
                itemView.buildDrawingCache(true);
                Bitmap drawingCache = itemView.getDrawingCache(true);
                if (drawingCache == null) {
                    Intent intent = new Intent();
                    intent.setType("text/plain");
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, newsCard.newsHead + "\n" + newsCard.newsBody);
                    getContext().startActivity(Intent.createChooser(intent, "azazte"));
                    itemView.destroyDrawingCache();
                    disableShare(itemView);
                    return;
                }
                final Bitmap bitmap = drawingCache.copy(Bitmap.Config.RGB_565, false);
                itemView.destroyDrawingCache();
                disableShare(itemView);
                new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        String imageInMemory = ImageFetcher.storeImageInMemory(newsCard.id, bitmap);
                        return null;
                    }
                }.execute();
                String url = newsCard.id + "_azazte_image.png";
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Daily updates on Economy, Finance, Business, Tax & Law in 70 words!\n" +
                        "Download app: https://goo.gl/BjupvI");
                final File file = getContext().getFileStreamPath(url);
                Uri uri = Uri.fromFile(file);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //intent.setType("text/plain");
                tabFragment.startActivity(Intent.createChooser(intent, "azazte"));
            }
        });
    }


    @Override
    public int getItemCount() {
        return newsCardList == null ? 0 : newsCardList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView newsSourceView;
        TextView newsBodyView;
        TextView newsHeadingView;
        TextView dateView;
        ImageView share;
        ImageView newsImage;
        ImageView bookmark;
        View itemView;
        TextView moreAt;
        TextView impactText;
        ImageView heart;
        ImageView comment;
        TextView discuss;
        TextView author;


        public MyViewHolder(View itemView) {
                  super(itemView);
                  this.itemView = itemView;
                  newsSourceView = (TextView) itemView.findViewById(R.id.newsSource);
                  newsBodyView = (TextView) itemView.findViewById(R.id.newstxt);
                  newsHeadingView = (TextView) itemView.findViewById(R.id.headtxt);
                  dateView = (TextView) itemView.findViewById(R.id.date);
                  share = (ImageView) itemView.findViewById(R.id.share);
                  newsImage = (ImageView) itemView.findViewById(R.id.imageView2);
                  bookmark = (ImageView) itemView.findViewById(R.id.bookmark);
                  moreAt = (TextView) itemView.findViewById(R.id.moreAt);
                  impactText = (TextView) itemView.findViewById(R.id.impactTxt);
                  discuss = (TextView) itemView.findViewById(R.id.discuss);
                  heart = (ImageView) itemView.findViewById(R.id.heart);
                  comment = (ImageView) itemView.findViewById(R.id.comment);
                  author = (TextView) itemView.findViewById(R.id.author);
        }
    }

    public List<NewsCard> getNewsCardList() {
        return newsCardList;
    }

    public void setNewsCardList(List<NewsCard> newsCardList) {
        this.newsCardList = newsCardList;
    }


    public void enableShare(View row) {

        //row.findViewById(R.id.header).setVisibility(View.VISIBLE);
        row.findViewById(R.id.playstore).setVisibility(View.VISIBLE);
        //  row.findViewById(R.id.moreAt).setVisibility(View.INVISIBLE);
        //  row.findViewById(R.id.newsSource).setVisibility(View.INVISIBLE);
        row.findViewById(R.id.share).setVisibility(View.INVISIBLE);
        row.findViewById(R.id.bookmark).setVisibility(View.INVISIBLE);
        row.findViewById(R.id.discuss).setVisibility(View.INVISIBLE);
        row.findViewById(R.id.comment).setVisibility(View.INVISIBLE);
        row.findViewById(R.id.heart).setVisibility(View.INVISIBLE);
    }

    public void disableShare(View row) {

        //row.findViewById(R.id.header).setVisibility(View.INVISIBLE);
        row.findViewById(R.id.playstore).setVisibility(View.INVISIBLE);
        //  row.findViewById(R.id.moreAt).setVisibility(View.VISIBLE);
        //  row.findViewById(R.id.newsSource).setVisibility(View.VISIBLE);
        row.findViewById(R.id.share).setVisibility(View.VISIBLE);
        row.findViewById(R.id.bookmark).setVisibility(View.VISIBLE);
        row.findViewById(R.id.discuss).setVisibility(View.VISIBLE);
        row.findViewById(R.id.comment).setVisibility(View.VISIBLE);
        row.findViewById(R.id.heart).setVisibility(View.VISIBLE);
    }
}
