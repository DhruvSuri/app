package com.app.azazte.azazte.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.azazte.azazte.Beans.NewsCard;

import com.app.azazte.azazte.NewUI;
import com.app.azazte.azazte.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class NewscardFragment extends Fragment {

    NewsCard newsCard;

    Animation slideup;

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
        slideup = AnimationUtils.loadAnimation(getContext(),
                R.anim.slideup);
        TextView newshead = (TextView) inflate.findViewById(R.id.headtxt);
        TextView newstxt = (TextView) inflate.findViewById(R.id.newstxt);
        TextView newsSource = (TextView) inflate.findViewById(R.id.newsSource);
        TextView date = (TextView) inflate.findViewById(R.id.date);
        TextView author = (TextView) inflate.findViewById(R.id.author);
        ImageView option = (ImageView) inflate.findViewById(R.id.options);
        ImageView image = (ImageView) inflate.findViewById(R.id.imageView2);

        setImageIntoView(picasso, image, newsCard.imageUrl);
        newshead.setText(newsCard.newsHead);
        newstxt.setText(newsCard.newsBody);
        newsSource.setText(newsCard.newsSourceName);
        date.setText(newsCard.date);
        author.setText(newsCard.author);


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
        RelativeLayout tray = (RelativeLayout) dialog.findViewById(R.id.overlay_layout);

        tray.startAnimation(slideup);


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
