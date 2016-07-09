package com.app.azazte.azazte.Utils.Registry;

import android.app.Fragment;
import android.util.ArraySet;

import com.app.azazte.azazte.BookmarkFragment;
import com.app.azazte.azazte.ListAdapter;
import com.app.azazte.azazte.TabFragment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sprinklr on 28/05/16.
 */
public class Registry {
    Map<Integer, ListAdapter> registeredAdapters = new HashMap<>();
    ListAdapter bookmarkAdapter;
    private static Registry adapterRegistry;
    Set<TabFragment> tabFragments = new HashSet<>();
    BookmarkFragment bookmarkFragment;

    public static Registry getInstance() {
        if (adapterRegistry == null) {
            adapterRegistry = new Registry();
        }
        return adapterRegistry;
    }

    private Registry() {

    }

    public void registerAdapter(ListAdapter adapter, int category) {
        registeredAdapters.put(category, adapter);
    }

    public void registerBookmarkAdapter(ListAdapter bookmarkAdapter){
        this.bookmarkAdapter = bookmarkAdapter;
    }

    public ListAdapter getRegisteredBookmarkAdapter(){
        return this.bookmarkAdapter;
    }

    public Map<Integer, ListAdapter> getRegisteredAdapters() {
        return registeredAdapters;
    }

    public void setRegisteredAdapters(Map<Integer, ListAdapter> registeredAdapters) {
        this.registeredAdapters = registeredAdapters;
    }

    public void registerTabFragment(TabFragment fragment){
        tabFragments.add(fragment);
    }


    public Set<TabFragment> getRegisteredTabFragments() {
        return tabFragments;
    }

    public void registerBookmarkFragment(BookmarkFragment fragment){
        bookmarkFragment = fragment;
    }

    public BookmarkFragment getRegisteredBookmarkFragment() {
        return bookmarkFragment;
    }
}
