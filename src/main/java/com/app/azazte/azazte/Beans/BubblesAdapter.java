package com.app.azazte.azazte.Beans;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.app.azazte.azazte.R;
import com.app.azazte.azazte.Utils.BlurBuilder;

import java.util.List;

import static xdroid.core.Global.getResources;


/**
 * Created by Mac on 12/10/16.
 */
public class BubblesAdapter extends RecyclerView.Adapter<BubblesAdapter.MyViewHolder> {

    private View inflate;
    private List<String> tokens;
    String channel;
    Context context;
    Activity activity;
    String id;
    LayoutInflater inflater;
    private static String DASH = "-";
    private static String REBRANDLY_DOMAIN = "https://www.rebrand.ly/finup-";


    public BubblesAdapter(List<String> tokens, String id, Context context, FragmentActivity activity, View inflate) {
        this.tokens = tokens;
        this.context = context;
        this.id = id;
        this.activity = activity;
        this.inflate = inflate;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.bubble_icon, parent, false);

        MyViewHolder holder = new MyViewHolder(itemView);


        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        channel = tokens.get(position);
        int drawable = getResources().getIdentifier(channel.toLowerCase(), "drawable", context.getPackageName());
        holder.bubbles.setBackgroundResource(drawable);

//        switch (channel) {
//
//            case "QUORA":
//                holder.bubbles.setBackgroundResource(R.drawable.quora);
//                break;
//            case "YOUTUBE":
//                holder.bubbles.setBackgroundResource(R.drawable.youtube);
//                break;
//            case "TWITTER":
//                holder.bubbles.setBackgroundResource(R.drawable.twitter);
//                break;
//            case "WIKI":
//                holder.bubbles.setBackgroundResource(R.drawable.wiki);
//                break;
//            default:
//                holder.bubbles.setBackgroundResource(R.drawable.info);
//        }


    }

    @Override
    public int getItemCount() {
        return tokens.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageButton bubbles;

        public MyViewHolder(View itemView) {
            super(itemView);

            bubbles = (ImageButton) itemView.findViewById(R.id.bubbleIcon);
            bubbles.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            showquestiondialog(REBRANDLY_DOMAIN + tokens.get(getPosition()).toUpperCase() + DASH + id);

        }
    }


    public void showquestiondialog(String url) {
        Bitmap map = takeScreenShot(activity);
        Bitmap blurmap = BlurBuilder.blur(activity, map);
        final Drawable draw = new BitmapDrawable(getResources(), blurmap);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.questiondialog);
        final FrameLayout blur = (FrameLayout) inflate.findViewById(R.id.blurFrame);
        RelativeLayout webLayout = (RelativeLayout) dialog.findViewById(R.id.webLayout);
        RelativeLayout parent = (RelativeLayout) dialog.findViewById(R.id.parent);
        RelativeLayout qnaLayout = (RelativeLayout) dialog.findViewById(R.id.qnaLayout);


        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blur.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                dialog.dismiss();
            }
        });

        setUpWebview(dialog, url);
        webLayout.setVisibility(View.VISIBLE);
        dialog.show();
        blur.setBackground(draw);
        // parent.setBackground(draw);
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

    private static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        Bitmap blur = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return blur;
    }


}