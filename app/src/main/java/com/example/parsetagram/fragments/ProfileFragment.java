package com.example.parsetagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.example.parsetagram.R;
import com.example.parsetagram.models.Post;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    public static final String  TAG = "ProfileFragment";
    RecyclerView rvProfile;
    ParseUser userToFilterBy;
    //ProfileAdapter adapter;
    List<Post> allPosts;
    SwipeRefreshLayout swipeContainer;
    //EndlessRecyclerViewScrollListener scrollListener;
    public ProfileFragment() {
        // Required empty public constructor
    }
    public ProfileFragment(ParseUser userToFilterBy){ this.userToFilterBy = userToFilterBy;}


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        rvProfile = view.findViewById(R.id.rvProfile);
        allPosts = new ArrayList<>();
        //adapter = new ProfileAdapter(getContext(), allPosts);
        //rvProfile.setAdapter(adapter);

        int numberOfColumns = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);

        //scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager){
        //    @Override
        //    public void onLoadMore(int page, int totalItemsCount, RecyclerView view){
        //        queryPosts(allPosts.size());
        //     }
        // };

        //  rvProfile.setLayoutManager(gridLayoutManager);
        // rvProfile.addOnScrollListener(scrollListener);

        queryPosts(0);
    }

    protected void queryPosts(int skipAmount){

    }
}