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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.azazte.azazte.AzazteWebView;
import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.NewUI;
import com.app.azazte.azazte.PrefManager;
import com.app.azazte.azazte.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import xdroid.toaster.Toaster;

public class NewscardFragment extends Fragment {

    NewsCard newsCard;

    Animation slideup;
    Animation slidedown;
    ImageButton shareButton;
    RelativeLayout brand;


    private OnFragmentInteractionListener mListener;
    Picasso picasso;

    public NewscardFragment(NewsCard newsCard, Context applicationContext) {
        this.newsCard = newsCard;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View inflate = inflater.inflate(R.layout.fragment_newscard, null);
        slideup = AnimationUtils.loadAnimation(getContext(),
                R.anim.slideup);
        slidedown = AnimationUtils.loadAnimation(getContext(),
                R.anim.slidedown);
        TextView newshead = (TextView) inflate.findViewById(R.id.headtxt);
        final TextView newstxt = (TextView) inflate.findViewById(R.id.newstxt);
        TextView newsSource = (TextView) inflate.findViewById(R.id.newsSource);
        TextView date = (TextView) inflate.findViewById(R.id.dateText);
        TextView moreAt = (TextView) inflate.findViewById(R.id.moreAt);
        TextView author = (TextView) inflate.findViewById(R.id.author);
        TextView impactLabel = (TextView) inflate.findViewById(R.id.textView18);

        final ImageView image = (ImageView) inflate.findViewById(R.id.imageView2);

        try {
            setImageDisplayHeight(image, null);
        } catch (Exception ignored) {
        }

        RelativeLayout header = (RelativeLayout) inflate.findViewById(R.id.header);
        final TextView impactText = (TextView) inflate.findViewById(R.id.impact);
        final View bookmarkView = inflate.findViewById(R.id.bookmarkLine);
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
        if (newsCard.impactLabel != null){
            impactLabel.setText(newsCard.impactLabel);
        }
        if (newsCard.impact.isEmpty()) {
            hideImpact(impactLayout, impactText);
        } else {
            impactText.setText(newsCard.impact.trim());
        }

        if (newsCard.isBookmarked == 1) {
            bookmarkView.setBackgroundColor(Color.parseColor("#2ce3e6"));
        } else {
            bookmarkView.setBackgroundColor(255);
        }

        hideBrand();


//        newstxt.post(new Runnable() {
//            @Override
//            public void run() {
//                final int bodyHeight = newstxt.getMeasuredHeight();
//                if (impactText.getVisibility() == View.INVISIBLE){
//                    setImageDisplayHeight(image, bodyHeight);
//                }
//                impactText.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        int impactHeight = impactText.getMeasuredHeight();
//                        setImageDisplayHeight(image, bodyHeight + impactHeight);
//                    }
//                });
//            }
//        });


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     ((NewUI) getActivity()).showTopBar();
            }
        });

        newshead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newsCard.isBookmarked == 0) {
                    Connector.getInstance().setBookmarked(newsCard.id);
                    newsCard.isBookmarked = 1;
                    bookmarkView.setBackgroundColor(Color.parseColor("#2ce3e6"));
                    Toaster.toast("Added to your Library");
                } else {
                    Connector.getInstance().unsetBookmarked(newsCard.id);
                    newsCard.isBookmarked = 0;
                    bookmarkView.setBackgroundColor(255);
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
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (PrefManager.getInstance().getImageState().equals(PrefManager.IMAGE_STATE_OFF)) {
                imageView.setImageResource(R.drawable.placeholder);
            } else {
                Picasso.with(imageView.getContext().getApplicationContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder)
                        .config(Bitmap.Config.RGB_565)
                        .resize(250, 350)
                        .into(imageView);
            }

            //picasso.load(imageUrl).noFade().placeholder(R.drawable.placeholder).resize(300, 300).into(imageView);
            //picasso.getSnapshot().dump();
        }
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
