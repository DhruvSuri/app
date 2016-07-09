package com.app.azazte.azazte;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.azazte.azazte.Utils.Api.ApiExecutor;
import com.app.azazte.azazte.Utils.Registry.Registry;

/**
 * Created by @vitovalov on 30/9/15.
 */
public class TabFragment extends Fragment {

    private ListAdapter adapter;


    public TabFragment(int category) {
        adapter = new ListAdapter(this);
        Registry.getInstance().registerAdapter(adapter, category);
        Registry.getInstance().registerTabFragment(this);
    }

    public TabFragment() {

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


        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        // Setup refresh listener which triggers new data loading
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiExecutor.getInstance().getNews(null, swipe);
                Toast.makeText(getActivity(), "Fetching News", Toast.LENGTH_SHORT).show();
            }
        });
        // Configure the refreshing colors
        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;

    }
}
