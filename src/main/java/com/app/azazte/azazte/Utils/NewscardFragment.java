package com.app.azazte.azazte.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.azazte.azazte.AzazteWebView;
import com.app.azazte.azazte.Beans.Bubble;
import com.app.azazte.azazte.Beans.BubblesAdapter;
import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Event.BubbleEvent;
import com.app.azazte.azazte.NewUI;
import com.app.azazte.azazte.R;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import io.fabric.sdk.android.Fabric;
import xdroid.toaster.Toaster;

public class NewscardFragment extends Fragment  {

    private static final String TWITTER = "TWITTER";
    public NewsCard newsCard;
    BubblesAdapter myadapter;
    ImageButton shareButton;
    ImageView imageView;
    RelativeLayout newsContent;
    RelativeLayout shareLayout;
    FrameLayout bodyMargin;
    ImageView bookmarkView;
    RelativeLayout brand;
    private View inflateHolder;
    View inflate;
    private OnFragmentInteractionListener mListener;


//    public NewscardFragment(NewsCard newsCard, Context applicationContext) {
//        this.newsCard = newsCard;
//    }

    public NewscardFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imageView.setImageDrawable(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        Fabric.with(this.getContext(), new Crashlytics());

        inflate = inflater.inflate(R.layout.fragment_newscard, null);
        inflateHolder = inflate;
      //  final View shareInflate = inflater.inflate(R.layout.sharelayout, null);

        inflateView(inflate);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               shareLayout.setVisibility(View.VISIBLE);
               shareBitmap(inflate, newsCard.id);
               shareButton.setClickable(false);
               shareLayout.setVisibility(View.INVISIBLE);

            }
        });
        return inflate;
    }



    public void inflateView(final View inflate) {
        TextView newshead = (TextView) inflate.findViewById(R.id.headtxt);
        newsContent = (RelativeLayout) inflate.findViewById(R.id.newsContent);
        shareLayout = (RelativeLayout) inflate.findViewById(R.id.ShareLayout);
        FrameLayout bookmarkFrame = (FrameLayout) inflate.findViewById(R.id.bookmarkFrame);
        final TextView newstxt = (TextView) inflate.findViewById(R.id.newstxt);
      //  final TextView impacttxt = (TextView) inflate.findViewById(R.id.impacttxt);
        final TextView shareNewstxt = (TextView) inflate.findViewById(R.id.shareNewsText);
        TextView newsSource = (TextView) inflate.findViewById(R.id.newsSource);
        TextView date = (TextView) inflate.findViewById(R.id.dateText);
        TextView moreAt = (TextView) inflate.findViewById(R.id.moreAt);
        TextView author = (TextView) inflate.findViewById(R.id.author);
        TextView impactLabel = (TextView) inflate.findViewById(R.id.impactLabel);



        //imapct tabs
        final RelativeLayout tabLayout = (RelativeLayout) inflate.findViewById(R.id.tabLayout);
        final RelativeLayout impactTab = (RelativeLayout) inflate.findViewById(R.id.newsImpactTabs);
        final RelativeLayout summaryTab = (RelativeLayout) inflate.findViewById(R.id.newsSummaryTabs);
        brand = (RelativeLayout) inflate.findViewById(R.id.brand);
        final FrameLayout impactMargin = (FrameLayout) inflate.findViewById(R.id.impactMargin);
        final FrameLayout summaryMargin = (FrameLayout) inflate.findViewById(R.id.summaryMargin);
        bodyMargin = (FrameLayout) inflate.findViewById(R.id.bodyMargin);
        final FrameLayout impactClick = (FrameLayout) inflate.findViewById(R.id.impactClick);
        final FrameLayout summaryClick = (FrameLayout) inflate.findViewById(R.id.summaryClick);
        final View line = (View) inflate.findViewById(R.id.line);
        imageView = (ImageView) inflate.findViewById(R.id.imageView2);
        imageView.getLayoutParams().height = AzazteUtils.getInstance().getImageViewHeight();
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        View bubbleBottomBar = inflate.findViewById(R.id.bottombar);
        bubbleBottomBar.getLayoutParams().height = AzazteUtils.getInstance().getBubbleHeight();
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        RelativeLayout header = (RelativeLayout) inflate.findViewById(R.id.header);
        bookmarkView = (ImageView) inflate.findViewById(R.id.bookmark);
        shareButton = (ImageButton) inflate.findViewById(R.id.shareNews);
        MixPanelUtils.trackNews(newsCard.newsHead.trim());
        setImageIntoView(this.getContext(), imageView, newsCard.imageUrl, R.drawable.placeholder);
        newshead.setText(newsCard.newsHead.trim());
        newstxt.setText(newsCard.newsBody.trim());
        shareNewstxt.setText(newsCard.newsBody.trim());
        newsSource.setText(newsCard.newsSourceName.trim());
      //  impacttxt.setText(newsCard.impact);

        long time = 0;
        if (newsCard.createdTimeEpoch == null) {
            time = new Date(newsCard.date).getTime();
        } else {
            time = Long.valueOf(newsCard.createdTimeEpoch);
        }

        date.setText(DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL));


        if (newsCard.impact.trim().length() != 0) {

            tabLayout.setVisibility(View.VISIBLE);
            // bodyMargin.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
           // impacttxt.setText(newsCard.impact);

        }


        author.setText(newsCard.author.trim());

        if (newsCard.impactLabel != null) {

            impactLabel.setText(newsCard.impactLabel);

        }


        if (newsCard.isBookmarked == 1) {
            bookmarkView.setVisibility(View.VISIBLE);
        } else {
            bookmarkView.setVisibility(View.INVISIBLE);
        }

        //   hideBrand();


        impactClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                impactMargin.setVisibility(View.VISIBLE);
                summaryMargin.setVisibility(View.GONE);
                summaryTab.setBackgroundColor(getResources().getColor(R.color.secondaryTab));
                impactTab.setBackgroundColor(getResources().getColor(R.color.tabColor));
                newstxt.setText(newsCard.impact.trim());
                shareNewstxt.setText(newsCard.impact.trim());


            }
        });


        summaryClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                impactMargin.setVisibility(View.GONE);
                summaryMargin.setVisibility(View.VISIBLE);
                summaryTab.setBackgroundColor(getResources().getColor(R.color.tabColor));
                impactTab.setBackgroundColor(getResources().getColor(R.color.secondaryTab));
                //   impactTab.animate().alpha(0.0f).setDuration(500);
                newstxt.setText(newsCard.newsBody.trim());
                shareNewstxt.setText(newsCard.newsBody.trim());


            }
        });


        bookmarkFrame.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if (newsCard.isBookmarked == 0) {
                        Connector.getInstance().setBookmarked(newsCard.id);
                        newsCard.isBookmarked = 1;
                        bookmarkView.setVisibility(View.VISIBLE);
                        Toaster.toast("Added to your Library");
                    } else {
                        Connector.getInstance().unsetBookmarked(newsCard.id);
                        newsCard.isBookmarked = 0;
                        bookmarkView.setVisibility(View.INVISIBLE);
                        Toaster.toast("Removed from your Library");
                    }


                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });


        moreAt.setOnClickListener(new View.OnClickListener() {
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

        newsSource.setOnClickListener(new View.OnClickListener() {
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

        newstxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewUI) getActivity()).showTopBar();

            }
        });


        setupBubbleListener(inflate);
        //setupbubblelistners(inflate, bubbles);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BubbleEvent event) {
        List<Bubble> bubbleList = event.getBubbleList();
        //setupbubblelistners(inflateHolder, bubbleList);
    }

    public void setImageIntoView(Context context, ImageView imageView, String imageUrl, int placeholder) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(placeholder)
                .into(imageView);
    }

    private void setupBubbleListener(View inflate) {

        ArrayList<Bubble> bubbles = Connector.getInstance().getBubbles(newsCard.id);
        if (bubbles.size() == 0) {
            return;
        }

        String answer = bubbles.get(0).getAnswer();
        StringTokenizer tokenizer = new StringTokenizer(answer, ",");
        List<String> tokens = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            tokens.add(token);
        }

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager sharelayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView bubbles_rv = (RecyclerView) inflate.findViewById(R.id.bubbles_rv);
        RecyclerView share_bubbles_rv = (RecyclerView) inflate.findViewById(R.id.share_bubbles_rv);
        bubbles_rv.setLayoutManager(layoutManager);
        share_bubbles_rv.setLayoutManager(sharelayoutManager);
        myadapter = new BubblesAdapter(tokens, newsCard.id, getContext(), getActivity(), inflate);
        bubbles_rv.setItemAnimator(new DefaultItemAnimator());
        bubbles_rv.setAdapter(myadapter);
        share_bubbles_rv.setAdapter(myadapter);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void shareBitmap(View view, String fileName) {
        try {

            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache(true);
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap( view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.draw(canvas);
           // Bitmap bitmap = view.getDrawingCache();
            File file = new File(getContext().getCacheDir(), fileName + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);

            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.putExtra(Intent.EXTRA_TEXT, "Discover news and related content from your favorite social channels\n" +
                    "Download finup: http://bit.ly/29Gnkgl");
            intent.setType("image/png");

            startActivity(Intent.createChooser(intent, "finup"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideImpact(View impactLayout, TextView impactText) {
        impactLayout.setVisibility(View.INVISIBLE);
        impactText.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        //   bodyMargin.setVisibility(View.VISIBLE);
        shareLayout.setVisibility(View.INVISIBLE);
        shareButton.setClickable(true);
    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
