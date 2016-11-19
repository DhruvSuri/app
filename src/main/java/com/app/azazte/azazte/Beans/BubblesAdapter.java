package com.app.azazte.azazte.Beans;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

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
import com.app.azazte.azazte.Utils.AzazteUtils;
import com.app.azazte.azazte.Utils.BlurBuilder;
import com.app.azazte.azazte.Utils.MixPanelUtils;

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
    WebView webView;

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
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        AzazteUtils.getInstance().setImageIntoView(this.context, holder.bubble, AzazteUtils.getInstance().getBubbleChannelURL(tokens.get(position).toUpperCase()), R.drawable.bubbleplaceholder);

    }

    @Override
    public int getItemCount() {
        return tokens.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageButton bubble;

        public MyViewHolder(View itemView) {
            super(itemView);
            bubble = (ImageButton) itemView.findViewById(R.id.bubbleIcon);
            bubble.getLayoutParams().height = AzazteUtils.getInstance().getBubbleHeight();
            bubble.getLayoutParams().width = AzazteUtils.getInstance().getBubbleWidth();
            bubble.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MixPanelUtils.track("BUBBLE");
            MixPanelUtils.track("BUBBLE" + tokens.get(getPosition()).toUpperCase());
            String url = AzazteUtils.getInstance().getBubbleLinkURL(id, tokens.get(getPosition()).toUpperCase());
            customTab(url);

            //showquestiondialog(AzazteUtils.getInstance().getBubbleLinkURL(id, tokens.get(getPosition()).toUpperCase()));
        }
    }

    private void customTab(String url) {


        //CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
// set toolbar color and/or setting custom actions before invoking build()
// Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
      //  CustomTabsIntent customTabsIntent = builder.build();
        // and launch the desired Url with CustomTabsIntent.launchUrl()
       // customTabsIntent.launchUrl(activity, Uri.parse(url));
    }



    public void showquestiondialog(String url) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.questiondialog);
        // setUpWebview(dialog, url);



        Bitmap map = takeScreenShot(activity);
        Bitmap blurmap = BlurBuilder.blur(activity, map);
        final Drawable draw = new BitmapDrawable(getResources(), blurmap);


        final FrameLayout blur = (FrameLayout) inflate.findViewById(R.id.blurFrame);
        RelativeLayout webLayout = (RelativeLayout) dialog.findViewById(R.id.webLayout);
        RelativeLayout parent = (RelativeLayout) dialog.findViewById(R.id.parent);
        RelativeLayout qnaLayout = (RelativeLayout) dialog.findViewById(R.id.qnaLayout);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                blur.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                webView.clearHistory();
                webView.destroy();
            }
        });


        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blur.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                webView.clearHistory();
                webView.destroy();
                dialog.dismiss();
            }
        });


        webLayout.setVisibility(View.VISIBLE);
        dialog.show();
        blur.setBackground(draw);
        // parent.setBackground(draw);
    }


    private void setUpWebview(Dialog dialog, String url) {

        webView = (WebView) dialog.findViewById(R.id.webView);
        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress);
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            // chromium, enable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
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
