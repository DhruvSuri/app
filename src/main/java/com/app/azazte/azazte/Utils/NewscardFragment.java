package com.app.azazte.azazte.Utils;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.azazte.azazte.Beans.NewsCard;

import com.app.azazte.azazte.Database.Connector;

import com.app.azazte.azazte.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewscardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewscardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewscardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public String id = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    NewsCard newsCard;

    Animation slideup;

    private OnFragmentInteractionListener mListener;

    public NewscardFragment() {
        // Required empty public constructor
    }

    public NewscardFragment(NewsCard newsCard) {
        this.newsCard = newsCard;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewscardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewscardFragment newInstance(String param1, String param2) {
        NewscardFragment fragment = new NewscardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_newscard, container, false);
        slideup = AnimationUtils.loadAnimation(getContext()   ,
                R.anim.slidedown);
        TextView newshead = (TextView) inflate.findViewById(R.id.headtxt);
        TextView newstxt = (TextView) inflate.findViewById(R.id.newstxt);
        TextView newsSource = (TextView) inflate.findViewById(R.id.newsSource);
        TextView date = (TextView) inflate.findViewById(R.id.date);
        TextView author = (TextView) inflate.findViewById(R.id.author);
        ImageView option = (ImageView) inflate.findViewById(R.id.options);
        newshead.setText(newsCard.newsHead);
        newstxt.setText(newsCard.newsBody);
        newsSource.setText(newsCard.newsSourceName);
        date.setText(newsCard.date);
        author.setText(newsCard.author);


        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOverLay();
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void showOverLay() {

        final Dialog dialog = new Dialog(getContext(),R.style.DialogAnimation);

        dialog.setContentView(R.layout.option_dialog);
        //   final ImageView help = (ImageView) dialog.findViewById(R.id.help);
        final RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.overlay_layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
