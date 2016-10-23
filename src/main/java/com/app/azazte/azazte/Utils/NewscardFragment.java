package com.app.azazte.azazte.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import xdroid.toaster.Toaster;

public class NewscardFragment extends Fragment {

    private static final String TWITTER = "TWITTER";
    NewsCard newsCard;
    BubblesAdapter myadapter;
    ImageButton shareButton;
    RelativeLayout brand;
    private View inflateHolder;


    private OnFragmentInteractionListener mListener;
    Picasso picasso;

    public NewscardFragment(NewsCard newsCard, Context applicationContext) {
        this.newsCard = newsCard;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View inflate = inflater.inflate(R.layout.fragment_newscard, null);
        inflateHolder = inflate;
        //final View shareInflate = inflater.inflate(R.layout.sharelayout, null);

        inflateView(inflate);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBrand();
                shareBitmap(inflate, newsCard.id);
                shareButton.setClickable(false);
                hideBrand();
            }
        });
        return inflate;
    }

    public void inflateView(final View inflate) {
        TextView newshead = (TextView) inflate.findViewById(R.id.headtxt);

        final TextView newstxt = (TextView) inflate.findViewById(R.id.newstxt);
        TextView newsSource = (TextView) inflate.findViewById(R.id.newsSource);
        TextView date = (TextView) inflate.findViewById(R.id.dateText);
        TextView moreAt = (TextView) inflate.findViewById(R.id.moreAt);
        TextView author = (TextView) inflate.findViewById(R.id.author);
        TextView impactLabel = (TextView) inflate.findViewById(R.id.textView18);

        //imapct tabs

        final RelativeLayout impactTab = (RelativeLayout) inflate.findViewById(R.id.impactTabs);
        final RelativeLayout summaryTab = (RelativeLayout) inflate.findViewById(R.id.summaryTabs);
        final FrameLayout impactMargin = (FrameLayout) inflate.findViewById(R.id.impactMargin);
        final FrameLayout summaryMargin = (FrameLayout) inflate.findViewById(R.id.summaryMargin);


        final ImageView image = (ImageView) inflate.findViewById(R.id.imageView2);
        image.getLayoutParams().height = AzazteUtils.getInstance().getImageViewHeight();
        image.setScaleType(ImageView.ScaleType.FIT_XY);

        View bubbleBottomBar = inflate.findViewById(R.id.bottombar);
        bubbleBottomBar.getLayoutParams().height = AzazteUtils.getInstance().getBubbleHeight();
        image.setScaleType(ImageView.ScaleType.FIT_XY);

        RelativeLayout header = (RelativeLayout) inflate.findViewById(R.id.header);
        final ImageView bookmarkView = (ImageView) inflate.findViewById(R.id.bookmark);
        shareButton = (ImageButton) inflate.findViewById(R.id.shareNews);
        brand = (RelativeLayout) inflate.findViewById(R.id.brand);
        MixPanelUtils.trackNews(newsCard.newsHead.trim());
        setImageIntoView(picasso, image, newsCard.imageUrl);
        newshead.setText(newsCard.newsHead.trim());
        newstxt.setText(newsCard.newsBody.trim());
        newsSource.setText(newsCard.newsSourceName.trim());

        long time = 0;
        if (newsCard.createdTimeEpoch == null) {
            time = new Date(newsCard.date).getTime();
        } else {
            time = Long.valueOf(newsCard.createdTimeEpoch);
        }

        date.setText(DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL));

        author.setText(newsCard.author.trim());
    //  if (newsCard.impactLabel != null) {
    //      impactLabel.setText(newsCard.impactLabel);
    //  }
    //  if (newsCard.impact.isEmpty()) {
    //      hideImpact(impactLayout, impactText);
    //  } else {
    //      impactText.setText(newsCard.impact.trim());
    //  }

        if (newsCard.isBookmarked == 1) {
            bookmarkView.setVisibility(View.VISIBLE);
        } else {
            bookmarkView.setVisibility(View.INVISIBLE);
        }

     //   hideBrand();


        impactTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                impactMargin.setVisibility(View.VISIBLE);
                summaryMargin.setVisibility(View.GONE);
                summaryTab.setBackgroundColor(Color.parseColor("#6ccdf8"));
                impactTab.setBackgroundColor(Color.parseColor("#0091EA"));
                // summaryTab.animate().alpha(1.0f).setDuration(500);
                newstxt.setText(newsCard.impact.trim());


            }
        });


        summaryTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                impactMargin.setVisibility(View.GONE);
                summaryMargin.setVisibility(View.VISIBLE);
                summaryTab.setBackgroundColor(Color.parseColor("#0091EA"));
                impactTab.setBackgroundColor(Color.parseColor("#6ccdf8"));
                //   impactTab.animate().alpha(0.0f).setDuration(500);
                newstxt.animate();
                newstxt.setText(newsCard.newsBody.trim());


            }
        });


        newshead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        RecyclerView bubbles_rv = (RecyclerView) inflate.findViewById(R.id.bubbles_rv);
        bubbles_rv.setLayoutManager(layoutManager);
        myadapter = new BubblesAdapter(tokens, newsCard.id, getContext(), getActivity(), inflate);
        bubbles_rv.setItemAnimator(new DefaultItemAnimator());
        bubbles_rv.setAdapter(myadapter);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void setImageDisplayHeight(ImageView image, Integer textHeight) {
        Display d = ((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;

        //int spaceAvailableForImage = displayHeight - textHeight;
        //image.getLayoutParams().height = (spaceAvailableForImage * 70)/100;

        int ratio = (realHeight - displayHeight) * 100 / realHeight;
        if (ratio > 4) {
            int realImageHeight = image.getLayoutParams().height;
            int reduction = (realImageHeight * ratio) / 100;
            image.getLayoutParams().height = realImageHeight - reduction;
            //Toaster.toast("reduced percentage : " + ratio + " and reduced height " + reduction);
        }
    }

    private void shareBitmap(View view, String fileName) {
        try {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache(true);
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = view.getDrawingCache();
            File file = new File(getContext().getCacheDir(), fileName + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);

            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.putExtra(Intent.EXTRA_TEXT, "Know the impact of business, economy and finance on YOU . All In 30 seconds! ?\n" +
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void setImageIntoView(Picasso picasso, ImageView imageView, String imageUrl) {


        //Glide.with(this).load(imageUrl).into(imageView);
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder2) // can also be a drawable
                .crossFade()
                .into(imageView);
    }


    @Override
    public void onResume() {
        super.onResume();
        shareButton.setClickable(true);
    }

    public void showBrand() {
        shareButton.setVisibility(View.INVISIBLE);
        brand.setVisibility(View.VISIBLE);
    }

    public void hideBrand() {
        shareButton.setVisibility(View.VISIBLE);
        brand.setVisibility(View.INVISIBLE);
    }


}
