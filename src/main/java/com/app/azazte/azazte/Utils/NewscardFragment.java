package com.app.azazte.azazte.Utils;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.azazte.azazte.AzazteWebView;
import com.app.azazte.azazte.Beans.NewsCard;

import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.Fetcher.BookmarksFetcher;
import com.app.azazte.azazte.NewUI;
import com.app.azazte.azazte.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

public class NewscardFragment extends Fragment {

    NewsCard newsCard;

    Animation slideup;
    Animation slidedown;

    private OnFragmentInteractionListener mListener;
    Picasso picasso;

    public NewscardFragment(NewsCard newsCard, Context applicationContext) {
        this.newsCard = newsCard;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View inflate = inflater.inflate(R.layout.fragment_newscard, container, false);
        View trayIcons = inflater.inflate(R.layout.option_dialog, container, false);
        slideup = AnimationUtils.loadAnimation(getContext(),
                R.anim.slideup);
        slidedown = AnimationUtils.loadAnimation(getContext(),
                R.anim.slidedown);
        TextView newshead = (TextView) inflate.findViewById(R.id.headtxt);
        TextView newstxt = (TextView) inflate.findViewById(R.id.newstxt);
        TextView newsSource = (TextView) inflate.findViewById(R.id.newsSource);
        TextView date = (TextView) inflate.findViewById(R.id.dateText);
        TextView moreAt = (TextView) inflate.findViewById(R.id.moreAt);
        TextView author = (TextView) inflate.findViewById(R.id.author);
        ImageButton option = (ImageButton) inflate.findViewById(R.id.options);
        ImageView image = (ImageView) inflate.findViewById(R.id.imageView2);
        final ImageView bookmark = (ImageView) inflate.findViewById(R.id.write);
        RelativeLayout header = (RelativeLayout) inflate.findViewById(R.id.header);
        TextView impact = (TextView) inflate.findViewById(R.id.impact);
        final View bookmarkView = inflate.findViewById(R.id.bookmarkLine);
        ImageButton shareButton = (ImageButton) inflate.findViewById(R.id.shareNews);

        setImageIntoView(picasso, image, newsCard.imageUrl);
        newshead.setText(newsCard.newsHead);
        newstxt.setText(newsCard.newsBody);
        newsSource.setText(newsCard.newsSourceName);
        date.setText(newsCard.date);
        author.setText(newsCard.author);
        impact.setText(newsCard.impact);
        if (newsCard.isBookmarked == 1) {
            bookmarkView.setBackgroundColor(Color.parseColor("#42DD91"));
        } else {
            bookmarkView.setBackgroundColor(255);
        }

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewUI) getActivity()).showTopBar();
            }
        });

        newshead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newsCard.isBookmarked == 0) {
                    Connector.getInstance().setBookmarked(newsCard.id);
                    newsCard.isBookmarked = 1;
                    bookmarkView.setBackgroundColor(Color.parseColor("#42DD91"));
                } else {
                    Connector.getInstance().unsetBookmarked(newsCard.id);
                    newsCard.isBookmarked = 0;
                    bookmarkView.setBackgroundColor(255);
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

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareBitmap(inflate, newsCard.id);
            }
        });

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsOverLay();
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

    private void shareBitmap(View view, String fileName) {
        try {
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
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent,"finup"));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void showOptionsOverLay() {

        final Dialog dialog = new Dialog(getContext(), R.style.DialogAnimation);

        dialog.setContentView(R.layout.option_dialog);
        final RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.parent);
        final RelativeLayout tray = (RelativeLayout) dialog.findViewById(R.id.overlay_layout);

        tray.startAnimation(slideup);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tray.startAnimation(slidedown);
                tray.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setImageIntoView(Picasso picasso, ImageView imageView, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.with(imageView.getContext().getApplicationContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .config(Bitmap.Config.RGB_565)
                    .into(imageView);
            //picasso.load(imageUrl).noFade().placeholder(R.drawable.placeholder).resize(300, 300).into(imageView);
            //picasso.getSnapshot().dump();
        }
    }


}
