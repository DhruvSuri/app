package com.app.azazte.azazte.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.azazte.azazte.NewUI;
import com.app.azazte.azazte.R;

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
        View inflate = inflater.inflate(layout, null);
        View readMoreButton = (Button) inflate.findViewById(R.id.readMore);
        readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewUI.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        return inflate;
    }
}
