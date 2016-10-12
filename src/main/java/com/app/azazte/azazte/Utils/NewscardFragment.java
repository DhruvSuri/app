package com.app.azazte.azazte.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.azazte.azazte.AzazteWebView;
import com.app.azazte.azazte.Beans.Bubble;
import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Event.BubbleEvent;
import com.app.azazte.azazte.NewUI;
import com.app.azazte.azazte.R;
import com.app.azazte.azazte.Utils.Api.ApiExecutor;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import xdroid.toaster.Toaster;

public class NewscardFragment extends Fragment {

    private static final String TWITTER = "TWITTER";
    NewsCard newsCard;

    ImageButton shareButton;
    RelativeLayout brand;
    private View inflateHolder;
    private static String DASH = "-";
    private static String REBRANDLY_DOMAIN = "https://www.rebrand.ly/finup-";


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
        ApiExecutor.getInstance().fetchBubbles(newsCard.id);
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

        try {
            setImageDisplayHeight(image, null);
        } catch (Exception ignored) {
        }

        RelativeLayout header = (RelativeLayout) inflate.findViewById(R.id.header);
        final TextView impactText = (TextView) inflate.findViewById(R.id.impact);
        final ImageView bookmarkView = (ImageView) inflate.findViewById(R.id.bookmark);
        shareButton = (ImageButton) inflate.findViewById(R.id.shareNews);
        brand = (RelativeLayout) inflate.findViewById(R.id.brand);
        View impactLayout = inflate.findViewById(R.id.linearLayout13);
        MixPanelUtils.trackNews(newsCard.newsHead.trim());
        setImageIntoView(picasso, image, newsCard.imageUrl);
        newshead.setText(newsCard.newsHead.trim());
        newstxt.setText(newsCard.newsBody.trim());
        newsSource.setText(newsCard.newsSourceName.trim());
        date.setText(newsCard.date.trim());
        author.setText(newsCard.author.trim());
        if (newsCard.impactLabel != null) {
            impactLabel.setText(newsCard.impactLabel);
        }
        if (newsCard.impact.isEmpty()) {
            hideImpact(impactLayout, impactText);
        } else {
            impactText.setText(newsCard.impact.trim());
        }

        if (newsCard.isBookmarked == 1) {
            bookmarkView.setVisibility(View.VISIBLE);
        } else {
            bookmarkView.setVisibility(View.INVISIBLE);
        }

        hideBrand();


        impactTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                impactMargin.setVisibility(View.VISIBLE);
                summaryMargin.setVisibility(View.GONE);
                summaryTab.setBackgroundColor(Color.parseColor("#87d8fd"));
                impactTab.setBackgroundColor(Color.parseColor("#33b6f3"));
                // newstxt.animate().alpha(1.0f).setDuration(500);
                newstxt.setText(newsCard.impact.trim());


            }
        });


        summaryTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                impactMargin.setVisibility(View.GONE);
                summaryMargin.setVisibility(View.VISIBLE);
                summaryTab.setBackgroundColor(Color.parseColor("#33b6f3"));
                impactTab.setBackgroundColor(Color.parseColor("#87d8fd"));
                //   newstxt.animate().alpha(0.0f).setDuration(500);
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

        impactText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewUI) getActivity()).showTopBar();

            }
        });

        impactLayout.setOnClickListener(new View.OnClickListener() {
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
        Button twitter = (Button) inflate.findViewById(R.id.twitter);
        int baseId = R.id.twitter;
        ArrayList<Bubble> bubbles = Connector.getInstance().getBubbles(newsCard.id);
        if (bubbles.size() == 0) {
            return;
        }
        String answer = bubbles.get(0).getAnswer();
        StringTokenizer tokenizer = new StringTokenizer(answer, ",");
        List<String> tokens = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
//            if (token.equals(TWITTER)) {
//                continue;
//            }
            tokens.add(token);
        }

        for (String channel : tokens) {
            baseId = setupButton(channel, inflate, baseId);
        }
    }

    private int setupButton(final String channel, View inflate, int baseId) {
        Button button = new Button(this.getContext());
        int drawable = this.getResources().getIdentifier(channel.toLowerCase(), "drawable", this.getContext().getPackageName());
        button.setBackground(this.getResources().getDrawable(drawable));
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (baseId != 0){
            params.addRule(RelativeLayout.RIGHT_OF, baseId);
        }
        params.width = 200;
        params.height = 200;
        params.setMargins(0, 0, 50, 0);
        button.setLayoutParams(params);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showquestiondialog(REBRANDLY_DOMAIN + channel.toUpperCase() + DASH + newsCard.id);
            }
        });
        ((RelativeLayout) inflate.findViewById(R.id.bubbleRelativeLayout)).addView(button);
        return button.getId();
    }

    private int intializeTwitter(View inflate) {
        Button twitter = (Button) inflate.findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showquestiondialog(REBRANDLY_DOMAIN + "twitter" + DASH + newsCard.id);
            }
        });
        return twitter.getId();
    }

    private void setupbubblelistners(View inflate, final List<Bubble> bubbleList) {
//        final Button q6 = (Button) inflate.findViewById(R.id.q6); // Youtube
//        final Button q7 = (Button) inflate.findViewById(R.id.q7); // Twitter
//        Button twitterCopy = new Button(this.getContext());
//        int drawable = this.getResources().getIdentifier("twitter", "drawable", this.getContext().getPackageName());
//        twitterCopy.setBackground(this.getResources().getDrawable(drawable));
//        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.RIGHT_OF, R.id.q9);
//        params.width = 60;
//        params.height = 60;
//        twitterCopy.setLayoutParams(params);
//        ((RelativeLayout) inflate.findViewById(R.id.bubbleRelativeLayout)).addView(twitterCopy);


//        final Button q8 = (Button) inflate.findViewById(R.id.q8); // quora
//        final Button q9 = (Button) inflate.findViewById(R.id.q9); // WIKI
//        final Button q10 = (Button) inflate.findViewById(R.id.q10); // Linkedin
//        if (bubbleList.size() == 0) {
//            q6.setVisibility(View.INVISIBLE);
//            q7.setVisibility(View.INVISIBLE);
//            q8.setVisibility(View.INVISIBLE);
//            q9.setVisibility(View.INVISIBLE);
//            q10.setVisibility(View.INVISIBLE);
//            return;
//        }
//
//        Bubble bubble = bubbleList.get(0);
//        String list = bubble.getAnswer();
//        if (!list.contains("youtube")) {
//            q6.setVisibility(View.INVISIBLE);
//        }
//        if (!list.contains("twitter")) {
//            q7.setVisibility(View.INVISIBLE);
//        }
//        if (!list.contains("quora")) {
//            q8.setVisibility(View.INVISIBLE);
//        }
//        if (!list.contains("WIKI")) {
//            q9.setVisibility(View.INVISIBLE);
//        }
//        if (!list.contains("linkedin")) {
//            q10.setVisibility(View.INVISIBLE);
//        }
//
//        q6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showquestiondialog("Why such valuation", "Profitable business with $230 million in revenue in 2015", REBRANDLY_DOMAIN + "youtube" + DASH + newsCard.id, null);
//            }
//        });
//
//        q7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showquestiondialog("Why bought", "Chinese ad-tech space lags behind US counterparts", REBRANDLY_DOMAIN + "twitter" + DASH + newsCard.id, null);
//            }
//        });
//
//        q8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showquestiondialog("Startup billionaires", "Flipkart ,Sanpdeal ,Infosys ,Paytm and Ola founders", REBRANDLY_DOMAIN + "quora" + DASH + newsCard.id, null);
//            }
//        });
//
//        q9.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showquestiondialog("Headquaters", "Global - Dubai.US - New York", REBRANDLY_DOMAIN + "WIKI" + DASH + newsCard.id, null);
//            }
//        });
//
//        q10.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showquestiondialog("Headquaters", "Global - Dubai.US - New York", REBRANDLY_DOMAIN + "linkedin" + DASH + newsCard.id, null);
//            }
//        });

    }


    public String getBubbleResponse(String sourceString) {
        ArrayList<Bubble> source = Connector.getInstance().getBubble(newsCard.id, sourceString);
        if (source.size() == 0) {
            return "finup.in";
        }
        return source.get(0).getAnswer();
    }

    private void fillBubblesDummyData(List<Bubble> bubbleList) {
        bubbleList.add(new Bubble());
        bubbleList.add(new Bubble());
        bubbleList.add(new Bubble());
        bubbleList.add(new Bubble());
        bubbleList.add(new Bubble());
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

    public void showquestiondialog(String url) {
        final Dialog dialog = new Dialog(getContext(), R.style.dialog);
        dialog.setContentView(R.layout.questiondialog);
        RelativeLayout webLayout = (RelativeLayout) dialog.findViewById(R.id.webLayout);
        RelativeLayout parent = (RelativeLayout) dialog.findViewById(R.id.parent);
        RelativeLayout qnaLayout = (RelativeLayout) dialog.findViewById(R.id.qnaLayout);

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        setUpWebview(dialog, url);
        webLayout.setVisibility(View.VISIBLE);
        dialog.show();
    }


    private void setUpWebview(Dialog dialog, String url) {

        WebView webView = (WebView) dialog.findViewById(R.id.webView);
        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                System.out.print("");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }


        });
    }

    private void setImageIntoView(Picasso picasso, ImageView imageView, String imageUrl) {


        Glide.with(this).load(imageUrl).into(imageView);

//        if (imageUrl != null && !imageUrl.isEmpty()) {
//            if (PrefManager.getInstance().getImageState().equals(PrefManager.IMAGE_STATE_OFF)) {
//                imageView.setImageResource(R.drawable.placeholder);
//            } else {
//                Picasso.with(imageView.getContext().getApplicationContext())
//                        .load(imageUrl)
//                        .placeholder(R.drawable.placeholder)
//                        .config(Bitmap.Config.RGB_565)
//                        .resize(250, 350)
//                        .into(imageView);
//            }

        //picasso.load(imageUrl).noFade().placeholder(R.drawable.placeholder).resize(300, 300).into(imageView);
        //picasso.getSnapshot().dump();
        //}
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
