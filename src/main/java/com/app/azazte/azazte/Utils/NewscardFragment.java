package com.app.azazte.azazte.Utils;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import static xdroid.core.Global.getContext;
import static xdroid.core.Global.getResources;

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
        View inflate = inflater.inflate(R.layout.fragment_newscard, container, false);
        View trayIcons = inflater.inflate(R.layout.option_dialog, container, false);
        slideup = AnimationUtils.loadAnimation(getContext(),
                R.anim.slideup);
        slidedown = AnimationUtils.loadAnimation(getContext(),
                R.anim.slidedown);
        TextView newshead = (TextView) inflate.findViewById(R.id.headtxt);
        TextView newstxt = (TextView) inflate.findViewById(R.id.newstxt);
        TextView newsSource = (TextView) inflate.findViewById(R.id.newsSource);
        TextView date = (TextView) inflate.findViewById(R.id.date);
        TextView moreAt = (TextView) inflate.findViewById(R.id.moreAt);
        TextView author = (TextView) inflate.findViewById(R.id.author);
        ImageButton option = (ImageButton) inflate.findViewById(R.id.options);
        ImageView image = (ImageView) inflate.findViewById(R.id.imageView2);
        final ImageView bookmark = (ImageView) inflate.findViewById(R.id.bookmark);
        RelativeLayout header = (RelativeLayout) inflate.findViewById(R.id.header);

        //option tray buttons
        ImageButton share =(ImageButton) trayIcons.findViewById(R.id.share);
        ImageButton whatsapp =(ImageButton) trayIcons.findViewById(R.id.whatsapp);
        ImageButton facebook =(ImageButton) trayIcons.findViewById(R.id.facebook);
        ImageButton twiter =(ImageButton) trayIcons.findViewById(R.id.twiter);

        setImageIntoView(picasso, image, newsCard.imageUrl);
        newshead.setText(newsCard.newsHead);
        newstxt.setText(newsCard.newsBody);
        newsSource.setText(newsCard.newsSourceName);
        date.setText(newsCard.date);
        author.setText(newsCard.author);



        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((NewUI) getActivity()).showTopBar();

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


        bookmark.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onClick(View v) {
                MixPanelUtils.track(MixPanelUtils.BOOKMARK + newsCard.newsHead);


                if (!BookmarksFetcher.bookmarkSet.contains(newsCard.id)) {

                    bookmark.setImageDrawable(getResources().getDrawable(R.drawable.bookmark));


                    BookmarksFetcher.bookmarkSet.add(newsCard.id);
                    Connector.connector.setBookmarked(newsCard.id);
                } else {
                    //  cardView.setCardBackgroundColor(255);


                    bookmark.setImageDrawable(getResources().getDrawable(R.drawable.notbookmark));
                    Connector.connector.unsetBookmarked(newsCard.id);
                    //headView.setTextColor(getContext().getColor(R.color.colorPrimary));
                    BookmarksFetcher.bookmarkSet.remove(newsCard.id);
                    newsCard.isBookmarked = 0;
                }
               // BookmarksFetcher.refresh();
            }
        });




        newstxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewUI) getActivity()).showTopBar();

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
     * <p/>
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
