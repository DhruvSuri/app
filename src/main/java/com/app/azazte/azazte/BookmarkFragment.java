package com.app.azazte.azazte;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.azazte.azazte.Fetcher.BookmarksFetcher;
import com.app.azazte.azazte.Utils.Registry.Registry;


public class BookmarkFragment extends Fragment {

    private ListAdapter adapter;

    public BookmarkFragment() {
        adapter = new ListAdapter(this.getParentFragment());
        Registry.getInstance().registerBookmarkAdapter(adapter);
        Registry.getInstance().registerBookmarkFragment(this);
        BookmarksFetcher.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(
                R.id.fragment_list_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        recyclerView.setAdapter(adapter);

        return view;
    }

}