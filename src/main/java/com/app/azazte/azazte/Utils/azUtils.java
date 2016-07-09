package com.app.azazte.azazte.Utils;

import android.app.Fragment;
import android.widget.Adapter;
import android.widget.ImageView;

import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.BookmarkFragment;
import com.app.azazte.azazte.ListAdapter;
import com.app.azazte.azazte.MainActivity;
import com.app.azazte.azazte.R;
import com.app.azazte.azazte.TabFragment;
import com.app.azazte.azazte.Utils.Registry.Registry;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by sprinklr on 13/04/16.
 */
public class azUtils {
    private static Picasso picasso;

    public static Set<String> commaSeparatedStringSet(String commaSeparatedString) {
        Set<String> partsSet = new HashSet<String>();
        if (commaSeparatedString != null) {
            String[] parts = commaSeparatedString.split(",");
            for (String curPart : parts) {
                partsSet.add(curPart.trim());
            }
        }
        return partsSet;
    }

    public Picasso getPicassoInstance() {
        return picasso;
    }

    public static void setPicassoInstance(MainActivity context) {
        azUtils.picasso = new Picasso.Builder(context).build();
    }

    public static void setImageIntoView(ImageView imageView, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            picasso.load(imageUrl).noFade().placeholder(R.drawable.placeholder).into(imageView);
            //picasso.getSnapshot().dump();
        }
    }

    public static void refreshFragment() {
        final Map<Integer, ListAdapter> registeredAdapters = Registry.getInstance().getRegisteredAdapters();
        for (ListAdapter adapter : registeredAdapters.values()) {
            adapter.notifyDataSetChanged();
        }
    }

    public static void refreshBookmarkFragment() {
        final ListAdapter registeredBookmarkAdapter = Registry.getInstance().getRegisteredBookmarkAdapter();
        registeredBookmarkAdapter.notifyDataSetChanged();
    }
}
