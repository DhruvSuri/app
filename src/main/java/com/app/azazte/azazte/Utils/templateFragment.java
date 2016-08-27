package com.app.azazte.azazte.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by home on 28/08/16.
 */
public class TemplateFragment extends Fragment {
    public static String LAYOUT = "layout";
    Integer layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        layout = getArguments().getInt(LAYOUT);
        return inflater.inflate(layout, null);
    }
}
